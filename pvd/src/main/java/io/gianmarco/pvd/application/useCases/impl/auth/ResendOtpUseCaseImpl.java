package io.gianmarco.pvd.application.useCases.impl.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.transaction.annotation.Transactional;

import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpOutput;
import io.gianmarco.pvd.application.services.EmailService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.EmailAlreadyVerifiedException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpCooldownException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class ResendOtpUseCaseImpl implements ResendOtpUseCase {

    private static final int RESEND_COOLDOWN_MINUTES = 2;
    private static final int OTP_EXPIRATION_MINUTES = 15;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final EmailService emailService;

    public ResendOtpUseCaseImpl(
            UserRepository userRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public ResendOtpOutput execute(ResendOtpInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();
        OtpType type = input.type();

        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        if (type == OtpType.EMAIL_VERIFICATION && user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        otpRepository
                .findLatestByEmailAndType(normalizedEmail, type)
                .ifPresent(lastOtp -> {
                    if (lastOtp.isInCooldown(RESEND_COOLDOWN_MINUTES)) {
                        long secondsLeft = lastOtp.secondsUntilCooldownEnds(
                                RESEND_COOLDOWN_MINUTES);
                        throw new OtpCooldownException(secondsLeft);
                    }
                });

        String otp = otpService.generate(6);
        String otpHash = otpService.hash(otp);

        Instant expiresAt = Instant.now().plus(
                OTP_EXPIRATION_MINUTES,
                ChronoUnit.MINUTES);

        // 5. Eliminar OTPs anteriores
        otpRepository.deleteByOwner(normalizedEmail, type);

        Otp newOtp = Otp.create(
                user.getId(),
                normalizedEmail,
                otpHash,
                type,
                expiresAt);

        otpRepository.save(newOtp);

        try {
            emailService.sendEmailVerification(normalizedEmail, user.getName(), otp);
        } catch (Exception e) {
            // rollback lógico
            otpRepository.deleteByOwner(normalizedEmail, type);
            throw new RuntimeException("Failed to send email");
        }

        return new ResendOtpOutput(
                "A new code was sent to your email.",
                "Se envió un nuevo código a tu correo.");
    }

}
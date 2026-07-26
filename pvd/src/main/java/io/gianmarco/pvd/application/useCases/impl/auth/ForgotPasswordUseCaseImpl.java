package io.gianmarco.pvd.application.useCases.impl.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.transaction.annotation.Transactional;

import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordOutput;
import io.gianmarco.pvd.application.services.EmailService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.OtpCooldownException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class ForgotPasswordUseCaseImpl implements ForgotPasswordUseCase {

    private static final int OTP_EXPIRATION_MINUTES = 15;
    private static final int RESEND_COOLDOWN_MINUTES = 5;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final EmailService emailService;

    public ForgotPasswordUseCaseImpl(
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
    public ForgotPasswordOutput execute(ForgotPasswordInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();

        // 1. Buscar usuario
        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        otpRepository
                .findLatestByEmailAndType(normalizedEmail, OtpType.PASSWORD_RESET)
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

        otpRepository.deleteByOwner(user.getEmail(), OtpType.PASSWORD_RESET);

        // 5. Crear nuevo OTP
        Otp newOtp = Otp.create(
                user.getId(),
                normalizedEmail,
                otpHash,
                OtpType.PASSWORD_RESET,
                expiresAt);

        otpRepository.save(newOtp);

        try {
            emailService.sendForgotPassword(normalizedEmail, user.getName(), otp);

            return new ForgotPasswordOutput(
                    "If an account with that email exists, a password reset OTP has been sent.",
                    "Si existe una cuenta con ese correo, se ha enviado un OTP para restablecer la contraseña.");
        } catch (Exception e) {
            // En caso de error al enviar el correo, eliminamos el OTP creado
            otpRepository.deleteByOwner(normalizedEmail, OtpType.PASSWORD_RESET);
            throw new RuntimeException("Failed to send recovery email");
        }
    }

}

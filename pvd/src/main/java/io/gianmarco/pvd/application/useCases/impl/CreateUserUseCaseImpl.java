package io.gianmarco.pvd.application.useCases.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import io.gianmarco.pvd.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.pvd.application.ports.auth.register.RegisterUserOutput;
import io.gianmarco.pvd.application.services.EmailService;
import io.gianmarco.pvd.application.services.HashService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.useCases.interfaces.CreateUserUseCase;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.UserAlreadyExistsException;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private static final long OTP_EXPIRATION_MINUTES = 15;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final HashService hasherService;
    private final EmailService emailService;

    public CreateUserUseCaseImpl(
            UserRepository userRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            HashService hasherService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.hasherService = hasherService;
        this.emailService = emailService;
    }

    @Override
    public RegisterUserOutput execute(RegisterUserInput input) {
        Optional<User> existingUser = userRepository.findByEmail(input.email());

        if (existingUser.isPresent() && existingUser.get().isEmailVerified()) {
            throw new UserAlreadyExistsException(input.email());
        }

        String otp = otpService.generate(6);
        String otpHash = otpService.hash(otp);
        Instant expiresAt = Instant.now().plus(OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        String hashedPassword = hasherService.hash(input.password());

        User user = existingUser.orElseGet(() -> User.create(input.name(), input.email(), hashedPassword));
        User savedUser = userRepository.save(user);

        otpRepository.deleteByOwner(input.email(), OtpType.EMAIL_VERIFICATION);

        Otp otpEntity = Otp.create(
                savedUser.getId(),
                input.email(),
                otpHash,
                OtpType.EMAIL_VERIFICATION,
                expiresAt);

        otpRepository.save(otpEntity);

        emailService.sendEmailVerification(input.email(), input.name(), otp);

        return new RegisterUserOutput(
                "An email was sent to confirm your account.",
                "Se envió un correo electrónico para confirmar su cuenta.");
    }

}

package io.gianmarco.pvd.infrastructure.useCases.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.gianmarco.pvd.application.services.EmailService;
import io.gianmarco.pvd.application.services.HashService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.useCases.impl.CreateUserUseCaseImpl;
import io.gianmarco.pvd.application.useCases.interfaces.CreateUserUseCase;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            HashService hasherService,
            EmailService emailService) {
        return new CreateUserUseCaseImpl(
                userRepository,
                otpRepository,
                otpService,
                hasherService,
                emailService);
    }
}

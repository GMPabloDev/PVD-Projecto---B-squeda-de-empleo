package io.gianmarco.pvd.infrastructure.useCases.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.gianmarco.pvd.application.services.EmailService;
import io.gianmarco.pvd.application.services.HashService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.services.TokenService;
import io.gianmarco.pvd.application.useCases.impl.auth.CreateUserUseCaseImpl;
import io.gianmarco.pvd.application.useCases.impl.auth.ForgotPasswordUseCaseImpl;
import io.gianmarco.pvd.application.useCases.impl.auth.ResendOtpUseCaseImpl;
import io.gianmarco.pvd.application.useCases.impl.auth.ResetPasswordUseCaseImpl;
import io.gianmarco.pvd.application.useCases.impl.auth.VerifyEmailUseCaseImpl;
import io.gianmarco.pvd.application.useCases.impl.auth.LoginUserUseCaseImpl;
import io.gianmarco.pvd.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.domain.repositories.session.SessionRepository;
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

    @Bean
    public LoginUserUseCase loginUserUseCase(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            RefreshTokenRepository refreshTokenRepository,
            TokenService tokenService,
            HashService hashService) {
        return new LoginUserUseCaseImpl(
                userRepository,
                sessionRepository,
                refreshTokenRepository,
                tokenService,
                hashService);
    }

    @Bean
    public VerifyEmailUseCase verifyEmailUseCase(
            UserRepository userRepository,
            OtpRepository otpRepository,
            RefreshTokenRepository refreshTokenRepository,
            SessionRepository sessionRepository,
            OtpService otpService,
            TokenService tokenService) {
        return new VerifyEmailUseCaseImpl(
                userRepository,
                sessionRepository,
                otpRepository,
                refreshTokenRepository,
                otpService,
                tokenService);
    }

    @Bean
    public ResendOtpUseCase resendOtpUseCase(
            UserRepository userRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            EmailService emailService) {
        return new ResendOtpUseCaseImpl(
                userRepository,
                otpRepository,
                otpService,
                emailService);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(
            UserRepository userRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            EmailService emailService) {
        return new ForgotPasswordUseCaseImpl(
                userRepository,
                otpRepository,
                otpService,
                emailService);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            HashService hashService) {
        return new ResetPasswordUseCaseImpl(
                userRepository,
                refreshTokenRepository,
                otpRepository,
                otpService,
                hashService);
    }
}

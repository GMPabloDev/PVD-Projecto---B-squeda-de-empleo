package io.gianmarco.pvd.application.useCases.impl.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailOutput;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.services.TokenService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.entities.RefreshToken;
import io.gianmarco.pvd.domain.entities.Session;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.EmailAlreadyVerifiedException;
import io.gianmarco.pvd.domain.exceptions.auth.InvalidOtpException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpExpiredException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpMaxAttemptsExceededException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpNotFoundException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.domain.repositories.session.SessionRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class VerifyEmailUseCaseImpl implements VerifyEmailUseCase {
    private static final int MAX_ATTEMPTS = 5;
    private static final long SESSION_DAYS = 7;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final OtpRepository otpRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpService otpService;
    private final TokenService tokenService;

    public VerifyEmailUseCaseImpl(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            OtpRepository otpRepository,
            RefreshTokenRepository refreshTokenRepository,
            OtpService otpService,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.otpRepository = otpRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpService = otpService;
        this.tokenService = tokenService;
    }

    @Override
    public VerifyEmailOutput execute(VerifyEmailInput input) {
        User user = userRepository
                .findByEmail(input.email())
                .orElseThrow(() -> new UserNotFoundException(input.email()));

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        Otp otp = otpRepository
                .findLatestByEmailAndType(input.email(), OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new OtpNotFoundException());

        if (otp.isExpired()) {
            otpRepository.deleteById(otp.getId());
            throw new OtpExpiredException();
        }

        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.deleteById(otp.getId());
            throw new OtpMaxAttemptsExceededException();
        }

        String hashedInput = otpService.hash(input.otp());
        boolean isValid = otp.verify(hashedInput);

        if (!isValid) {
            otp.increaseAttempts();
            otpRepository.save(otp);

            throw new InvalidOtpException(otp.attemptsLeft(MAX_ATTEMPTS));
        }

        user.verifyEmail();
        userRepository.save(user);
        otpRepository.deleteById(otp.getId());

        // 🔐 8. Generar tokens
        String accessToken = tokenService.generateAccessToken(user);
        String refreshTokenValue = tokenService.generateRefreshToken(
                user.getId());

        // 📱 9. Crear sesión
        Instant expiresAt = Instant.now().plus(SESSION_DAYS, ChronoUnit.DAYS);

        Session session = Session.create(user.getId(), expiresAt);
        Session savedSession = sessionRepository.save(session);

        // ♻️ 10. Guardar refresh token
        RefreshToken refreshToken = RefreshToken.create(
                user.getId(),
                savedSession.getId(),
                refreshTokenValue,
                expiresAt);

        refreshTokenRepository.save(refreshToken);

        return new VerifyEmailOutput(accessToken, refreshTokenValue);

    }
}

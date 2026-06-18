package io.gianmarco.pvd.application.useCases.impl.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenInput;
import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenOutput;
import io.gianmarco.pvd.application.services.TokenService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.RefreshTokenUseCase;
import io.gianmarco.pvd.domain.entities.RefreshToken;
import io.gianmarco.pvd.domain.entities.Session;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.InvalidTokenException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.domain.repositories.session.SessionRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private static final long SESSION_DAYS = 7;

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public RefreshTokenUseCaseImpl(
            TokenService tokenService,
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository) {
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public RefreshTokenOutput execute(RefreshTokenInput input) {
        UUID userId = tokenService.validateRefreshToken(input.refreshToken());

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(input.refreshToken())
                .orElseThrow(InvalidTokenException::new);

        if (storedToken.isExpired() || storedToken.isRevoked()) {
            throw new InvalidTokenException();
        }

        storedToken.revoke();
        refreshTokenRepository.save(storedToken);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshTokenValue = tokenService.generateRefreshToken(user.getId());

        Instant expiresAt = Instant.now().plus(SESSION_DAYS, ChronoUnit.DAYS);

        Session session = Session.create(user.getId(), expiresAt);
        Session savedSession = sessionRepository.save(session);

        RefreshToken newRefreshToken = RefreshToken.create(
                user.getId(),
                savedSession.getId(),
                newRefreshTokenValue,
                expiresAt);

        refreshTokenRepository.save(newRefreshToken);

        return new RefreshTokenOutput(newAccessToken, newRefreshTokenValue);
    }
}

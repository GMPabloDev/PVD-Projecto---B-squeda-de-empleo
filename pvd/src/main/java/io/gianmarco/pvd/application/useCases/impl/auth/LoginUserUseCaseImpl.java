package io.gianmarco.pvd.application.useCases.impl.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import io.gianmarco.pvd.application.ports.auth.login.LoginUserInput;
import io.gianmarco.pvd.application.ports.auth.login.LoginUserOutput;
import io.gianmarco.pvd.application.services.HashService;
import io.gianmarco.pvd.application.services.TokenService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.pvd.domain.entities.RefreshToken;
import io.gianmarco.pvd.domain.entities.Session;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.CredentialsInvalidException;
import io.gianmarco.pvd.domain.exceptions.auth.EmailUnverifiedException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.domain.repositories.session.SessionRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    public final UserRepository userRepository;
    public final SessionRepository sessionRepository;
    public final RefreshTokenRepository refreshTokenRepository;
    public final TokenService tokenService;
    public final HashService hashService;

    public LoginUserUseCaseImpl(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            RefreshTokenRepository refreshTokenRepository,
            TokenService tokenService,
            HashService hashService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.hashService = hashService;
    }

    @Override
    public LoginUserOutput execute(LoginUserInput input) {
        User user = userRepository
                .findByEmail(input.email())
                .orElseThrow(() -> new UserNotFoundException(input.email()));

        if (!user.isEmailVerified()) {
            throw new EmailUnverifiedException();
        }

        Boolean isPasswordValid = this.hashService.compare(
                input.password(),
                user.getPassword());

        if (!isPasswordValid) {
            throw new CredentialsInvalidException();
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshTokenValue = tokenService.generateRefreshToken(
                user.getId());

        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        Session session = Session.create(user.getId(), expiresAt);
        Session savedSession = sessionRepository.save(session);

        RefreshToken refreshToken = RefreshToken.create(
                user.getId(),
                savedSession.getId(),
                refreshTokenValue,
                expiresAt);

        refreshTokenRepository.save(refreshToken);

        return new LoginUserOutput(accessToken, refreshTokenValue);
    }
}
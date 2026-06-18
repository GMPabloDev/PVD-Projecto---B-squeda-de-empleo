package io.gianmarco.pvd.application.useCases.impl.auth;

import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordOutput;
import io.gianmarco.pvd.application.services.HashService;
import io.gianmarco.pvd.application.services.OtpService;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.InvalidOtpException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpExpiredException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpMaxAttemptsExceededException;
import io.gianmarco.pvd.domain.exceptions.auth.OtpNotFoundException;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class ResetPasswordUseCaseImpl implements ResetPasswordUseCase {

    private static final int MAX_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final HashService hashService;

    public ResetPasswordUseCaseImpl(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            OtpRepository otpRepository,
            OtpService otpService,
            HashService hashService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.hashService = hashService;
    }

    @Override
    public ResetPasswordOutput execute(ResetPasswordInput input) {
        String normalizedEmail = input.email().trim().toLowerCase();

        User user = userRepository
                .findByEmail(input.email())
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));

        Otp otp = otpRepository
                .findLatestByEmailAndType(user.getEmail(), OtpType.PASSWORD_RESET)
                .orElseThrow(OtpNotFoundException::new);

        if (otp.isExpired()) {
            otpRepository.deleteById(otp.getId());
            throw new OtpExpiredException();
        }

        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.deleteById(otp.getId());
            throw new OtpMaxAttemptsExceededException();
        }

        boolean isValid = otpService.verify(input.otp(), otp.getOtpHash());

        if (!isValid) {
            otp.increaseAttempts();
            otpRepository.save(otp);

            int attemptsLeft = otp.attemptsLeft(MAX_ATTEMPTS);

            throw new InvalidOtpException(attemptsLeft);
        }

        String hashedPassword = hashService.hash(input.newPassword());
        user.changePassword(hashedPassword); // 🔥 dominio
        userRepository.save(user); // 🔥 persistencia

        otpRepository.deleteById(otp.getId());
        refreshTokenRepository.deleteAllByUser(user.getId());

        return new ResetPasswordOutput(
                "Password updated successfully",
                "Contraseña actualizada correctamente");
    }

}

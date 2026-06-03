package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class InvalidOtpException extends DomainException {

    public InvalidOtpException(int attemptsLeft) {
        super(
            "Invalid OTP. Attempts left: " + attemptsLeft,
            "Código inválido. Intentos restantes: " + attemptsLeft,
            ErrorType.VALIDATION
        );
    }
}
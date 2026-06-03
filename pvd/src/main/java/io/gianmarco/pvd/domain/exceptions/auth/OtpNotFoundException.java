package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class OtpNotFoundException extends DomainException {

    public OtpNotFoundException() {
        super(
            "No active OTP found.",
            "El código de verificación es inválido o ha expirado.",
            ErrorType.NOT_FOUND
        );
    }
}
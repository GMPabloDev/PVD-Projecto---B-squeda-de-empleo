package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class OtpExpiredException extends DomainException {

    public OtpExpiredException() {
        super(
            "OTP has expired.",
            "El código de verificación ha expirado.",
            ErrorType.VALIDATION
        );
    }
}
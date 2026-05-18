package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class TokenExpiredException extends DomainException {
    public TokenExpiredException() {
        super(
                "JWT token has expired.",
                "Your session has expired. Please log in again.",
                ErrorType.UNAUTHORIZED);
    }
}

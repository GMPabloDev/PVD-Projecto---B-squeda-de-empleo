package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException() {
        super(
                "JWT token is invalid or malformed.",
                "Invalid authentication token.",
                ErrorType.UNAUTHORIZED);
    }
}

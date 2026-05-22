package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String email) {
        super(
                "User with email " + email + " already exists",
                "Unable to create account. Please try again.",
                ErrorType.CONFLICT);
    }
}
package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String email) {
        super(
                "User not found for email: " + email,
                "El usuario no fue encontrado.",
                ErrorType.NOT_FOUND);
    }
}
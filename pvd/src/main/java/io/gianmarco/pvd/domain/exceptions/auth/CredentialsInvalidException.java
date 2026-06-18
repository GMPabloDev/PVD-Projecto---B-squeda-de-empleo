package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class CredentialsInvalidException extends DomainException {

    public CredentialsInvalidException() {
        super(
            "Invalid credentials.",
            "Correo electrónico o contraseña incorrectos",
            ErrorType.UNAUTHORIZED
        );
    }
}
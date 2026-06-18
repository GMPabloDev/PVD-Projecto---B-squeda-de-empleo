package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class EmailUnverifiedException extends DomainException {

    public EmailUnverifiedException() {
        super(
            "Email not verified for user with email $email",
            "Por favor verifica tu correo electrónico antes de iniciar sesión",
            ErrorType.UNAUTHORIZED
        );
    }
}
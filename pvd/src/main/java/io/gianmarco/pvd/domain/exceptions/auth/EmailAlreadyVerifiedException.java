package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class EmailAlreadyVerifiedException extends DomainException {

    public EmailAlreadyVerifiedException() {
        super(
                "Email already verified.",
                "Este correo ya ha sido verificado.",
                ErrorType.VALIDATION);
    }
}
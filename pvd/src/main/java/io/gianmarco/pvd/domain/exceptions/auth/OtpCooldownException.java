package io.gianmarco.pvd.domain.exceptions.auth;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

public class OtpCooldownException extends DomainException {

    public OtpCooldownException(long seconds) {
        super(
            "Please wait " + seconds + " seconds before requesting a new code.",
            "Espere " +
                seconds +
                " segundos antes de solicitar un nuevo código.",
            ErrorType.VALIDATION
        );
    }
}
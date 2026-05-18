package io.gianmarco.pvd.domain.exceptions;


public abstract class DomainException extends RuntimeException {

    private final String publicMessage;
    private final ErrorType type;

    protected DomainException(
            String message,
            String publicMessage,
            ErrorType type
    ) {
        super(message);
        this.publicMessage = publicMessage;
        this.type = type;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public ErrorType getType() {
        return type;
    }
}
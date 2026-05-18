package io.gianmarco.pvd.application.ports.auth.register;

public record RegisterUserInput(String name, String email, String password) {
}

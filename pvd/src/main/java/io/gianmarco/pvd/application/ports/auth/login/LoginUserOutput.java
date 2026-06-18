package io.gianmarco.pvd.application.ports.auth.login;

public record LoginUserOutput(String accessToken, String refreshToken) {}
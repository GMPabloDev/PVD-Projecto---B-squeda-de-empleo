package io.gianmarco.pvd.application.ports.auth.verifyEmail;

public record VerifyEmailOutput(String accessToken, String refreshToken) {
}
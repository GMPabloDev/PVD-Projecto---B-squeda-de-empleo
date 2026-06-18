package io.gianmarco.pvd.application.ports.auth.refresh;

public record RefreshTokenOutput(String accessToken, String refreshToken) {
}

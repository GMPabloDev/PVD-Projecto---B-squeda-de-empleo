package io.gianmarco.pvd.presentation.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "refreshToken is required") String refreshToken
) {}

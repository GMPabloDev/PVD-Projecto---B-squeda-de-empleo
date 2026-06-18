package io.gianmarco.pvd.presentation.dtos.auth;
import io.gianmarco.pvd.domain.entities.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResendOtpRequest(
    @NotBlank(message = "Email is required") @Email String email,
    @NotNull(message = "Type is required") OtpType type
) {}
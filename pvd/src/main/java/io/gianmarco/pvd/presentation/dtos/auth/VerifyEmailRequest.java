package io.gianmarco.pvd.presentation.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record VerifyEmailRequest(
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank(message = "Otp is required") @Length(min = 6, max = 50) String otp
) {}

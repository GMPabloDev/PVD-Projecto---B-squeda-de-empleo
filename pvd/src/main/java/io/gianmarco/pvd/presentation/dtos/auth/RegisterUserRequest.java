package io.gianmarco.pvd.presentation.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50)
    String name,
    @NotBlank(message = "email is required") @Email String email,
    @NotBlank(message = "email is required")
    @Size(min = 3, max = 50)
    String password
) {}

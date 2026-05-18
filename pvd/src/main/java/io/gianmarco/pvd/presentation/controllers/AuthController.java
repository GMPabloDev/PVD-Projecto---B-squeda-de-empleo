package io.gianmarco.pvd.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gianmarco.pvd.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.pvd.application.ports.auth.register.RegisterUserOutput;
import io.gianmarco.pvd.application.useCases.interfaces.CreateUserUseCase;
import io.gianmarco.pvd.presentation.dtos.ApiResponse;
import io.gianmarco.pvd.presentation.dtos.auth.RegisterUserRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;

    public AuthController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    public ResponseEntity<ApiResponse<RegisterUserOutput>> register(
            @Valid @RequestBody RegisterUserRequest request) {
        RegisterUserInput input = new RegisterUserInput(request.name(), request.email(), request.password());
        RegisterUserOutput output = createUserUseCase.execute(input);
        return ResponseEntity.status(201).body(ApiResponse.success(output));
    }
}

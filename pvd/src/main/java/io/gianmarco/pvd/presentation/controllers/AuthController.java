package io.gianmarco.pvd.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordOutput;
import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserInput;
import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserOutput;
import io.gianmarco.pvd.application.ports.auth.login.LoginUserInput;
import io.gianmarco.pvd.application.ports.auth.login.LoginUserOutput;
import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenInput;
import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenOutput;
import io.gianmarco.pvd.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.pvd.application.ports.auth.register.RegisterUserOutput;
import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailOutput;
import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpOutput;
import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordOutput;
import io.gianmarco.pvd.application.useCases.interfaces.auth.CreateUserUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ForgotPasswordUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.LoginUserUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.RefreshTokenUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResendOtpUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.ResetPasswordUseCase;
import io.gianmarco.pvd.application.useCases.interfaces.auth.VerifyEmailUseCase;
import io.gianmarco.pvd.presentation.dtos.ApiResponse;
import io.gianmarco.pvd.presentation.dtos.auth.ForgotPasswordRequest;
import io.gianmarco.pvd.presentation.dtos.auth.LoginUserRequest;
import io.gianmarco.pvd.presentation.dtos.auth.RefreshTokenRequest;
import io.gianmarco.pvd.presentation.dtos.auth.RegisterUserRequest;
import io.gianmarco.pvd.presentation.dtos.auth.ResendOtpRequest;
import io.gianmarco.pvd.presentation.dtos.auth.ResetPasswordRequest;
import io.gianmarco.pvd.presentation.dtos.auth.VerifyEmailRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final ResendOtpUseCase resendOtpUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(
            CreateUserUseCase createUserUseCase,
            VerifyEmailUseCase verifyEmailUseCase,
            LoginUserUseCase loginUserUseCase,
            ResendOtpUseCase resendOtpUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            ForgotPasswordUseCase forgotPasswordUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase,
            RefreshTokenUseCase refreshTokenUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.verifyEmailUseCase = verifyEmailUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.resendOtpUseCase = resendOtpUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterUserOutput>> register(
            @Valid @RequestBody RegisterUserRequest request) {
        RegisterUserInput input = new RegisterUserInput(request.name(), request.email(), request.password());
        RegisterUserOutput output = createUserUseCase.execute(input);
        return ResponseEntity.status(201).body(ApiResponse.success(output));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<VerifyEmailOutput>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request) {
        VerifyEmailInput input = new VerifyEmailInput(request.email(), request.otp());
        VerifyEmailOutput output = verifyEmailUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginUserOutput>> login(
            @Valid @RequestBody LoginUserRequest request) {
        LoginUserInput input = new LoginUserInput(request.email(), request.password());
        LoginUserOutput output = loginUserUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<ResendOtpOutput>> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {
        ResendOtpInput input = new ResendOtpInput(request.email(), request.type());
        ResendOtpOutput output = resendOtpUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordInput input = new ResetPasswordInput(
                request.email(),
                request.otp(),
                request.newPassword());
        ResetPasswordOutput output = resetPasswordUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output.message()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordInput input = new ForgotPasswordInput(request.email());
        ForgotPasswordOutput output = forgotPasswordUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output.message()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetCurrentUserOutput>> me(
            Authentication authentication) {
        String userId = authentication.getName();
        GetCurrentUserInput input = new GetCurrentUserInput(java.util.UUID.fromString(userId));
        GetCurrentUserOutput output = getCurrentUserUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenOutput>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenInput input = new RefreshTokenInput(request.refreshToken());
        RefreshTokenOutput output = refreshTokenUseCase.execute(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }
}
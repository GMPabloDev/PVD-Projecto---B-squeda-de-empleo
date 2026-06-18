package io.gianmarco.pvd.application.ports.auth.resetPassword;

public record ResetPasswordInput(
        String email,
        String otp,
        String newPassword) {
}
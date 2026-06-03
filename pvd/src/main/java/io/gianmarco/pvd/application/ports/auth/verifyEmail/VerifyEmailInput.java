package io.gianmarco.pvd.application.ports.auth.verifyEmail;

public record VerifyEmailInput(String email, String otp) {
}
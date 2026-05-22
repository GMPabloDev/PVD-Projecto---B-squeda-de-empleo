package io.gianmarco.pvd.application.services;

public interface EmailService {
    void sendEmailVerification(String email, String name, String otp);

    void sendForgotPassword(String email, String name, String otp);
}

package io.gianmarco.pvd.application.services;

public interface OtpService {
    String generate(int length);
    String hash(String otp);
    boolean verify(String otp, String hashedOtp);
}

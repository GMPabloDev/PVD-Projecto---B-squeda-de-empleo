package io.gianmarco.pvd.application.ports.auth.resendOtp;

import io.gianmarco.pvd.domain.entities.OtpType;

public record ResendOtpInput(String email, OtpType type) {}
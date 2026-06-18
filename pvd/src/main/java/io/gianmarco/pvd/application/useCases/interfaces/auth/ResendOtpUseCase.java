package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpInput;
import io.gianmarco.pvd.application.ports.auth.resendOtp.ResendOtpOutput;

public interface ResendOtpUseCase {
    ResendOtpOutput execute(ResendOtpInput input);
}
package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.pvd.application.ports.auth.forgotPassword.ForgotPasswordOutput;

public interface ForgotPasswordUseCase {
    ForgotPasswordOutput execute(ForgotPasswordInput input);
}
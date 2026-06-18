package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.pvd.application.ports.auth.resetPassword.ResetPasswordOutput;

public interface ResetPasswordUseCase {
    ResetPasswordOutput execute(ResetPasswordInput input);
}
package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.pvd.application.ports.auth.verifyEmail.VerifyEmailOutput;

public interface VerifyEmailUseCase {
    VerifyEmailOutput execute(VerifyEmailInput input);
}
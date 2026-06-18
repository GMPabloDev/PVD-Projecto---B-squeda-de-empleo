package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserInput;
import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserOutput;

public interface GetCurrentUserUseCase {
    GetCurrentUserOutput execute(GetCurrentUserInput input);
}

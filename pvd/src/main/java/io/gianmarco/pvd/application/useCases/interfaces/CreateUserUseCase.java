package io.gianmarco.pvd.application.useCases.interfaces;

import io.gianmarco.pvd.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.pvd.application.ports.auth.register.RegisterUserOutput;

public interface CreateUserUseCase {
    RegisterUserOutput execute(RegisterUserInput input);
}

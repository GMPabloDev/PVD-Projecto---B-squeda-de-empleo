package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.login.LoginUserInput;
import io.gianmarco.pvd.application.ports.auth.login.LoginUserOutput;

public interface LoginUserUseCase {
    LoginUserOutput execute(LoginUserInput input);
}
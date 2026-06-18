package io.gianmarco.pvd.application.useCases.interfaces.auth;

import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenInput;
import io.gianmarco.pvd.application.ports.auth.refresh.RefreshTokenOutput;

public interface RefreshTokenUseCase {
    RefreshTokenOutput execute(RefreshTokenInput input);
}

package io.gianmarco.pvd.application.useCases.impl.auth;

import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserInput;
import io.gianmarco.pvd.application.ports.auth.getCurrentUser.GetCurrentUserOutput;
import io.gianmarco.pvd.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.exceptions.auth.UserNotFoundException;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;

public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetCurrentUserOutput execute(GetCurrentUserInput input) {
        User user = userRepository
                .findById(input.userId())
                .orElseThrow(() -> new UserNotFoundException(input.userId().toString()));

        return new GetCurrentUserOutput(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles(),
                user.isEmailVerified());
    }
}

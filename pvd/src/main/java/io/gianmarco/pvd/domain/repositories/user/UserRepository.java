package io.gianmarco.pvd.domain.repositories.user;

import java.util.Optional;
import java.util.UUID;

import io.gianmarco.pvd.domain.entities.User;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);
}

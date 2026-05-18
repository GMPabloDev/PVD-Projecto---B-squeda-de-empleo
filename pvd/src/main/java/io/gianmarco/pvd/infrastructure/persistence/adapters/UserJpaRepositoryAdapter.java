package io.gianmarco.pvd.infrastructure.persistence.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;

public interface UserJpaRepositoryAdapter
        extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}

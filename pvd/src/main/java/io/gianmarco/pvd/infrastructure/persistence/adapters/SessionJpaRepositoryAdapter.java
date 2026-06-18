package io.gianmarco.pvd.infrastructure.persistence.adapters;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import io.gianmarco.pvd.infrastructure.persistence.entities.session.SessionJpaEntity;

public interface SessionJpaRepositoryAdapter
    extends JpaRepository<SessionJpaEntity, UUID> {}
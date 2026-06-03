package io.gianmarco.pvd.domain.repositories.session;

import java.util.Optional;
import java.util.UUID;
import io.gianmarco.pvd.domain.entities.Session;

public interface SessionRepository {
    Session save(Session session);

    Optional<Session> findById(UUID id);
}
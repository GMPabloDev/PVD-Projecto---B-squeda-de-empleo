package io.gianmarco.pvd.infrastructure.mappers.session;


import java.util.UUID;

import org.springframework.stereotype.Component;

import io.gianmarco.pvd.domain.entities.Session;
import io.gianmarco.pvd.infrastructure.persistence.entities.session.SessionJpaEntity;

@Component
public class SessionMapper {

    public SessionJpaEntity toJpa(Session session) {
        SessionJpaEntity entity = new SessionJpaEntity();
        entity.setId(session.getId());
        entity.setIsActive(session.isActive());
        entity.setLastSeenAt(session.getLastSeenAt());
        entity.setExpiresAt(session.getExpiresAt());
        return entity;
    }

    public Session toDomain(SessionJpaEntity entity) {
        UUID userId = entity.getUser() != null ? entity.getUser().getId() : null;
        return Session.restore(
            entity.getId(),
            userId,
            entity.getIsActive(),
            entity.getLastSeenAt(),
            entity.getExpiresAt()
        );
    }
}
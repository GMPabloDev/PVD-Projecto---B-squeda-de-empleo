package io.gianmarco.pvd.infrastructure.mappers.session;


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
        return Session.restore(
            entity.getId(),
            entity.getUser().getId(),
            entity.getIsActive(),
            entity.getLastSeenAt(),
            entity.getExpiresAt()
        );
    }
}
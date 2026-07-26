package io.gianmarco.pvd.infrastructure.mappers.refreshToken;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.gianmarco.pvd.domain.entities.RefreshToken;
import io.gianmarco.pvd.infrastructure.persistence.entities.refreshToken.RefreshTokenJpaEntity;

@Component
public class RefreshTokenMapper {

    public RefreshTokenJpaEntity toJpa(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entityJpa = new RefreshTokenJpaEntity();

        entityJpa.setId(refreshToken.getId());
        entityJpa.setToken(refreshToken.getToken());
        entityJpa.setExpiresAt(refreshToken.getExpiresAt());
        entityJpa.setRevokedAt(refreshToken.getRevokedAt());

        return entityJpa;
    }

    public RefreshToken toDomain(RefreshTokenJpaEntity entityJpa) {
        UUID userId = entityJpa.getUser() != null ? entityJpa.getUser().getId() : null;
        UUID sessionId = entityJpa.getSession() != null ? entityJpa.getSession().getId() : null;
        return RefreshToken.restore(
                entityJpa.getId(),
                userId,
                sessionId,
                entityJpa.getToken(),
                entityJpa.getExpiresAt(),
                entityJpa.getRevokedAt());
    }

}
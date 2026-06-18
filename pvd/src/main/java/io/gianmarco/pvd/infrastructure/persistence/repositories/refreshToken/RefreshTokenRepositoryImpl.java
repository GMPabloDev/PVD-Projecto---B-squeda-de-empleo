package io.gianmarco.pvd.infrastructure.persistence.repositories.refreshToken;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.gianmarco.pvd.domain.entities.RefreshToken;
import io.gianmarco.pvd.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.pvd.infrastructure.mappers.refreshToken.RefreshTokenMapper;
import io.gianmarco.pvd.infrastructure.persistence.adapters.RefreshTokenJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.adapters.SessionJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.adapters.UserJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.entities.refreshToken.RefreshTokenJpaEntity;
import io.gianmarco.pvd.infrastructure.persistence.entities.session.SessionJpaEntity;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepositoryAdapter jpa;
    private final UserJpaRepositoryAdapter userJpa;
    private final SessionJpaRepositoryAdapter sessionJpa;
    private final RefreshTokenMapper mapper;

    @Override
    public RefreshToken save(RefreshToken token) {
        RefreshTokenJpaEntity entity = mapper.toJpa(token);

        UserJpaEntity userRef = userJpa.getReferenceById(token.getUserId());
        entity.setUser(userRef);

        if (token.getSessionId() != null) {
            SessionJpaEntity sessionRef = sessionJpa.getReferenceById(
                    token.getSessionId());
            entity.setSession(sessionRef);
        }

        RefreshTokenJpaEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpa.findByToken(token).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(String token) {
        jpa.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteAllByUser(UUID userId) {
        jpa.deleteAllByUserId(userId);
    }

}

package io.gianmarco.pvd.infrastructure.persistence.repositories.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.gianmarco.pvd.domain.entities.Session;
import io.gianmarco.pvd.domain.repositories.session.SessionRepository;
import io.gianmarco.pvd.infrastructure.mappers.session.SessionMapper;
import io.gianmarco.pvd.infrastructure.persistence.adapters.SessionJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.adapters.UserJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.entities.session.SessionJpaEntity;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepositoryAdapter jpaRepository;
    private final UserJpaRepositoryAdapter userJpa;
    private final SessionMapper mapper;

    @Override
    public Session save(Session session) {
        SessionJpaEntity entity = mapper.toJpa(session);

        UserJpaEntity userRef = userJpa.getReferenceById(session.getUserId());
        entity.setUser(userRef);

        SessionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        return jpaRepository.findById(sessionId).map(mapper::toDomain);
    }

}

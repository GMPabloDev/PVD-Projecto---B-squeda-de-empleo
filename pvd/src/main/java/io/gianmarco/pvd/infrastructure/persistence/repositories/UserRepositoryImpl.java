package io.gianmarco.pvd.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.repositories.UserRepository;
import io.gianmarco.pvd.infrastructure.mappers.user.UserMapper;
import io.gianmarco.pvd.infrastructure.persistence.adapters.UserJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepositoryAdapter jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

}

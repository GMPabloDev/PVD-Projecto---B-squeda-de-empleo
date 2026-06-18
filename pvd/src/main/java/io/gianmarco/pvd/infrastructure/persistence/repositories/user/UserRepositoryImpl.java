package io.gianmarco.pvd.infrastructure.persistence.repositories.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.repositories.user.UserRepository;
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
        UserJpaEntity entity;

        if (user.getId() != null) {
            // Si ya existe en BD, cargamos la entidad gestionada y la actualizamos
            entity = jpaRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "User not found for update: " + user.getId()));
            mapper.copy(user, entity); // actualiza los campos sobre la entidad gestionada
        } else {
            // Es nuevo: creamos la entidad desde cero
            entity = mapper.toJpa(user);
        }

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

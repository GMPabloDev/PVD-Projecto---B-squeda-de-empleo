package io.gianmarco.pvd.infrastructure.mappers.user;

import org.springframework.stereotype.Component;
import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;

import java.util.HashSet;

@Component
public class UserMapper {

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        copy(user, entity);
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {

        // ✅ Usar getters para null-safety (Lombok puede dar null en wrappers)
        return User.restore(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getEmailVerified() != null ? entity.getEmailVerified() : false,
                entity.getDisabled() != null ? entity.getDisabled() : false,
                entity.getRoles() != null ? new HashSet<>(entity.getRoles()) : new HashSet<>(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    // ✅ Método adicional útil para actualizaciones
    public void copy(User source, UserJpaEntity target) {
        if (source.getId() != null) {
            target.setId(source.getId());
        }
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setPassword(source.getPassword());
        target.setEmailVerified(source.isEmailVerified());
        target.setDisabled(source.isDisabled());
        target.setRoles(new HashSet<>(source.getRoles()));
    }
}
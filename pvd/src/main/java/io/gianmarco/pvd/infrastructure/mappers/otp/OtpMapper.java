package io.gianmarco.pvd.infrastructure.mappers.otp;

import java.util.UUID;

import org.springframework.stereotype.Component;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.infrastructure.persistence.adapters.UserJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.entities.otp.OtpJpaEntity;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;

@Component
public class OtpMapper {

    // toJpa ahora recibe el repositorio para resolver la relación
    public OtpJpaEntity toJpa(Otp otp, UserJpaRepositoryAdapter userRepo) {
        OtpJpaEntity entity = new OtpJpaEntity();
        copy(otp, entity, userRepo);
        return entity;
    }

    public void copy(Otp source, OtpJpaEntity target, UserJpaRepositoryAdapter userRepo) {
        target.setOtp(source.getOtpHash());
        target.setType(source.getType());
        target.setEmail(source.getEmail());
        target.setAttempts(source.getAttempts());
        target.setExpiresAt(source.getExpiresAt());

        if (source.getUserId() != null) {
            // ✅ Entidad gestionada, no un objeto nuevo con solo el ID
            UserJpaEntity userRef = userRepo.getReferenceById(source.getUserId());
            target.setUser(userRef);
        } else {
            target.setUser(null);
        }
    }

    public Otp toDomain(OtpJpaEntity entity) {
        UUID userId = entity.getUser() != null ? entity.getUser().getId() : null;
        return Otp.restore(
                entity.getId(),
                userId,
                entity.getEmail(),
                entity.getOtp(),
                entity.getType(),
                entity.getAttempts() != null ? entity.getAttempts() : 0,
                entity.getCreatedAt(),
                entity.getExpiresAt());
    }
}
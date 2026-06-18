package io.gianmarco.pvd.infrastructure.mappers.otp;

import java.util.UUID;

import org.springframework.stereotype.Component;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.infrastructure.persistence.entities.otp.OtpJpaEntity;

@Component
public class OtpMapper {

    public OtpJpaEntity toJpa(Otp otp) {
        OtpJpaEntity entity = new OtpJpaEntity();
        copy(otp, entity);
        return entity;
    }

    public void copy(Otp source, OtpJpaEntity target) {
        target.setOtp(source.getOtpHash());
        target.setType(source.getType());
        target.setEmail(source.getEmail());
        target.setAttempts(source.getAttempts());
        target.setExpiresAt(source.getExpiresAt());
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
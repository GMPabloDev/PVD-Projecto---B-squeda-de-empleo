package io.gianmarco.pvd.infrastructure.mappers.otp;

import java.util.UUID;

import org.springframework.stereotype.Component;
import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.infrastructure.persistence.entities.otp.OtpJpaEntity;
import io.gianmarco.pvd.infrastructure.persistence.entities.user.UserJpaEntity;

@Component
public class OtpMapper {

    public OtpJpaEntity toJpa(Otp otp) {
        if (otp == null) {
            throw new IllegalArgumentException("Otp cannot be null");
        }
        OtpJpaEntity entity = new OtpJpaEntity();
        copy(otp, entity);
        return entity;
    }

    public Otp toDomain(OtpJpaEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("OtpJpaEntity cannot be null");
        }

        // userId puede ser null si el OTP es para email sin registrar
        UUID userId = entity.getUser() != null
                ? entity.getUser().getId()
                : null;

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

    public void copy(Otp source, OtpJpaEntity target) {
        if (source == null) {
            throw new IllegalArgumentException("Source Otp cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target OtpJpaEntity cannot be null");
        }

        if (source.getId() != null) {
            target.setId(source.getId());
        }

        target.setOtp(source.getOtpHash());
        target.setType(source.getType());
        target.setEmail(source.getEmail());
        target.setAttempts(source.getAttempts());
        target.setExpiresAt(source.getExpiresAt());

        if (source.getUserId() != null) {
            UserJpaEntity userRef = new UserJpaEntity();
            userRef.setId(source.getUserId());
            target.setUser(userRef);
        } else {
            target.setUser(null);
        }
    }
}
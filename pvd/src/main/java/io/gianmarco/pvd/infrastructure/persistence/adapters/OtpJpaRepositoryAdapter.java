package io.gianmarco.pvd.infrastructure.persistence.adapters;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.infrastructure.persistence.entities.otp.OtpJpaEntity;

public interface OtpJpaRepositoryAdapter extends JpaRepository<OtpJpaEntity, UUID> {
    Optional<OtpJpaEntity> findByEmailAndType(
            String email,
            OtpType type);

    void deleteByEmailAndType(
            @Param("email") String email,
            @Param("type") OtpType type);

    // Para deleteExpired
    @Modifying
    @Query("DELETE FROM OtpJpaEntity o WHERE o.expiresAt < :now")
    int deleteByExpiresAtBefore(@Param("now") Instant now);
}

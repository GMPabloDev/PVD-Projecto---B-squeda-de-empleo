package io.gianmarco.pvd.infrastructure.persistence.adapters;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.gianmarco.pvd.infrastructure.persistence.entities.refreshToken.RefreshTokenJpaEntity;

public interface RefreshTokenJpaRepositoryAdapter
        extends JpaRepository<RefreshTokenJpaEntity, UUID> {
    Optional<RefreshTokenJpaEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshTokenJpaEntity r WHERE r.token = :token")
    void deleteByToken(@Param("token") String token);

    @Modifying
    @Query("DELETE FROM RefreshTokenJpaEntity r WHERE r.user.id = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);
}
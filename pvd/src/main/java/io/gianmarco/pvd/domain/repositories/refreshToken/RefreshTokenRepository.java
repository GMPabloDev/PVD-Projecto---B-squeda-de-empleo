package io.gianmarco.pvd.domain.repositories.refreshToken;
import io.gianmarco.pvd.domain.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void delete(String token);
    void deleteAllByUser(UUID userId);
}
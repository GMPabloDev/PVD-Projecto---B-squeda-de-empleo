package io.gianmarco.pvd.domain.entities;
import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private UUID id;
    private UUID userId;
    private UUID sessionId;
    private String token;
    private Instant expiresAt;
    private Instant revokedAt;

    private RefreshToken() {
    }

    public static RefreshToken create(
            UUID userId,
            UUID sessionId,
            String token,
            Instant expiresAt) {
        RefreshToken rt = new RefreshToken();
        rt.userId = userId;
        rt.sessionId = sessionId;
        rt.token = token;
        rt.expiresAt = expiresAt;
        return rt;
    }

    public static RefreshToken restore(
            UUID id,
            UUID userId,
            UUID sessionId,
            String token,
            Instant expiresAt,
            Instant revokedAt) {
        RefreshToken rt = new RefreshToken();
        rt.id = id;
        rt.userId = userId;
        rt.sessionId = sessionId;
        rt.token = token;
        rt.expiresAt = expiresAt;
        rt.revokedAt = revokedAt;
        return rt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public void revoke() {
        this.revokedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }
}
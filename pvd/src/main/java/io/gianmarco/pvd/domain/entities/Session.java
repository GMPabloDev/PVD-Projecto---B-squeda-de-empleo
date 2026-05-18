package io.gianmarco.pvd.domain.entities;

import java.time.Instant;
import java.util.UUID;

public class Session {

    private UUID id;
    private UUID userId;
    private boolean active;
    private Instant lastSeenAt;
    private Instant expiresAt;

    private Session() {}

    public static Session create(UUID userId, Instant expiresAt) {
        Session session = new Session();
        session.userId = userId;
        session.active = true;
        session.lastSeenAt = Instant.now();
        session.expiresAt = expiresAt;
        return session;
    }

    public static Session restore(
        UUID id,
        UUID userId,
        boolean active,
        Instant lastSeenAt,
        Instant expiresAt
    ) {
        Session session = new Session();
        session.id = id;
        session.userId = userId;
        session.active = active;
        session.lastSeenAt = lastSeenAt;
        session.expiresAt = expiresAt;
        return session;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void touch() {
        this.lastSeenAt = Instant.now();
    }

    public void invalidate() {
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
package io.gianmarco.pvd.domain.entities;

import java.time.Instant;
import java.util.UUID;

public class Otp {

    private UUID id;
    private UUID userId;
    private String email;
    private String otpHash;
    private OtpType type;
    private int attempts;
    private Instant createdAt;
    private Instant expiresAt;

    private Otp() {}

    public static Otp create(
        UUID userId,
        String email,
        String otpHash,
        OtpType type,
        Instant expiresAt
    ) {
        Otp otp = new Otp();
        otp.userId = userId;
        otp.email = email;
        otp.otpHash = otpHash;
        otp.type = type;
        otp.expiresAt = expiresAt;
        otp.createdAt = Instant.now();
        otp.attempts = 0;
        return otp;
    }

    public static Otp restore(
        UUID id,
        UUID userId,
        String email,
        String otpHash,
        OtpType type,
        int attempts,
        Instant createdAt,
        Instant expiresAt
    ) {
        Otp otp = new Otp();
        otp.id = id;
        otp.userId = userId;
        otp.email = email;
        otp.otpHash = otpHash;
        otp.type = type;
        otp.attempts = attempts;
        otp.createdAt = createdAt;
        otp.expiresAt = expiresAt;
        return otp;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void increaseAttempts() {
        this.attempts++;
    }

    public boolean verify(String hashedInput) {
        return this.otpHash.equals(hashedInput);
    }

    public int attemptsLeft(int maxAttempts) {
        return maxAttempts - this.attempts;
    }

    public boolean isInCooldown(long cooldownMinutes) {
        return createdAt != null &&
            Instant.now().isBefore(createdAt.plusSeconds(cooldownMinutes * 60));
    }

    public long secondsUntilCooldownEnds(long cooldownMinutes) {
        if (createdAt == null) return 0;
        Instant cooldownEnd = createdAt.plusSeconds(cooldownMinutes * 60);
        long secondsLeft = java.time.Duration.between(Instant.now(), cooldownEnd).getSeconds();
        return Math.max(0, secondsLeft);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public OtpType getType() {
        return type;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}

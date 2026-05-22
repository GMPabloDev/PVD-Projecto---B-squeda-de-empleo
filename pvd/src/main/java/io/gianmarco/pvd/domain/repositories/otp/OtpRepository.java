package io.gianmarco.pvd.domain.repositories.otp;

import java.util.Optional;
import java.util.UUID;

import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;

public interface OtpRepository {
    Otp save(Otp otp);

    Optional<Otp> findLatestByEmailAndType(String email, OtpType type);

    Optional<Otp> findById(UUID id);

    void deleteByOwner(String email, OtpType type);

    void deleteById(UUID id);

    int deleteExpired();
}

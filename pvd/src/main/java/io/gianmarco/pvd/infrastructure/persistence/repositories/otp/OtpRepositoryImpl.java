package io.gianmarco.pvd.infrastructure.persistence.repositories.otp;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.gianmarco.pvd.domain.entities.Otp;
import io.gianmarco.pvd.domain.entities.OtpType;
import io.gianmarco.pvd.domain.repositories.otp.OtpRepository;
import io.gianmarco.pvd.infrastructure.mappers.otp.OtpMapper;
import io.gianmarco.pvd.infrastructure.persistence.adapters.OtpJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.adapters.UserJpaRepositoryAdapter;
import io.gianmarco.pvd.infrastructure.persistence.entities.otp.OtpJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class OtpRepositoryImpl implements OtpRepository {

    private final OtpJpaRepositoryAdapter jpaRepository;
    private final OtpMapper mapper;
    private final UserJpaRepositoryAdapter userJpaRepository;

    @Override
    public Otp save(Otp otp) {
        OtpJpaEntity entity;

        if (otp.getId() != null) {
            entity = jpaRepository.findById(otp.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Otp not found for update: " + otp.getId()));
            mapper.copy(otp, entity);
        } else {
            entity = mapper.toJpa(otp);
        }

        if (otp.getUserId() != null) {
            entity.setUser(userJpaRepository.getReferenceById(otp.getUserId()));
        }

        OtpJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Otp> findLatestByEmailAndType(String email, OtpType type) {
        return jpaRepository
                .findByEmailAndType(email, OtpType.valueOf(
                        type.name()))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Otp> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByOwner(String email, OtpType type) {
        jpaRepository.deleteByEmailAndType(email, OtpType.valueOf(type.name()));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int deleteExpired() {
        return jpaRepository.deleteByExpiresAtBefore(Instant.now());
    }
}

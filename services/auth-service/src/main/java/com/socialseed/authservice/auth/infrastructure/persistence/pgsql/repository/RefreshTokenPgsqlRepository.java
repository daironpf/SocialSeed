package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository;

import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.RefreshTokenPgsqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenPgsqlRepository extends JpaRepository<RefreshTokenPgsqlEntity, UUID> {
    List<RefreshTokenPgsqlEntity> findAllByUserId(UUID userId);

    Optional<RefreshTokenPgsqlEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    List<RefreshTokenPgsqlEntity> findByUserId(UUID userId);

    void deleteByExpiryDateBefore(java.time.Instant now);
}

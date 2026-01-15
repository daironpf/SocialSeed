package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository;

import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.RefreshTokenPgsqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenPgsqlRepository extends JpaRepository<RefreshTokenPgsqlEntity, UUID> {
    Optional<RefreshTokenPgsqlEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);
}

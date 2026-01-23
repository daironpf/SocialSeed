package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository;

import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
📌 Repositorio JPA para manejar la persistencia de usuarios en PostgreSQL.
 */
@Repository
public interface AuthUserPgsqlRepository extends JpaRepository<AuthUserPgsqlEntity, UUID> {

    Optional<AuthUserPgsqlEntity> findByUsername(String username);
    Optional<AuthUserPgsqlEntity> findByEmail(String email);
    Optional<AuthUserPgsqlEntity> findByResetPasswordToken(String resetPasswordToken);

    // Verificar existencia
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

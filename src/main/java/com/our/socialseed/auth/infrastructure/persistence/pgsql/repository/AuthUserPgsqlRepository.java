package com.our.socialseed.auth.infrastructure.persistence.pgsql.repository;

import com.our.socialseed.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
📌 Repositorio JPA para manejar la persistencia de usuarios en PostgreSQL.
 */
@Repository
public interface AuthUserPgsqlRepository extends JpaRepository<AuthUserPgsqlEntity, UUID> {

    // Buscar por username
    Optional<AuthUserPgsqlEntity> findByUsername(String username);

    // Buscar por email
    Optional<AuthUserPgsqlEntity> findByEmail(String email);

    // Verificar existencia
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

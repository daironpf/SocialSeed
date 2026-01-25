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

    Optional<AuthUserPgsqlEntity> findByVerificationToken(String verificationToken);

    Optional<AuthUserPgsqlEntity> findByEmailChangeToken(String emailChangeToken);

    // Verificar existencia
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE AuthUserPgsqlEntity u SET u.resetPasswordToken = NULL, u.resetPasswordTokenExpiry = NULL WHERE u.resetPasswordTokenExpiry < :now")
    void clearExpiredResetPasswordTokens(@org.springframework.data.repository.query.Param("now") java.time.Instant now);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE AuthUserPgsqlEntity u SET u.verificationToken = NULL, u.verificationTokenExpiry = NULL WHERE u.verificationTokenExpiry < :now AND u.emailVerified = false")
    void clearExpiredVerificationTokens(@org.springframework.data.repository.query.Param("now") java.time.Instant now);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE AuthUserPgsqlEntity u SET u.credentialsNonExpired = false WHERE u.lastPasswordChangedAt < :threshold AND u.credentialsNonExpired = true")
    int expirePasswords(@org.springframework.data.repository.query.Param("threshold") java.time.Instant threshold);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE AuthUserPgsqlEntity u SET u.username = :newUsername WHERE u.id = :userId")
    void updateUsername(@org.springframework.data.repository.query.Param("userId") java.util.UUID userId,
            @org.springframework.data.repository.query.Param("newUsername") String newUsername);
}

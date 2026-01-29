package com.socialseed.authservice.auth.infrastructure.persistence.pgsql;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.mapper.AuthUserPgsqlMapper;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AuthUserRepositoryAdapter implements AuthUserRepository {

    private final AuthUserPgsqlRepository jpaRepository;

    public AuthUserRepositoryAdapter(AuthUserPgsqlRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public AuthUser save(AuthUser authUser) {
        AuthUserPgsqlEntity entity = AuthUserPgsqlMapper.toEntity(authUser);
        AuthUserPgsqlEntity saved = jpaRepository.save(entity);
        return AuthUserPgsqlMapper.toDomain(saved);
    }

    @Override
    public Optional<AuthUser> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findByUserName(String username) {
        return jpaRepository.findByUsername(username)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    public boolean existByUserId(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<AuthUser> findByResetPasswordToken(String token) {
        return jpaRepository.findByResetPasswordToken(token)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findByVerificationToken(String token) {
        return jpaRepository.findByVerificationToken(token)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findByEmailChangeToken(String token) {
        return jpaRepository.findByEmailChangeToken(token)
                .map(AuthUserPgsqlMapper::toDomain);
    }

    @Override
    @Transactional
    public void clearExpiredResetPasswordTokens(java.time.Instant now) {
        jpaRepository.clearExpiredResetPasswordTokens(now);
    }

    @Override
    @Transactional
    public void clearExpiredEmailVerificationTokens(java.time.Instant now) {
        jpaRepository.clearExpiredVerificationTokens(now);
    }

    @Override
    @Transactional
    public void updateUsername(UUID userId, String newUsername) {
        jpaRepository.updateUsername(userId, newUsername);
    }

    @Override
    public long countUsersWithRole(String role) {
        return jpaRepository.countByRolesContaining(role);
    }
}

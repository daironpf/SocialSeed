package com.socialseed.authservice.auth.infrastructure.persistence.pgsql;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.mapper.AuthUserPgsqlMapper;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthUserRepositoryAdapter implements AuthUserRepository {

    private final AuthUserPgsqlRepository jpaRepository;

    public AuthUserRepositoryAdapter(AuthUserPgsqlRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // --------------------------
    // Métodos del puerto
    // --------------------------
    @Override
    @Transactional
    public AuthUser save(AuthUser authUser) {
        validateUser(authUser);              // <- valida y completa campos obligatorios
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

    private void validateUser(AuthUser authUser) {
        if (authUser.getUsername() == null || authUser.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede ser nulo o vacío");
        }
        if (authUser.getEmail() == null || authUser.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email no puede ser nulo o vacío");
        }
        if (authUser.getPassword() == null || authUser.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password no puede ser nulo o vacío");
        }
        if (authUser.getRoles() == null) {
            authUser.setRoles(new HashSet<>());
        }
        if (authUser.getCreatedAt() == null) {
            authUser.setCreatedAt(Instant.now());
        }
        if (!authUser.isEnabled()) {
            authUser.setEnabled(true);
        }
        if (!authUser.isAccountNonExpired()) {
            authUser.setAccountNonExpired(true);
        }
        if (!authUser.isAccountNonLocked()) {
            authUser.setAccountNonLocked(true);
        }
        if (!authUser.isCredentialsNonExpired()) {
            authUser.setCredentialsNonExpired(true);
        }
    }

}
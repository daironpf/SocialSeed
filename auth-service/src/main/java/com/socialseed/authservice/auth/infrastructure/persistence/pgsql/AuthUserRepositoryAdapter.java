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
}
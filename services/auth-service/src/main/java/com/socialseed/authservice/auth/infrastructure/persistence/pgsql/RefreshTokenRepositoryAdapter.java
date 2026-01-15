package com.socialseed.authservice.auth.infrastructure.persistence.pgsql;

import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.mapper.RefreshTokenMapper;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.RefreshTokenPgsqlRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {
    private final RefreshTokenPgsqlRepository repository;

    public RefreshTokenRepositoryAdapter(RefreshTokenPgsqlRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        repository.save(RefreshTokenMapper.toEntity(refreshToken));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(RefreshTokenMapper::toDomain);
    }

    @Override
    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        repository.deleteByUserId(userId);
    }
}

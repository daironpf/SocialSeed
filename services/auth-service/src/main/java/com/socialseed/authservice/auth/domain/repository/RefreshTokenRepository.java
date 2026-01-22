package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
    
    void deleteByUserId(UUID userId);

    java.util.List<RefreshToken> findByUserId(UUID userId);
}

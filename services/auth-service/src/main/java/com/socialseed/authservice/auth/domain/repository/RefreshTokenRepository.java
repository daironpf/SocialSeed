package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.model.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    List<RefreshToken> findAllByUserId(UUID userId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    List<RefreshToken> findByUserId(UUID userId);

    void deleteByExpiryDateBefore(java.time.Instant now);
}

package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CleanupExpiredTokens {

  private static final Logger logger = LoggerFactory.getLogger(CleanupExpiredTokens.class);
  private final RefreshTokenRepository refreshTokenRepository;

  public CleanupExpiredTokens(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Transactional
  public void execute() {
    Instant now = Instant.now();
    logger.info("Executing cleanup of expired tokens before {}", now);
    refreshTokenRepository.deleteByExpiryDateBefore(now);
    logger.info("Expired tokens cleanup completed.");
  }
}

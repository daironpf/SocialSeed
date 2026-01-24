package com.socialseed.authservice.auth.infrastructure.job;

import com.socialseed.authservice.auth.application.usecase.CleanupExpiredTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupJob {

  private static final Logger logger = LoggerFactory.getLogger(TokenCleanupJob.class);
  private final CleanupExpiredTokens cleanupExpiredTokens;

  public TokenCleanupJob(CleanupExpiredTokens cleanupExpiredTokens) {
    this.cleanupExpiredTokens = cleanupExpiredTokens;
  }

  // Default: Run every day at midnight. Configurable via 'auth.job.cleanup-cron'
  @Scheduled(cron = "${auth.job.cleanup-cron:0 0 0 * * ?}")
  public void runCleanup() {
    logger.info("Starting scheduled token cleanup job...");
    try {
      cleanupExpiredTokens.execute();
      logger.info("Token cleanup job finished successfully.");
    } catch (Exception e) {
      logger.error("Error during token cleanup job", e);
    }
  }
}

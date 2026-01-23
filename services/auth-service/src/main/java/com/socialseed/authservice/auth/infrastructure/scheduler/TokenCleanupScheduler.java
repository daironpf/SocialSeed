package com.socialseed.authservice.auth.infrastructure.scheduler;

import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 📌 Task scheduler to clean up expired security tokens.
 */
@Component
public class TokenCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(TokenCleanupScheduler.class);
    private final AuthUserRepository userRepository;

    public TokenCleanupScheduler(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cleans up expired password reset and email verification tokens every hour.
     */
    @Scheduled(cron = "${auth.cleanup.cron:0 0 * * * *}")
    public void cleanupExpiredTokens() {
        log.info("Starting scheduled cleanup of expired tokens...");
        Instant now = Instant.now();

        try {
            userRepository.clearExpiredResetPasswordTokens(now);
            log.info("Expired password reset tokens cleared.");

            userRepository.clearExpiredEmailVerificationTokens(now);
            log.info("Expired email verification tokens cleared.");
        } catch (Exception e) {
            log.error("Error during token cleanup execution", e);
        }
    }
}

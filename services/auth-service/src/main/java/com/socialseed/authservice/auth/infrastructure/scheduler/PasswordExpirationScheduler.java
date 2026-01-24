package com.socialseed.authservice.auth.infrastructure.scheduler;

import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class PasswordExpirationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PasswordExpirationScheduler.class);
    private final AuthUserPgsqlRepository authUserRepository;

    @Value("${auth.password.expiration-days:90}")
    private int expirationDays;

    public PasswordExpirationScheduler(AuthUserPgsqlRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    /**
     * Runs daily at midnight to identify and flag expired passwords.
     */
    @Scheduled(cron = "${auth.password.expiration-cron:0 0 0 * * ?}")
    @Transactional
    public void checkPasswordExpiration() {
        logger.info("Starting password expiration check job...");

        Instant threshold = Instant.now().minus(expirationDays, ChronoUnit.DAYS);
        int affectedRows = authUserRepository.expirePasswords(threshold);

        if (affectedRows > 0) {
            logger.info("Found and flagged {} users with expired passwords.", affectedRows);
        } else {
            logger.info("No expired passwords found.");
        }
    }
}

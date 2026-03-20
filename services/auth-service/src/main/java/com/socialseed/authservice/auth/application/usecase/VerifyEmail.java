package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.contracts.auth.events.AuthUserEmailVerified;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class VerifyEmail {

    private static final Logger logger = LoggerFactory.getLogger(VerifyEmail.class);

    private final AuthUserRepository authUserRepository;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String emailVerifiedTopic;

    public VerifyEmail(
            AuthUserRepository authUserRepository,
            KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${kafka.topic.auth-user-email-verified:auth.user.email.verified.v1}") String emailVerifiedTopic) {
        this.authUserRepository = authUserRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.emailVerifiedTopic = emailVerifiedTopic;
    }

    public void execute(String token) {
        logger.info("Processing email verification for token: {}...", token.substring(0, Math.min(5, token.length())));

        AuthUser authUser = authUserRepository.findByVerificationToken(token)
                .orElseThrow(() -> {
                    logger.warn("Email verification failed: Invalid token");
                    return new BusinessException(ErrorCode.VERIFICATION_TOKEN_INVALID);
                });

        // Check if already verified
        if (authUser.isEmailVerified()) {
            logger.warn("Email verification failed: Email already verified for user {}", authUser.getId());
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // Check if token is expired
        if (authUser.getVerificationTokenExpiry() == null ||
                authUser.getVerificationTokenExpiry().isBefore(Instant.now())) {
            logger.warn("Email verification failed: Token expired for user {}", authUser.getId());
            throw new BusinessException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
        }

        // Mark email as verified and clear token (single-use)
        authUser.setEmailVerified(true);
        authUser.setVerificationToken(null);
        authUser.setVerificationTokenExpiry(null);

        authUserRepository.save(authUser);

        logger.info("AUDIT: Email verified successfully for user {} (email: {})", authUser.getId(),
                authUser.getEmail());

        // Emit Kafka event
        AuthUserEmailVerified event = AuthUserEmailVerified.newBuilder()
                .setUserId(authUser.getId().toString())
                .setEmail(authUser.getEmail())
                .setVerifiedAt(
                        com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
                .build();

        kafkaTemplate.send(emailVerifiedTopic, authUser.getId().toString(), event.toByteArray());
        logger.info("EmailVerified event emitted for user {}", authUser.getId());
    }
}

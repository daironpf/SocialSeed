package com.socialseed.authservice.auth.application.service;

import com.socialseed.contracts.auth.events.AuthUserEmailChanged;
import com.socialseed.contracts.auth.events.AuthUserUsernameChanged;
import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import com.socialseed.contracts.socialuser.UpdateEmailRequest;
import com.socialseed.contracts.socialuser.UpdateUsernameRequest;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserSyncService {

  private static final Logger logger = LoggerFactory.getLogger(UserSyncService.class);

  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub;

  public UserSyncService(KafkaTemplate<String, byte[]> kafkaTemplate,
      SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub) {
    this.kafkaTemplate = kafkaTemplate;
    this.socialUserServiceStub = socialUserServiceStub;
  }

  @Retryable(retryFor = { StatusRuntimeException.class,
      RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
  public void syncUsernameChange(UUID userId, String oldUsername, String newUsername) {
    logger.info("Syncing username change for user {}: {} -> {}", userId, oldUsername, newUsername);

    // 1. gRPC Sync
    UpdateUsernameRequest request = UpdateUsernameRequest.newBuilder()
        .setUserId(userId.toString())
        .setNewUsername(newUsername)
        .build();

    var response = socialUserServiceStub.updateUsername(request);
    if (!response.getSuccess()) {
      throw new RuntimeException("SocialUser service failed to update username: " + response.getMessage());
    }

    // 2. Kafka Event
    AuthUserUsernameChanged event = AuthUserUsernameChanged.newBuilder()
        .setUserId(userId.toString())
        .setOldUsername(oldUsername)
        .setNewUsername(newUsername)
        .setUpdatedAt(com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
        .build();

    kafkaTemplate.send("auth.user.username.changed", userId.toString(), event.toByteArray());
  }

  @Retryable(retryFor = { StatusRuntimeException.class,
      RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
  public void syncEmailChange(UUID userId, String oldEmail, String newEmail) {
    logger.info("Syncing email change for user {}: {} -> {}", userId, oldEmail, newEmail);

    // 1. gRPC Sync
    UpdateEmailRequest request = UpdateEmailRequest.newBuilder()
        .setUserId(userId.toString())
        .setNewEmail(newEmail)
        .build();

    var response = socialUserServiceStub.updateEmail(request);
    if (!response.getSuccess()) {
      throw new RuntimeException("SocialUser service failed to update email: " + response.getMessage());
    }

    // 2. Kafka Event
    AuthUserEmailChanged event = AuthUserEmailChanged.newBuilder()
        .setUserId(userId.toString())
        .setOldEmail(oldEmail)
        .setNewEmail(newEmail)
        .setUpdatedAt(com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
        .build();

    kafkaTemplate.send("auth.user.email.changed", userId.toString(), event.toByteArray());
  }

  @Recover
  public void recover(Exception e, UUID userId, String oldVal, String newVal) {
    logger.error("Failed to sync user data for user {} after retries. Error: {}", userId, e.getMessage());
    // Push to a "manual inspection" topic as per requirement
    String errorMessage = String.format("FAILED_SYNC|USER:%s|OLD:%s|NEW:%s|ERROR:%s", userId, oldVal, newVal,
        e.getMessage());
    kafkaTemplate.send("auth.user.sync.failures", userId.toString(), errorMessage.getBytes());
  }
}

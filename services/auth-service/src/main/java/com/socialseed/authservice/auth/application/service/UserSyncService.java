package com.socialseed.authservice.auth.application.service;

import com.socialseed.contracts.auth.events.AuthUserEmailChanged;
import com.socialseed.contracts.auth.events.AuthUserUsernameChanged;
import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import com.socialseed.contracts.socialuser.UpdateEmailRequest;
import com.socialseed.contracts.socialuser.UpdateUsernameRequest;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
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

  private final KafkaTemplate<String, byte[]> kafkaTemplateBytes;
  private final SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub;
  private final String usernameChangedTopic;
  private final String emailChangedTopic;
  private final String syncFailuresTopic;

  public UserSyncService(
          KafkaTemplate<String, byte[]> kafkaTemplateBytes,
          SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub,
          @Value("${kafka.topic.auth-user-username-changed:auth.user.username.changed.v1}") String usernameChangedTopic,
          @Value("${kafka.topic.auth-user-email-changed:auth.user.email.changed.v1}") String emailChangedTopic,
          @Value("${kafka.topic.auth-user-sync-failures:auth.user.sync.failures}") String syncFailuresTopic) {
    this.kafkaTemplateBytes = kafkaTemplateBytes;
    this.socialUserServiceStub = socialUserServiceStub;
    this.usernameChangedTopic = usernameChangedTopic;
    this.emailChangedTopic = emailChangedTopic;
    this.syncFailuresTopic = syncFailuresTopic;
  }

  @Retryable(retryFor = { StatusRuntimeException.class,
      RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
  public void syncUsernameChange(UUID userId, String oldUsername, String newUsername) {
    logger.info("Syncing username change for user {}: {} -> {}", userId, oldUsername, newUsername);

    UpdateUsernameRequest request = UpdateUsernameRequest.newBuilder()
        .setUserId(userId.toString())
        .setNewUsername(newUsername)
        .build();

    var response = socialUserServiceStub.updateUsername(request);
    if (!response.getSuccess()) {
      throw new RuntimeException("SocialUser service failed to update username: " + response.getMessage());
    }

    AuthUserUsernameChanged event = AuthUserUsernameChanged.newBuilder()
        .setUserId(userId.toString())
        .setOldUsername(oldUsername)
        .setNewUsername(newUsername)
        .setUpdatedAt(com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
        .build();

    kafkaTemplateBytes.send(usernameChangedTopic, userId.toString(), event.toByteArray());
  }

  @Retryable(retryFor = { StatusRuntimeException.class,
      RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
  public void syncEmailChange(UUID userId, String oldEmail, String newEmail) {
    logger.info("Syncing email change for user {}: {} -> {}", userId, oldEmail, newEmail);

    UpdateEmailRequest request = UpdateEmailRequest.newBuilder()
        .setUserId(userId.toString())
        .setNewEmail(newEmail)
        .build();

    var response = socialUserServiceStub.updateEmail(request);
    if (!response.getSuccess()) {
      throw new RuntimeException("SocialUser service failed to update email: " + response.getMessage());
    }

    AuthUserEmailChanged event = AuthUserEmailChanged.newBuilder()
        .setUserId(userId.toString())
        .setOldEmail(oldEmail)
        .setNewEmail(newEmail)
        .setUpdatedAt(com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
        .build();

    kafkaTemplateBytes.send(emailChangedTopic, userId.toString(), event.toByteArray());
  }

  @Recover
  public void recoverSyncUsernameChange(Exception e, UUID userId, String oldUsername, String newUsername) {
    logger.error("Failed to sync username change for user {} after retries. Error: {}", userId, e.getMessage());
    String errorMessage = String.format("FAILED_SYNC|USER:%s|OLD:%s|NEW:%s|ERROR:%s", userId, oldUsername, newUsername,
        e.getMessage());
    kafkaTemplateBytes.send(syncFailuresTopic, userId.toString(), errorMessage.getBytes());
  }

  @Recover
  public void recoverSyncEmailChange(Exception e, UUID userId, String oldEmail, String newEmail) {
    logger.error("Failed to sync email change for user {} after retries. Error: {}", userId, e.getMessage());
    String errorMessage = String.format("FAILED_SYNC|USER:%s|OLD:%s|NEW:%s|ERROR:%s", userId, oldEmail, newEmail,
        e.getMessage());
    kafkaTemplateBytes.send(syncFailuresTopic, userId.toString(), errorMessage.getBytes());
  }
}

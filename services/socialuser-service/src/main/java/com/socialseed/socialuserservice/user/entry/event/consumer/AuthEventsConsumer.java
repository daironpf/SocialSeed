package com.socialseed.socialuserservice.user.entry.event.consumer;

import com.socialseed.contracts.auth.events.AuthUserUsernameChanged;
import com.socialseed.socialuserservice.user.application.usecase.UserUseCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthEventsConsumer {
  private static final Logger log = LoggerFactory.getLogger(AuthEventsConsumer.class);

  private final UserUseCases userUseCases;

  public AuthEventsConsumer(UserUseCases userUseCases) {
    this.userUseCases = userUseCases;
  }

  @KafkaListener(topics = "auth.user.username.changed", groupId = "socialuser-service-group")
  public void consumeUsernameChanged(byte[] message) {
    try {
      var event = AuthUserUsernameChanged.parseFrom(message);
      log.info("Received AuthUserUsernameChanged event for user {}", event.getUserId());

      // Call use case (idempotent because Neo4j SET is idempotent)
      userUseCases.changeUsername(UUID.fromString(event.getUserId()), event.getNewUsername());

    } catch (Exception e) {
      log.error("Error processing AuthUserUsernameChanged event", e);
    }
  }

  @KafkaListener(topics = "auth.user.email.changed", groupId = "socialuser-service-group")
  public void consumeEmailChanged(byte[] message) {
    try {
      var event = com.socialseed.contracts.auth.events.AuthUserEmailChanged.parseFrom(message);
      log.info("Received AuthUserEmailChanged event for user {}", event.getUserId());

      // Call use case (idempotent because Neo4j SET is idempotent)
      userUseCases.changeEmail(UUID.fromString(event.getUserId()), event.getNewEmail());

    } catch (Exception e) {
      log.error("Error processing AuthUserEmailChanged event", e);
    }
  }
}

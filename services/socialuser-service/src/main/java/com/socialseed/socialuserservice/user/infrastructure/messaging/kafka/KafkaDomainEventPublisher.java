package com.socialseed.socialuserservice.user.infrastructure.messaging.kafka;

import com.google.protobuf.util.Timestamps;
import com.socialseed.contracts.socialuser.events.SocialUserCreated;
import com.socialseed.socialuserservice.user.domain.event.DomainEvent;
import com.socialseed.socialuserservice.user.domain.event.SocialUserCreatedEvent;
import com.socialseed.socialuserservice.user.domain.event.SocialUserProfileUpdatedEvent;
import com.socialseed.socialuserservice.user.domain.event.SocialUserVacationEndedEvent;
import com.socialseed.socialuserservice.user.domain.event.SocialUserVacationStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaDomainEventPublisher.class);

    private static final String TOPIC_USER_CREATED = "socialuser.user.created";
    private static final String TOPIC_PROFILE_UPDATED = "socialuser.profile.updated";
    private static final String TOPIC_VACATION_STARTED = "socialuser.vacation.started";
    private static final String TOPIC_VACATION_ENDED = "socialuser.vacation.ended";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaDomainEventPublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(DomainEvent event) {
        if (event instanceof SocialUserCreatedEvent e) {
            publishUserCreated(e);
        } else if (event instanceof SocialUserProfileUpdatedEvent e) {
            publishProfileUpdated(e);
        } else if (event instanceof SocialUserVacationStartedEvent e) {
            publishVacationStarted(e);
        } else if (event instanceof SocialUserVacationEndedEvent e) {
            publishVacationEnded(e);
        } else {
            log.warn("Unknown event type: {}", event.getClass().getSimpleName());
        }
    }

    private void publishUserCreated(SocialUserCreatedEvent event) {
        try {
            SocialUserCreated proto = SocialUserCreated.newBuilder()
                    .setUserId(event.userId().toString())
                    .setEmail(event.email())
                    .setUsername(event.username())
                    .setCreatedAt(Timestamps.fromMillis(event.occurredAt().toEpochMilli()))
                    .build();
            kafkaTemplate.send(TOPIC_USER_CREATED, proto.toByteArray());
            log.info("Published SocialUserCreatedEvent for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Failed to publish SocialUserCreatedEvent for userId={}", event.userId(), e);
        }
    }

    private void publishProfileUpdated(SocialUserProfileUpdatedEvent event) {
        try {
            var proto = com.socialseed.contracts.socialuser.events.SocialUserProfileUpdated.newBuilder()
                    .setUserId(event.userId().toString())
                    .setUpdatedAt(Timestamps.fromMillis(event.occurredAt().toEpochMilli()))
                    .build();
            kafkaTemplate.send(TOPIC_PROFILE_UPDATED, proto.toByteArray());
            log.info("Published SocialUserProfileUpdatedEvent for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Failed to publish SocialUserProfileUpdatedEvent for userId={}", event.userId(), e);
        }
    }

    private void publishVacationStarted(SocialUserVacationStartedEvent event) {
        try {
            var proto = com.socialseed.contracts.socialuser.events.SocialUserVacationStarted.newBuilder()
                    .setUserId(event.userId().toString())
                    .setStartedAt(Timestamps.fromMillis(event.startedAt().toEpochMilli()))
                    .build();
            kafkaTemplate.send(TOPIC_VACATION_STARTED, proto.toByteArray());
            log.info("Published SocialUserVacationStartedEvent for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Failed to publish SocialUserVacationStartedEvent for userId={}", event.userId(), e);
        }
    }

    private void publishVacationEnded(SocialUserVacationEndedEvent event) {
        try {
            var proto = com.socialseed.contracts.socialuser.events.SocialUserVacationEnded.newBuilder()
                    .setUserId(event.userId().toString())
                    .setEndedAt(Timestamps.fromMillis(event.endedAt().toEpochMilli()))
                    .build();
            kafkaTemplate.send(TOPIC_VACATION_ENDED, proto.toByteArray());
            log.info("Published SocialUserVacationEndedEvent for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Failed to publish SocialUserVacationEndedEvent for userId={}", event.userId(), e);
        }
    }
}

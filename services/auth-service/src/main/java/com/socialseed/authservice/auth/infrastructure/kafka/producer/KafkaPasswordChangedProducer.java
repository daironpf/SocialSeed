package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.google.protobuf.util.Timestamps;
import com.socialseed.authservice.auth.domain.event.PasswordChangedEvent;
import com.socialseed.authservice.auth.domain.repository.PasswordChangedEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPasswordChangedProducer implements PasswordChangedEventPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(KafkaPasswordChangedProducer.class);
    private final String topic;

    public KafkaPasswordChangedProducer(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${kafka.topic.auth-password-changed:auth.password.changed.v1}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(PasswordChangedEvent event) {
        try {
            var proto = com.socialseed.contracts.auth.events.AuthPasswordChanged.newBuilder()
                    .setUserId(String.valueOf(event.userId()))
                    .setEmail(event.email())
                    .setChangedAt(Timestamps.fromMillis(event.occurredAt()))
                    .build();
            kafkaTemplate.send(topic, proto.toByteArray());
            log.info("Published AuthPasswordChanged event for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Failed to publish AuthPasswordChanged event for userId={}", event.userId(), e);
        }
    }
}

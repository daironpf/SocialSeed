package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.contracts.auth.events.AuthUserRegistered;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.google.protobuf.util.Timestamps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserRegisteredProducer implements UserRegisteredEventPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String topic;

    public KafkaUserRegisteredProducer(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${kafka.topic.auth-user-registered}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(UserRegisteredEvent event) {

        AuthUserRegistered proto = AuthUserRegistered.newBuilder()
                .setUserId(String.valueOf(event.userId()))
                .setEmail(event.email())
                .setUsername(event.username())
                .setCreatedAt(Timestamps.fromMillis(event.occurredAt()))
                .build();

        kafkaTemplate.send(topic, proto.toByteArray());
    }
}

package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.auth.AuthUserRegistered;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserRegisteredProducer implements UserRegisteredEventPublisher {

    private static final String TOPIC = "auth.user.registered";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaUserRegisteredProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(UserRegisteredEvent event) {

        AuthUserRegistered proto = AuthUserRegistered.newBuilder()
                .setUserId(String.valueOf(event.userId()))
                .setEmail(event.email())
                .setUsername(event.username())
                .setCreatedAt(event.occurredAt())
                .build();

        kafkaTemplate.send(TOPIC, proto.toByteArray());
    }
}

package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.contracts.auth.events.AuthUserRegistered;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.google.protobuf.util.Timestamps;
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
                // Convertimos el long (milis) al formato que espera Proto
                .setCreatedAt(Timestamps.fromMillis(event.occurredAt()))
                .build();

        kafkaTemplate.send(TOPIC, proto.toByteArray());
    }
}

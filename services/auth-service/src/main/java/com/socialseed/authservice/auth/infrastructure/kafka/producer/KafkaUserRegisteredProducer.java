package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.auth.AuthUserRegistered;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUserRegisteredProducer implements UserRegisteredEventPublisher {

    private static final String TOPIC = "auth.user.registered";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

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


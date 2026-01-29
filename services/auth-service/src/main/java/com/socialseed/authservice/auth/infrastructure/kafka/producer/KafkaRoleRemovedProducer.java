package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.authservice.auth.domain.event.RoleRemovedEvent;
import com.socialseed.authservice.auth.domain.repository.RoleRemovedEventPublisher;
import com.socialseed.contracts.auth.events.AuthUserRoleRemoved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoleRemovedProducer implements RoleRemovedEventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaRoleRemovedProducer.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${app.kafka.topics.role-removed:auth.user.role.removed}")
    private String roleRemovedTopic;
    
    public KafkaRoleRemovedProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public void publish(RoleRemovedEvent event) {
        try {
            AuthUserRoleRemoved protoEvent = AuthUserRoleRemoved.newBuilder()
                    .setUserId(event.userId().toString())
                    .setEmail(event.email())
                    .setUsername(event.username())
                    .setRole(event.role())
                    .setRemovedBy(event.removedBy().toString())
                    .setRemovedAt(com.google.protobuf.Timestamp.newBuilder()
                            .setSeconds(event.removedAt().getEpochSecond())
                            .setNanos(event.removedAt().getNano())
                            .build())
                    .build();
            
            kafkaTemplate.send(roleRemovedTopic, protoEvent);
            log.info("RoleRemoved event published for user: {}, role: {}", event.userId(), event.role());
        } catch (Exception e) {
            log.error("Failed to publish RoleRemoved event for user: {}, role: {}", event.userId(), event.role(), e);
        }
    }
}
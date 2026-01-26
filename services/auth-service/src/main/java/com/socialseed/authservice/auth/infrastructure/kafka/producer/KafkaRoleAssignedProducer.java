package com.socialseed.authservice.auth.infrastructure.kafka.producer;

import com.socialseed.authservice.auth.domain.event.RoleAssignedEvent;
import com.socialseed.authservice.auth.domain.repository.RoleAssignedEventPublisher;
import com.socialseed.contracts.auth.events.AuthUserRoleAssigned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class KafkaRoleAssignedProducer implements RoleAssignedEventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaRoleAssignedProducer.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${app.kafka.topics.role-assigned:auth.user.role.assigned}")
    private String roleAssignedTopic;
    
    public KafkaRoleAssignedProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public void publish(RoleAssignedEvent event) {
        try {
            AuthUserRoleAssigned protoEvent = AuthUserRoleAssigned.newBuilder()
                    .setUserId(event.userId().toString())
                    .setEmail(event.email())
                    .setUsername(event.username())
                    .setRole(event.role())
                    .setAssignedBy(event.assignedBy().toString())
                    .setAssignedAt(com.google.protobuf.Timestamp.newBuilder()
                            .setSeconds(event.assignedAt().getEpochSecond())
                            .setNanos(event.assignedAt().getNano())
                            .build())
                    .build();
            
            kafkaTemplate.send(roleAssignedTopic, protoEvent);
            log.info("RoleAssigned event published for user: {}, role: {}", event.userId(), event.role());
        } catch (Exception e) {
            log.error("Failed to publish RoleAssigned event for user: {}, role: {}", event.userId(), event.role(), e);
        }
    }
}
package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.event.RoleAssignedEvent;

public interface RoleAssignedEventPublisher {
    void publish(RoleAssignedEvent event);
}
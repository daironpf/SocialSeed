package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.event.RoleRemovedEvent;

public interface RoleRemovedEventPublisher {
    void publish(RoleRemovedEvent event);
}
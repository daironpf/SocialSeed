package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;

public interface UserRegisteredEventPublisher {
    void publish(UserRegisteredEvent event);
}

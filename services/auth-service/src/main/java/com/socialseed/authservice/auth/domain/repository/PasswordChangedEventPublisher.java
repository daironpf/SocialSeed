package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.event.PasswordChangedEvent;

public interface PasswordChangedEventPublisher {
    void publish(PasswordChangedEvent event);
}

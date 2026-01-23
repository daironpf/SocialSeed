package com.socialseed.authservice.auth.domain.service;

import java.util.UUID;

public interface LoginAttemptService {
    void recordFailedLogin(UUID userId);
    void recordSuccessfulLogin(UUID userId);
}

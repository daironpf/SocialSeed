package com.socialseed.authservice.auth.domain.service;

import java.time.Duration;

public interface TokenBlacklistService {
    void blacklistToken(String jti, Duration expiration);

    boolean isBlacklisted(String jti);
}

package com.socialseed.authservice.auth.domain.model;

import java.util.Set;

public record AuthResult(
    String token,
    String refreshToken,
    Set<String> roles
) {}

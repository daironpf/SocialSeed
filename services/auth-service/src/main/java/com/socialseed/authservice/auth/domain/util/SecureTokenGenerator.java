package com.socialseed.authservice.auth.domain.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class SecureTokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_BYTE_LENGTH = 32;

    private SecureTokenGenerator() {}

    public static String generate() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

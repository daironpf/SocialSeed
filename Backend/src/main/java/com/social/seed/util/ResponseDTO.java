package com.social.seed.util;

import org.springframework.http.HttpStatus;

public record ResponseDTO(
        HttpStatus status,
        Object response,
        String message
) {
}

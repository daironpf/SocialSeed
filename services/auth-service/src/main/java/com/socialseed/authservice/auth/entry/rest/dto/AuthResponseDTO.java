package com.socialseed.authservice.auth.entry.rest.dto;

import java.util.Set;

public record AuthResponseDTO(
        String token,
        String refreshToken,
        Set<String> roles) {
}
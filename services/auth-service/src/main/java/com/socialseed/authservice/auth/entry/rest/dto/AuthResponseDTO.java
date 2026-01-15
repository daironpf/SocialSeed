package com.socialseed.authservice.auth.entry.rest.dto;

import java.util.Set;

public class AuthResponseDTO {
    public String token;
    public String refreshToken;
    public Set<String> roles;

    public AuthResponseDTO(String token, String refreshToken, Set<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }
}
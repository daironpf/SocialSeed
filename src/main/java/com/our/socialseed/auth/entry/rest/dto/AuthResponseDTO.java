package com.our.socialseed.auth.entry.rest.dto;

import java.util.HashSet;
import java.util.Set;

public class AuthResponseDTO {
    public String token;
    public Set<String> roles;

    public AuthResponseDTO(String token, Set<String> roles) {
        this.token = token;
        this.roles = roles;
    }
}

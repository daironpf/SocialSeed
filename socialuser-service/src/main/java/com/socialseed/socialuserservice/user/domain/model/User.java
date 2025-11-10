package com.socialseed.socialuserservice.user.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    // password was to -> service auth
    private String password;
    private String fullName;

    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
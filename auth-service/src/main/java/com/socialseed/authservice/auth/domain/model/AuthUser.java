package com.socialseed.authservice.auth.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class AuthUser {
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Set<String> roles = new HashSet<>();

    // Seguridad
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private int failedLoginAttempts = 0;
    private Instant lastFailedLoginAt;
    private String lastFailedLoginIp;

    // Metadatos
    private Instant createdAt = Instant.now();
    private Instant updatedAt;
    private Instant lastLoginAt;
    private String lastLoginIp;

    // Tokens (reset y verificación)
    private String resetPasswordToken;
    private Instant resetPasswordTokenExpiry;
    private boolean emailVerified = false;
    private String verificationToken;
    private Instant verificationTokenExpiry;

    // 2FA
    private boolean twoFactorEnabled = false;
    private String twoFactorSecret;

    public AuthUser(UUID id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles.add("ROLE_USER");

        // Seguridad
        this.failedLoginAttempts = 0;
        this.lastFailedLoginAt = Instant.now();
        this.lastFailedLoginIp = "127.0.0.1";

        // Metadatos
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastLoginAt = Instant.now();
        this.lastLoginIp = "127.0.0.1";

        // Tokens (reset y verificación)
        this.resetPasswordToken = "no";
        this.resetPasswordTokenExpiry = Instant.now() ;
        this.emailVerified = true;
        this.verificationToken = "no";
        this.verificationTokenExpiry = Instant.now() ;

        // 2FA
        this.twoFactorEnabled = false;
        this.twoFactorSecret = "secret";

    }
}

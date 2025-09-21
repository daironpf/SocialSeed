package com.our.socialseed.auth.domain.model;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
📌 Representa el usuario y su autenticación en la base de datos.
 */
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

    // Getters / Setters (lombok puede ayudarte aquí)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Instant getLastFailedLoginAt() {
        return lastFailedLoginAt;
    }

    public void setLastFailedLoginAt(Instant lastFailedLoginAt) {
        this.lastFailedLoginAt = lastFailedLoginAt;
    }

    public String getLastFailedLoginIp() {
        return lastFailedLoginIp;
    }

    public void setLastFailedLoginIp(String lastFailedLoginIp) {
        this.lastFailedLoginIp = lastFailedLoginIp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Instant getResetPasswordTokenExpiry() {
        return resetPasswordTokenExpiry;
    }

    public void setResetPasswordTokenExpiry(Instant resetPasswordTokenExpiry) {
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Instant getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerificationTokenExpiry(Instant verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }
}

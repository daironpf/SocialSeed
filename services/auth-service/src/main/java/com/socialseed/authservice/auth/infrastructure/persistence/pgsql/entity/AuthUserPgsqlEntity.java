package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/*
📌 Entidad JPA para usuarios en la base de datos PostgreSQL.
Incluye autenticación, seguridad y metadatos de auditoría.
*/
@Entity
@Table(
        name = "auth_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_auth_user_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_auth_user_email", columnNames = "email")
        }
)
public class AuthUserPgsqlEntity {

    /**
     * ID asignado manualmente.
     * Este valor proviene del nodo creado en SocialUser-Service.
     */
    @Id
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Column(nullable = false, length = 50)
    private String username;

    @NotNull
    @Column(nullable = false, length = 100)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "auth_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    /* ===== Seguridad ===== */

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    private Instant lastFailedLoginAt;
    private String lastFailedLoginIp;
    private Instant lastPasswordChangedAt;

    /* ===== Auditoría ===== */

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant lastLoginAt;
    private String lastLoginIp;

    /* ===== Tokens ===== */

    private String resetPasswordToken;
    private Instant resetPasswordTokenExpiry;

    @Column(nullable = false)
    private boolean emailVerified = false;

    private String verificationToken;
    private Instant verificationTokenExpiry;

    /* ===== 2FA ===== */

    private boolean twoFactorEnabled = false;
    private String twoFactorSecret;

    /* ==========================================================
       ================ Constructors =============================
       ========================================================== */

    /**
     * Constructor requerido por JPA
     */
    protected AuthUserPgsqlEntity() {
        // JPA only
    }

    /**
     * Constructor completo (equivalente a @AllArgsConstructor)
     */
    public AuthUserPgsqlEntity(
            UUID id,
            String username,
            String email,
            String password,
            Set<String> roles,
            boolean enabled,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            int failedLoginAttempts,
            Instant lastFailedLoginAt,
            String lastFailedLoginIp,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt,
            String lastLoginIp,
            String resetPasswordToken,
            Instant resetPasswordTokenExpiry,
            boolean emailVerified,
            String verificationToken,
            Instant verificationTokenExpiry,
            boolean twoFactorEnabled,
            String twoFactorSecret,
            Instant lastPasswordChangedAt
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles != null ? roles : new HashSet<>();
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lastFailedLoginAt = lastFailedLoginAt;
        this.lastFailedLoginIp = lastFailedLoginIp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.lastLoginIp = lastLoginIp;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
        this.emailVerified = emailVerified;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiry = verificationTokenExpiry;
        this.twoFactorEnabled = twoFactorEnabled;
        this.twoFactorSecret = twoFactorSecret;
        this.lastPasswordChangedAt = lastPasswordChangedAt;
    }

    /* ==========================================================
       ================= Getters & Setters =======================
       ========================================================== */

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
        this.roles = roles != null ? roles : new HashSet<>();
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

    public Instant getUpdatedAt() {
        return updatedAt;
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

    public Instant getLastPasswordChangedAt() {
        return lastPasswordChangedAt;
    }

    public void setLastPasswordChangedAt(Instant lastPasswordChangedAt) {
        this.lastPasswordChangedAt = lastPasswordChangedAt;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private UUID id;
        private String username;
        private String email;
        private String password;
        private Set<String> roles = new HashSet<>();

        private boolean enabled = true;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private int failedLoginAttempts = 0;

        private Instant lastFailedLoginAt;
        private String lastFailedLoginIp;

        private Instant createdAt;
        private Instant updatedAt;
        private Instant lastLoginAt;
        private String lastLoginIp;

        private String resetPasswordToken;
        private Instant resetPasswordTokenExpiry;

        private boolean emailVerified = false;
        private String verificationToken;
        private Instant verificationTokenExpiry;

        private boolean twoFactorEnabled = false;
        private String twoFactorSecret;
        private Instant lastPasswordChangedAt;

        /* ===== Fluent setters ===== */

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles != null ? roles : new HashSet<>();
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder failedLoginAttempts(int failedLoginAttempts) {
            this.failedLoginAttempts = failedLoginAttempts;
            return this;
        }

        public Builder lastFailedLoginAt(Instant lastFailedLoginAt) {
            this.lastFailedLoginAt = lastFailedLoginAt;
            return this;
        }

        public Builder lastFailedLoginIp(String lastFailedLoginIp) {
            this.lastFailedLoginIp = lastFailedLoginIp;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder lastLoginAt(Instant lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder lastLoginIp(String lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
            return this;
        }

        public Builder resetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
            return this;
        }

        public Builder resetPasswordTokenExpiry(Instant resetPasswordTokenExpiry) {
            this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder verificationToken(String verificationToken) {
            this.verificationToken = verificationToken;
            return this;
        }

        public Builder verificationTokenExpiry(Instant verificationTokenExpiry) {
            this.verificationTokenExpiry = verificationTokenExpiry;
            return this;
        }

        public Builder twoFactorEnabled(boolean twoFactorEnabled) {
            this.twoFactorEnabled = twoFactorEnabled;
            return this;
        }

        public Builder twoFactorSecret(String twoFactorSecret) {
            this.twoFactorSecret = twoFactorSecret;
            return this;
        }

        public Builder lastPasswordChangedAt(Instant lastPasswordChangedAt) {
            this.lastPasswordChangedAt = lastPasswordChangedAt;
            return this;
        }

        /* ===== Build ===== */

        public AuthUserPgsqlEntity build() {
            return new AuthUserPgsqlEntity(
                    id,
                    username,
                    email,
                    password,
                    roles,
                    enabled,
                    accountNonExpired,
                    accountNonLocked,
                    credentialsNonExpired,
                    failedLoginAttempts,
                    lastFailedLoginAt,
                    lastFailedLoginIp,
                    createdAt,
                    updatedAt,
                    lastLoginAt,
                    lastLoginIp,
                    resetPasswordToken,
                    resetPasswordTokenExpiry,
                    emailVerified,
                    verificationToken,
                    verificationTokenExpiry,
                    twoFactorEnabled,
                    twoFactorSecret,
                    lastPasswordChangedAt
            );
        }
    }

}
package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserPgsqlEntity {
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
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    // Seguridad
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    private Instant lastFailedLoginAt;
    private String lastFailedLoginIp;

    // Auditoría
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant lastLoginAt;
    private String lastLoginIp;

    // Tokens
    private String resetPasswordToken;
    private Instant resetPasswordTokenExpiry;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    private String verificationToken;
    private Instant verificationTokenExpiry;

    // 2FA
    @Builder.Default
    private boolean twoFactorEnabled = false;

    private String twoFactorSecret;
}
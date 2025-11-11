package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.mapper;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;

public final class AuthUserPgsqlMapper {
    private AuthUserPgsqlMapper(){} // clase no instanciable, solo utilidad

    // -------------------------
    // Mapper: Domain → Entity
    // -------------------------
    public static AuthUserPgsqlEntity toEntity(AuthUser authUser) {
        if (authUser == null) return null;

        return AuthUserPgsqlEntity.builder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .email(authUser.getEmail())
                .password(authUser.getPassword())
                .roles(authUser.getRoles())
                .enabled(authUser.isEnabled())
                .accountNonExpired(authUser.isAccountNonExpired())
                .accountNonLocked(authUser.isAccountNonLocked())
                .credentialsNonExpired(authUser.isCredentialsNonExpired())
                .failedLoginAttempts(authUser.getFailedLoginAttempts())
                .lastFailedLoginAt(authUser.getLastFailedLoginAt())
                .lastFailedLoginIp(authUser.getLastFailedLoginIp())
                .createdAt(authUser.getCreatedAt())
                .updatedAt(authUser.getUpdatedAt())
                .lastLoginAt(authUser.getLastLoginAt())
                .lastLoginIp(authUser.getLastLoginIp())
                .resetPasswordToken(authUser.getResetPasswordToken())
                .resetPasswordTokenExpiry(authUser.getResetPasswordTokenExpiry())
                .emailVerified(authUser.isEmailVerified())
                .verificationToken(authUser.getVerificationToken())
                .verificationTokenExpiry(authUser.getVerificationTokenExpiry())
                .twoFactorEnabled(authUser.isTwoFactorEnabled())
                .twoFactorSecret(authUser.getTwoFactorSecret())
                .build();
    }

    // -------------------------
    // Mapper: Entity → Domain
    // -------------------------
    public static AuthUser toDomain(AuthUserPgsqlEntity entity) {
        if (entity == null) return null;

        // Usa el constructor principal (id, username, email, password)
        AuthUser authUser = new AuthUser(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword()
        );
        // El resto se asigna con setters
        authUser.setRoles(entity.getRoles());
        authUser.setEnabled(entity.isEnabled());
        authUser.setAccountNonExpired(entity.isAccountNonExpired());
        authUser.setAccountNonLocked(entity.isAccountNonLocked());
        authUser.setCredentialsNonExpired(entity.isCredentialsNonExpired());
        authUser.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        authUser.setLastFailedLoginAt(entity.getLastFailedLoginAt());
        authUser.setLastFailedLoginIp(entity.getLastFailedLoginIp());
        authUser.setCreatedAt(entity.getCreatedAt());
        authUser.setUpdatedAt(entity.getUpdatedAt());
        authUser.setLastLoginAt(entity.getLastLoginAt());
        authUser.setLastLoginIp(entity.getLastLoginIp());
        authUser.setResetPasswordToken(entity.getResetPasswordToken());
        authUser.setResetPasswordTokenExpiry(entity.getResetPasswordTokenExpiry());
        authUser.setEmailVerified(entity.isEmailVerified());
        authUser.setVerificationToken(entity.getVerificationToken());
        authUser.setVerificationTokenExpiry(entity.getVerificationTokenExpiry());
        authUser.setTwoFactorEnabled(entity.isTwoFactorEnabled());
        authUser.setTwoFactorSecret(entity.getTwoFactorSecret());

        return authUser;
    }
}
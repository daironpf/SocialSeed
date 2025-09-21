package com.our.socialseed.auth.infrastructure.persistence.pgsql.mapper;

import com.our.socialseed.auth.domain.model.AuthUser;
import com.our.socialseed.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;

public class AuthUserPgsqlMapper {
    private AuthUserPgsqlMapper(){} // clase no instanciable, solo utilidad

    public static AuthUserPgsqlEntity toEntity(AuthUser authUser) {
        AuthUserPgsqlEntity entity = new AuthUserPgsqlEntity();
        entity.setId(authUser.getId());
        entity.setUsername(authUser.getUsername());
        entity.setEmail(authUser.getEmail());
        entity.setPassword(authUser.getPassword());
        entity.setRoles(authUser.getRoles());
        entity.setEnabled(authUser.isEnabled());
        entity.setAccountNonExpired(authUser.isAccountNonExpired());
        entity.setAccountNonLocked(authUser.isAccountNonLocked());
        entity.setCredentialsNonExpired(authUser.isCredentialsNonExpired());
        entity.setFailedLoginAttempts(authUser.getFailedLoginAttempts());
        entity.setLastFailedLoginAt(authUser.getLastFailedLoginAt());
        entity.setLastFailedLoginIp(authUser.getLastFailedLoginIp());
        entity.setCreatedAt(authUser.getCreatedAt());
        entity.setUpdatedAt(authUser.getUpdatedAt());
        entity.setLastLoginAt(authUser.getLastLoginAt());
        entity.setLastLoginIp(authUser.getLastLoginIp());
        entity.setResetPasswordToken(authUser.getResetPasswordToken());
        entity.setResetPasswordTokenExpiry(authUser.getResetPasswordTokenExpiry());
        entity.setEmailVerified(authUser.isEmailVerified());
        entity.setVerificationToken(authUser.getVerificationToken());
        entity.setVerificationTokenExpiry(authUser.getVerificationTokenExpiry());
        entity.setTwoFactorEnabled(authUser.isTwoFactorEnabled());
        entity.setTwoFactorSecret(authUser.getTwoFactorSecret());
        return entity;
    }

    public static AuthUser toDomain(AuthUserPgsqlEntity entity) {
        AuthUser authUser = new AuthUser(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword()
        );
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

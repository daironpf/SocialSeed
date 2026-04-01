package com.socialseed.authservice.auth.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthUserTest {

    private static final UUID TEST_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "SecureP@ss1";

    @Test
    void constructor_shouldInitializeWithDefaultValues() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        assertThat(user.getId()).isEqualTo(TEST_ID);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(user.getRoles()).containsExactly("ROLE_USER");
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.getFailedLoginAttempts()).isZero();
        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.isTwoFactorEnabled()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getLastPasswordChangedAt()).isNotNull();
    }

    @Test
    void constructor_shouldSetRoleUserByDefault() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles()).contains("ROLE_USER");
    }

    @Test
    void shouldSetAndGetUsername() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setUsername("newusername");

        assertThat(user.getUsername()).isEqualTo("newusername");
    }

    @Test
    void shouldSetAndGetEmail() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setEmail("new@example.com");

        assertThat(user.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void shouldSetAndGetPassword() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setPassword("NewP@ssw0rd");

        assertThat(user.getPassword()).isEqualTo("NewP@ssw0rd");
    }

    @Test
    void shouldSetAndGetRoles() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Set<String> newRoles = Set.of("ROLE_USER", "ROLE_ADMIN");

        user.setRoles(newRoles);

        assertThat(user.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void shouldAddRoleToExistingRoles() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.getRoles().add("ROLE_ADMIN");

        assertThat(user.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void shouldTrackFailedLoginAttempts() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setFailedLoginAttempts(3);

        assertThat(user.getFailedLoginAttempts()).isEqualTo(3);
    }

    @Test
    void shouldLockAccountWhenAccountNonLockedIsFalse() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setAccountNonLocked(false);

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    void shouldDisableAccount() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setEnabled(false);

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    void shouldExpireCredentials() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setCredentialsNonExpired(false);

        assertThat(user.isCredentialsNonExpired()).isFalse();
    }

    @Test
    void shouldExpireAccount() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setAccountNonExpired(false);

        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    void shouldSetLastFailedLoginInfo() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant now = Instant.now();

        user.setLastFailedLoginAt(now);
        user.setLastFailedLoginIp("192.168.1.100");

        assertThat(user.getLastFailedLoginAt()).isEqualTo(now);
        assertThat(user.getLastFailedLoginIp()).isEqualTo("192.168.1.100");
    }

    @Test
    void shouldSetLastLoginInfo() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant now = Instant.now();

        user.setLastLoginAt(now);
        user.setLastLoginIp("10.0.0.1");

        assertThat(user.getLastLoginAt()).isEqualTo(now);
        assertThat(user.getLastLoginIp()).isEqualTo("10.0.0.1");
    }

    @Test
    void shouldSetVerificationToken() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant expiry = Instant.now().plusSeconds(86400);

        user.setVerificationToken("verification-token-123");
        user.setVerificationTokenExpiry(expiry);

        assertThat(user.getVerificationToken()).isEqualTo("verification-token-123");
        assertThat(user.getVerificationTokenExpiry()).isEqualTo(expiry);
    }

    @Test
    void shouldSetResetPasswordToken() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant expiry = Instant.now().plusSeconds(3600);

        user.setResetPasswordToken("reset-token-456");
        user.setResetPasswordTokenExpiry(expiry);

        assertThat(user.getResetPasswordToken()).isEqualTo("reset-token-456");
        assertThat(user.getResetPasswordTokenExpiry()).isEqualTo(expiry);
    }

    @Test
    void shouldEnableTwoFactor() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setTwoFactorEnabled(true);
        user.setTwoFactorSecret("TOTP-SECRET-KEY");

        assertThat(user.isTwoFactorEnabled()).isTrue();
        assertThat(user.getTwoFactorSecret()).isEqualTo("TOTP-SECRET-KEY");
    }

    @Test
    void shouldSetEmailChangeInfo() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant expiry = Instant.now().plusSeconds(86400);

        user.setPendingEmail("newemail@example.com");
        user.setEmailChangeToken("change-token-789");
        user.setEmailChangeTokenExpiry(expiry);

        assertThat(user.getPendingEmail()).isEqualTo("newemail@example.com");
        assertThat(user.getEmailChangeToken()).isEqualTo("change-token-789");
        assertThat(user.getEmailChangeTokenExpiry()).isEqualTo(expiry);
    }

    @Test
    void shouldMarkEmailAsUnverified() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        user.setEmailVerified(false);

        assertThat(user.isEmailVerified()).isFalse();
    }

    @Test
    void shouldUpdateTimestamps() {
        AuthUser user = new AuthUser(TEST_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        Instant newTime = Instant.now().plusSeconds(3600);

        user.setUpdatedAt(newTime);
        user.setLastPasswordChangedAt(newTime);

        assertThat(user.getUpdatedAt()).isEqualTo(newTime);
        assertThat(user.getLastPasswordChangedAt()).isEqualTo(newTime);
    }
}

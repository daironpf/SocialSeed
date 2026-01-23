package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class LoginTrackingIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository jpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UUID userId;
    private String email = "test@example.com";
    private String password = "Password123!";
    private String ip = "192.168.1.1";

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
        userId = UUID.randomUUID();
        AuthUser user = new AuthUser(userId, "testuser", email, passwordEncoder.encode(password));
        authUserRepository.save(user);
    }

    @Test
    void shouldIncrementFailedAttemptsOnInvalidPassword() {
        assertThatThrownBy(() -> authService.login(email, "wrongpassword", ip))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("auth.error.invalid_credentials");

        AuthUser updatedUser = authUserRepository.findByEmail(email).orElseThrow();
        assertThat(updatedUser.getFailedLoginAttempts()).isEqualTo(1);
        assertThat(updatedUser.getLastFailedLoginIp()).isEqualTo(ip);
        assertThat(updatedUser.getLastFailedLoginAt()).isNotNull();
    }

    @Test
    void shouldLockAccountAfterFiveFailedAttempts() {
        for (int i = 1; i <= 5; i++) {
            assertThatThrownBy(() -> authService.login(email, "wrongpassword", ip))
                .isInstanceOf(BusinessException.class);
        }

        AuthUser updatedUser = authUserRepository.findByEmail(email).orElseThrow();
        assertThat(updatedUser.getFailedLoginAttempts()).isEqualTo(5);
        assertThat(updatedUser.isAccountNonLocked()).isFalse();

        assertThatThrownBy(() -> authService.login(email, password, ip))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("auth.error.account_locked");
    }

    @Test
    void shouldResetFailedAttemptsOnSuccessfulLogin() {
        // Record some failures
        assertThatThrownBy(() -> authService.login(email, "wrongpassword", ip))
                .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> authService.login(email, "wrongpassword", ip))
                .isInstanceOf(BusinessException.class);

        AuthUser userWithFailures = authUserRepository.findByEmail(email).orElseThrow();
        assertThat(userWithFailures.getFailedLoginAttempts()).isEqualTo(2);

        // Successful login
        authService.login(email, password, "10.0.0.1");

        AuthUser successfulUser = authUserRepository.findByEmail(email).orElseThrow();
        assertThat(successfulUser.getFailedLoginAttempts()).isEqualTo(0);
        assertThat(successfulUser.getLastLoginIp()).isEqualTo("10.0.0.1");
        assertThat(successfulUser.getLastLoginAt()).isNotNull();
        assertThat(successfulUser.getLastFailedLoginAt()).isNull();
    }
}

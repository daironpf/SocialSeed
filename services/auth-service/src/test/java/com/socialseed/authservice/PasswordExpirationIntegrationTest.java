package com.socialseed.authservice;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.infrastructure.scheduler.PasswordExpirationScheduler;
import com.socialseed.authservice.auth.infrastructure.service.AuthServiceImpl;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PasswordExpirationIntegrationTest {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private PasswordExpirationScheduler passwordExpirationScheduler;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    private UUID userId;
    private final String email = "expired@example.com";
    private final String password = "Password123!";

    @BeforeEach
    @Transactional
    void setUp() {
        authUserRepository.deleteAll();
        userId = UUID.randomUUID();
        AuthUser authUser = new AuthUser(userId, "expireduser", email, password);
        authService.register(authUser, userId);
    }

    @Test
    @Transactional
    void shouldFailLoginWhenPasswordIsExpired() {
        // 1. Manually backdate password change to exceed 90 days
        var entity = authUserRepository.findById(userId).orElseThrow();
        entity.setLastPasswordChangedAt(Instant.now().minus(91, ChronoUnit.DAYS));
        authUserRepository.saveAndFlush(entity);

        // 2. Trigger scheduler
        passwordExpirationScheduler.checkPasswordExpiration();

        // 3. Clear persistence context to force reload from DB
        entityManager.flush();
        entityManager.clear();

        // 4. Verify flagging in DB
        var updatedEntity = authUserRepository.findById(userId).orElseThrow();
        assertFalse(updatedEntity.isCredentialsNonExpired(), "Credentials should be flagged as expired");

        // 5. Attempt login and expect failure
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(email, password, "127.0.0.1");
        });

        assertEquals(ErrorCode.PASSWORD_EXPIRED, exception.getErrorCode());
    }

    @Test
    @Transactional
    void shouldAllowLoginAfterPasswordChange() {
        // 1. Flag as expired
        var entity = authUserRepository.findById(userId).orElseThrow();
        entity.setCredentialsNonExpired(false);
        authUserRepository.saveAndFlush(entity);

        // 2. Change password
        String newPassword = "NewPassword123!";
        authService.changePassword(userId, password, newPassword);

        // 3. Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // 4. Verify flag is reset
        var updatedEntity = authUserRepository.findById(userId).orElseThrow();
        assertTrue(updatedEntity.isCredentialsNonExpired(), "Credentials should be marked as non-expired after change");
        assertNotNull(updatedEntity.getLastPasswordChangedAt());

        // 5. Attempt login and expect success
        AuthResult result = authService.login(email, newPassword, "127.0.0.1");
        assertNotNull(result.token());
    }
}

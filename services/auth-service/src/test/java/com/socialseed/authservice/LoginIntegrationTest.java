package com.socialseed.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.entry.rest.dto.LoginRequestDTO;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_EMAIL = "logintest@example.com";
    private static final String TEST_PASSWORD = "TestPassword123!";

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("loginuser")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build();
        authUserRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO(TEST_EMAIL, TEST_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());

        // Verify metadata updated
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertNotNull(updatedUser.getLastLoginAt());
        assertEquals(0, updatedUser.getFailedLoginAttempts());
    }

    @Test
    void shouldFailWithInvalidCredentials() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("loginuser")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build();
        authUserRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO(TEST_EMAIL, "WrongPassword123!");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // Verify failed attempt tracked
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertEquals(1, updatedUser.getFailedLoginAttempts());
        assertNotNull(updatedUser.getLastFailedLoginAt());
    }

    @Test
    void shouldLockAccountAfterFiveFailedAttempts() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("loginuser")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build();
        authUserRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO(TEST_EMAIL, "WrongPassword123!");

        // Act: Attempt 5 failed logins
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        // Assert: Account should be locked
        AuthUserPgsqlEntity lockedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertFalse(lockedUser.isAccountNonLocked());
        assertEquals(5, lockedUser.getFailedLoginAttempts());

        // Verify locked account cannot login even with correct password
        LoginRequestDTO correctRequest = new LoginRequestDTO(TEST_EMAIL, TEST_PASSWORD);
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldResetFailedAttemptsOnSuccessfulLogin() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("loginuser")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .failedLoginAttempts(3)
                .build();
        authUserRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO(TEST_EMAIL, TEST_PASSWORD);

        // Act
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertEquals(0, updatedUser.getFailedLoginAttempts());
        assertNull(updatedUser.getLastFailedLoginAt());
    }
}

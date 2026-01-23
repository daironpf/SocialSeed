package com.socialseed.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.entry.rest.dto.ResendVerificationEmailRequestDTO;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResendVerificationEmailIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_EMAIL = "resendtest@example.com";

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldResendVerificationEmailSuccessfully() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("resenduser")
                .email(TEST_EMAIL)
                .password("password")
                .emailVerified(false)
                .build();
        authUserRepository.save(user);

        ResendVerificationEmailRequestDTO request = new ResendVerificationEmailRequestDTO(TEST_EMAIL);

        // Act
        mockMvc.perform(post("/auth/resend-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertNotNull(updatedUser.getVerificationToken());
        assertNotNull(updatedUser.getVerificationTokenExpiry());
    }

    @Test
    void shouldFailIfUserNotFound() throws Exception {
        // Arrange
        ResendVerificationEmailRequestDTO request = new ResendVerificationEmailRequestDTO("nonexistent@example.com");

        // Act & Assert
        mockMvc.perform(post("/auth/resend-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailIfAlreadyVerified() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("resenduser")
                .email(TEST_EMAIL)
                .password("password")
                .emailVerified(true) // Already verified
                .build();
        authUserRepository.save(user);

        ResendVerificationEmailRequestDTO request = new ResendVerificationEmailRequestDTO(TEST_EMAIL);

        // Act & Assert
        mockMvc.perform(post("/auth/resend-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

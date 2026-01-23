package com.socialseed.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.entry.rest.dto.ResetPasswordRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResetPasswordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldResetPasswordWithValidToken() throws Exception {
        // Arrange
        String token = "valid-token";
        String newPassword = "NewPassword123!";
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("resetuser")
                .email("reset@example.com")
                .password("oldPassword")
                .resetPasswordToken(token)
                .resetPasswordTokenExpiry(Instant.now().plusSeconds(3600))
                .build();
        authUserRepository.save(user);

        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO(token, newPassword);

        // Act
        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail("reset@example.com").orElseThrow();
        assertNull(updatedUser.getResetPasswordToken());
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }

    @Test
    void shouldFailWithInvalidToken() throws Exception {
        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO("invalid-token", "NewPassword123!");

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void shouldFailWithExpiredToken() throws Exception {
        String token = "expired-token";
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("expireduser")
                .email("expired@example.com")
                .password("oldPassword")
                .resetPasswordToken(token)
                .resetPasswordTokenExpiry(Instant.now().minusSeconds(3600))
                .build();
        authUserRepository.save(user);

        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO(token, "NewPassword123!");

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

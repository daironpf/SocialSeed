package com.socialseed.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.entry.rest.dto.ForgotPasswordRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ForgotPasswordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldGenerateTokenForValidEmail() throws Exception {
        // Arrange
        AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
        authUserRepository.save(user);

        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("test@example.com");

        // Act
        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        // Assert
        AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail("test@example.com").orElseThrow();
        assertNotNull(updatedUser.getResetPasswordToken());
    }

    @Test
    void shouldReturnOkForUnknownEmail_ToPreventEnumeration() throws Exception {
         // Arrange
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("unknown@example.com");

        // Act
        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidEmailFormat() throws Exception {
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("invalid-email");

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

package com.socialseed.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.entry.rest.dto.VerifyEmailRequestDTO;
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

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VerifyEmailIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private AuthUserPgsqlRepository authUserRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private static final String TEST_EMAIL = "verifytest@example.com";
        private static final String TEST_TOKEN = "valid-verification-token";

        @BeforeEach
        void setUp() {
                authUserRepository.deleteAll();
        }

        @Test
        void shouldVerifyEmailSuccessfully() throws Exception {
                // Arrange
                AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                                .id(UUID.randomUUID())
                                .username("verifyuser")
                                .email(TEST_EMAIL)
                                .password("password")
                                .emailVerified(false)
                                .verificationToken(TEST_TOKEN)
                                .verificationTokenExpiry(Instant.now().plusSeconds(3600))
                                .build();
                authUserRepository.save(user);

                VerifyEmailRequestDTO request = new VerifyEmailRequestDTO(TEST_TOKEN);

                // Act
                mockMvc.perform(post("/auth/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                // Assert
                AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail(TEST_EMAIL).orElseThrow();
                assertTrue(updatedUser.isEmailVerified());
                assertNull(updatedUser.getVerificationToken());
                assertNull(updatedUser.getVerificationTokenExpiry());
        }

        @Test
        void shouldVerifyEmailViaGetEndpoint() throws Exception {
                // Arrange
                String getToken = "get-verification-token";
                AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                                .id(UUID.randomUUID())
                                .username("getverifyuser")
                                .email("getverify@example.com")
                                .password("password")
                                .emailVerified(false)
                                .verificationToken(getToken)
                                .verificationTokenExpiry(Instant.now().plusSeconds(3600))
                                .build();
                authUserRepository.save(user);

                // Act - Using GET /auth/verify?token=...
                mockMvc.perform(get("/auth/verify")
                                .param("token", getToken))
                                .andExpect(status().isOk());

                // Assert
                AuthUserPgsqlEntity updatedUser = authUserRepository.findByEmail("getverify@example.com").orElseThrow();
                assertTrue(updatedUser.isEmailVerified());
                assertNull(updatedUser.getVerificationToken());
        }

        @Test
        void shouldFailWithInvalidToken() throws Exception {
                // Arrange
                VerifyEmailRequestDTO request = new VerifyEmailRequestDTO("invalid-token");

                // Act & Assert
                mockMvc.perform(post("/auth/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailWithInvalidTokenViaGet() throws Exception {
                // Act & Assert - GET endpoint with invalid token
                mockMvc.perform(get("/auth/verify")
                                .param("token", "invalid-get-token"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailWithExpiredToken() throws Exception {
                // Arrange
                AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                                .id(UUID.randomUUID())
                                .username("verifyuser")
                                .email(TEST_EMAIL)
                                .password("password")
                                .emailVerified(false)
                                .verificationToken(TEST_TOKEN)
                                .verificationTokenExpiry(Instant.now().minusSeconds(3600)) // Expired
                                .build();
                authUserRepository.save(user);

                VerifyEmailRequestDTO request = new VerifyEmailRequestDTO(TEST_TOKEN);

                // Act & Assert
                mockMvc.perform(post("/auth/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailIfAlreadyVerified() throws Exception {
                // Arrange
                AuthUserPgsqlEntity user = AuthUserPgsqlEntity.builder()
                                .id(UUID.randomUUID())
                                .username("verifyuser")
                                .email(TEST_EMAIL)
                                .password("password")
                                .emailVerified(true) // Already verified
                                .verificationToken(TEST_TOKEN)
                                .verificationTokenExpiry(Instant.now().plusSeconds(3600))
                                .build();
                authUserRepository.save(user);

                VerifyEmailRequestDTO request = new VerifyEmailRequestDTO(TEST_TOKEN);

                // Act & Assert
                mockMvc.perform(post("/auth/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }
}

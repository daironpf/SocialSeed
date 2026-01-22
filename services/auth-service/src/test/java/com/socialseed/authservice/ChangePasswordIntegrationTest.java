package com.socialseed.authservice;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.entry.rest.dto.ChangePasswordRequestDTO;
import com.socialseed.apiresponse.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChangePasswordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private final String oldPassword = "currentPassword123";
    private final String newPassword = "newSecurePassword456";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        AuthUser user = new AuthUser(userId, "testuser", "test@example.com", passwordEncoder.encode(oldPassword));
        authUserRepository.save(user);

        RefreshToken token = new RefreshToken(UUID.randomUUID(), "some-token", userId, Instant.now().plusSeconds(3600), false, false);
        refreshTokenRepository.save(token);
    }

    @Test
    @WithMockUser
    void shouldChangePasswordAndInvalidateTokens() throws Exception {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO(oldPassword, newPassword);

        mockMvc.perform(post("/auth/" + userId + "/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify password updated
        AuthUser updatedUser = authUserRepository.findById(userId).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));

        // Verify tokens invalidated
        assertTrue(refreshTokenRepository.findByUserId(userId).isEmpty());
    }

    @Test
    @WithMockUser
    void shouldFailWithIncorrectCurrentPassword() throws Exception {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("wrongPassword", newPassword);

        mockMvc.perform(post("/auth/" + userId + "/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // Verify password NOT updated
        AuthUser user = authUserRepository.findById(userId).orElseThrow();
        assertTrue(passwordEncoder.matches(oldPassword, user.getPassword()));
    }
}

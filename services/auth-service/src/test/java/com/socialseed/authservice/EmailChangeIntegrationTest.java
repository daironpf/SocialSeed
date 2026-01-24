package com.socialseed.authservice;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.infrastructure.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmailChangeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    private UUID userId;
    private final String username = "emailchanger";
    private final String currentEmail = "old@example.com";
    private final String newEmail = "new@example.com";

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
        userId = UUID.randomUUID();
        AuthUser authUser = new AuthUser(userId, username, currentEmail, "Password123!");
        authService.register(authUser, userId);
    }

    @Test
    @WithMockUser(username = "emailchanger")
    void shouldInitiateAndVerifyEmailChange() throws Exception {
        // 1. Initiate Change
        String initiateJson = """
                {
                    "newEmail": "%s"
                }
                """.formatted(newEmail);

        mockMvc.perform(post("/auth/change-email")
                .contentType("application/json")
                .content(initiateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Verification email sent to new address."));

        // Verify Pending State
        AuthUserPgsqlEntity user = authUserRepository.findById(userId).orElseThrow();
        assertEquals(currentEmail, user.getEmail());
        assertEquals(newEmail, user.getPendingEmail());
        assertNotNull(user.getEmailChangeToken());

        // 2. Verify Change
        String verifyJson = """
                {
                    "token": "%s"
                }
                """.formatted(user.getEmailChangeToken());

        mockMvc.perform(post("/auth/verify-email-change")
                .contentType("application/json")
                .content(verifyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email changed successfully."));

        // Verify Final State
        AuthUserPgsqlEntity updatedUser = authUserRepository.findById(userId).orElseThrow();
        assertEquals(newEmail, updatedUser.getEmail());
        assertNull(updatedUser.getPendingEmail());
        assertNull(updatedUser.getEmailChangeToken());
    }

    @Test
    @WithMockUser(username = "emailchanger")
    void shouldFailIfEmailAlreadyExists() throws Exception {
        // Create another user with the target email
        AuthUser otherUser = new AuthUser(UUID.randomUUID(), "other", newEmail, "Pass123!");
        authService.register(otherUser, otherUser.getId());

        String initiateJson = """
                {
                    "newEmail": "%s"
                }
                """.formatted(newEmail);

        mockMvc.perform(post("/auth/change-email")
                .contentType("application/json")
                .content(initiateJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }
}

package com.socialseed.authservice;

import com.socialseed.authservice.auth.domain.model.AuthUser;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GetUserRolesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    private UUID userId;
    private final String username = "testuser";
    private final String email = "testuser@example.com";

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
        userId = UUID.randomUUID();
        AuthUser authUser = new AuthUser(userId, username, email, "Password123!");
        authService.register(authUser, userId);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldReturnRolesForSelf() throws Exception {
        mockMvc.perform(get("/auth/" + userId + "/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.message").value("User roles retrieved successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnRolesForAdminEvaluatingAnyUser() throws Exception {
        mockMvc.perform(get("/auth/" + userId + "/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value("ROLE_USER"));
    }

    @Test
    @WithMockUser(username = "otheruser", roles = {"USER"})
    void shouldForbidAccessToOtherUserRoles() throws Exception {
        mockMvc.perform(get("/auth/" + userId + "/roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedForAnonymous() throws Exception {
        mockMvc.perform(get("/auth/" + userId + "/roles"))
                .andExpect(status().isUnauthorized());
    }
}

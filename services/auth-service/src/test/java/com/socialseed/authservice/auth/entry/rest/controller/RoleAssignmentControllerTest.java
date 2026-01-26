package com.socialseed.authservice.auth.entry.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialseed.authservice.auth.application.usecase.AssignRoleToUser;
import com.socialseed.authservice.auth.entry.rest.dto.AssignRoleRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleAssignmentController.class)
class RoleAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignRoleToUser assignRoleToUser;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void assignRole_ShouldReturnSuccess_WhenAdminAssignsRole() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String role = "ROLE_ADMIN";
        Set<String> expectedRoles = new HashSet<>();
        expectedRoles.add("ROLE_USER");
        expectedRoles.add("ROLE_ADMIN");

        AssignRoleRequestDTO request = new AssignRoleRequestDTO(userId.toString(), role);
        
        when(assignRoleToUser.execute(any(UUID.class), any(String.class), any(UUID.class)))
                .thenReturn(expectedRoles);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/roles/assign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[?(@ == 'ROLE_ADMIN')]").exists())
                .andExpect(jsonPath("$.message").value("auth.role.assign.success"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void assignRole_ShouldReturnForbidden_WhenNonAdminTriesToAssignRole() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String role = "ROLE_ADMIN";
        AssignRoleRequestDTO request = new AssignRoleRequestDTO(userId.toString(), role);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/roles/assign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void assignRole_ShouldReturnUnauthorized_WhenUnauthenticatedUserTriesToAssignRole() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String role = "ROLE_ADMIN";
        AssignRoleRequestDTO request = new AssignRoleRequestDTO(userId.toString(), role);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/roles/assign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void assignRole_ShouldReturnBadRequest_WhenInvalidUUID() throws Exception {
        // Given
        String invalidUserId = "invalid-uuid";
        String role = "ROLE_ADMIN";
        AssignRoleRequestDTO request = new AssignRoleRequestDTO(invalidUserId, role);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/roles/assign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void assignRole_ShouldReturnBadRequest_WhenInvalidRole() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String invalidRole = "INVALID_ROLE";
        AssignRoleRequestDTO request = new AssignRoleRequestDTO(userId.toString(), invalidRole);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/roles/assign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
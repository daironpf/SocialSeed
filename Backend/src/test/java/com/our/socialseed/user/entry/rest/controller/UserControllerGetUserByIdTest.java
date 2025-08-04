package com.our.socialseed.user.entry.rest.controller;

import com.our.socialseed.user.application.usecase.GetUserById;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.entry.rest.dto.UserResponseDTO;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para GetUserById:
 * ✅ 1. Happy Path – Usuario encontrado (200 OK)
 * ✅ 2. Usuario no encontrado – 404 Not Found
 * ✅ 3. UUID inválido – 400 Bad Request
 * ✅ 4. Falla inesperada – 500 Internal Server Error
 */
@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerGetUserByIdTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserUseCases userUseCases;
    @Autowired private GetUserById getUserById;

    private UUID validUserId;
    private User user;

    @BeforeEach
    void setUp() {
        validUserId = UUID.randomUUID();
        user = new User(validUserId, "username", "email@example.com", "hashedPassword", "Full Name");
        when(userUseCases.getUserById()).thenReturn(getUserById);
    }

    @Test
    @WithMockUser
    void shouldReturnUser_whenUserExists() throws Exception {
        when(getUserById.execute(validUserId)).thenReturn(Optional.of(user));
        UserResponseDTO expectedDto = UserRestMapper.toResponse(user);

        mockMvc.perform(get("/api/users/{id}", validUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedDto.id().toString()))
                .andExpect(jsonPath("$.username").value(expectedDto.username()))
                .andExpect(jsonPath("$.email").value(expectedDto.email()))
                .andExpect(jsonPath("$.fullName").value(expectedDto.fullName()));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        when(getUserById.execute(validUserId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", validUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenIdIsNotUUID() throws Exception {
        mockMvc.perform(get("/api/users/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerError_whenUseCaseFails() throws Exception {
        when(getUserById.execute(validUserId)).thenThrow(new RuntimeException("Unexpected"));

        mockMvc.perform(get("/api/users/{id}", validUserId))
                .andExpect(status().isInternalServerError());
    }
}

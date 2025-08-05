package com.our.socialseed.user.entry.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.our.socialseed.user.application.usecase.UpdateUser;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import com.our.socialseed.user.entry.rest.dto.UserUpdateRequestDTO;
import com.our.socialseed.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerUpdateUserTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserUseCases userUseCases;
    @Autowired private UpdateUser updateUser;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(userUseCases, updateUser);
        when(userUseCases.updateUser()).thenReturn(updateUser);
    }

    @Test
    @WithMockUser
    void shouldReturnNoContent_whenUserUpdatedSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequestDTO request = new UserUpdateRequestDTO(
                "newUsername",
                "new.email@example.com",
                "New Full Name"
        );

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(updateUser).execute(eq(userId), any(User.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenRequestValidationFails() throws Exception {
        UUID userId = UUID.randomUUID();
        // Invalid: username is blank, email invalid, fullName null
        UserUpdateRequestDTO invalidRequest = new UserUpdateRequestDTO(
                "   ",
                "not-an-email",
                null
        );

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(updateUser);
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerError_whenUseCaseFails() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequestDTO request = new UserUpdateRequestDTO(
                "validUsername",
                "email@example.com",
                "Valid Full Name"
        );

        doThrow(new RuntimeException("Unexpected error"))
                .when(updateUser).execute(eq(userId), any(User.class));

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}

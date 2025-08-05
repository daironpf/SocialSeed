package com.our.socialseed.user.entry.rest.controller;

import com.our.socialseed.user.application.usecase.ChangeUserPassword;
import com.our.socialseed.user.application.usecase.GetUserById;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerChangePasswordTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserUseCases userUseCases;
    @Autowired private ChangeUserPassword changeUserPassword;

    @BeforeEach
    void setUp() {
        reset(userUseCases, changeUserPassword); // 👈 Limpia cualquier stubbing previo
        when(userUseCases.changeUserPassword()).thenReturn(changeUserPassword);
    }

    @Test
    @WithMockUser
    void shouldReturnNoContent_whenPasswordChangedSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/api/users/{id}/change-password", userId)
                        .param("currentPassword", "oldPass123")
                        .param("newPassword", "newPass456")
                        .with(csrf())  // 👈 agrega el token para el auth
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(changeUserPassword).execute(userId, "oldPass123", "newPass456");
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerError_whenUseCaseThrowsException() throws Exception {
        UUID userId = UUID.randomUUID();
        doThrow(new RuntimeException("Unexpected error"))
                .when(changeUserPassword).execute(any(), any(), any());

        mockMvc.perform(post("/api/users/{id}/change-password", userId)
                        .param("currentPassword", "oldPass123")
                        .param("newPassword", "newPass456")
                        .with(csrf())  // 👈 agrega el token para el auth
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
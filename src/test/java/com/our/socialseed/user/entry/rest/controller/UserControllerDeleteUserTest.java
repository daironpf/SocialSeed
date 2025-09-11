package com.our.socialseed.user.entry.rest.controller;

import com.our.socialseed.user.application.usecase.DeleteUser;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para DeleteUser:
 * ✅ 1. Happy Path – Eliminación exitosa (204 No Content)
 * ✅ 2. UUID inválido – 400 Bad Request
 * ✅ 3. Falla en la lógica de negocio – 500 Internal Server Error
 */
@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerDeleteUserTest {
    // <editor-fold desc="Dependencias e inicialización">
    @Autowired private MockMvc mockMvc;
    @Autowired private UserUseCases userUseCases;
    @Autowired private DeleteUser deleteUser;

    private UUID validUserId;

    @BeforeEach
    void setUp() {
        validUserId = UUID.randomUUID();
        when(userUseCases.deleteUser()).thenReturn(deleteUser);
    }
    // </editor-fold>

    @Test
    @WithMockUser
    void shouldReturnNoContent_whenUserIsDeletedSuccessfully() throws Exception {
        doNothing().when(deleteUser).execute(validUserId);

        mockMvc.perform(delete("/api/users/{id}", validUserId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenIdIsNotUUID() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", "invalid-uuid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerError_whenUseCaseThrowsException() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(deleteUser).execute(validUserId);

        mockMvc.perform(delete("/api/users/{id}", validUserId)
                        .with(csrf()))
                .andExpect(status().isInternalServerError());

    }
}

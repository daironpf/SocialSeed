package com.our.socialseed.user.entry.rest.controller;

import com.our.socialseed.user.application.usecase.GetAllUsers;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import com.our.socialseed.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerGetAllUsersTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserUseCases userUseCases;
    @MockBean private GetAllUsers getAllUsers;

    @BeforeEach
    void setUp() {
        // Devuelve el mock de GetAllUsers del TestConfiguration
        when(userUseCases.getAllUsers()).thenReturn(getAllUsers);
        System.out.println("getAllUsers instance: " + getAllUsers.getClass());
    }

    @Test
    @WithMockUser
    void shouldReturnUserList_whenUsersExist() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "testuser",
                "test@example.com",
                "encodedPassword",
                "Test User"
        );

        when(getAllUsers.execute()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].fullName").value("Test User"));
    }

    @Test
    @WithMockUser
    void shouldReturnNoContent_whenNoUsersExist() throws Exception {
        when(getAllUsers.execute()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerError_whenUseCaseThrowsException() throws Exception {
        when(getAllUsers.execute()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isInternalServerError());
    }
}

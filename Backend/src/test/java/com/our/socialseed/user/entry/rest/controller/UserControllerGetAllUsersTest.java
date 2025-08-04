package com.our.socialseed.user.entry.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.our.socialseed.user.application.usecase.GetAllUsers;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.entry.rest.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
    @Autowired private UserUseCases userUseCases;
    @Autowired private GetAllUsers getAllUsers;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserUseCases userUseCases() {
            return mock(UserUseCases.class);
        }

        @Bean
        public GetAllUsers getAllUsers() {
            return mock(GetAllUsers.class);
        }
    }

    @BeforeEach
    void setUp() {
        when(userUseCases.getAllUsers()).thenReturn(getAllUsers);
    }

    @Test
    @WithMockUser
    void shouldReturnUserList_whenUsersExist() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "testuser", "test@example.com", "Test User", "Test User");
        when(getAllUsers.execute()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
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
package com.our.socialseed.user.entry.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.our.socialseed.user.application.usecase.*;
import com.our.socialseed.user.config.MockUserUseCasesConfig;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.entry.rest.dto.UserCreateRequestDTO;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerCreateUserTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserUseCases userUseCases;
    @Autowired private CreateUser createUser;

    @BeforeEach
    void setUp() {
        when(userUseCases.createUser()).thenReturn(createUser);
    }

    @Test
    @WithMockUser
    void createUser_shouldReturnCreatedUser() throws Exception {
        // Arrange: DTO de entrada
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "password123",
                "John Doe"
        );

        // Mapper al modelo de dominio
        User domainUser = UserRestMapper.toDomain(requestDTO);
        UUID userId = UUID.randomUUID();
        domainUser.setId(userId);

        // Stubbing: cuando se llame a userUseCases.createUser() → devuelve mock de CreateUser
        when(userUseCases.createUser()).thenReturn(createUser);
        // Stubbing: cuando se llame a createUser.execute() → devuelve el usuario creado
        when(createUser.execute(any(User.class))).thenReturn(domainUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }
}

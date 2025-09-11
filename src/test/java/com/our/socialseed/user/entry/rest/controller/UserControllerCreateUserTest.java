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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* Tests para CreateUser:
✅ 1. Happy Path (Caso exitoso)
    shouldReturnOk_whenUserIsCreatedSuccessfully

❌ 2. Tests de validación del input – 400 Bad Request
Username
    shouldReturnBadRequest_whenUsernameIsNull
    shouldReturnBadRequest_whenUsernameIsBlank
    shouldReturnBadRequest_whenUsernameExceedsMaxLength

Email
    shouldReturnBadRequest_whenEmailIsNull
    shouldReturnBadRequest_whenEmailIsBlank
    shouldReturnBadRequest_whenEmailIsInvalid

Password
    shouldReturnBadRequest_whenPasswordIsNull
    shouldReturnBadRequest_whenPasswordIsBlank
    shouldReturnBadRequest_whenPasswordTooShort
    shouldReturnBadRequest_whenPasswordTooLong

Full Name
    shouldReturnBadRequest_whenFullNameIsNull
    shouldReturnBadRequest_whenFullNameIsBlank
    shouldReturnBadRequest_whenFullNameExceedsMaxLength

 */
@WebMvcTest(UserController.class)
@Import(MockUserUseCasesConfig.class)
class UserControllerCreateUserTest {
    // <editor-fold desc="dependencia y iniciacion">
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserUseCases userUseCases;
    @Autowired private CreateUser createUser;

    @BeforeEach
    void setUp() {
        when(userUseCases.createUser()).thenReturn(createUser);
    }
    // </editor-fold>

    @Test
    @WithMockUser
    void shouldReturnOk_whenUserIsCreatedSuccessfully() throws Exception {
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

    // <editor-fold desc="Username">
    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenUsernameIsNull() throws Exception {
        // Arrange: username es null
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                null,
                "john@example.com",
                "password123",
                "John Doe"
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'username')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {
        // Arrange: DTO con username en blanco
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "   ", // username en blanco (espacios)
                "john@example.com",
                "password123",
                "John Doe"
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'username')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenUsernameExceedsMaxLength() throws Exception {
        // Arrange: username con más de 30 caracteres
        String longUsername = "a".repeat(31); // 31 caracteres

        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                longUsername,
                "john@example.com",
                "password123",
                "John Doe"
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'username')]").exists());
    }
    // </editor-fold>

    // <editor-fold desc="Email">
    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenEmailIsNull() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                null,
                "password123",
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'email')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenEmailIsBlank() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "",
                "password123",
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'email')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "not-an-email",
                "password123",
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'email')]").exists());
    }
    // </editor-fold>

    // <editor-fold desc="Password">
    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenPasswordIsNull() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                null,
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'password')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "",
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'password')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenPasswordTooShort() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "123", // too short
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'password')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenPasswordTooLong() throws Exception {
        String longPassword = "a".repeat(61); // 61 chars, max is likely 60

        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                longPassword,
                "John Doe"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'password')]").exists());
    }
    // </editor-fold>

    // <editor-fold desc="Full Name">
    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenFullNameIsNull() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "password123",
                null
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'fullName')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenFullNameIsBlank() throws Exception {
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "password123",
                ""
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'fullName')]").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequest_whenFullNameExceedsMaxLength() throws Exception {
        String longFullName = "a".repeat(101); // Límite supongamos que es 100

        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO(
                "johndoe",
                "john@example.com",
                "password123",
                longFullName
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'fullName')]").exists());
    }
    // </editor-fold>
}

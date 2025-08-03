package com.our.socialseed.user.entry.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.our.socialseed.user.application.usecase.*;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.entry.rest.dto.UserCreateRequestDTO;
import com.our.socialseed.user.entry.rest.dto.UserUpdateRequestDTO;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@Import(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CreateUser createUser;
    @Mock
    private GetUserById getUserById;
    @Mock
    private GetAllUsers getAllUsers;
    @Mock
    private UpdateUser updateUser;
    @Mock
    private DeleteUser deleteUser;
    @Mock
    private ChangeUserPassword changeUserPassword;

    private UserUseCases userUseCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userUseCases = mock(UserUseCases.class);
        when(userUseCases.createUser()).thenReturn(createUser);
        when(userUseCases.getUserById()).thenReturn(getUserById);
        when(userUseCases.getAllUsers()).thenReturn(getAllUsers);
        when(userUseCases.updateUser()).thenReturn(updateUser);
        when(userUseCases.deleteUser()).thenReturn(deleteUser);
        when(userUseCases.changeUserPassword()).thenReturn(changeUserPassword);
    }

    private UserController createController() {
        return new UserController(userUseCases);
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("johndoe", "john@example.com", "password123", "John Doe");
        User user = UserRestMapper.toDomain(dto);
        user.setId(UUID.randomUUID());

        when(createUser.execute(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    void getUserById_shouldReturnUserResponseDTO() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "johndoe", "john@example.com", "pass", "John Doe");

        when(getUserById.execute(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "user1", "user1@example.com", "pass1", "User One"),
                new User(UUID.randomUUID(), "user2", "user2@example.com", "pass2", "User Two")
        );

        when(getAllUsers.execute()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()));
    }

    @Test
    void getAllUsers_shouldReturnNoContentWhenEmpty() throws Exception {
        when(getAllUsers.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUser_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequestDTO requestDTO = new UserUpdateRequestDTO("updatedUser", "new@example.com", "New Name");

        doNothing().when(updateUser).execute(eq(userId), any(User.class));

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(deleteUser).execute(userId);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void changePassword_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        String currentPassword = "oldPass";
        String newPassword = "newPass";

        doNothing().when(changeUserPassword).execute(userId, currentPassword, newPassword);

        mockMvc.perform(post("/api/users/" + userId + "/change-password")
                        .param("currentPassword", currentPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isNoContent());
    }
}

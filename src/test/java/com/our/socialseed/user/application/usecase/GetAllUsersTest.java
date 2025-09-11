package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
Explicación rápida:
Mockeamos UserRepository y simulamos que devuelve una lista con dos usuarios.
Verificamos que el método execute() retorna esa lista correctamente.
Verificamos que se llamó findAll() exactamente una vez.
* */
class GetAllUsersTest {

    private UserRepository userRepository;
    private GetAllUsers getAllUsers;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        getAllUsers = new GetAllUsers(userRepository);
    }

    @Test
    void shouldReturnListOfUsers() {
        // Arrange
        User user1 = new User(UUID.randomUUID(), "user1", "user1@mail.com", "pwd1", "User One");
        User user2 = new User(UUID.randomUUID(), "user2", "user2@mail.com", "pwd2", "User Two");
        List<User> mockUsers = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> users = getAllUsers.execute();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));

        verify(userRepository, times(1)).findAll();
    }
}

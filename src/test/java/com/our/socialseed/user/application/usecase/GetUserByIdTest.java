package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
Explicación rápida:
Se prueba que cuando el usuario existe, se retorna correctamente.
Se prueba que cuando no existe, retorna Optional.empty.
Se verifica que el repositorio se llame correctamente.
* */
class GetUserByIdTest {

    private UserRepository userRepository;
    private GetUserById getUserById;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        getUserById = new GetUserById(userRepository);
    }

    @Test
    void shouldReturnUserWhenFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "jose", "jose@mail.com", "pwd", "Jose Ramirez");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = getUserById.execute(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = getUserById.execute(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }
}
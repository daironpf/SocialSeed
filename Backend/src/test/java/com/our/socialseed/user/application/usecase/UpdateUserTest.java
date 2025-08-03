package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

/*
Explicación rápida:
Simulamos que el usuario existe y validamos que:
Se preserve la contraseña original.
Se actualice el resto de campos.
Simulamos que el usuario no existe y verificamos que no se guarda nada.
* */
class UpdateUserTest {

    private UserRepository userRepository;
    private UpdateUser updateUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        updateUser = new UpdateUser(userRepository);
    }

    @Test
    void shouldUpdateUserIfExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "oldUsername", "old@mail.com", "oldPassword", "Old Name");
        User updatedUser = new User(null, "newUsername", "new@mail.com", "newPassword", "New Name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        updateUser.execute(userId, updatedUser);

        // Assert
        // Verify that the updatedUser got the id set and password preserved
        verify(userRepository).save(argThat(user ->
                user.getId().equals(userId) &&
                        user.getPassword().equals(existingUser.getPassword()) &&
                        user.getUsername().equals("newUsername") &&
                        user.getEmail().equals("new@mail.com") &&
                        user.getFullName().equals("New Name")
        ));
    }

    @Test
    void shouldNotSaveIfUserDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User updatedUser = new User(null, "username", "email@mail.com", "password", "Name");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        updateUser.execute(userId, updatedUser);

        // Assert
        verify(userRepository, never()).save(any());
    }
}

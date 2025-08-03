package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

/*
Explicación rápida:
Mockeamos el repositorio.
Llamamos a execute con un UUID.
Verificamos que el repositorio recibió la llamada a deleteById exactamente una vez con ese UUID.
* */
class DeleteUserTest {

    private UserRepository userRepository;
    private DeleteUser deleteUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deleteUser = new DeleteUser(userRepository);
    }

    @Test
    void shouldDeleteUserById() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        deleteUser.execute(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}

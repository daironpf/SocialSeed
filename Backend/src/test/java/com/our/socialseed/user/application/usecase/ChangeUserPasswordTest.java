package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChangeUserPasswordTest {
/*
vamos a crear el test unitario para la clase ChangeUserPassword. Este test debe cubrir varios casos clave:
Usuario existe y contraseña actual coincide → contraseña cambia correctamente
Usuario no existe → lanza excepción
Contraseña actual no coincide → lanza excepción

Explicación rápida:
Usamos mocks para UserRepository y PasswordEncoder
Simulamos la búsqueda y las validaciones de contraseña
Verificamos que se lanza excepción en casos de error
Verificamos que se guarda el usuario con la nueva contraseña cuando todo es correcto
* */
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ChangeUserPassword changeUserPassword;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        changeUserPassword = new ChangeUserPassword(userRepository, passwordEncoder);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "juan", "juan@mail.com", "encodedOldPwd", "Juan Perez");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPwd", "encodedOldPwd")).thenReturn(true);
        when(passwordEncoder.encode("newPwd")).thenReturn("encodedNewPwd");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        changeUserPassword.execute(userId, "oldPwd", "newPwd");

        // Assert
        assertEquals("encodedNewPwd", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> changeUserPassword.execute(userId, "anyPwd", "newPwd"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCurrentPasswordIncorrect() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "juan", "juan@mail.com", "encodedOldPwd", "Juan Perez");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPwd", "encodedOldPwd")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> changeUserPassword.execute(userId, "wrongOldPwd", "newPwd"));

        assertEquals("Current password is incorrect", exception.getMessage());
    }
}

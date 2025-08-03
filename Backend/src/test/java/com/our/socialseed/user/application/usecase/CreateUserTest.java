package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CreateUser createUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        createUser = new CreateUser(userRepository, passwordEncoder);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        User inputUser = new User(
                null,
                "maria",
                "maria@example.com",
                "1234",
                "Maria Lopez"
        );

        when(passwordEncoder.encode("1234")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = createUser.execute(inputUser);

        // Assert
        assertNotNull(result.getId(), "ID should be generated");
        assertEquals("maria", result.getUsername());
        assertEquals("maria@example.com", result.getEmail());
        assertEquals("Maria Lopez", result.getFullName());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals(Set.of("ROLE_USER"), result.getRoles());

        verify(passwordEncoder, times(1)).encode("1234");
        verify(userRepository, times(1)).save(result);
    }
}

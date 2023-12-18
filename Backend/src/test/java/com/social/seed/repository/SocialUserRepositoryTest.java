package com.social.seed.repository;

import com.social.seed.model.SocialUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataNeo4jTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SocialUserRepositoryTest {

    @Autowired
    private SocialUserRepository socialUserRepository;

    @Test
    public void existByEmail_WhenUserExists_ShouldReturnTrue() {
        // Arrange (Configurar el escenario de prueba)
        String emailToCheck = "test@example.com";
        // Puedes insertar un usuario de prueba en la base de datos aquí si es necesario
        SocialUser user = new SocialUser();
        user.setEmail("test@example.com");
        socialUserRepository.save(user);

        // Act (Actuar - ejecutar el método que estás probando)
        Boolean userExists = socialUserRepository.existByEmail(emailToCheck);

        // Assert (Afirmar - realizar afirmaciones sobre el resultado)
        assertNotNull(userExists);
        assertTrue(userExists);
    }

    @Test
    public void existByEmail_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String emailToCheck = "nonexistent@example.com";
        // Asegúrate de que el usuario con este correo electrónico no exista en la base de datos

        // Act
        Boolean userExists = socialUserRepository.existByEmail(emailToCheck);

        // Assert
        assertNotNull(userExists);
        assertFalse(userExists);
    }

    @Test
    public void existByUserName_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        String userNameToCheck = "testUser";
        // Puedes insertar un usuario de prueba en la base de datos aquí si es necesario
        SocialUser user = new SocialUser();
        user.setUserName("testUser");
        socialUserRepository.save(user);

        // Act
        Boolean userExists = socialUserRepository.existByUserName(userNameToCheck);

        // Assert
        assertNotNull(userExists);
        assertTrue(userExists);
    }

    @Test
    public void existByUserName_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String userNameToCheck = "nonexistentUser";
        // Asegúrate de que el usuario con este nombre de usuario no exista en la base de datos

        // Act
        Boolean userExists = socialUserRepository.existByUserName(userNameToCheck);

        // Assert
        assertNotNull(userExists);
        assertFalse(userExists);
    }

    @Test
    public void givenNewSocialUser_whenSaveSocialUser_thenVerifySavedSocialUser() {
        // Given
        SocialUser user = new SocialUser();
        user.setEmail("test@example.com");

        // When
        socialUserRepository.save(user);
        Optional<SocialUser> foundUser = socialUserRepository.findByEmail("test@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    public void givenNewSocialUser_whenSaveSocialUser_thenFindSocialUserById() {
        // Given
        SocialUser user = new SocialUser();
        user.setId("1");
        user.setFullName("Test User");
        user.setDateBorn(LocalDateTime.now());
        user.setLanguage("English");

        // When
        socialUserRepository.save(user);
        Optional<SocialUser> foundUser = socialUserRepository.findById("1");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getFullName());
        assertEquals("English", foundUser.get().getLanguage());
    }

    @Test
    public void givenNewSocialUserInDatabase_whenUpdateSocialUser_thenVerifyChanges() {
        // Given
        SocialUser user = new SocialUser();
        user.setId("1");
        user.setFullName("Test User");
        user.setDateBorn(LocalDateTime.now());
        user.setLanguage("English");
        socialUserRepository.save(user);

        // When
        socialUserRepository.update("1", "Updated User", LocalDateTime.now(), "Spanish");
        Optional<SocialUser> updatedUser = socialUserRepository.findById("1");

        // Then
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated User", updatedUser.get().getFullName());
        assertEquals("Spanish", updatedUser.get().getLanguage());
    }
}
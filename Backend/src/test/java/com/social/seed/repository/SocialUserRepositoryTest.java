package com.social.seed.repository;

import com.social.seed.model.SocialUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataNeo4jTest
public class SocialUserRepositoryTest {

    @Autowired
    private SocialUserRepository socialUserRepository;

    /**
     * Set up test data before each test.
     */
    @Before
    public void setUp(){
        // clean all
        socialUserRepository.deleteAll();

        // Adding test data
        // user #1
        socialUserRepository.save(
                SocialUser.builder()
                        .userName("maria1")
                        .email("maria1@gmail.com")
                        .dateBorn(LocalDateTime.parse("1992-01-04T00:00:00"))
                        .fullName("Maria del Laurel Perez")
                        .language("ES")
                        .registrationDate(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .onVacation(false)
                        .followersCount(0)
                        .friendCount(0)
                        .followingCount(0)
                        .friendRequestCount(0)
                        .build()
        );
        // user #2
        socialUserRepository.save(
                SocialUser.builder()
                        .userName("daisi97")
                        .email("daisi97@gmail.com")
                        .dateBorn(LocalDateTime.parse("1987-02-04T00:00:00"))
                        .fullName("Daisisi Ferexy Zoan")
                        .language("EN")
                        .registrationDate(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .onVacation(false)
                        .followersCount(0)
                        .friendCount(0)
                        .followingCount(0)
                        .friendRequestCount(0)
                        .build()
        );
        // user #3
        socialUserRepository.save(
                SocialUser.builder()
                        .userName("gelacio32")
                        .email("gelacio32@gmail.com")
                        .dateBorn(LocalDateTime.parse("1962-10-11T00:00:00"))
                        .fullName("Gelacio Perez Perez")
                        .language("ES")
                        .registrationDate(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .onVacation(false)
                        .followersCount(0)
                        .friendCount(0)
                        .followingCount(0)
                        .friendRequestCount(0)
                        .build()
        );
    }

    /**
     * Clean up test data after each test.
     */
    @After
    public void tearDown() {
        socialUserRepository.deleteAll();
    }

    /**
     * Verifies that a user exists by email.
     */
    @Test
    public void existsByEmailShouldReturnTrueForExistingSocialUser() {
        // When
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");

        // Then
        assertTrue(userExists);
    }

    /**
     * Verifies that a user does not exist by email.
     */
    @Test
    public void existsByEmailShouldReturnFalseForNonExistingSocialUser() {
        // When
        Boolean userExists = socialUserRepository.existByEmail("emailnoexist.com");

        // Then
        assertFalse(userExists);
    }

    /**
     * Verifies that a user exists by username.
     */
    @Test
    public void existsByUserNameShouldReturnTrueForExistingSocialUser() {
        // When
        Boolean userExists = socialUserRepository.existByUserName("gelacio32");

        // Then
        assertTrue(userExists);
    }

    /**
     * Verifies that a user does not exist by username.
     */
    @Test
    public void existsByUserNameShouldReturnFalseForNonExistingSocialUser() {
        // When
        Boolean userExists = socialUserRepository.existByUserName("usernamenoexist");

        // Then
        assertFalse(userExists);
    }

    /**
     * Verifies that finding a user by email returns the correct information.
     */
    @Test
    public void findByEmailShouldReturnExistingSocialUser(){
        // Given
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");
        assertTrue(userExists);

        // When
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // Then
        assertTrue(socialUser.isPresent());
        assertEquals("gelacio32", socialUser.get().getUserName());
        assertEquals("gelacio32@gmail.com", socialUser.get().getEmail());
        assertEquals("Gelacio Perez Perez", socialUser.get().getFullName());
        assertEquals("ES", socialUser.get().getLanguage());
        assertTrue(socialUser.get().getIsActive());
        assertFalse(socialUser.get().getIsDeleted());
        assertFalse(socialUser.get().getOnVacation());
    }

    /**
     * Verifies that updating an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserSucceed() {
        // Given
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");
        assertTrue(userExists);
        Optional<SocialUser> socialUserToUpdate = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.update(
                socialUserToUpdate.get().getId(),
                "Nombre Actualizado Completo",
                LocalDateTime.parse("1999-01-01T00:00:00"),
                "EN"
        );
        Optional<SocialUser> updatedSocialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // Then
        assertTrue(updatedSocialUser.isPresent());
        assertEquals("Nombre Actualizado Completo", updatedSocialUser.get().getFullName());
        assertEquals("EN", updatedSocialUser.get().getLanguage());
        assertEquals(LocalDateTime.parse("1999-01-01T00:00:00"), updatedSocialUser.get().getDateBorn());
    }

    /**
     * Verifies that updating the username of an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserNameSucceed(){
        // Given
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");
        assertTrue(userExists);
        Optional<SocialUser> socialUserToUpdate = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.updateSocialUserName(socialUserToUpdate.get().getId(), "nuevoUsuario");
        Optional<SocialUser> updatedSocialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // Then
        assertTrue(updatedSocialUser.isPresent());
        assertEquals("nuevoUsuario", updatedSocialUser.get().getUserName());
    }

    /**
     * Verifies that updating the email of an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserEmailSucceed(){
        // Given
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");
        assertTrue(userExists);
        Optional<SocialUser> socialUserToUpdate = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.updateSocialUserEmail(socialUserToUpdate.get().getId(), "nuevoemail@gmail.com");
        Optional<SocialUser> updatedSocialUser = socialUserRepository.findById(socialUserToUpdate.get().getId());

        // Then
        assertTrue(updatedSocialUser.isPresent());
        assertEquals("nuevoemail@gmail.com", updatedSocialUser.get().getEmail());
    }

    /**
     * Verifies that a user B is a follower of user A.
     */
    @Test
    public void IsUserBFollowerOfUserA(){
        // Given
        Optional<SocialUser> gelacio = socialUserRepository.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = socialUserRepository.findByEmail("maria1@gmail.com");
        socialUserRepository.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // When
        Boolean IsUserBFollowerOfUserA = socialUserRepository.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );

        // Then
        assertTrue(IsUserBFollowerOfUserA);
    }

    /**
     * Verifies that unfollowing user A removes follower B.
     */
    @Test
    public void unFollowUserA_shouldRemoveFollowerB(){
        // Given
        Optional<SocialUser> gelacio = socialUserRepository.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = socialUserRepository.findByEmail("maria1@gmail.com");
        socialUserRepository.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );
        Boolean IsUserBFollowerOfUserA = socialUserRepository.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verificado que SI existe la relacion de Seguidor de (b)<-[r:FOLLOWED_BY]-(a)
        assertTrue(IsUserBFollowerOfUserA);

        // When
        socialUserRepository.unFollowTheUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );

        // Then
        IsUserBFollowerOfUserA = socialUserRepository.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verificado que NO existe la relacion de Seguidor de (b)<-[r:FOLLOWED_BY]-(a)
        assertFalse(IsUserBFollowerOfUserA);
    }

    /**
     * Verifies that vacation mode is deactivated.
     */
    @Test
    public void isVacationModeDeactivatedShouldReturnTrue() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        Boolean vacationMode = socialUserRepository.isVacationModeActivated(socialUser.get().getId());

        // Then
        assertFalse(vacationMode);
    }

    /**
     * Verifies that activating vacation mode is successful.
     */
    @Test
    public void activateVacationModeActivatesModeSuccessfully() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.activateVacationMode(socialUser.get().getId());

        // Then
        Boolean vacationMode = socialUserRepository.isVacationModeActivated(socialUser.get().getId());
        assertTrue(vacationMode);
    }

    /**
     * Verifies that deactivating vacation mode is successful.
     */
    @Test
    public void deactivateVacationModeActivatesModeSuccessfully() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.deactivateVacationMode(socialUser.get().getId());

        // Then
        Boolean vacationMode = socialUserRepository.isVacationModeActivated(socialUser.get().getId());
        assertFalse(vacationMode);
    }

    /**
     * Verifies that deleting an existing social user is successful.
     */
    @Test
    public void deleteExistingSocialUserShouldSucceed() {
        // Given
        Boolean userExists = socialUserRepository.existByEmail("gelacio32@gmail.com");
        assertTrue(userExists);
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.deleteById(socialUser.get().getId());

        // Then
        Optional<SocialUser> deleteSocialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");
        assertFalse(deleteSocialUser.isPresent());
    }

    /**
     * Tests if a user is Active in the database.
     */
    @Test
    public void isSocialUserActivatedShouldReturnTrue() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        boolean socialUserActivated = socialUserRepository.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertTrue(socialUserActivated);
    }

    /**
     * Tests if a user can be activated successfully.
     */
    @Test
    public void shouldActivateSocialUserWhenGivenUserId() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.activateSocialUser(socialUser.get().getId());
        boolean socialUserActivated = socialUserRepository.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertTrue(socialUserActivated);
    }

    /**
     * Tests if a user can be deactivated successfully.
     */
    @Test
    public void shouldDeactivateSocialUserWhenGivenUserId() {
        //Given
        Optional<SocialUser> socialUser = socialUserRepository.findByEmail("gelacio32@gmail.com");

        // When
        socialUserRepository.deactivateSocialUser(socialUser.get().getId());
        boolean socialUserActivated = socialUserRepository.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertFalse(socialUserActivated);
    }
}
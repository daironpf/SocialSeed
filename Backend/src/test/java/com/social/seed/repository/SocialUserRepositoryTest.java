/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Unit tests for the {@link SocialUserRepository} class.
 * These tests cover various functionalities of the repository for managing {@link SocialUser}.
 * <p>
 * The test class is annotated with {@code @DataNeo4jTest}, indicating that it's a Spring Data Neo4j test
 * and will be using a Neo4j test slice context for efficient testing.
 * <p>
 *  @author Dairon Pérez Frías
 *  @since 2023-12-24
 */
@DataNeo4jTest
class SocialUserRepositoryTest {

    @Autowired
    private SocialUserRepository underTest;

    // region Setup and Tear down
    /**
     * This method is executed before each test case to set up the necessary test data.
     * It calls the createTestData method to populate the repository with sample SocialUser data.
     */
    @BeforeEach
    void setUp() {
        createTestData();
    }

    /**
     * This method is executed after each test case to clean up any data created during testing.
     * It calls the cleanAllData method to delete all social users from the repository.
     */
    @AfterEach
    void tearDown() {
        cleanAllData();
    }
    // endregion

    // region Existence Checks
    /**
     * Tests whether the SocialUserRepository correctly determines the existence of a social user by email.
     * It asserts that the repository returns true when the social user with the specified email exists,
     * and false when the social user with the specified email does not exist.
     */
    @Test
    void shouldCheckWhenSocialUserByEmailExists() {
        // Verifies if a social user with a given email exists
        assertUserByEmailExists("maria1@gmail.com", true);

        // Verifies if a social user with a non-existing email returns false
        assertUserByEmailExists("noemailexist@gmail.com", false);
    }

    /**
     * Tests whether the SocialUserRepository correctly determines the existence of a social user by username.
     * It asserts that the repository returns true when the social user with the specified username exists,
     * and false when the social user with the specified username does not exist.
     */
    @Test
    void shouldCheckWhenSocialUserByUserNameExists() {
        // Verifies if a social user with a given username exists
        assertUserByUserNameExists("gelacio32", true);

        // Verifies if a social user with a non-existing username returns false
        assertUserByUserNameExists("usernamenoexist", false);
    }
    // endregion

    // region Find by Email
    /**
     * Tests whether the SocialUserRepository can successfully find an existing social user by email.
     * It verifies that the repository correctly indicates the existence of the social user by email,
     * retrieves the social user using the findByEmail method, and asserts that the retrieved user's
     * properties match the expected values.
     */
    @Test
    void findByEmailShouldReturnExistingSocialUser() {
        // Given: A social user with a specific email exists
        boolean userExists = underTest.existByEmail("gelacio32@gmail.com");
        assertThat(userExists).isTrue();

        // When: Trying to find a social user by email
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // Then: Verifies that the existing social user's properties are as expected
        assertSocialUserProperties(socialUser.orElseThrow());
    }
    // endregion

    // region Update Operations
    /**
     * Verifies that updating an existing social user is successful.
     * It ensures that the SocialUserRepository correctly updates the properties of an existing social user,
     * including full name, date of birth, and language, and asserts that the updated user's properties match
     * the expected values after the update operation.
     */
    @Test
    public void updateExistingSocialUserSucceed() {
        // Given: An existing social user with a specific email
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // When: Updating the existing social user's properties
        assertThat(socialUserToUpdate).isPresent();
        underTest.update(
                socialUserToUpdate.get().getId(),
                "Updated Full Name",
                LocalDateTime.parse("1999-01-01T00:00:00"),
                "EN"
        );
        Optional<SocialUser> updatedSocialUser = underTest.findByEmail("gelacio32@gmail.com");

        // Then: Verifies that the updated social user's properties are as expected
        assertThat(updatedSocialUser)
                .isPresent()
                .satisfies(actualUser -> {
                    SocialUser user = actualUser.get();
                    assertThat(user.getFullName()).isEqualTo("Updated Full Name");
                    assertThat(user.getLanguage()).isEqualTo("EN");
                    assertThat(user.getDateBorn()).isEqualTo(LocalDateTime.parse("1999-01-01T00:00:00"));
                });
    }

    /**
     * Verifies that updating the username of an existing social user is successful.
     * It ensures that the SocialUserRepository correctly updates the username of an existing social user
     * and asserts that the updated user's username matches the expected value after the update operation.
     */
    @Test
    public void updateExistingSocialUserNameSucceed(){
        // Given: An existing social user with a specific email
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // When: Updating the existing social user's username
        assertThat(socialUserToUpdate).isPresent();
        underTest.updateSocialUserName(socialUserToUpdate.get().getId(), "newUsername");
        Optional<SocialUser> updatedSocialUser = underTest.findByEmail("gelacio32@gmail.com");

        // Then: Verifies that the updated social user's username is as expected
        assertThat(updatedSocialUser).isPresent();
        assertThat(updatedSocialUser.get().getUserName()).isEqualTo("newUsername");
    }

    /**
     * Verifies that updating the email of an existing social user is successful.
     * It ensures that the SocialUserRepository correctly updates the email of an existing social user
     * and asserts that the updated user's email matches the expected value after the update operation.
     */
    @Test
    public void updateExistingSocialUserEmailSucceed(){
        // Given: An existing social user with a specific email
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // When: Updating the existing social user's email
        assertThat(socialUserToUpdate).isPresent();
        underTest.updateSocialUserEmail(socialUserToUpdate.get().getId(), "newemail@gmail.com");
        Optional<SocialUser> updatedSocialUser = underTest.findById(socialUserToUpdate.get().getId());

        // Then: Verifies that the updated social user's email is as expected
        assertThat(updatedSocialUser).isPresent();
        assertThat(updatedSocialUser.get().getEmail()).isEqualTo("newemail@gmail.com");
    }
    // endregion

    // region Relationship Management
    /**
     * Verifies that a user B is a follower of user A.
     * It creates a follower relationship between two social users, checks if user B is a follower of user A,
     * and asserts that the result is true after the relationship is established.
     */
    @Test
    public void IsUserBFollowerOfTheUserA(){
        // Given: Two social users, Gelacio and Maria
        Optional<SocialUser> gelacio = underTest.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = underTest.findByEmail("maria1@gmail.com");

        // When: Creating a relationship where Maria follows Gelacio
        assertThat(gelacio).isPresent();
        assertThat(maria).isPresent();
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // Then: Verifies that Maria is a follower of Gelacio
        Boolean IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        assertThat(IsUserBFollowerOfUserA).isTrue();
    }

    /**
     * Verifies that unfollowing user A removes follower B.
     * It establishes a follower relationship between two social users, checks if the relationship exists,
     * unfollows user A, and asserts that follower B is no longer following user A after the unfollow operation.
     */
    @Test
    public void unFollowUserA_shouldRemoveFollowerB(){
        // Given: Two social users, Gelacio and Maria
        Optional<SocialUser> gelacio = underTest.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = underTest.findByEmail("maria1@gmail.com");

        // Creating a relationship where Maria follows Gelacio
        assertThat(gelacio).isPresent();
        assertThat(maria).isPresent();
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // Verify if the relationship exists (Maria follows Gelacio)
        Boolean IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        assertThat(IsUserBFollowerOfUserA).isTrue();

        // When: Unfollowing user A (Gelacio)
        underTest.unFollowTheUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );

        // Then: Verifies that follower B (Maria) is no longer following user A (Gelacio)
        IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verify if the relationship no longer exists
        assertThat(IsUserBFollowerOfUserA).isFalse();
    }
    // endregion

    // region Vacation Mode Operations
    /**
     * Verifies that vacation mode is deactivated.
     * It checks if a specific social user's vacation mode is deactivated and asserts that the result is true.
     */
    @Test
    public void isVacationModeDeactivatedShouldReturnTrue() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Checking if the vacation mode is deactivated for the user
        assertThat(socialUser).isPresent();
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());

        // Then: Verifies that the vacation mode is deactivated
        assertThat(vacationMode).isFalse();
    }

    /**
     * Verifies that activating vacation mode is successful.
     * It activates vacation mode for a specific social user and asserts that the vacation mode is activated successfully.
     */
    @Test
    public void activateVacationModeActivatesModeSuccessfully() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Activating vacation mode for the user
        assertThat(socialUser).isPresent();
        underTest.activateVacationMode(socialUser.get().getId());

        // Then: Verifies that the vacation mode is activated successfully
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());
        assertThat(vacationMode).isTrue();
    }

    /**
     * Verifies that deactivating vacation mode is successful.
     * It deactivates vacation mode for a specific social user and asserts that the vacation mode is deactivated successfully.
     */
    @Test
    public void deactivateVacationModeActivatesModeSuccessfully() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Deactivating vacation mode for the user
        assertThat(socialUser).isPresent();
        underTest.deactivateVacationMode(socialUser.get().getId());

        // Then: Verifies that the vacation mode is deactivated successfully
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());
        assertThat(vacationMode).isFalse();
    }
    // endregion

    // region Deletion and Activation Operations
    /**
     * Verifies that deleting an existing social user is successful.
     * It deletes a specific social user and asserts that the user is successfully deleted from the repository.
     */
    @Test
    public void deleteExistingSocialUserShouldSucceed() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Deleting the social user
        assertThat(socialUser).isPresent();
        underTest.deleteById(socialUser.get().getId());

        // Then: Verifies that the social user is successfully deleted
        Optional<SocialUser> deleteSocialUser = underTest.findByEmail("gelacio32@gmail.com");
        assertThat(deleteSocialUser).isEmpty();
    }

    /**
     * Tests if a user is Active in the database.
     * It checks if a specific social user is activated and asserts that the result is true.
     */
    @Test
    public void isSocialUserActivatedShouldReturnTrue() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Checking if the user is activated
        assertThat(socialUser).isPresent();
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());

        // Then: Verifies that the user is activated
        assertThat(socialUserActivated).isTrue();
    }

    /**
     * Tests if a user can be activated successfully.
     * It activates a specific social user and asserts that the user is successfully activated.
     */
    @Test
    public void shouldActivateSocialUserWhenGivenUserId() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Activating the user
        assertThat(socialUser).isPresent();
        underTest.activateSocialUser(socialUser.get().getId());

        // Then: Verifies that the user is activated successfully
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());
        assertThat(socialUserActivated).isTrue();
    }

    /**
     * Tests if a user can be deactivated successfully.
     * It deactivates a specific social user and asserts that the user is successfully deactivated.
     */
    @Test
    public void shouldDeactivateSocialUserWhenGivenUserId() {
        // Given: A specific social user (Gelacio)
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When: Deactivating the user
        assertThat(socialUser).isPresent();
        underTest.deactivateSocialUser(socialUser.get().getId());

        // Then: Verifies that the user is deactivated successfully
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());
        assertThat(socialUserActivated).isFalse();
    }
    // endregion

    // region Utility Methods
    /**
     * Asserts whether a social user with the given email exists as expected.
     *
     * @param email    The email to check for existence.
     * @param expected The expected result (true if the user exists, false otherwise).
     */
    private void assertUserByEmailExists(String email, boolean expected) {
        assertThat(underTest.existByEmail(email)).isEqualTo(expected);
    }

    /**
     * Asserts whether a social user with the given username exists as expected.
     *
     * @param userName The username to check for existence.
     * @param expected The expected result (true if the user exists, false otherwise).
     */
    private void assertUserByUserNameExists(String userName, boolean expected) {
        assertThat(underTest.existByUserName(userName)).isEqualTo(expected);
    }

    /**
     * Asserts whether the properties of a given social user match the expected values.
     *
     * @param socialUser The social user to verify.
     */
    private void assertSocialUserProperties(SocialUser socialUser) {
        assertThat(socialUser.getUserName()).isEqualTo("gelacio32");
        assertThat(socialUser.getEmail()).isEqualTo("gelacio32@gmail.com");
        assertThat(socialUser.getFullName()).isEqualTo("Gelacio Perez Perez");
        assertThat(socialUser.getLanguage()).isEqualTo("ES");
        assertThat(socialUser.getIsActive()).isTrue();
        assertThat(socialUser.getIsDeleted()).isFalse();
        assertThat(socialUser.getOnVacation()).isFalse();
    }

    /**
     * Creates test data by saving three social users into the repository.
     * User #1: Maria
     * User #2: Lucas
     * User #3: Gelacio
     */
    private void createTestData() {
        // user #1
        underTest.save(createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES"));

        // user #2
        underTest.save(createSocialUser("lucas7", "lucas7@gmail.com", "1987-02-04T00:00:00", "Lucas Des Von", "EN"));

        // user #3
        underTest.save(createSocialUser("gelacio32", "gelacio32@gmail.com", "1962-10-11T00:00:00", "Gelacio Perez Perez", "ES"));
    }

    /**
     * Creates a SocialUser object with the specified properties.
     *
     * @param userName   The username of the social user.
     * @param email      The email of the social user.
     * @param dateBorn   The date of birth of the social user in the format "yyyy-MM-dd'T'HH:mm:ss".
     * @param fullName   The full name of the social user.
     * @param language   The language preference of the social user.
     * @return A SocialUser object with the specified properties.
     */
    private SocialUser createSocialUser(String userName, String email, String dateBorn, String fullName, String language) {
        return SocialUser.builder()
                .userName(userName)
                .email(email)
                .dateBorn(LocalDateTime.parse(dateBorn))
                .fullName(fullName)
                .language(language)
                .registrationDate(LocalDateTime.now())
                .isActive(true)
                .isDeleted(false)
                .onVacation(false)
                .followersCount(0)
                .friendCount(0)
                .followingCount(0)
                .friendRequestCount(0)
                .build();
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }
    // endregion
}
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

@DataNeo4jTest
class SocialUserRepositoryTest {

    @Autowired
    private SocialUserRepository underTest;

    @BeforeEach
    void setUp() {
        createTestData();
    }

    @AfterEach
    void tearDown() {
        cleanAllData();
    }

    @Test
    void shouldCheckWhenSocialUserByEmailExists() {
        assertUserByEmailExists("maria1@gmail.com", true);
        assertUserByEmailExists("noemailexist@gmail.com", false);
    }

    @Test
    void shouldCheckWhenSocialUserByUserNameExists() {
        assertUserByUserNameExists("gelacio32", true);
        assertUserByUserNameExists("usernamenoexist", false);
    }

    @Test
    void findByEmailShouldReturnExistingSocialUser() {
        // given
        Boolean userExists = underTest.existByEmail("gelacio32@gmail.com");
        assertThat(userExists).isTrue();

        // when
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // then
        assertSocialUserProperties(socialUser.orElseThrow());
    }

    /**
     * Verifies that updating an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserSucceed() {
        // given
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // when
        underTest.update(
                socialUserToUpdate.get().getId(),
                "Nombre Actualizado Completo",
                LocalDateTime.parse("1999-01-01T00:00:00"),
                "EN"
        );
        Optional<SocialUser> updatedSocialUser = underTest.findByEmail("gelacio32@gmail.com");

        // then
        assertThat(updatedSocialUser.isPresent()).isTrue();
        assertThat(updatedSocialUser.get().getFullName()).isEqualTo("Nombre Actualizado Completo");
        assertThat(updatedSocialUser.get().getLanguage()).isEqualTo("EN");
        assertThat(updatedSocialUser.get().getDateBorn()).isEqualTo(LocalDateTime.parse("1999-01-01T00:00:00"));
    }

    /**
     * Verifies that updating the username of an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserNameSucceed(){
        // given
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // when
        underTest.updateSocialUserName(socialUserToUpdate.get().getId(), "nuevoUsuario");
        Optional<SocialUser> updatedSocialUser = underTest.findByEmail("gelacio32@gmail.com");

        // then
        assertThat(updatedSocialUser.isPresent()).isTrue();
        assertThat(updatedSocialUser.get().getUserName()).isEqualTo("nuevoUsuario");
    }

    /**
     * Verifies that updating the email of an existing social user is successful.
     */
    @Test
    public void updateExistingSocialUserEmailSucceed(){
        // given
        assertUserByEmailExists("gelacio32@gmail.com", true);
        Optional<SocialUser> socialUserToUpdate = underTest.findByEmail("gelacio32@gmail.com");

        // when
        underTest.updateSocialUserEmail(socialUserToUpdate.get().getId(), "nuevoemail@gmail.com");
        Optional<SocialUser> updatedSocialUser = underTest.findById(socialUserToUpdate.get().getId());

        // then
        assertThat(updatedSocialUser.isPresent()).isTrue();
        assertThat(updatedSocialUser.get().getEmail()).isEqualTo("nuevoemail@gmail.com");
    }

    /**
     * Verifies that a user B is a follower of user A.
     */
    @Test
    public void IsUserBFollowerOfTheUserA(){
        // given
        Optional<SocialUser> gelacio = underTest.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = underTest.findByEmail("maria1@gmail.com");
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // when
        Boolean IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );

        // then
        assertThat(IsUserBFollowerOfUserA).isTrue();
    }

    /**
     * Verifies that unfollowing user A removes follower B.
     */
    @Test
    public void unFollowUserA_shouldRemoveFollowerB(){
        // given
        Optional<SocialUser> gelacio = underTest.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = underTest.findByEmail("maria1@gmail.com");
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );
        Boolean IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verify if Exist (b)<-[r:FOLLOWED_BY]-(a)
        assertThat(IsUserBFollowerOfUserA).isTrue();

        // when
        underTest.unFollowTheUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );

        // then
        IsUserBFollowerOfUserA = underTest.IsUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verify if NOT Exist (b)<-[r:FOLLOWED_BY]-(a)
        assertThat(IsUserBFollowerOfUserA).isFalse();
    }

    /**
     * Verifies that vacation mode is deactivated.
     */
    @Test
    public void isVacationModeDeactivatedShouldReturnTrue() {
        // given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // when
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());

        // then
        assertThat(vacationMode).isFalse();
    }

    /**
     * Verifies that activating vacation mode is successful.
     */
    @Test
    public void activateVacationModeActivatesModeSuccessfully() {
        //Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        underTest.activateVacationMode(socialUser.get().getId());

        // Then
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());
        assertThat(vacationMode).isTrue();
    }


    /**
     * Verifies that deactivating vacation mode is successful.
     */
    @Test
    public void deactivateVacationModeActivatesModeSuccessfully() {
        //Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        underTest.deactivateVacationMode(socialUser.get().getId());

        // Then
        Boolean vacationMode = underTest.isVacationModeActivated(socialUser.get().getId());
        assertThat(vacationMode).isFalse();
    }

    /**
     * Verifies that deleting an existing social user is successful.
     */
    @Test
    public void deleteExistingSocialUserShouldSucceed() {
        // Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        underTest.deleteById(socialUser.get().getId());

        // Then
        Optional<SocialUser> deleteSocialUser = underTest.findByEmail("gelacio32@gmail.com");
        assertThat(deleteSocialUser.isPresent()).isFalse();
    }

    /**
     * Tests if a user is Active in the database.
     */
    @Test
    public void isSocialUserActivatedShouldReturnTrue() {
        //Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertThat(socialUserActivated).isTrue();
    }

    /**
     * Tests if a user can be activated successfully.
     */
    @Test
    public void shouldActivateSocialUserWhenGivenUserId() {
        //Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        underTest.activateSocialUser(socialUser.get().getId());
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertThat(socialUserActivated).isTrue();
    }

    /**
     * Tests if a user can be deactivated successfully.
     */
    @Test
    public void shouldDeactivateSocialUserWhenGivenUserId() {
        //Given
        Optional<SocialUser> socialUser = underTest.findByEmail("gelacio32@gmail.com");

        // When
        underTest.deactivateSocialUser(socialUser.get().getId());
        boolean socialUserActivated = underTest.isSocialUserActivated(socialUser.get().getId());

        // Then
        assertThat(socialUserActivated).isFalse();
    }

    // region util
    private void assertUserByEmailExists(String email, boolean expected) {
        assertThat(underTest.existByEmail(email)).isEqualTo(expected);
    }

    private void assertUserByUserNameExists(String userName, boolean expected) {
        assertThat(underTest.existByUserName(userName)).isEqualTo(expected);
    }

    private void assertSocialUserProperties(SocialUser socialUser) {
        assertThat(socialUser.getUserName()).isEqualTo("gelacio32");
        assertThat(socialUser.getEmail()).isEqualTo("gelacio32@gmail.com");
        assertThat(socialUser.getFullName()).isEqualTo("Gelacio Perez Perez");
        assertThat(socialUser.getLanguage()).isEqualTo("ES");
        assertThat(socialUser.getIsActive()).isTrue();
        assertThat(socialUser.getIsDeleted()).isFalse();
        assertThat(socialUser.getOnVacation()).isFalse();
    }

    private void createTestData() {
        // user #1
        underTest.save(createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES"));

        // user #2
        underTest.save(createSocialUser("daisi97", "daisi97@gmail.com", "1987-02-04T00:00:00", "Daisisi Ferexy Zoan", "EN"));

        // user #3
        underTest.save(createSocialUser("gelacio32", "gelacio32@gmail.com", "1962-10-11T00:00:00", "Gelacio Perez Perez", "ES"));
    }

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

    private void cleanAllData() {
        underTest.deleteAll();
    }
    // endregion
}

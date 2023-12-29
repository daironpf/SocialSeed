package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link FriendsRepository}, focusing on testing individual methods and functionalities
 * in isolation for managing { Friends Relationships between SocialUsers }.
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2023-12-27
 */
@DataNeo4jTest
//@EnableNeo4jRepositories(basePackageClasses = FriendsRepository.class)
class FriendsRepositoryTest {

    @Autowired
    private FriendsRepository underTest;
    @Autowired
    private SocialUserRepository socialUserRepository;
//    @Autowired
//    private Util util;

    //region variables
    private SocialUser user1;
    private SocialUser user2;
    private SocialUser user3;
    //endregion

    // region Setup and Tear down
    /**
     * This method is executed before each test case to set up the necessary test data.
     * It calls the createTestData method to populate the repository with sample HashTag data.
     */
    @BeforeEach
    void setUp() {
        cleanAllData();
        createTestData();
    }

    /**
     * This method is executed after each test case to clean up any data created during testing.
     * It calls the cleanAllData method to delete all HashTag from the repository.
     */
    @AfterEach
    public void tearDown() {
        cleanAllData();
    }
    // endregion

    @Test
    void existsFriendRequest() {
        // Given:
        underTest.createFriendRequest(
                user1.getId(),
                user2.getId(),
                LocalDateTime.now()
        );

        // When:
        boolean expectedTrue = underTest.existsFriendRequest(user1.getId(),user2.getId());
        boolean expectedFalse = underTest.existsFriendRequest(user1.getId(),user3.getId());

        // Then:
        assertThat(expectedTrue).isTrue();
        assertThat(expectedFalse).isFalse();
    }

    @Test
    void createFriendRequest() {
        // Given: existing user1 and user2

        // When
        underTest.createFriendRequest(
                user1.getId(),
                user2.getId(),
                LocalDateTime.now()
        );

        // Then:
        SocialUser userResult = socialUserRepository.findById(user2.getId()).get();
        assertThat(userResult.getFriendRequestCount()).isEqualTo(1);
        boolean expected = underTest.existsFriendRequest(user1.getId(),user2.getId());
        assertThat(expected).isTrue();
    }

    @Test
    void cancelRequestFriendship() {
        // Given:
        underTest.createFriendRequest(
                user1.getId(),
                user2.getId(),
                LocalDateTime.now()
        );
        boolean expected = underTest.existsFriendRequest(user1.getId(),user2.getId());
        assertThat(expected).isTrue();

        // When:
        underTest.cancelRequestFriendship(user1.getId(),user2.getId());
        expected = underTest.existsFriendRequest(user1.getId(),user2.getId());

        // Then:
        assertThat(expected).isFalse();
    }

    @Test
    void acceptedRequestFriendship() {
        // Given:
        underTest.createFriendRequest(
                user1.getId(),
                user2.getId(),
                LocalDateTime.now()
        );
        boolean expected = underTest.existsFriendRequest(user1.getId(),user2.getId());
        assertThat(expected).isTrue();

        // When:
        underTest.acceptedRequestFriendship(
                user2.getId(),
                user1.getId(),
                LocalDateTime.now()
        );
        expected = underTest.existsFriendRequest(user1.getId(),user2.getId());
        assertThat(expected).isFalse();

        // Then:
        SocialUser user1WithFriend = socialUserRepository.findById(user1.getId()).get();
        assertThat(user1WithFriend.getFriendCount()).isEqualTo(1);
    }

//    @Test
//    void existsFriendRequestByUserToAccept() {
//    }
//
//    @Test
//    void existsFriendship() {
//    }
//
//    @Test
//    void deleteFriendship() {
//    }

    // region Utility Methods
    /**
     * Creates test data by saving three SocialUser into the repository.
     * User #1: FirstTagToTest
     * User #2: SecondTagToTest
     * User #3: ThirdTagToTest
     */
    private void createTestData() {
        // user #1
        this.user1 = underTest.save(createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES"));

        // user #2
        this.user2 = underTest.save(createSocialUser("lucas7", "lucas7@gmail.com", "1987-02-04T00:00:00", "Lucas Des Von", "EN"));

        // user #3
        this.user3 = underTest.save(createSocialUser("gelacio32", "gelacio32@gmail.com", "1962-10-11T00:00:00", "Gelacio Perez Perez", "ES"));
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }
    //endregion

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
    public SocialUser createSocialUser(String userName, String email, String dateBorn, String fullName, String language) {
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
}
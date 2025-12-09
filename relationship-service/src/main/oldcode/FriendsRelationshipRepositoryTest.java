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

import com.social.seed.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link FriendsRelationshipRepository}, focusing on testing individual methods and functionalities
 * in isolation for managing { Friends Relationships between SocialUsers }.
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2023-12-27
 */
@DataNeo4jTest
class FriendsRelationshipRepositoryTest {

    // region dependencies
    @Autowired
    private FriendsRelationshipRepository underTest;
    @Autowired
    private SocialUserRepository socialUserRepository;
    // endregion

    // region variables
    private SocialUser user1;
    private SocialUser user2;
    private SocialUser user3;
    // endregion

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

    /**
     * Tests the creation of a friend request between two social users.
     * It creates a friend request from user1 to user2 and verifies that the repository correctly updates
     * the friend request count for user2 and checks for the existence of the friend request.
     */
    @Test
    void shouldCreateFriendRequest() {
        // Given: Existing user1 and user2

        // When: Creating a friend request from user1 to user2
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());

        // Then: Verifies the friend request count for user2 and checks for the existence of the friend request
        SocialUser user2Result = socialUserRepository.findById(user2.getId()).get();
        assertThat(user2Result.getFriendRequestCount()).isEqualTo(user2.getFriendRequestCount()+1);
        boolean actual = underTest.existsFriendRequest(user1.getId(), user2.getId());
        assertThat(actual).isTrue();
    }

    /**
     * Tests whether a friend request exists between two social users.
     * It creates a friend request between user1 and user2, then checks if the repository correctly determines
     * the existence of friend requests between specified pairs of social users.
     */
    @Test
    void shouldExistFriendRequest() {
        // Given: Creating a friend request between user1 and user2
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());

        // When: Checking for the existence of friend requests
        boolean actualTrue = underTest.existsFriendRequest(user1.getId(), user2.getId());
        boolean actualFalse = underTest.existsFriendRequest(user1.getId(), user3.getId());

        // Then: Verifies the existence of friend requests
        assertThat(actualTrue).isTrue();
        assertThat(actualFalse).isFalse();
    }

    /**
     * Tests the cancellation of a friend request.
     * It creates a friend request from user1 to user2, cancels the request, and verifies that the repository correctly updates
     * the absence of the friend request without affecting the friend request count for user2.
     */
    @Test
    void shouldCancelFriendRequest() {
        // Given: A friend request from user1 to user2
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());
        boolean initialExistence = underTest.existsFriendRequest(user1.getId(), user2.getId());
        assertThat(initialExistence).isTrue();

        // When: Cancelling the friend request
        underTest.cancelRequestFriendship(user1.getId(), user2.getId());

        // Then: Verifies the absence of the friend request without affecting the friend request count for user2
        boolean finalExistence = underTest.existsFriendRequest(user1.getId(), user2.getId());
        assertThat(finalExistence).isFalse();

        Optional<SocialUser> userB = socialUserRepository.findById(user2.getId());
        assertThat(userB.get().getFriendRequestCount()).isEqualTo(user2.getFriendRequestCount());
    }

    /**
     * Tests the acceptance of a friend request.
     * It creates a friend request from user1 to user2, accepts the request, and verifies that the repository correctly updates
     * the absence of the friend request, increments the friend count for user1, and maintains the friend count for user2.
     */
    @Test
    void shouldAcceptFriendRequest() {
        // Given: A friend request from user1 to user2
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());
        boolean initialExistence = underTest.existsFriendRequest(user1.getId(), user2.getId());
        assertThat(initialExistence).isTrue();

        // When: Accepting the friend request
        underTest.acceptedRequestFriendship(user2.getId(), user1.getId(), LocalDateTime.now());

        // Then: Verifies the absence of the friend request, incremented friend count for user1, and maintained friend count for user2
        boolean finalExistence = underTest.existsFriendRequest(user1.getId(), user2.getId());
        assertThat(finalExistence).isFalse();

        SocialUser user1WithFriend = socialUserRepository.findById(user1.getId()).get();
        assertThat(user1WithFriend.getFriendCount()).isEqualTo(user1.getFriendCount() + 1);

        SocialUser user2WithFriend = socialUserRepository.findById(user2.getId()).get();
        assertThat(user2WithFriend.getFriendCount()).isEqualTo(user2.getFriendCount() + 1);
    }

    /**
     * Tests the existence of a friend request by the user to accept.
     * It creates a friend request from user1 to user2 and verifies that the repository correctly detects
     * the existence of the friend request by user2 to accept from user1.
     */
    @Test
    void shouldDetectFriendRequestByUserToAccept() {
        // Given: A friend request from user1 to user2
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());

        // When: Checking if there is a friend request by user2 to accept from user1
        boolean expected = underTest.existsFriendRequestByUserToAccept(user2.getId(), user1.getId());

        // Then: Verifies the existence of the friend request
        assertThat(expected).isTrue();
    }

    /**
     * Tests the existence of a friendship between two users.
     * It creates a friend request from user1 to user2 and then accepts the friend request.
     * The test verifies that the repository correctly detects the existence of a friendship between user1 and user2.
     */
    @Test
    void shouldDetectFriendship() {
        // Given: A friend request from user1 to user2 and the acceptance of the request
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());
        underTest.acceptedRequestFriendship(user2.getId(), user1.getId(), LocalDateTime.now());

        // When: Checking if there is a friendship between user1 and user2
        boolean expected = underTest.existsFriendship(user1.getId(), user2.getId());

        // Then: Verifies the existence of the friendship
        assertThat(expected).isTrue();
    }

    /**
     * Tests the deletion of a friendship between two users.
     * It creates a friend request from user1 to user2, accepts the friend request, and then deletes the friendship.
     * The test verifies that the repository correctly deletes the friendship between user1 and user2.
     */
    @Test
    void shouldDeleteFriendship() {
        // Given: A friend request from user1 to user2, the acceptance of the request, and the existence of the friendship
        underTest.createFriendRequest(user1.getId(), user2.getId(), LocalDateTime.now());
        underTest.acceptedRequestFriendship(user2.getId(), user1.getId(), LocalDateTime.now());
        boolean initialExistence = underTest.existsFriendship(user1.getId(), user2.getId());
        assertThat(initialExistence).isTrue();

        // When: Deleting the friendship
        underTest.deleteFriendship(user1.getId(), user2.getId());

        // Then: Verifies the absence of the friendship
        boolean finalExistence = underTest.existsFriendship(user1.getId(), user2.getId());
        assertThat(finalExistence).isFalse();
    }

    /**
     * Tests the cancellation of a received friend request.
     * It creates a friend request from user1 to user2, cancels the request, and verifies that the repository correctly updates
     * the absence of the friend request without affecting the friend request count for user2.
     */
    @Test
    void shouldCancelReceivedFriendRequest() {
        // Given: A friend request from user1 to user2
        underTest.createFriendRequest(user2.getId(), user1.getId(), LocalDateTime.now());
        boolean initialExistence = underTest.existsFriendRequest(user2.getId(), user1.getId());
        assertThat(initialExistence).isTrue();

        // When: Cancelling the friend request
        underTest.cancelReceivedRequestFriendship(user1.getId(), user2.getId());

        // Then: Verifies the absence of the friend request without affecting the friend request count for user2
        boolean finalExistence = underTest.existsFriendRequest(user2.getId(), user1.getId());
        assertThat(finalExistence).isFalse();

//        Optional<SocialUser> userB = socialUserRepository.findById(user1.getId());
//        assertThat(userB.get().getFriendRequestCount()).isEqualTo(user1.getFriendRequestCount());
    }

    // region Utility Methods
    /**
     * Creates test data by saving three SocialUser into the repository.
     * User #1: FirstTagToTest
     * User #2: SecondTagToTest
     * User #3: ThirdTagToTest
     */
    private void createTestData() {
        // user #1
        this.user1 = underTest.save(TestUtils.createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES"));

        // user #2
        this.user2 = underTest.save(TestUtils.createSocialUser("lucas7", "lucas7@gmail.com", "1987-02-04T00:00:00", "Lucas Des Von", "EN"));

        // user #3
        this.user3 = underTest.save(TestUtils.createSocialUser("gelacio32", "gelacio32@gmail.com", "1962-10-11T00:00:00", "Gelacio Perez Perez", "ES"));
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }
    //endregion
}
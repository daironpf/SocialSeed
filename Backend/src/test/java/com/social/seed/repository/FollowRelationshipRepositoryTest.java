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
 * Unit tests for the {@link FollowRelationshipRepository}, focusing on testing individual methods and functionalities
 * in isolation for managing { Follow Relationships between SocialUsers }.
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-03-27
 */
@DataNeo4jTest
public class FollowRelationshipRepositoryTest {

    // region dependencies
    @Autowired
    private FollowRelationshipRepository underTest;
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
     * Verifies that a user B is a follower of user A.
     * It creates a follower relationship between two social users, checks if user B is a follower of user A,
     * and asserts that the result is true after the relationship is established.
     */
    @Test
    void IsUserBFollowerOfTheUserA(){
        // Given: Two social users, Gelacio and Maria
        Optional<SocialUser> gelacio = socialUserRepository.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = socialUserRepository.findByEmail("maria1@gmail.com");

        // When: Creating a relationship where Maria follows Gelacio
        assertThat(gelacio).isPresent();
        assertThat(maria).isPresent();
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // Then: Verifies that Maria is a follower of Gelacio
        Boolean IsUserBFollowerOfUserA = underTest.isUserBFollowerOfUserA(
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
    void unFollowUserA_shouldRemoveFollowerB(){
        // Given: Two social users, Gelacio and Maria
        Optional<SocialUser> gelacio = socialUserRepository.findByEmail("gelacio32@gmail.com");
        Optional<SocialUser> maria = socialUserRepository.findByEmail("maria1@gmail.com");

        // Creating a relationship where Maria follows Gelacio
        assertThat(gelacio).isPresent();
        assertThat(maria).isPresent();
        underTest.createUserBFollowUserA(
                gelacio.get().getId(),
                maria.get().getId(),
                LocalDateTime.now()
        );

        // Verify if the relationship exists (Maria follows Gelacio)
        Boolean IsUserBFollowerOfUserA = underTest.isUserBFollowerOfUserA(
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
        IsUserBFollowerOfUserA = underTest.isUserBFollowerOfUserA(
                gelacio.get().getId(),
                maria.get().getId()
        );
        // Verify if the relationship no longer exists
        assertThat(IsUserBFollowerOfUserA).isFalse();
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

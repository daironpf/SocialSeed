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

import com.social.seed.model.HashTag;
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
 * Unit tests for the {@link SocialUserInterestInHashTagRepository}, focusing on testing individual methods and functionalities
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2023-12-29
 */
@DataNeo4jTest
class SocialUserInterestInHashTagRepositoryTest {

    // region dependencies
    @Autowired
    private SocialUserInterestInHashTagRepository underTest;
    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    // endregion

    // region variables
    SocialUser socialUser1;
    HashTag hashTag1;
    HashTag hashTag2;
    HashTag hashTag3;
    // endregion

    // region Setup and Tear down
    /**
     * This method is executed before each test case to set up the necessary test data.
     * It calls the createTestData method to populate the repository with sample data.
     */
    @BeforeEach
    void setUp() {
        cleanAllData();
        createTestData();
    }

    /**
     * This method is executed after each test case to clean up any data created during testing.
     * It calls the cleanAllData method to delete all from underTest repository.
     */
    @AfterEach
    void tearDown() { cleanAllData(); }

    // endregion

    /**
     * Tests the repository's ability to add interest for a social user in a hashtag.
     * It adds interest in two hashtags for a user and verifies the correctness of the count in each hashtag.
     */
    @Test
    void shouldAddInterestForUserInHashTags() {
        // Given: An existing socialUser and three hashTags

        // When: Adding interest in two hashtags for socialUser1
        underTest.addInterest(socialUser1.getId(), hashTag1.getId(), LocalDateTime.now());
        underTest.addInterest(socialUser1.getId(), hashTag2.getId(), LocalDateTime.now());

        // Then: Verifies the correctness of the interest count in each hashtag
        // HashTag1
        Optional<HashTag> hashTag1Optional = hashTagRepository.findById(hashTag1.getId());
        assertThat(hashTag1Optional).isPresent();
        assertThat(hashTag1Optional.get().getSocialUserInterestIn()).isEqualTo(hashTag1.getSocialUserInterestIn() + 1);

        // HashTag2
        Optional<HashTag> hashTag2Optional = hashTagRepository.findById(hashTag2.getId());
        assertThat(hashTag2Optional).isPresent();
        assertThat(hashTag2Optional.get().getSocialUserInterestIn()).isEqualTo(hashTag2.getSocialUserInterestIn() + 1);
    }

    /**
     * Tests whether the repository correctly determines the existence of a user's interest in a hashtag.
     * It asserts that the repository returns true when the interest exists and false when it does not.
     */
    @Test
    void shouldCheckExistenceOfUserInterestInHashTag() {
        // Given: An interest from socialUser1 to hashTag3
        underTest.addInterest(socialUser1.getId(), hashTag3.getId(), LocalDateTime.now());

        // When: Checking the existence of the interest
        boolean expectedTrue = underTest.existsInterest(socialUser1.getId(), hashTag3.getId());
        boolean expectedFalse = underTest.existsInterest(socialUser1.getId(), hashTag2.getId());

        // Then: Verifies the correctness of the existence checks
        assertThat(expectedTrue).isTrue();
        assertThat(expectedFalse).isFalse();
    }

    /**
     * Tests the deletion of a user's interest in a hashtag.
     * It adds an interest from socialUser1 to hashTag3, deletes the interest, and then verifies that the interest is no longer present.
     * The test also checks that the hashtag's interest count is correctly updated.
     */
    @Test
    void shouldDeleteUserInterestInHashTag() {
        // Given: An interest from socialUser1 to hashTag3 and the initial existence of the interest
        underTest.addInterest(socialUser1.getId(), hashTag3.getId(), LocalDateTime.now());
        boolean initialExistence = underTest.existsInterest(socialUser1.getId(), hashTag3.getId());
        assertThat(initialExistence).isTrue();

        // When: Deleting the interest
        underTest.deleteInterest(socialUser1.getId(), hashTag3.getId());

        // Then: Verifies the absence of the interest and the correct update of the hashtag's interest count
        boolean finalExistence = underTest.existsInterest(socialUser1.getId(), hashTag3.getId());
        assertThat(finalExistence).isFalse();

        Optional<HashTag> hashTag = hashTagRepository.findById(hashTag3.getId());
        assertThat(hashTag).isPresent();
        assertThat(hashTag3.getSocialUserInterestIn()).isEqualTo(hashTag.get().getSocialUserInterestIn());
    }


    // region Utility Methods
    /**
     * Creates test data by saving three HashTag and one SocialUser into the repository.
     * SocialUser #1: maria1
     * HashTag #1: FirstTagToTest
     * HashTag #2: SecondTagToTest
     * HashTag #3: ThirdTagToTest
     */
    private void createTestData() {
        // user #1
        this.socialUser1 = socialUserRepository.save(TestUtils.createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES"));

        // hashTag #1
        this.hashTag1 = hashTagRepository.save(TestUtils.createHashTag("1", "FirstTagToTest", 4, 10));

        // hashTag #2
        this.hashTag2 = hashTagRepository.save(TestUtils.createHashTag("2", "SecondTagToTest", 5, 11));

        // hashTag #3
        this.hashTag3 = hashTagRepository.save(TestUtils.createHashTag("3", "ThirdTagToTest", 6, 12));
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }
    // endregion
}
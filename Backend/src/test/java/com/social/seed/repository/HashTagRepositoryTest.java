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
import com.social.seed.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Unit tests for the {@link HashTagRepository}, focusing on testing individual methods and functionalities
 * in isolation for managing {@link HashTag}.
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2023-12-25
 */
@DataNeo4jTest
class HashTagRepositoryTest {

    @Autowired
    private HashTagRepository underTest;

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
    void tearDown() {
        cleanAllData();
    }
    // endregion

    // region Existence Checks
    /**
     * Tests whether the HashTagRepository correctly determines the existence of a HashTag by name.
     * It asserts that the repository returns true when the HashTag with the specified name exists,
     * and false when the HashTag with the specified name does not exist.
     */
    @Test
    void shouldCheckWhenHashTagByNameExists() {
        // Verifies if a HashTag with a given name exists
        assertHashTagByNameExists("FirstTagToTest", true);

        // Verifies if a HashTag with a non-existing name returns false
        assertHashTagByNameExists("Pepe", false);
    }

    @Test
    void shouldCheckWhenHashTagByIdExists() {
        assertHashTagByIdExists("1",true);

        assertHashTagByIdExists("noId",false);
    }
    // endregion

    // region Update Operations
    /**
     * Verifies that updating an existing HashTag is successful.
     * It ensures that the HashTagRepository correctly updates the properties of an existing HashTag,
     * including the name, and asserts that the updated HashTag's name matches the expected value after the update operation.
     */
    @Test
    void shouldUpdateExistingHashTagSuccessfully() {
        // Given: An existing HashTag with a specific name
        String name = "FirstTagToTest";
        Optional<HashTag> hashTag = underTest.findByName(name);
        assertThat(hashTag).isPresent();

        // When: Updating the existing HashTag properties
        String newName = "FirstTagToTestUpdate";
        underTest.update(hashTag.get().getId(), newName);
        Optional<HashTag> updatedHashTagOptional = underTest.findById(hashTag.get().getId());

        // Then: Verifies that the updated HashTag property is as expected
        assertThat(updatedHashTagOptional)
                .isPresent()
                .map(HashTag::getName)
                .contains(newName);

    }
    // endregion

    // region Find by Name
    /**
     * Verifies that retrieving a HashTag by name is successful for a valid name.
     * It checks if the HashTagRepository correctly finds a HashTag by name,
     * asserts that the HashTag is present, and verifies that its name matches the expected value.
     */
    @Test
    void shouldRetrieveHashTagByNameSuccessfullyForValidName() {
        // When
        Optional<HashTag> foundHashTagOptional = underTest.findByName("SecondTagToTest");

        // Then
        assertThat(foundHashTagOptional)
                .isPresent()
                .hasValueSatisfying(hashTag -> assertThat(hashTag.getName()).isEqualTo("SecondTagToTest"));
    }

    /**
     * Verifies that retrieving a HashTag by name returns null for an invalid name.
     * It checks if the HashTagRepository correctly handles the case when a HashTag with the specified name does not exist,
     * asserts that the HashTag is not present.
     */
    @Test
    void shouldRetrieveNullForInvalidHashTagByName() {
        // When
        Optional<HashTag> foundHashTagOptional = underTest.findByName("NonExistentHashTag");

        // Then
        assertThat(foundHashTagOptional).isNotPresent();
    }
    // endregion

    // region create
    /**
     * Tests the repository's ability to successfully create and save a new HashTag.
     * It ensures that the repository correctly saves the HashTag and retrieves it by ID,
     * asserting that the saved HashTag's name matches the expected value.
     */
    @Test
    void shouldSucceedWhenCreatingHashTag() {
        // Given: A new HashTag with a specific name
        HashTag newHashTag = new HashTag();
        newHashTag.setName("NewHashTag");

        // When: Saving the new HashTag
        underTest.save(newHashTag);
        Optional<HashTag> savedHashTagOptional = underTest.findById(newHashTag.getId());

        // Then: Verifies that the saved HashTag's name is as expected
        assertThat(savedHashTagOptional)
                .isPresent()
                .map(HashTag::getName)
                .contains("NewHashTag");
    }
    // endregion

    // region delete
    /**
     * Tests the repository's ability to successfully delete a HashTag.
     * It retrieves a HashTag by name, deletes it by ID, and verifies that the HashTag is no longer present in the repository.
     */
    @Test
    void shouldSucceedWhenDeletingHashTag() {
        // Given: An existing HashTag with a specific name
        Optional<HashTag> hashTagToDelete = underTest.findByName("ThirdTagToTest");
        assertThat(hashTagToDelete).isPresent();

        // When: Deleting the existing HashTag
        underTest.deleteById(hashTagToDelete.get().getId());
        Optional<HashTag> deletedHashTagOptional = underTest.findById(hashTagToDelete.get().getId());

        // Then: Verifies that the HashTag is no longer present in the repository
        assertThat(deletedHashTagOptional).isEmpty();
    }
    // endregion

    // region List All
    /**
     * Tests the repository's ability to list all existing HashTags.
     * It retrieves all HashTags using pagination, verifies the total count, and checks if the expected HashTag names are present.
     */
    @Test
    void shouldListAllExistingHashTags() {
        // Given: Page request for the first page with 10 items
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: Retrieving all HashTags using pagination
        Page<HashTag> hashTagPage = underTest.findAll(pageRequest);
        List<HashTag> testHashTags = hashTagPage.getContent();

        // Then: Verifies the total count and checks if the expected HashTag names are present
        assertThat(hashTagPage.getTotalElements()).isEqualTo(3);

        Set<String> expectedNames = Set.of("FirstTagToTest", "SecondTagToTest", "ThirdTagToTest");
        Set<String> actualNames = testHashTags.stream().map(HashTag::getName).collect(Collectors.toSet());

        assertThat(actualNames).isEqualTo(expectedNames);
    }
    //endregion

    // region Utility Methods
    /**
     * Asserts whether a HashTag with the given name exists as expected.
     *
     * @param name    The name of the HashTag to check for existence.
     * @param expected The expected result (true if the HashTag exists, false otherwise).
     */
    private void assertHashTagByNameExists(String name, boolean expected) {
        assertThat(underTest.existByName(name)).isEqualTo(expected);
    }

    private void assertHashTagByIdExists(String id, boolean expected) {
        assertThat(underTest.existById(id)).isEqualTo(expected);
    }

    /**
     * Creates test data by saving three HashTag into the repository.
     * HashTag #1: FirstTagToTest
     * HashTag #2: SecondTagToTest
     * HashTag #3: ThirdTagToTest
     */
    private void createTestData() {
        // hashTag #1
        underTest.save(TestUtils.createHashTag("1", "FirstTagToTest", 4, 10));

        // hashTag #2
        underTest.save(TestUtils.createHashTag("2", "SecondTagToTest", 5, 11));

        // hashTag #3
        underTest.save(TestUtils.createHashTag("3", "ThirdTagToTest", 6, 12));
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }
    // endregion
}
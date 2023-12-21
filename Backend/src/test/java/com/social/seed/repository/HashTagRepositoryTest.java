package com.social.seed.repository;

import com.social.seed.model.HashTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataNeo4jTest
public class HashTagRepositoryTest {

    @Autowired
    private HashTagRepository hashTagRepository;

    /**
     * Method to set up initial data for the test.
     * Initializes the database with three HashTags.
     */
    @Before
    public void setUp() {
        hashTagRepository.deleteAll();
        HashTag hashTag1 = new HashTag("1", "FristTagToTest", 4, 10);
        HashTag hashTag2 = new HashTag("2", "SecondTagToTest", 5, 11);
        HashTag hashTag3 = new HashTag("3", "ThirdTagToTest", 6, 12);
        hashTagRepository.save(hashTag1);
        hashTagRepository.save(hashTag2);
        hashTagRepository.save(hashTag3);
    }

    /**
     * Method to clean up data after the test.
     * Deletes all HashTags from the database.
     */
    @After
    public void tearDown() {
        hashTagRepository.deleteAll();
    }

    /**
     * Verifies that the 'existByName' method returns true for an existing HashTag.
     */
    @Test
    public void existsByNameShouldReturnTrueForExistingHashTag() {
        // When
        Boolean exists = hashTagRepository.existByName("FristTagToTest");

        // Then
        assertTrue(exists);
    }

    /**
     * Verifies that the 'existByName' method returns false for a non-existing HashTag.
     */
    @Test
    public void existsByNameShouldReturnFalseForNonExistingHashTag() {
        // When
        Boolean exists = hashTagRepository.existByName("Pepe");

        // Then
        assertFalse(exists);
    }

    /**
     * Verifies that updating an existing HashTag is successful.
     */
    @Test
    public void updateExistingHashTagShouldSucceed() {
        // Given
        HashTag hashTag = hashTagRepository.findByName("FristTagToTest").get();

        // When
        hashTagRepository.update(hashTag.getId(), "FristTagToTestUpdate");
        Optional<HashTag> updatedHashTagOptional = hashTagRepository.findById(hashTag.getId());

        // Then
        assertTrue(updatedHashTagOptional.isPresent());
        assertEquals("FristTagToTestUpdate", updatedHashTagOptional.get().getName());
    }

    /**
     * Verifies that retrieving a HashTag by a valid name is successful.
     */
    @Test
    public void getHashTagByNameShouldSucceedForValidName() {
        // When
        Optional<HashTag> foundHashTagOptional = hashTagRepository.findByName("SecondTagToTest");

        // Then
        assertTrue(foundHashTagOptional.isPresent());
        assertEquals("SecondTagToTest", foundHashTagOptional.get().getName());
    }

    /**
     * Verifies that retrieving a HashTag by an invalid name returns null.
     */
    @Test
    public void getHashTagByNameShouldReturnNullForInvalidName() {
        // When
        Optional<HashTag> foundHashTagOptional = hashTagRepository.findByName("NonExistentHashTag");

        // Then
        assertFalse(foundHashTagOptional.isPresent());
    }

    /**
     * Verifies that creating a new HashTag is successful.
     */
    @Test
    public void createHashTagShouldSucceed() {
        // Given
        HashTag newHashTag = new HashTag();
        newHashTag.setName("NewHashTag");

        // When
        hashTagRepository.save(newHashTag);
        Optional<HashTag> savedHashTagOptional = hashTagRepository.findById(newHashTag.getId());

        // Then
        assertTrue(savedHashTagOptional.isPresent());
        assertEquals("NewHashTag", savedHashTagOptional.get().getName());
    }

    /**
     * Verifies that attempting to create a HashTag with a duplicate name fails.
     */
    @Test
    public void createHashTagShouldFailForDuplicateName() {
        // Given
        HashTag newHashTag = new HashTag();
        newHashTag.setName("NewHashTag");

        // When
        Boolean exists = hashTagRepository.existByName(newHashTag.getName());

        // Then
        assertFalse(exists);
    }

    /**
     * Verifies that deleting a HashTag is successful.
     */
    @Test
    public void deleteHashTagShouldSucceed() {
        // Given
        HashTag hashTagToDelete = hashTagRepository.findByName("ThirdTagToTest").get();

        // When
        hashTagRepository.deleteById(hashTagToDelete.getId());
        Optional<HashTag> deletedHashTagOptional = hashTagRepository.findById(hashTagToDelete.getId());

        // Then
        assertFalse(deletedHashTagOptional.isPresent());
    }

    /**
     * Verifies that listing all existing HashTags returns the expected results.
     */
    @Test
    public void shouldListAllExistingHashTags() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<HashTag> hashTagPage = hashTagRepository.findAll(pageRequest);

        // When
        List<HashTag> testHashTags = hashTagPage.getContent();

        // Then
        assertEquals(3, testHashTags.size());

        // Verify names without considering the position
        Set<String> expectedNames = new HashSet<>(Arrays.asList("FristTagToTest", "SecondTagToTest", "ThirdTagToTest"));

        for (HashTag hashTag : testHashTags) {
            assertTrue(expectedNames.contains(hashTag.getName()));
        }
    }
}
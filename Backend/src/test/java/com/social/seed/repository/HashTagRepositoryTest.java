package com.social.seed.repository;

import com.social.seed.model.HashTag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataNeo4jTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HashTagRepositoryTest {

    @Autowired
    private HashTagRepository hashTagRepository;

    /**
     * Given a HashTag in the database,
     * when checking for existence by name,
     * then the result should be true.
     */
    @Test
    public void givenHashTagInDatabase_whenExistByName_thenReturnTrue() {
        // Given
        HashTag hashTag = new HashTag();
        hashTag.setName("TestHashTag");
        hashTagRepository.save(hashTag);

        // When
        Boolean exists = hashTagRepository.existByName("TestHashTag");

        // Then
        assertTrue(exists);
    }

    /**
     * Given a HashTag in the database,
     * when checking for existence by a non-existent name,
     * then the result should be false.
     */
    @Test
    public void givenHashTagInDatabase_whenExistByNonExistentName_thenReturnFalse() {
        // Given
        HashTag hashTag = new HashTag();
        hashTag.setName("TestHashTag");
        hashTagRepository.save(hashTag);

        // When
        Boolean exists = hashTagRepository.existByName("NonExistentHashTag");

        // Then
        assertFalse(exists);
    }

    /**
     * Given a HashTag in the database,
     * when updating the HashTag,
     * then verify the changes.
     */
    @Test
    public void givenHashTagInDatabase_whenUpdateHashTag_thenVerifyChanges() {
        // Given
        HashTag hashTag = new HashTag();
        hashTag.setName("TestHashTag");
        hashTagRepository.save(hashTag);

        // When
        hashTagRepository.update(hashTag.getId(), "UpdatedHashTag");
        Optional<HashTag> updatedHashTagOptional = hashTagRepository.findById(hashTag.getId());

        // Then
        assertTrue(updatedHashTagOptional.isPresent());
        assertEquals("UpdatedHashTag", updatedHashTagOptional.get().getName());
    }

    /**
     * Given a HashTag in the database,
     * when finding a HashTag by name,
     * then the result should be the expected HashTag.
     */
    @Test
    public void givenHashTagInDatabase_whenFindByName_thenReturnHashTag() {
        // Given
        HashTag hashTag = new HashTag();
        hashTag.setName("TestHashTag");
        hashTagRepository.save(hashTag);

        // When
        Optional<HashTag> foundHashTagOptional = hashTagRepository.findByName("TestHashTag");

        // Then
        assertTrue(foundHashTagOptional.isPresent());
        assertEquals("TestHashTag", foundHashTagOptional.get().getName());
    }

    /**
     * When finding a non-existent HashTag by name,
     * then the result should be an empty optional.
     */
    @Test
    public void givenNonExistentHashTag_whenFindByName_thenReturnEmptyOptional() {
        // When
        Optional<HashTag> foundHashTagOptional = hashTagRepository.findByName("NonExistentHashTag");

        // Then
        assertFalse(foundHashTagOptional.isPresent());
    }

    /**
     * Given a new HashTag,
     * when saving the new HashTag,
     * then verify that the HashTag is saved correctly.
     */
    @Test
    public void givenNewHashTag_whenSaveHashTag_thenVerifySavedHashTag() {
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
     * Given a HashTag in the database,
     * when deleting the HashTag by ID,
     * then verify that the HashTag is deleted.
     */
    @Test
    public void givenHashTagInDatabase_whenDeleteHashTagById_thenVerifyDeletion() {
        // Given
        HashTag hashTagToDelete = new HashTag();
        hashTagToDelete.setName("HashTagToDelete");
        hashTagRepository.save(hashTagToDelete);

        // When
        hashTagRepository.deleteById(hashTagToDelete.getId());
        Optional<HashTag> deletedHashTagOptional = hashTagRepository.findById(hashTagToDelete.getId());

        // Then
        assertFalse(deletedHashTagOptional.isPresent());
    }

    /**
     * Given existing HashTags in the database,
     * when finding all HashTags with pagination,
     * then verify the total number of elements.
     */
    @Test
    public void givenExistingHashTags_whenFindAllWithPageable_thenVerifyTotalElements() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<HashTag> hashTagPage = hashTagRepository.findAll(pageRequest);
        long totalActual = hashTagPage.getTotalElements();

        HashTag hashTag1 = new HashTag();
        hashTag1.setName("HashTag1");
        hashTagRepository.save(hashTag1);

        HashTag hashTag2 = new HashTag();
        hashTag2.setName("HashTag2");
        hashTagRepository.save(hashTag2);

        HashTag hashTag3 = new HashTag();
        hashTag3.setName("HashTag3");
        hashTagRepository.save(hashTag3);

        totalActual += 3;

        // When
        Page<HashTag> testHashTagPage = hashTagRepository.findAll(pageRequest);

        // Then
        assertEquals(totalActual, testHashTagPage.getTotalElements());
    }
}
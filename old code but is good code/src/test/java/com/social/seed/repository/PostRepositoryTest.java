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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Unit tests for the {@link PostRepository}, focusing on testing individual methods and functionalities
 * in isolation for managing {@link Post}.
 * <p>
 * The tests use the {@code @DataNeo4jTest} annotation to provide an embedded Neo4j environment
 * for the repository layer.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2023-12-25
 */
@DataNeo4jTest
class PostRepositoryTest {
    //region dependencies
    @Autowired
    private PostRepository underTest;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private SocialUserRepository socialUserRepository;
    //endregion

    //region variables
    private SocialUser user1;
    private Post post1;
    private Post post2;
    private Post post3;
    //endregion

    // region Setup and Tear down
    /**
     * This method is executed before each test case to set up the necessary test data.
     * It calls the createTestData method to populate the repository with sample Post data.
     */
    @BeforeEach
    void setUp(){
        cleanAllData();
        createTestData();
    }

    /**
     * This method is executed after each test case to clean up any data created during testing.
     * It calls the cleanAllData method to delete all Post from the repository.
     */
    @AfterEach
    void tearDown(){ cleanAllData();}
    //endregion

    // region Existence Checks
    /**
     * Verifies that the repository correctly returns an existing Post by its ID.
     * It retrieves a Post by its ID and asserts that the returned Post is present,
     * and its content matches the expected content.
     */
    @Test
    void existsByIdShouldReturnTrueForExistingPost() {
        // When: Retrieving a Post by its ID
        Optional<Post> post = underTest.findById(post1.getId());

        // Then: Verifies that the Post is present and its content matches the expected content
        assertThat(post)
                .as("The retrieved Post should be present and have matching content")
                .isPresent()
                .map(Post::getContent)
                .isEqualTo(Optional.of(post1.getContent()));
    }
    // endregion

    /**
     * Tests the method to list all existing posts, ensuring that the correct number of posts is retrieved and that
     * each post has the expected content.
     */
    @Test
    void shouldListAllExistingPost() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> posts = underTest.findAll(pageRequest);

        // When
        List<Post> testPosts = posts.getContent();

        // Then
        assertThat(testPosts)
                .as("The number of retrieved posts should match the expected count")
                .hasSize(3);

        // Verify names without considering the position
        Set<String> expectedContent = new HashSet<>(Arrays.asList(
                post1.getContent(),
                post2.getContent(),
                post3.getContent())
        );

        for (Post post : testPosts) {
            assertThat(expectedContent)
                    .as("Each retrieved post should have expected content")
                    .contains(post.getContent());
        }
    }

    /**
     * Tests the method to update an existing post, ensuring that the post is successfully updated with the new content,
     * update date, and image URL.
     */
    @Test
    void updateExistingPostShouldSucceed() {
        // Given
        Post postToUpdate = underTest.findById(post1.getId()).get();

        // When
        underTest.update(
                postToUpdate.getId(),
                "Here testing the content, new content :)",
                LocalDateTime.parse("1909-01-01T00:00:00"),
                "/nueva/url/img.jpg"
        );
        Optional<Post> postUpdated = underTest.findById(post1.getId());

        // Then
        assertThat(postUpdated)
                .as("The updated Post should be present")
                .isPresent();
        assertThat(postUpdated.get().getContent())
                .as("The content of the updated Post should not be equal to the original content")
                .usingComparator(String::compareToIgnoreCase)
                .isNotEqualTo(post1.getContent());
        assertThat(postUpdated.get().getUpdateDate())
                .as("The update date of the Post should match the expected update date")
                .isEqualTo(LocalDateTime.parse("1909-01-01T00:00:00"));
        assertThat(postUpdated.get().getImageUrl())
                .as("The image URL of the Post should not be equal to the original image URL")
                .usingComparator(String::compareToIgnoreCase)
                .isNotEqualTo(post1.getImageUrl());
    }

    /**
     * Tests the method to create a new post, ensuring that the post is saved successfully with the correct content,
     * author, and hashtags.
     */
    @Test
    void createNewPostShouldSucceed() {
        // When: Creating a new post
        Post newPost = underTest.save(
                Post.builder()
                        .content("My create post is #NewPost to the #Eternity")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/myloved.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );

        // Saving hashtags associated with the new post
        saveHashtagsInPost(newPost, user1);

        // Retrieving the saved post from the database
        Optional<Post> savedPost = underTest.findById(newPost.getId());

        // Then: Verifying the results

        // Ensure that the post is present in the database
        assertThat(savedPost)
                .as("The saved Post should be present in the database")
                .isPresent();

        // Verify that the content of the saved post matches the expected content
        assertThat(savedPost.get().getContent())
                .usingComparator(String::compareToIgnoreCase)
                .withFailMessage("Contents should be equal")
                .isEqualTo("My create post is #NewPost to the #Eternity");

        // Verify that the author's ID of the saved post matches user1's ID
        assertThat(savedPost.get().getAuthor().getAuthor().getId())
                .withFailMessage("The post author's ID should be equal to user1's ID")
                .isEqualTo(user1.getId());

        // Verify hashtags without considering the position

        // Define expected hashtags
        Set<String> expectedHashTags = new HashSet<>(Arrays.asList(
                "NewPost",
                "Eternity"
        ));

        // Retrieve actual hashtags from the saved post
        List<HashTag> actualHashTags = savedPost.get().getHashTags();

        // Verify that the actual hashtags match the expected hashtags
        assertThat(actualHashTags)
                .as("Verify that the actual hashtags match the expected hashtags")
                .extracting(HashTag::getName)
                .containsExactlyInAnyOrderElementsOf(expectedHashTags);

        // Verify that each hashtag is tagged in only one post
        assertThat(actualHashTags)
                .as("Verify that each hashtag is tagged in only one post")
                .extracting(HashTag::getPostTaggedIn)
                .containsOnly(1);
    }

    //region LIKE
    /**
     * Tests the method to check if a liked relationship exists between a social user and a post,
     * ensuring the correct result when the relationship exists.
     * It creates a liked relationship between user1 and post2,
     * checks if user1 has liked post2, and verifies that the result is true,
     * indicating that user1 has liked post2.
     */
    @Test
    void existsLikedBetweenSocialUserAndPostShouldReturnTrue() {
        // Given: Creating a liked relationship between user1 and post2
        underTest.createUserByIdLikedPostById(
                user1.getId(),
                post2.getId(),
                LocalDateTime.now()
        );

        // When: Checking if user1 has liked post2
        boolean expected = underTest.isUserByIdLikedPostById(
                user1.getId(), post2.getId()
        );

        // Then: Verifying the result
        // Ensure that the result is true, indicating that user1 has liked post2
        assertThat(expected)
                .as("Ensure that the result is true, indicating that user1 has liked post2")
                .isTrue();
    }

    /**
     * Tests the method to check if a liked relationship exists between a social user and a post,
     * ensuring the correct result when the relationship does not exist.
     * It checks if user1 has liked post3, and verifies that the result is false,
     * indicating that user1 has not liked post3.
     */
    @Test
    void doNotExistsLikedBetweenSocialUserAndPostShouldReturnFalse() {
        // When: Checking if user1 has liked post3
        boolean actualResult = underTest.isUserByIdLikedPostById(
                user1.getId(), post3.getId()
        );

        // Then: Verifying the result

        // Ensure that the result is false, indicating that user1 has not liked post3
        assertThat(actualResult)
                .as("Ensure that the result is false, indicating that user1 has not liked post3")
                .isFalse();
    }

    /**
     * Tests the method to create a liked relationship between a social user and a post, ensuring success.
     * It creates a liked relationship between user1 and post1, checks if user1 has liked post1,
     * retrieves the updated post from the database, and verifies that the liked count is now 1
     * and user1 has successfully liked post1.
     */
    @Test
    void createLikedBetweenSocialUserAndPostShouldSuccessful() {
        // Given: Creating a liked relationship between user1 and post1
        underTest.createUserByIdLikedPostById(
                user1.getId(),
                post1.getId(),
                LocalDateTime.now()
        );

        // When: Checking if user1 has liked post1
        boolean existLiked = underTest.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );

        // Then: Verifying the results

        // Retrieve the updated post from the database
        Post resultPost = underTest.findById(post1.getId()).orElseThrow();

        // Verify that the liked count of post1 is now 1
        assertThat(resultPost.getLikedCount())
                .as("Verify that the liked count of post1 is now 1")
                .isEqualTo(1);

        // Verify that user1 has successfully liked post1
        assertThat(existLiked)
                .as("Verify that user1 has successfully liked post1")
                .isTrue();
    }

    /**
     * Tests the method to delete a liked relationship between a social user and a post, ensuring success.
     * It creates a liked relationship between user1 and post1, checks if the relationship exists before deletion,
     * deletes the liked relationship, and verifies the results after deletion.
     */
    @Test
    void deleteLikedBetweenSocialUserAndPostShouldSuccessful() {
        // Given: Creating a liked relationship between user1 and post1
        underTest.createUserByIdLikedPostById(
                user1.getId(),
                post1.getId(),
                LocalDateTime.now()
        );

        // Check if the liked relationship exists before deletion
        boolean existLikedBeforeDeletion = underTest.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );
        assertThat(existLikedBeforeDeletion)
                .as("Check if the liked relationship exists before deletion")
                .isTrue();

        // When: Deleting the liked relationship between user1 and post1
        underTest.deleteUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );

        // Then: Verifying the results after deletion

        // Check if the liked relationship no longer exists after deletion
        boolean existLikedAfterDeletion = underTest.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );
        assertThat(existLikedAfterDeletion)
                .as("Check if the liked relationship no longer exists after deletion")
                .isFalse();
    }
    //endregion

    /**
     * Tests the method to check if a user is the author of a post by ID.
     * It creates a new post authored by user1, saves hashtags associated with the new post,
     * checks if user1 is the author of the new post, and verifies the result.
     */
    @Test
    void isUserAuthorOfThePostByIdShouldReturnTrue() {
        // Given: Creating a new post authored by user1
        Post newPost = underTest.save(
                Post.builder()
                        .content("I feel #Happy Today because of you :)")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/imge.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        // Save hashtags associated with the new post
        saveHashtagsInPost(newPost, user1);

        // When: Checking if user1 is the author of the new post
        boolean isAuthor = underTest.isUserAuthorOfThePostById(user1.getId(), newPost.getId());

        // Then: Verifying the result

        // Ensure that user1 is the author of the new post
        assertThat(isAuthor)
                .as("Expected user1 to be the author of the post")
                .isTrue();
    }

    /**
     * Tests the successful creation of a relationship between a post and a hashtag.
     * It creates a new post by user1, ensures that the post has no hashtags initially,
     * creates and saves a new hashtag, creates a relationship between the post and the hashtag,
     * and verifies that the post now has two hashtags with the expected names.
     */
    @Test
    void createRelationshipTaggedWithHashTagShouldBeSuccessful() {
        // Given: Create a new post by user1
        Post newPost = createTestPost("I feel #Happy Today because of you :)", "aqui/img.png");

        // Ensure that the post has no hashtags initially
        assertThat(newPost.getHashTags())
                .as("Ensure that the post has no hashtags initially")
                .hasSize(1);

        // Create and save a new hashtag
        HashTag newHashTag = new HashTag("uno", "Today", 6, 12);
        hashTagRepository.save(newHashTag);

        // When: Creating a relationship between the post and the hashtag
        underTest.createRelationshipTaggedWithHashTag(newPost.getId(), newHashTag.getId());

        // Then: Verify the results

        // Retrieve the updated post from the database
        Optional<Post> updatedPost = underTest.findById(newPost.getId());
        assertThat(updatedPost)
                .as("Retrieve the updated post from the database")
                .isPresent();

        // Verify that the post now has two hashtags
        assertThat(updatedPost.get().getHashTags())
                .as("Verify that the post now has two hashtags")
                .hasSize(2);

        assertThat(updatedPost.get().getHashTags())
                .as("Verify that the post's hashtags are as expected")
                .extracting(HashTag::getName)
                .containsExactlyInAnyOrder("Happy", "Today");
    }

    /**
     * Tests the successful deletion of all relationships between a post and hashtags.
     * It ensures that the post initially has one or more hashtags, deletes all relationships,
     * and verifies that the post no longer has any hashtags after deletion.
     */
    @Test
    void deleteAllRelationshipTaggedWithHashTagShouldBeSuccessful() {
        // Given: Ensure that post1 initially has one or more hashtags
        assertThat(post1.getHashTags())
                .as("Ensure that post1 initially has one or more hashtags")
                .isNotEmpty(); // Use isNotEmpty for clarity

        // When: Deleting all relationships with hashtags for post1
        underTest.deleteAllRelationshipTaggedWithHashTag(post1.getId());

        // Then: Verify that post1 no longer has any hashtags
        Post updatedPost = underTest.findById(post1.getId()).orElseThrow();
        assertThat(updatedPost.getHashTags())
                .as("Verify that post1 no longer has any hashtags")
                .isEmpty();
    }

    //region utils
    /**
     * Saves hashtags associated with a post and creates a "posted by" relationship.
     * @param _post The post to save hashtags for.
     * @param _user The user who posted the post.
     */
    private void saveHashtagsInPost(Post _post, SocialUser _user){
        // Extract the hashtags from the content of the post
        String[] hashtags = extractHashtags(_post.getContent());

        // Save all the hashtags relationship with the post
        this.saveAllTheHashtagsRelationshipWithPost(_post.getId(), hashtags);

        // Create the posted by relationship
        underTest.createPostedRelationship(
                _post.getId(),
                _user.getId(),
                LocalDateTime.now()
        );
    }

    /**
     * Extracts hashtags from the content of a post using a regular expression.
     * @param postContent The content of the post.
     * @return An array of hashtags extracted from the content.
     */
    static String[] extractHashtags(String postContent) {
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(postContent);

        StringBuilder hashtagsBuilder = new StringBuilder();

        while (matcher.find()) {
            hashtagsBuilder.append(matcher.group()).append(" ");
        }

        // Remove last white space if necessary
        String hashtagsString = hashtagsBuilder.toString().trim();

        // Split the result into an array of hashtags
        return hashtagsString.split(" ");
    }

    /**
     * Saves relationships between a post and the hashtags extracted from its content.
     * If a hashtag doesn't exist, it is created before establishing the relationship.
     *
     * @param idPost   The ID of the post to establish relationships with hashtags.
     * @param hashtags An array of hashtags extracted from the content of the post.
     */
    private void saveAllTheHashtagsRelationshipWithPost(String idPost, String[] hashtags) {
        for (String hashtagText : hashtags) {
            // Delete the "#" symbol from the text
            String cleanedHashtagText = hashtagText.replace("#", "");

            Optional<HashTag> hashTag = hashTagRepository.findByName(cleanedHashtagText);

            // Validate if the hashtag exists
            if (hashTag.isPresent()) {
                // If it exists, the relationship is created
                underTest.createRelationshipTaggedWithHashTag(idPost, hashTag.get().getId());
            } else {
                // If the hashtag does not exist, create it and then create the relationship
                HashTag savedNewHashTag = hashTagRepository.save(
                        HashTag.builder()
                                .name(cleanedHashtagText)
                                .socialUserInterestIn(0)
                                .postTaggedIn(0)
                                .build()
                );
                underTest.createRelationshipTaggedWithHashTag(idPost, savedNewHashTag.getId());
            }
        }
    }

    /**
     * Deletes all data from the Repository, cleaning up the test environment.
     */
    private void cleanAllData() {
        underTest.deleteAll();
    }

    /**
     * Creates and saves test data for users and posts to be used in unit tests.
     * It creates a test user (user1) and three test posts (post1, post2, post3).
     */
    private void createTestData() {
        // Creating and saving a test user
        this.user1 = socialUserRepository.save(
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

        // Creating and saving three test posts
        this.post1 = createTestPost("Today I am very #Happy to share with all of you my #FirstPost","images/miprimer.jpg");
        this.post2 = createTestPost("My #FirstPost is a Huge change in my #live","images/miotro.jpg");
        this.post3 = createTestPost("My #Post is the onlyOne I #loved","images/myloved.jpg");
    }

    /**
     * Creates and saves a test Post with the specified content and image URL.
     * The method also associates hashtags with the created post.
     *
     * @param content   The content of the post.
     * @param imageUrl  The URL of the image attached to the post.
     * @return The created Post object with associated hashtags.
     */
    private Post createTestPost(String content, String imageUrl) {
        // Create a new Post and save it
        Post post = underTest.save(
                Post.builder()
                        .content(content)
                        .updateDate(LocalDateTime.now())
                        .imageUrl(imageUrl)
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );

        // Save hashtags associated with the new post
        saveHashtagsInPost(post, user1);

        // Return the saved Post object
        return underTest.findById(post.getId()).get();
    }
    //endregion
}

package com.social.seed.repository;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataNeo4jTest
public class PostRepositoryTest {
    //region dependencies
    @Autowired
    private PostRepository postRepository;
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

    // region config
    @Before
    public void setUp(){
        cleanAllData();
        createTestData();
    }

    @After
    public void tearDown(){ cleanAllData();}
    //endregion

    @Test
    public void existsByIdShouldReturnTrueForExistingPost(){
        // When
        Post post = postRepository.findById(post1.getId()).get();

        // Then
        assertEquals(post1.getContent(),post.getContent());
    }

    @Test
    public void shouldListAllExistingPost() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> posts = postRepository.findAll(pageRequest);

        // When
        List<Post> testPosts = posts.getContent();

        // Then
        assertEquals(3, testPosts.size());

        // Verify names without considering the position
        Set<String> expectedContent = new HashSet<>(Arrays.asList(
                post1.getContent(),
                post2.getContent(),
                post3.getContent())
        );

        for (Post post : testPosts) {
            assertTrue(expectedContent.contains(post.getContent()));
        }
    }

    @Test
    public void updateExistingPostShouldSucceed() {
        // Given
        Post postToUpdate = postRepository.findById(post1.getId()).get();

        // When
        postRepository.update(
                postToUpdate.getId(),
                "Estoy Testeando este post",
                LocalDateTime.parse("1909-01-01T00:00:00"),
                "/nueva/url/img.jpg"
        );
        Optional<Post> postUpdated = postRepository.findById(post1.getId());

        // Then
        assertTrue(postUpdated.isPresent());
        assertNotEquals(post1.getContent(), postUpdated.get().getContent());
        assertEquals("Estoy Testeando este post",postUpdated.get().getContent());

        assertNotEquals(post1.getUpdateDate(), postUpdated.get().getUpdateDate());
        assertEquals(LocalDateTime.parse("1909-01-01T00:00:00"), postUpdated.get().getUpdateDate());

        assertNotEquals(post1.getImageUrl(), postUpdated.get().getImageUrl());
        assertEquals("/nueva/url/img.jpg", postUpdated.get().getImageUrl());
    }

    @Test
    public void createNewPostShouldSucceed() {
        // When
        Post newPost = postRepository.save(
                Post.builder()
                        .content("My create post is #NewPost to the #Eternity")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/myloved.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        saveHashtagsInPost(newPost, user1);

        Optional<Post> savedPost = postRepository.findById(newPost.getId());

        // Then
        assertTrue(savedPost.isPresent());
        assertEquals("My create post is #NewPost to the #Eternity", savedPost.get().getContent());
        assertEquals(user1.getId(), savedPost.get().getAuthor().getAuthor().getId());

        // Verify names without considering the position
        Set<String> expectedHashTag = new HashSet<>(Arrays.asList(
                "NewPost",
                "Eternity"
                )
        );

        List<HashTag> hashTags = savedPost.get().getHashTags();

        for (HashTag tag: hashTags) {
            assertTrue(expectedHashTag.contains(tag.getName()));
            assertEquals(1, tag.getPostTaggedIn());
        }

    }

    //region LIKE
    @Test
    public void existsLikedBetweenSocialUserAndPostShouldReturnTrue() {
        // Given
        postRepository.createUserByIdLikedPostById(
                user1.getId(),
                post2.getId(),
                LocalDateTime.now()
        );

        // When
        boolean existLiked = postRepository.isUserByIdLikedPostById(
                user1.getId(), post2.getId()
        );

        // Then
        assertTrue(existLiked);
    }

    @Test
    public void doNotExistsLikedBetweenSocialUserAndPostShouldReturnFalse() {
        // When
        boolean existLiked = postRepository.isUserByIdLikedPostById(
                user1.getId(), post3.getId()
        );

        // Then
        assertFalse(existLiked);
    }

    @Test
    public void createLikedBetweenSocialUserAndPostShouldSuccessful() {
        // Given
        postRepository.createUserByIdLikedPostById(
                user1.getId(),
                post1.getId(),
                LocalDateTime.now()
        );

        // When
        boolean existLiked = postRepository.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );

        // Then
        Post resultPost = postRepository.findById(post1.getId()).get();
        assertEquals(1,resultPost.getLikedCount());
        assertTrue(existLiked);
    }

    @Test
    public void deleteLikedBetweenSocialUserAndPostShouldSuccessful() {
        // Given
        postRepository.createUserByIdLikedPostById(
                user1.getId(),
                post1.getId(),
                LocalDateTime.now()
        );
        boolean existLiked = postRepository.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );
        assertTrue(existLiked);

        // When
        postRepository.deleteUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );

        // Then
        existLiked = postRepository.isUserByIdLikedPostById(
                user1.getId(), post1.getId()
        );
        assertFalse(existLiked);
    }
    //endregion

    @Test
    public void isUserAuthorOfThePostByIdShouldReturnTrue() {
        // Given
        Post newPost = postRepository.save(
                Post.builder()
                        .content("I feel #Happy Today because of you :)")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/imge.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        saveHashtagsInPost(newPost, user1);

        // When
        boolean isAuthor = postRepository.isUserAuthorOfThePostById(user1.getId(), newPost.getId());

        // Then
        assertTrue(isAuthor);
    }

    @Test
    public void createRelationshipTaggedWithHashTagShouldSuccessful() {
        // Given
        Post newPost = postRepository.save(
                Post.builder()
                        .content("I feel #Happy Today because of you :)")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/imge.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        // revisar que cuando no existe # en el texto da un error contando como que se creó un hashtag
        saveHashtagsInPost(newPost, user1);
        Post resultPost = postRepository.findById(newPost.getId()).get();
        assertEquals(1,resultPost.getHashTags().size());

        HashTag newHashTag = new HashTag("uno", "Today", 6, 12);
        hashTagRepository.save(newHashTag);

        // When
        postRepository.createRelationshipTaggedWithHashTag(
                newPost.getId(),
                newHashTag.getId()
        );

        // Then
        resultPost = postRepository.findById(newPost.getId()).get();
        assertEquals(2,resultPost.getHashTags().size());
        //assertTrue(existLiked);
    }

    @Test
    public void deleteAllRelationshipTaggedWithHashTagShouldSuccessful() {
        // Given
        Post newPost = postRepository.save(
                Post.builder()
                        .content("I feel #Happy Today #because of you :)")
                        .updateDate(LocalDateTime.now())
                        .imageUrl("images/imge.jpg")
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        // revisar que cuando no existe # en el texto da un error contando como que se creó un hashtag
        saveHashtagsInPost(newPost, user1);
        Post resultPost = postRepository.findById(newPost.getId()).get();
        assertEquals(2,resultPost.getHashTags().size());

        // When
        postRepository.deleteAllRelationshipTaggedWithHashTag(newPost.getId());

        // Then
        resultPost = postRepository.findById(newPost.getId()).get();
        assertEquals(0,resultPost.getHashTags().size());
    }

    //region utils
    private void saveHashtagsInPost(Post _post, SocialUser _user){
        // Extract the hashtags from the content of the post
        String[] hashtags = extractHashtags(_post.getContent());

        // Save all the hashtags relationship with the post
        this.saveAllTheHashtagsRelationshipWithPost(_post.getId(), hashtags);

        // Create the posted by relationship
        postRepository.createPostedRelationship(
                _post.getId(),
                _user.getId(),
                LocalDateTime.now()
        );
    }

    public static String[] extractHashtags(String postContent) {
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

    private void saveAllTheHashtagsRelationshipWithPost(String idPost, String[] hashtags) {
        for (String hashtagText : hashtags) {
            // Delete the "#" symbol from the text
            String cleanedHashtagText = hashtagText.replace("#", "");

            Optional<HashTag> hashTag = hashTagRepository.findByName(cleanedHashtagText);

            // Validate if the hashtag exists
            if (hashTag.isPresent()) {
                // If it exists, the relationship is created
                postRepository.createRelationshipTaggedWithHashTag(idPost, hashTag.get().getId());
            } else {
                // If the hashtag does not exist, create it and then create the relationship
                HashTag savedNewHashTag = hashTagRepository.save(
                        HashTag.builder()
                                .name(cleanedHashtagText)
                                .socialUserInterestIn(0)
                                .postTaggedIn(0)
                                .build()
                );
                postRepository.createRelationshipTaggedWithHashTag(idPost, savedNewHashTag.getId());
            }
        }
    }

    private void cleanAllData() {
        postRepository.deleteAll();
    }

    private void createTestData() {
        // Adding test data
        // User
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
        // Posts
        this.post1 = createTestPost("Today I am very #Happy to share with all of you my #FirstPost","images/miprimer.jpg");
        this.post2 = createTestPost("My #FirstPost is a Huge change in my #live","images/miotro.jpg");
        this.post3 = createTestPost("My #Post is the onlyOne i #loved","images/myloved.jpg");
    }

    private Post createTestPost(String content, String imageUrl) {
        Post post = postRepository.save(
                Post.builder()
                        .content(content)
                        .updateDate(LocalDateTime.now())
                        .imageUrl(imageUrl)
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );
        saveHashtagsInPost(post, user1);

        return postRepository.findById(post.getId()).get();
    }
    //endregion
}

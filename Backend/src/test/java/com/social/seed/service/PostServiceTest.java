package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.repository.PostRepository;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import com.social.seed.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link PostService}, focusing on testing individual methods and functionalities
 * for managing { Post }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-13
 */
@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    // Class under test
    @InjectMocks
    private PostService underTest;
    // region dependencies
    @Mock
    private PostRepository postRepository;
    @Mock
    private ResponseService responseService;
    @Mock
    private ValidationService validationService;
    // endregion

    // region Sample Post for testing
    private Post post1;
    private SocialUser socialUser1;
    private HashTag hashTag1;
    // endregion

    @BeforeEach
    void setUp() {
        // Initialize sample data for testing
        // HashTag
        hashTag1 = new HashTag("1", "PrimerHashTag", 0, 0);
        // SocialUser
        socialUser1 = TestUtils.createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES");
        socialUser1.setId("1");
        // Post
        post1 = new Post(
                "1",
                "This is the First Post",
                LocalDateTime.now(),
                "/img/imagen.jpg",
                true,
                10,
                null,
                null
        );
    }

    @Test
    void getPostById_Success() {
        // Mocks
        when(validationService.postExistsById(any())).thenReturn(true);
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        // Act
        ResponseEntity<Object> responseEntity = underTest.getPostById(post1.getId());

        // Assert
        verify(postRepository, times(1)).findById(post1.getId());
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        Post postResponse = (Post)response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify HashTag details
        TestUtils.assertPostEquals(postResponse, post1);
    }

    @Test
    void getPostById_PostNotFound() {
        // Mocks
        when(validationService.postExistsById(any())).thenReturn(false);

        // Mocking the Post not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.getPostById(post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }

    @Test
    void getAllPosts() {
    }

    @Test
    void getPostFeed() {
    }

    // region Create New Post
    @Test
    void createNewPost_Success() {
        // Mocks
        when(validationService.userExistsById(any())).thenReturn(true);

        // Mocking the Response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Mock repository save
        when(postRepository.save(any())).thenReturn(post1);

        //




    }

    @Test
    void createNewPost_NotFound_SocialUser() {
        // Mocks
        when(validationService.userExistsById(any())).thenReturn(false);

        // Mocking the Response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createNewPost(post1, socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }
    // endregion

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void createSocialUserlikedPost() {
    }

    @Test
    void deleteSocialUserlikedPost() {
    }
}
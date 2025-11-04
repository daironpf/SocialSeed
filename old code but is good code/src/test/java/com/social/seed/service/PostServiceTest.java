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
package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.repository.PostRepository;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
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
    private HashTagRepository hashTagRepository;
    @Mock
    private ResponseService responseService;
    // endregion

    // region Sample Post for testing
    private Post post1;
    private final String idUserRequest = "user1";
    // endregion

    @BeforeEach
    void setUp() {
        // Initialize sample data for testing
        post1 = new Post(
                "1",
                "This is the First #Post",
                LocalDateTime.now(),
                "/img/imagen.jpg",
                true,
                10,
                null,
                null
        );
    }

    // region Get Post By: ID
    @Test
    void getPostById_Success() {
        // Mock
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
    // endregion

    // region Create New Post
    @Test
    void createNewPost_Success_WithExistHashTag() {
        // HashTag
        HashTag hashTag2 = new HashTag("2", "Post", 0, 0);

        // Mocks
        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        // Mocking the Response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Mock repository
        when(postRepository.save(any())).thenReturn(post1);
        doNothing().when(postRepository).createPostedRelationship(any(), any(), any());
        doNothing().when(postRepository).createRelationshipTaggedWithHashTag(any(), any());
        when(hashTagRepository.findByName(any())).thenReturn(Optional.of(hashTag2));

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createNewPost(post1,idUserRequest);

        // Verify interactions
        verify(postRepository, times(1)).save(any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        Post postResponse = (Post) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Created Successful");

        // Verify social user details
        TestUtils.assertPostEquals(postResponse, post1);
    }

    @Test
    void createNewPost_Success_WithNoExistHashTag() {
        // HashTag
        HashTag hashTag2 = new HashTag("2", "Post", 0, 0);

        // Mocks
        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        // Mocking the Response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Mock repository
        when(postRepository.save(any())).thenReturn(post1);
        doNothing().when(postRepository).createPostedRelationship(any(), any(), any());
        doNothing().when(postRepository).createRelationshipTaggedWithHashTag(any(), any());

        // HashTagRepository
        when(hashTagRepository.findByName(any())).thenReturn(Optional.empty());
        when(hashTagRepository.save(any())).thenReturn(hashTag2);

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createNewPost(post1,idUserRequest);

        // Verify interactions
        verify(postRepository, times(1)).save(any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        Post postResponse = (Post) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Created Successful");

        // Verify social user details
        TestUtils.assertPostEquals(postResponse, post1);
    }
    // endregion

    // region Update Post
    @Test
    void updatePost_Success() {
        // HashTag
        HashTag hashTag2 = new HashTag("2", "Post", 0, 0);

        // Mock Repository
        doNothing().when(postRepository).update(any(), any(), any(), any());
        doNothing().when(postRepository).deleteAllRelationshipTaggedWithHashTag(anyString());
        doNothing().when(postRepository).createRelationshipTaggedWithHashTag(any(), any());
        when(postRepository.findById(any())).thenReturn(Optional.of(post1));
        when(hashTagRepository.findByName(any())).thenReturn(Optional.of(hashTag2));

        // Mocking the Response
        when(responseService.successResponseWithMessage(any(), any())).thenCallRealMethod();

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.updatePost(idUserRequest, post1);

        // Verify interactions
        verify(postRepository, times(1)).update(any(), any(), any(), any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Updated");
    }
    // endregion

    // Region Delete Post
    @Test
    void deletePost_Success() {
        // Mocking the Response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Mocking the repository
        doNothing().when(postRepository).deleteById(anyString());

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.deletePost(idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
    // endregion

    // region Create SocialUser -Liked-> Post
    @Test
    void createSocialUserLikedPost_Success() {
        // Mocking the Response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Mocking the repository
        doNothing().when(postRepository).createUserByIdLikedPostById(anyString(),anyString(),any());

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createSocialUserlikedPost(idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
    // endregion

    // region Delete SocialUser -Liked-> Post
    @Test
    void deleteSocialUserLikedPost_Success() {
        // Mocking the Response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Mocking the repository
        doNothing().when(postRepository).deleteUserByIdLikedPostById(anyString(),anyString());

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.deleteSocialUserlikedPost(idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
    // endregion
}
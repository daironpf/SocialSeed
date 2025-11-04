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
package com.social.seed.validation;

import com.social.seed.model.Post;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link PostServiceValidator}, focusing on testing individual methods and functionalities
 * for managing { Validations data for Post }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-17
 */
@ExtendWith(MockitoExtension.class)
class PostServiceValidatorTest {
    // Class under test
    @InjectMocks
    private PostServiceValidator underTest;
    // region dependencies
    @Mock
    private ValidationService validationService;
    @Mock
    private ResponseService responseService;
    // endregion

    // region Sample Data for testing
    private final Post post1 = new Post(
            "1",
            "This is the First #Post",
            LocalDateTime.now(),
            "/img/image.jpg",
            true,
            10,
            null,
            null
    );
    private final String idUserRequest = "user1";
    // endregion

    // region aroundGetPostById
    @Test
    void aroundGetPostById_Success() throws Throwable {
        // Mock validationService
        when(validationService.postExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundGetPostById(
                mock(ProceedingJoinPoint.class), post1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).postExistsById(eq(post1.getId()));
        verify(responseService, never()).postNotFoundResponse(anyString());
    }

    @Test
    void aroundGetPostById_PostNotFound() throws Throwable {
        // Mock validationService
        when(validationService.postExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundGetPostById(
                mock(ProceedingJoinPoint.class), post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }
    // endregion

    // region aroundCreateNewPost
    @Test
    void aroundCreateNewPost_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCreateNewPost(
                mock(ProceedingJoinPoint.class), post1, idUserRequest);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(responseService, never()).userNotFoundResponse(anyString());
    }

    @Test
    void aroundCreateNewPost_SocialUserNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateNewPost(
                mock(ProceedingJoinPoint.class), post1, idUserRequest);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }
    // endregion

    // region aroundUpdatePost
    @Test
    void aroundUpdatePost_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userAuthorOfThePostById(anyString(),anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUpdatePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).postExistsById(eq(post1.getId()));
        verify(validationService, times(1)).userAuthorOfThePostById(eq(idUserRequest),eq(post1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).postNotFoundResponse(anyString());
        verify(responseService, never()).isNotPostAuthor();
    }

    @Test
    void aroundUpdatePost_SocialUserNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdatePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundUpdatePost_PostNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdatePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }

    @Test
    void aroundUpdatePost_FORBIDDEN_UserNotAuthor() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userAuthorOfThePostById(anyString(),anyString())).thenReturn(false);


        // Mocking the HashTag not found response
        when(responseService.isNotPostAuthor()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdatePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the request is not the Author of the Post.");
    }
    // endregion

    // region aroundDeletePost
    @Test
    void aroundDeletePost_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userAuthorOfThePostById(anyString(),anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeletePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).postExistsById(eq(post1.getId()));
        verify(validationService, times(1)).userAuthorOfThePostById(eq(idUserRequest),eq(post1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).postNotFoundResponse(anyString());
        verify(responseService, never()).isNotPostAuthor();
    }

    @Test
    void aroundDeletePost_SocialUserNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeletePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundDeletePost_PostNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeletePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }

    @Test
    void aroundDeletePost_FORBIDDEN_UserNotAuthor() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userAuthorOfThePostById(anyString(),anyString())).thenReturn(false);


        // Mocking the HashTag not found response
        when(responseService.isNotPostAuthor()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeletePost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the request is not the Author of the Post.");
    }
    // endregion

    // region aroundCreateSocialUserLikedPost
    @Test
    void aroundCreateSocialUserLikedPost_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userLikedPost(anyString(),anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCreateSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).postExistsById(eq(post1.getId()));
        verify(validationService, times(1)).userLikedPost(eq(idUserRequest),eq(post1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).postNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }


    @Test
    void aroundCreateSocialUserLikedPost_SocialUserNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundCreateSocialUserLikedPost_PostNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }

    @Test
    void aroundCreateSocialUserLikedPost_PostAlreadyLiked() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userLikedPost(anyString(),anyString())).thenReturn(true);


        // Mocking the HashTag not found response
        when(responseService.conflictResponseWithMessage(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Post is already liked by this user");
    }
    // endregion

    // region aroundDeleteSocialUserLikedPost
    @Test
    void aroundDeleteSocialUserLikedPost_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userLikedPost(anyString(),anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeleteSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).postExistsById(eq(post1.getId()));
        verify(validationService, times(1)).userLikedPost(eq(idUserRequest),eq(post1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).postNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }


    @Test
    void aroundDeleteSocialUserLikedPost_SocialUserNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundDeleteSocialUserLikedPost_PostNotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.postNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Post not found with ID: %s", post1.getId()));
    }

    @Test
    void aroundDeleteSocialUserLikedPost_PostDoNotLiked() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.postExistsById(anyString())).thenReturn(true);
        when(validationService.userLikedPost(anyString(),anyString())).thenReturn(false);


        // Mocking the HashTag not found response
        when(responseService.conflictResponseWithMessage(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteSocialUserLikedPost(
                mock(ProceedingJoinPoint.class), idUserRequest, post1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Post is Not liked by this user");
    }
    // endregion
}
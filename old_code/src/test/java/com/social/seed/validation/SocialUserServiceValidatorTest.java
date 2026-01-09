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

import com.social.seed.model.SocialUser;
import com.social.seed.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link SocialUserServiceValidator}, focusing on testing individual methods and functionalities
 * for managing { The Validator data for SocialUser }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-17
 */
@ExtendWith(MockitoExtension.class)
class SocialUserServiceValidatorTest {
    // region dependencies
    @InjectMocks
    private SocialUserServiceValidator underTest;
    @Mock
    private ValidationService validationService;
    @Mock
    private ResponseService responseService;
    // endregion

    // region Sample SocialUser for testing
    private SocialUser socialUser1;
    // endregion

    @BeforeEach
    void setUp() {
        socialUser1 =  TestUtils.createSocialUser(
                "user1",
                "user1@gmail.com",
                "1997-02-27T00:00:00",
                "User FirstName SecondName",
                "EN"
        );
        socialUser1.setId("user-1");
    }

    // region aroundGetSocialUserByUserName
    @Test
    void aroundGetSocialUserByUserName_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundGetSocialUserByUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getUserName());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistByUserName(eq(socialUser1.getUserName()));
        verify(responseService, never()).notFoundWithMessageResponse(anyString());
    }

    @Test
    void aroundGetSocialUserByUserName_NotFound_UserName() throws Throwable {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundGetSocialUserByUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getUserName());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The User with userName: [ %s ] was not found.", socialUser1.getUserName()));
    }
    // endregion

    // region aroundGetSocialUserByEmail
    @Test
    void aroundGetSocialUserByEmail_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistByEmail(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundGetSocialUserByEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getEmail());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistByEmail(eq(socialUser1.getEmail()));
        verify(responseService, never()).notFoundWithMessageResponse(anyString());
    }

    @Test
    void aroundGetSocialUserByEmail_NotFound_Email() throws Throwable {
        // Mock validationService
        when(validationService.userExistByEmail(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundGetSocialUserByEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getEmail());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The User with email: [ %s ] was not found.", socialUser1.getEmail()));
    }
    // endregion

    // region aroundGetSocialUserById
    @Test
    void aroundGetSocialUserById_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundGetSocialUserById(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(responseService, never()).userNotFoundResponse(anyString());
    }

    @Test
    void aroundGetSocialUserById_NotFound_Id() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundGetSocialUserById(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }
    // endregion

    // region aroundCreateNewSocialUser
    @Test
    void aroundCreateNewSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(false);
        when(validationService.userExistByEmail(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCreateNewSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistByUserName(eq(socialUser1.getUserName()));
        verify(validationService, times(1)).userExistByEmail(eq(socialUser1.getEmail()));

        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundCreateNewSocialUser_Exist_UserName() throws Throwable {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(true);

        // Mocking the SocialUser not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateNewSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The userName [ %s ] already exists", socialUser1.getUserName()));
    }

    @Test
    void aroundCreateNewSocialUser_Exist_Email() throws Throwable {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(false);
        when(validationService.userExistByEmail(anyString())).thenReturn(true);

        // Mocking the SocialUser not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateNewSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The Email [ %s ] already exists", socialUser1.getEmail()));
    }
    // endregion

    // region aroundUpdateSocialUser
    @Test
    void aroundUpdateSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUpdateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(responseService, never()).userNotFoundResponse(anyString());
    }

    @Test
    void aroundUpdateSocialUser_NotFound_Id() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundUpdateSocialUser_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUser(
                mock(ProceedingJoinPoint.class), "other-id", socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the update request is not the owner of this.");
    }
    // endregion

    // region aroundDeleteSocialUser
    @Test
    void aroundDeleteSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeleteSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));

        verify(responseService, never()).forbiddenResponseWithMessage(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
    }

    @Test
    void aroundDeleteSocialUser_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteSocialUser(
                mock(ProceedingJoinPoint.class), "other-id", socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the delete request is not the owner of this.");
    }

    @Test
    void aroundDeleteSocialUser_NotFound_Id() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }
    // endregion

    // region aroundUpdateSocialUserName
    @Test
    void aroundUpdateSocialUserName_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByUserName(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUpdateSocialUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "new-UserName");

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).userExistByUserName(eq("new-UserName"));

        verify(responseService, never()).forbiddenResponseWithMessage(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundUpdateSocialUserName_NotFound_Id() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "new-UserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundUpdateSocialUserName_UserName_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByUserName(anyString())).thenReturn(true);

        // Mocking the SocialUser not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "new-UserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The userName [ %s ] already exists", "new-UserName"));
    }

    @Test
    void aroundUpdateSocialUserName_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserName(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "user-id", "new-UserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user who is requesting the userName change is not the owner of this");
    }
    // endregion

    // region aroundUpdateSocialUserEmail
    @Test
    void aroundUpdateSocialUserEmail_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByEmail(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUpdateSocialUserEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "username@gmail.com");

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).userExistByEmail(eq("username@gmail.com"));

        verify(responseService, never()).forbiddenResponseWithMessage(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundUpdateSocialUserEmail_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "user-id", "username@gmail.com");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user who is requesting the Email change is not the owner of this");
    }


    @Test
    void aroundUpdateSocialUserEmail_NotFound_Id() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "username@gmail.com");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundUpdateSocialUserEmail_UserName_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByEmail(anyString())).thenReturn(true);

        // Mocking the SocialUser not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateSocialUserEmail(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId(), "username@gmail.com");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The Email [ %s ] already exists", "username@gmail.com"));
    }
    // endregion

    // region aroundFollowSocialUser
    @Test
    void aroundFollowSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(),anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundFollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isUserBFollowerOfUserA(eq(socialUser1.getId()),eq("id-user-target"));

        verify(responseService, never()).forbiddenResponseWithMessage(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundFollowSocialUser_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundFollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("the user to be followed cannot be the same");
    }

    @Test
    void aroundFollowSocialUser_UserRequest_NotFound_ById() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundFollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundFollowSocialUser_UserToFollow_NotFound_ById() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.userExistsById("id-user-target")).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundFollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", "id-user-target"));
    }

    @Test
    void aroundFollowSocialUser_Already_isUserBFollowerOfUserA() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.userExistsById("id-user-target")).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(),anyString())).thenReturn(true);

        // Mocking the SocialUser not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundFollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("User %s is already being followed.", "id-user-target"));
    }
    // endregion

    // region aroundUnfollowSocialUser
    @Test
    void aroundUnfollowSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(),anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUnfollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isUserBFollowerOfUserA(eq(socialUser1.getId()),eq("id-user-target"));

        verify(responseService, never()).forbiddenResponseWithMessage(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).dontUnFollow(anyString());
    }

    @Test
    void aroundUnfollowSocialUser_FORBIDDEN_DiferentID() throws Throwable {
        // Mocking response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUnfollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("the user to be unfollowed cannot be the same");
    }

    @Test
    void aroundUnfollowSocialUser_UserRequest_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUnfollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundUnfollowSocialUser_UserToUnFollow_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.userExistsById("id-user-target")).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUnfollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", "id-user-target"));
    }

    @Test
    void aroundUnfollowSocialUser_Already_isUserBFollowerOfUserA() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(),anyString())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.dontUnFollow(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUnfollowSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId(), "id-user-target");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("User %s is not being followed.", "id-user-target"));
    }
    // endregion

    // region aroundActivateVacationMode
    @Test
    void aroundActivateVacationMode_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isVacationModeActivated(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundActivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isVacationModeActivated(eq(socialUser1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundActivateVacationMode_UserRequest_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundActivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundActivateVacationMode_Already_Active() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.isVacationModeActivated(socialUser1.getId())).thenReturn(true);

        // Mocking the response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundActivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Vacation Mode is already Active");
    }
    // endregion

    // region aroundDeactivateVacationMode
    @Test
    void aroundDeactivateVacationMode_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isVacationModeActivated(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeactivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isVacationModeActivated(eq(socialUser1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundDeactivateVacationMode_UserRequest_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeactivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundDeactivateVacationMode_Already_Active() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.isVacationModeActivated(socialUser1.getId())).thenReturn(false);

        // Mocking the response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeactivateVacationMode(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Vacation Mode is already Deactivated");
    }
    // endregion

    // region aroundActivateSocialUser
    @Test
    void aroundActivateSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isSocialUserActivated(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundActivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isSocialUserActivated(eq(socialUser1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundActivateSocialUser_UserRequest_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundActivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundActivateSocialUser_Already_Active() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.isSocialUserActivated(socialUser1.getId())).thenReturn(true);

        // Mocking the response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundActivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Social User is already Active");
    }
    // endregion

    // region aroundDeactivateSocialUser
    @Test
    void aroundDeactivateSocialUser_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isSocialUserActivated(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeactivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).userExistsById(eq(socialUser1.getId()));
        verify(validationService, times(1)).isSocialUserActivated(eq(socialUser1.getId()));

        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundDeactivateSocialUser_UserRequest_NotFound() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the SocialUser not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeactivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void aroundDeactivateSocialUser_Already_Deactivated() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(validationService.isSocialUserActivated(socialUser1.getId())).thenReturn(false);

        // Mocking the response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeactivateSocialUser(
                mock(ProceedingJoinPoint.class), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Social User is already Deactivated");
    }
    // endregion
}
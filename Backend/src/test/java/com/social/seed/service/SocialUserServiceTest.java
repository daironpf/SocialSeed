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

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.utils.TestUtils;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link SocialUserService}, focusing on testing individual methods and functionalities
 * for managing { SocialUsers }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-08
 */
@ExtendWith(MockitoExtension.class)
class SocialUserServiceTest {
    // Class under test
    @InjectMocks
    private SocialUserService underTest;
    // region dependencies
    @Mock
    private SocialUserRepository socialUserRepository;
    @Mock
    private ResponseService responseService;
    @Mock
    private ValidationService validationService;
    // endregion

    // region Sample social user for testing
    private SocialUser socialUser1;
    // endregion

    @BeforeEach
    void setUp() {
        // Initialize sample data for testing
        socialUser1 = TestUtils.createSocialUser("maria1", "maria1@gmail.com", "1992-01-04T00:00:00", "Maria del Laurel Perez", "ES");
        socialUser1.setId("1");
    }

    // region Find by
    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserByUserName(String)} method
     * when retrieving social user details for a valid userName.
     */
    @Test
    void shouldReturnSuccessResponseWithSocialUserDetailsForValidUserName() {
        // Arrange
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(socialUserRepository.findByUserName(socialUser1.getUserName())).thenReturn(Optional.of(socialUser1));
        when(validationService.userExistByUserName(socialUser1.getUserName())).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByUserName(socialUser1.getUserName());

        // Assert
        verify(socialUserRepository, times(1)).findByUserName(socialUser1.getUserName());

        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserByUserName(String)} method
     * when retrieving social user details for invalid UserName.
     */
    @Test
    void shouldReturnNotFoundResponseForNonExistUserName() {
        // Arrange
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();
        when(validationService.userExistByUserName("fakeUser")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByUserName("fakeUser");

        // Assert
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The User with userName: [ %s ] was not found.", "fakeUser"));
    }

    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserByEmail(String)} method
     * when retrieving social user details for a valid Email.
     */
    @Test
    void shouldReturnSuccessResponseWithSocialUserDetailsForValidEmail() {
        // Arrange
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(socialUserRepository.findByEmail(socialUser1.getEmail())).thenReturn(Optional.of(socialUser1));
        when(validationService.userExistByEmail(socialUser1.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByEmail(socialUser1.getEmail());

        // Assert
        verify(socialUserRepository, times(1)).findByEmail(socialUser1.getEmail());

        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserByEmail(String)} method
     * when retrieving social user details for invalid Email.
     */
    @Test
    void shouldReturnNotFoundResponseForNonExistEmail() {
        // Arrange
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();
        when(validationService.userExistByEmail("fake@email.test")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByEmail("fake@email.test");

        // Assert
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The User with email: [ %s ] was not found.", "fake@email.test"));
    }
    // endregion

    // region CRUD
    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserById(String)} method
     * when retrieving Social User details for a valid ID.
     */
    @Test
    void shouldReturnSuccessResponseWithSocialUserDetailsForValidId() {
        // Arrange
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(socialUserRepository.findById(socialUser1.getId())).thenReturn(Optional.of(socialUser1));
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserById(socialUser1.getId());

        // Assert
        verify(socialUserRepository, times(1)).findById(socialUser1.getId());
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserById(String)} method
     * when retrieving social user details for invalid ID.
     */
    @Test
    void shouldReturnNotFoundResponseForNonExistId() {
        // Arrange
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();
        when(validationService.userExistsById("fakeId")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserById("fakeId");

        // Assert
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", "fakeId"));
    }


    @Test
    void testCreateNewSocialUser() {
        // Mock validationService
        when(validationService.userExistByUserName(anyString())).thenReturn(false);
        when(validationService.userExistByEmail(anyString())).thenReturn(false);

        // Mock repository save
        when(socialUserRepository.save(any())).thenReturn(socialUser1);

        // Mock success response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createNewSocialUser(socialUser1);

        // Verify interactions
        verify(validationService, times(1)).userExistByUserName(anyString());
        verify(validationService, times(1)).userExistByEmail(anyString());
        verify(socialUserRepository, times(1)).save(any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = (SocialUser) response.response();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Created Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    @Test
    void createNewSocialUser_Conflict_UserName() {
        // Mocking the validation service
        when(validationService.userExistByUserName(anyString())).thenReturn(true);

        // Mocking the conflict response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createNewSocialUser(socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The userName [ %s ] already exists", socialUser1.getUserName()));
    }

    @Test
    void createNewSocialUser_Conflict_Email() {
        // Mocking the validation service
        when(validationService.userExistByUserName(anyString())).thenReturn(false);
        when(validationService.userExistByEmail(anyString())).thenReturn(true);

        // Mocking the conflict response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createNewSocialUser(socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The Email [ %s ] already exists", socialUser1.getEmail()));
    }

    // region Update Social User
    @Test
    void updateSocialUser_Success() {
        // Mocking the validation service
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // Mocking the repository update and findById methods
        when(socialUserRepository.findById(anyString())).thenReturn(Optional.of(socialUser1));
        doNothing().when(socialUserRepository).update(anyString(), any(), any(), any());

        // Mocking the success response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUser(socialUser1.getId(), socialUser1);

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = (SocialUser) response.response();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    @Test
    void updateSocialUser_Forbidden() {
        // Mocking the conflict response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method with a different userId
        ResponseEntity<Object> responseEntity = underTest.updateSocialUser("differentUserId", socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the update request is not the owner of this.");
    }

    @Test
    void updateSocialUser_UserNotFound() {
        // Mocking the validation service for user not found
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the user not found response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUser(socialUser1.getId(), socialUser1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }
    // endregion

    // region Delete Social User
    @Test
    void deleteSocialUser_Success() {
        // Mocking the validation service
        when(validationService.userExistsById(anyString())).thenReturn(true);

        // Mocking the repository deleteById method
        doNothing().when(socialUserRepository).deleteById(anyString());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteSocialUser("userIdToDelete", "userIdToDelete");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void deleteSocialUser_Forbidden() {
        // Mocking the success response
        when(responseService.forbiddenResponseWithMessage(anyString())).thenCallRealMethod();

        // Calling the actual service method with a different userId
        ResponseEntity<Object> responseEntity = underTest.deleteSocialUser("differentUserId", "userIdToDelete");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user making the delete request is not the owner of this.");
    }

    @Test
    void deleteSocialUser_UserNotFound() {
        // Mocking the validation service for user not found
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the user not found response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteSocialUser(socialUser1.getId(),socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }
    // endregion

    // region Update SocialUser Name
    @Test
    void updateSocialUserName_Success() {
        // Mocking the validation service
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByUserName(anyString())).thenReturn(false);

        // Mocking the repository updateSocialUserName and findById methods
        doNothing().when(socialUserRepository).updateSocialUserName(anyString(), anyString());
        when(socialUserRepository.findById(anyString())).thenReturn(Optional.of(socialUser1));

        // Mocking the success response
        when(responseService.successResponseWithMessage(any(), anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserName("userId", "userId", "newUserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("The username was updated successfully.");
    }

    @Test
    void updateSocialUserName_Forbidden() {
        // Mocking the validation service
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method with a different userId
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserName("differentUserId", "userIdToUpdate", "newUserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user who is requesting the userName change is not the owner of this");
    }

    @Test
    void updateSocialUserName_UserNotFound() {
        // Mocking the validation service for user not found
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the user not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserName(socialUser1.getId(),socialUser1.getId(), "newUserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.",socialUser1.getId()));
    }

    @Test
    void updateSocialUserName_Conflict() {
        // Mocking the validation service for existing userName
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByUserName(anyString())).thenReturn(true);

        // Mocking the conflict response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserName(socialUser1.getId(),socialUser1.getId(), "existingUserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The userName [ %s ] already exists", "existingUserName"));
    }
    // endregion

    // region Update Email
    @Test
    void updateSocialUserEmail_Success() {
        // Mocking the validation service
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByEmail(anyString())).thenReturn(false);

        // Mocking the repository updateSocialUserEmail and findById methods
        doNothing().when(socialUserRepository).updateSocialUserEmail(anyString(), anyString());
        when(socialUserRepository.findById(anyString())).thenReturn(Optional.of(socialUser1));

        // Mocking the success response
        when(responseService.successResponseWithMessage(any(), anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserEmail(socialUser1.getId(),socialUser1.getId(), "newEmail");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("The email was updated successfully.");
    }

    @Test
    void updateSocialUserEmail_Forbidden() {
        // Mocking the response
        when(responseService.forbiddenResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method with a different userId
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserEmail("differentUserId", socialUser1.getId(), "newEmail");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user who is requesting the Email change is not the owner of this");
    }

    @Test
    void updateSocialUserEmail_UserNotFound() {
        // Mocking the validation service for user not found
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the user not found response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserEmail(socialUser1.getId(),socialUser1.getId(), "newEmail");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.",socialUser1.getId()));
    }

    @Test
    void updateSocialUserEmail_Conflict() {
        // Mocking the validation service for existing email
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistByEmail(anyString())).thenReturn(true);

        // Mocking the conflict response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserEmail(socialUser1.getId(), socialUser1.getId(), socialUser1.getEmail());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The Email [ %s ] already exists",socialUser1.getEmail()));
    }
    // endregion

    // region Follow
    @Test
    void followSocialUser_Success() {
        // Mocking the validation service
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(), anyString())).thenReturn(false);

        // Mocking the repository createUserBFollowUserA method
        doNothing().when(socialUserRepository).createUserBFollowUserA(anyString(), anyString(), any());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.followSocialUser(socialUser1.getId(), "userIdToFollow");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void followSocialUser_Forbidden_SameUser() {
        // Mocking the success response
        when(responseService.forbiddenResponseWithMessage(anyString())).thenCallRealMethod();

        // Calling the actual service method with the same userIds
        ResponseEntity<Object> responseEntity = underTest.followSocialUser(socialUser1.getId(),socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("the user to be followed cannot be the same");
    }

    @Test
    void followSocialUser_UserNotFound() {
        // Mocking the validation service for user not found
        when(validationService.userExistsById(anyString())).thenReturn(false);

        // Mocking the user not found response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.followSocialUser(socialUser1.getId(), "userIdToFollow");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.",socialUser1.getId()));
    }

    @Test
    void followSocialUser_UserToFollowNotFound() {
        // Mocking the validation service for user to follow not found
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.userExistsById("userIdToFollow")).thenReturn(false);

        // Mocking the user not found response
        when(responseService.notFoundWithMessageResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.followSocialUser(socialUser1.getId(),"userIdToFollow");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo("The User to be followed has not been found");
    }

    @Test
    void followSocialUser_AlreadyFollowed() {
        // Mocking the validation service for already followed
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.isUserBFollowerOfUserA(anyString(), anyString())).thenReturn(true);

        // Mocking the already followed response
        when(responseService.alreadyFollow(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.followSocialUser(socialUser1.getId(),"userIdToFollow");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The User to be followed has not been found");
    }
    // endregion

//    // region unfollow
//    @Test
//    void unfollowSocialUser_Success() {
//        // Mocking the validation service
//        when(validationService.userExistsById(anyString())).thenReturn(true);
//        when(validationService.isUserBFollowerOfUserA(anyString(), anyString())).thenReturn(true);
//
//        // Mocking the repository unFollowTheUserA method
//        doNothing().when(socialUserRepository).unFollowTheUserA(anyString(), anyString());
//
//        // Mocking the success response
//        when(responseService.successResponse(anyString())).thenReturn(ResponseEntity.ok().build());
//
//        // Calling the actual service method
//        ResponseEntity<Object> responseEntity = socialUserService.unfollowSocialUser("userId", "userIdToUnfollow");
//
//        // Assertions
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//        // Add more assertions based on your specific requirements
//    }
//
//    @Test
//    void unfollowSocialUser_Forbidden_SameUser() {
//        // Calling the actual service method with the same userIds
//        ResponseEntity<Object> responseEntity = socialUserService.unfollowSocialUser("userId", "userId");
//
//        // Assertions
//        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
//        // Add more assertions based on your specific requirements
//    }
//
//    @Test
//    void unfollowSocialUser_UserNotFound() {
//        // Mocking the validation service for user not found
//        when(validationService.userExistsById(anyString())).thenReturn(false);
//
//        // Mocking the user not found response
//        when(responseService.userNotFoundResponse(anyString())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//
//        // Calling the actual service method
//        ResponseEntity<Object> responseEntity = socialUserService.unfollowSocialUser("nonexistentUserId", "userIdToUnfollow");
//
//        // Assertions
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        // Add more assertions based on your specific requirements
//    }
//
//    @Test
//    void unfollowSocialUser_UserToUnfollowNotFound() {
//        // Mocking the validation service for user to unfollow not found
//        when(validationService.userExistsById(anyString())).thenReturn(true);
//        when(validationService.userExistsById("userIdToUnfollow")).thenReturn(false);
//
//        // Mocking the user not found response
//        when(responseService.notFoundWithMessageResponse(anyString())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//
//        // Calling the actual service method
//        ResponseEntity<Object> responseEntity = socialUserService.unfollowSocialUser("userId", "userIdToUnfollow");
//
//        // Assertions
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        // Add more assertions based on your specific requirements
//    }
//
//    @Test
//    void unfollowSocialUser_DontUnfollow() {
//        // Mocking the validation service for not following
//        when(validationService.userExistsById(anyString())).thenReturn(true);
//        when(validationService.isUserBFollowerOfUserA(anyString(), anyString())).thenReturn(false);
//
//        // Mocking the don't unfollow response
//        when(responseService.dontUnFollow(anyString())).thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).build());
//
//        // Calling the actual service method
//        ResponseEntity<Object> responseEntity = socialUserService.unfollowSocialUser("userId", "userIdToUnfollow");
//
//        // Assertions
//        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//        // Add more assertions based on your specific requirements
//    }
    // endregion
}
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
import com.social.seed.repository.FriendsRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link FriendsService}, focusing on testing individual methods and functionalities
 * for managing { The Friends to SocialUsers }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-12
 */
@ExtendWith(MockitoExtension.class)
class FriendsServiceTest {
    // Class under test
    @InjectMocks
    private FriendsService underTest;
    // region dependencies
    @Mock
    private FriendsRepository friendsRepository;
    @Mock
    private ResponseService responseService;
    @Mock
    private ValidationService validationService;
    // endregion

    // region Sample social user for testing
    private SocialUser socialUser1;
    private SocialUser socialUser2;
    // endregion

    @BeforeEach
    void setUp() {
        // Initialize sample data for testing
        socialUser1 = TestUtils.createSocialUser("user1", "user1@gmail.com", "1992-01-04T00:00:00", "User Number One", "ES");
        socialUser1.setId("1");
        socialUser2 = TestUtils.createSocialUser("user2", "user2@gmail.com", "1987-03-12T00:00:00", "User Number Two", "EN");
        socialUser2.setId("2");
    }

    // region Create RequestFriendship
    @Test
    void createRequestFriendship_Success() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(),anyString())).thenReturn(false);
        when(validationService.existsFriendship(anyString(),anyString())).thenReturn(false);

        // Mock repository create
        doNothing().when(friendsRepository).createFriendRequest(anyString(),anyString(),any());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void createRequestFriendship_UserRequestNotFound() {
        // Mock validationService
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void createRequestFriendship_UserToBeFriendNotFound() {
        // Mock validationService
        when(validationService.userExistsById(socialUser2.getId())).thenReturn(false);
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser2.getId()));
    }

    @Test
    void createRequestFriendship_Forbidden_SameUser() {
        // Mocking the success response
        when(responseService.forbiddenDuplicateSocialUser()).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user cannot be the same.");
    }

    @Test
    void createRequestFriendship_Conflict_FriendRequest_AlreadyExists() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friend Request already exists");
    }

    @Test
    void createRequestFriendship_Conflict_Friendship_AlreadyExists() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(false);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.createRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friendship already exists");
    }
    // endregion

    @Test
    void cancelRequestFriendship() {
    }

    @Test
    void acceptedRequestFriendship() {
    }

    @Test
    void deleteFriendship() {
    }
}
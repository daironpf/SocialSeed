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
import com.social.seed.repository.FriendsRelationshipRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link FriendsRelationshipService}, focusing on testing individual methods and functionalities
 * for managing { The Friends to SocialUsers }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-12
 */
@ExtendWith(MockitoExtension.class)
class FriendsRelationshipServiceTest {
    // Class under test
    @InjectMocks
    private FriendsRelationshipService underTest;
    // region dependencies
    @Mock
    private FriendsRelationshipRepository friendsRelationshipRepository;
    @Mock
    private ResponseService responseService;
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

    @Test
    void createRequestFriendship_Success() {
        // Mock repository
        doNothing().when(friendsRelationshipRepository).createFriendRequest(anyString(),anyString(),any());

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
    void cancelRequestFriendship_Success() {
        // Mock repository
        doNothing().when(friendsRelationshipRepository).cancelRequestFriendship(anyString(),anyString());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.cancelRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void acceptedRequestFriendship_Success() {
        // Mock repository
        doNothing().when(friendsRelationshipRepository).acceptedRequestFriendship(anyString(), anyString(), any());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.acceptedRequestFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void deleteFriendship_Success() {
        // Mock repository
        doNothing().when(friendsRelationshipRepository).deleteFriendship(anyString(), anyString());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteFriendship(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void cancelReceivedRequest_Success() {
        // Mock repository
        doNothing().when(friendsRelationshipRepository).cancelReceivedRequestFriendship(anyString(),anyString());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.cancelReceivedRequest(socialUser1.getId(), socialUser2.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
}
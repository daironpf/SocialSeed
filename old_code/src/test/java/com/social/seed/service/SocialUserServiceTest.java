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
    void getSocialUserByUserName_Success() {
        // Mock the response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Mock repository
        when(socialUserRepository.findByUserName(socialUser1.getUserName())).thenReturn(Optional.of(socialUser1));

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByUserName(socialUser1.getUserName());

        // Assert
        verify(socialUserRepository, times(1)).findByUserName(socialUser1.getUserName());

        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }



    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserByEmail(String)} method
     * when retrieving social user details for a valid Email.
     */
    @Test
    void getSocialUserByEmail_Success() {
        // Mock the response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Mock repository
        when(socialUserRepository.findByEmail(socialUser1.getEmail())).thenReturn(Optional.of(socialUser1));

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByEmail(socialUser1.getEmail());

        // Assert
        verify(socialUserRepository, times(1)).findByEmail(socialUser1.getEmail());

        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }
    // endregion

    // region CRUD
    /**
     * Tests the behavior of the {@link SocialUserService#getSocialUserById(String)} method
     * when retrieving Social User details for a valid ID.
     */
    @Test
    void getSocialUserById_Success() {
        // Mock repository
        when(socialUserRepository.findById(socialUser1.getId())).thenReturn(Optional.of(socialUser1));

        // Mock the response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.getSocialUserById(socialUser1.getId());

        // Assert
        verify(socialUserRepository, times(1)).findById(socialUser1.getId());
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    @Test
    void createSocialUser_Success() {
        // Mock repository
        when(socialUserRepository.save(any())).thenReturn(socialUser1);

        // Mock the response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createNewSocialUser(socialUser1);

        // Verify interactions
        verify(socialUserRepository, times(1)).save(any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        SocialUser socialUserResponse = (SocialUser) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Created Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    @Test
    void updateSocialUser_Success() {
        // Mocking the repository
        when(socialUserRepository.findById(anyString())).thenReturn(Optional.of(socialUser1));
        doNothing().when(socialUserRepository).update(anyString(), any(), any(), any());

        // Mocking the response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUser(socialUser1.getId(), socialUser1);

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        SocialUser socialUserResponse = (SocialUser) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }

    @Test
    void deleteSocialUser_Success() {
        // Mocking the repository
        doNothing().when(socialUserRepository).deleteById(anyString());

        // Mocking the response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteSocialUser("userIdToDelete", "userIdToDelete");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    // endregion

    // region Update Uniques Properties
    @Test
    void updateSocialUserName_Success() {
        // Mocking the repository
        doNothing().when(socialUserRepository).updateSocialUserName(anyString(), anyString());
        when(socialUserRepository.findById(anyString())).thenReturn(Optional.of(socialUser1));

        // Mocking the response
        when(responseService.successResponseWithMessage(any(), anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateSocialUserName("userId", "userId", "newUserName");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("The username was updated successfully.");
    }

    @Test
    void updateSocialUserEmail_Success() {
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
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("The email was updated successfully.");
    }
    // endregion

    // region Vacation Module
    @Test
    void activateVacationMode_Success() {
        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Mocking the repository unFollowTheUserA method
        doNothing().when(socialUserRepository).activateVacationMode(anyString());

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.activateVacationMode(socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void deActivateVacationMode_Success() {
        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Mocking the repository unFollowTheUserA method
        doNothing().when(socialUserRepository).deactivateVacationMode(anyString());

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deactivateVacationMode(socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
    // endregion

    // region Activate Module
    @Test
    void activateSocialUser_Success() {
        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Mocking the repository unFollowTheUserA method
        doNothing().when(socialUserRepository).activateSocialUser(anyString());

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.activateSocialUser(socialUser1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void DeActivateSocialUser_Success() {
        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Mocking the repository unFollowTheUserA method
        doNothing().when(socialUserRepository).deactivateSocialUser(anyString());

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deactivateSocialUser(socialUser1.getId());

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
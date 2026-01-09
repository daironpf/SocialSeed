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
import com.social.seed.repository.HashTagRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link HashTagService}, focusing on testing individual methods and functionalities
 * for managing { HashTag }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-10
 */
@ExtendWith(MockitoExtension.class)
class HashTagServiceTest {
    // Class under test
    @InjectMocks
    private HashTagService underTest;
    // region dependencies
    @Mock
    private HashTagRepository hashTagRepository;
    @Mock
    private ResponseService responseService;
    // endregion

    // region Sample HashTag for testing
    private HashTag hashTag1;
    // endregion

    @BeforeEach
    void setUp() {
        hashTag1 = new HashTag("1", "PrimerHashTag", 0, 0);
    }

    @Test
    void getHashTagById_Success() {
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(hashTagRepository.findById(hashTag1.getId())).thenReturn(Optional.of(hashTag1));

        // Act
        ResponseEntity<Object> responseEntity = underTest.getHashTagById(hashTag1.getId());

        // Assert
        verify(hashTagRepository, times(1)).findById(hashTag1.getId());
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        HashTag hashTagResponse = ((Optional<HashTag>) response.response()).orElse(null);

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify HashTag details
        TestUtils.assertHashTagEquals(hashTagResponse, hashTag1);
    }

    @Test
    void createHashTag_Success() {
        // Mock repository save
        when(hashTagRepository.save(any())).thenReturn(hashTag1);

        // Mock success response
        when(responseService.successCreatedResponse(any())).thenCallRealMethod();

        // Call the method
        ResponseEntity<Object> responseEntity = underTest.createNewHashTag(hashTag1);

        // Verify interactions
        verify(hashTagRepository, times(1)).save(any());

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        HashTag hashTagResponse = (HashTag) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Created Successful");

        // Verify social user details
        TestUtils.assertHashTagEquals(hashTagResponse, hashTag1);
    }

    @Test
    void updateHashTag_Success() {
        // Mocking the repository update and findById methods
        when(hashTagRepository.findById(anyString())).thenReturn(Optional.of(hashTag1));
        doNothing().when(hashTagRepository).update(anyString(), anyString());

        // Mocking the success response
        when(responseService.successResponseWithMessage(any(), any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.updateHashTag(hashTag1);

        // Assert
        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        assert response != null;
        HashTag hashTagResponse = (HashTag) response.response();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Updated");

        // Verify social user details
        TestUtils.assertHashTagEquals(hashTagResponse, hashTag1);
    }

    @Test
    void deleteHashTag_Success() {
        // Mocking the repository deleteById method
        doNothing().when(hashTagRepository).deleteById(anyString());

        // Mocking the success response
        when(responseService.successResponseWithMessage(any(),any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteHashTag(hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
}
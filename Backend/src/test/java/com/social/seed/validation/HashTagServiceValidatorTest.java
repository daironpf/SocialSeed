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

import com.social.seed.model.HashTag;
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
 * Test class for the {@link HashTagServiceValidator}, focusing on testing individual methods and functionalities
 * for managing { The Validator data for HashTag }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-17
 */
@ExtendWith(MockitoExtension.class)
class HashTagServiceValidatorTest {
    // region dependencies
    @InjectMocks
    private HashTagServiceValidator underTest;
    @Mock
    private ValidationService validationService;
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

    // region aroundGetHashTagById
    @Test
    void aroundGetHashTagById_Success() throws Throwable {
        // Mock validationService
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundGetHashTagById(
                mock(ProceedingJoinPoint.class), hashTag1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsById(eq(hashTag1.getId()));
        verify(responseService, never()).hashTagNotFoundResponse(anyString());
    }


    @Test
    void aroundGetHashTagById_HashTagNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.hashTagExistsById(hashTag1.getId())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.hashTagNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundGetHashTagById(
                mock(ProceedingJoinPoint.class), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", hashTag1.getId()));
    }
    // endregion

    // region aroundCreateNewHashTag
    @Test
    void aroundCreateNewHashTag_Success() throws Throwable {
        // Mock validationService
        when(validationService.hashTagExistsByName(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCreateNewHashTag(
                mock(ProceedingJoinPoint.class), hashTag1);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsByName(eq(hashTag1.getName()));
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundCreateNewHashTag_Conflict_Name() throws Throwable {
        // Mocking the validation service
        when(validationService.hashTagExistsByName(anyString())).thenReturn(true);

        // Mocking the conflict response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateNewHashTag(
                mock(ProceedingJoinPoint.class), hashTag1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(String.format("The HashTag with the name [ %s ] already exists", hashTag1.getName()));
    }
    // endregion

    // region aroundUpdateHashTag
    @Test
    void aroundUpdateHashTag_Success() throws Throwable {
        // Mock validationService
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);
        when(validationService.hashTagExistsByName(anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundUpdateHashTag(
                mock(ProceedingJoinPoint.class), hashTag1);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsByName(eq(hashTag1.getName()));
        verify(validationService, times(1)).hashTagExistsById(eq(hashTag1.getId()));
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundUpdateHashTag_HashTagNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.hashTagExistsById(anyString())).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.hashTagNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateHashTag(
                mock(ProceedingJoinPoint.class), hashTag1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", hashTag1.getId()));
    }

    @Test
    void aroundUpdateHashTag_NameAlreadyExists() throws Throwable {
        // Mocking the validation service
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);
        when(validationService.hashTagExistsByName(anyString())).thenReturn(true);

        // Mocking the user not found response
        when(responseService.conflictResponseWithMessage(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundUpdateHashTag(
                mock(ProceedingJoinPoint.class), hashTag1);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo(
                String.format("The HashTag with the name [ %s ] already exists", hashTag1.getName()));
    }
    // endregion

    // region aroundDeleteHashTag
    @Test
    void aroundDeleteHashTag_Success() throws Throwable {
        // Mock validationService
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeleteHashTag(
                mock(ProceedingJoinPoint.class), hashTag1.getId());

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsById(eq(hashTag1.getId()));
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundDeleteHashTag_HashTagNotFound() throws Throwable {
        // Mocking the validation service
        when(validationService.hashTagExistsById(anyString())).thenReturn(false);

        // Mocking the success response
        when(responseService.hashTagNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteHashTag(
                mock(ProceedingJoinPoint.class), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(
                String.format("Hashtag not found with ID: %s", hashTag1.getId())
        );
    }
    // endregion
}
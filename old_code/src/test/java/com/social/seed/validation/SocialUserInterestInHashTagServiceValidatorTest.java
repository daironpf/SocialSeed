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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

/**
 * Test class for the {@link SocialUserInterestInHashTagServiceValidator}, focusing on testing individual methods and functionalities
 * for managing { Validations data for SocialUser interest in HashTag }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-17
 */
@ExtendWith(MockitoExtension.class)
class SocialUserInterestInHashTagServiceValidatorTest {
    // Class under test
    @InjectMocks
    private SocialUserInterestInHashTagServiceValidator underTest;
    // region dependencies
    @Mock
    private ValidationService validationService;
    @Mock
    private ResponseService responseService;
    // endregion

    private final String idUserRequest = "1-socialuser";
    private final String idHashTag = "1-hashtag";

    // region aroundAddInterest
    @Test
    void aroundAddInterest_Success() throws Throwable {
        // Mock validationService
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundAddInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsById(eq(idHashTag));
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).existsInterest(eq(idUserRequest),eq(idHashTag));

        verify(responseService, never()).hashTagNotFoundResponse(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundAddInterest_UserNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAddInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundAddInterest_HashTagNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.hashTagExistsById(idHashTag)).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.hashTagNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAddInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", idHashTag));
    }

    @Test
    void aroundAddInterest_InterestAlreadyExists() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.hashTagExistsById(idHashTag)).thenReturn(true);
        when(validationService.existsInterest(idUserRequest,idHashTag)).thenReturn(true);

        // Mocking the HashTag not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAddInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Interest already exists");
    }
    // endregion

    // region aroundDeleteInterest
    @Test
    void aroundDeleteInterest_Success() throws Throwable {

        // Mock validationService
        when(validationService.hashTagExistsById(anyString())).thenReturn(true);
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeleteInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // The return is Null because this guarantees that the observed method will be executed
        assertThat(result).isNull();

        // Verification of interactions with simulated services (Mock)
        verify(validationService, times(1)).hashTagExistsById(eq(idHashTag));
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).existsInterest(eq(idUserRequest),eq(idHashTag));

        verify(responseService, never()).hashTagNotFoundResponse(anyString());
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundDeleteInterest_UserNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.userNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundDeleteInterest_HashTagNotFound() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.hashTagExistsById(idHashTag)).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.hashTagNotFoundResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", idHashTag));
    }

    @Test
    void aroundDeleteInterest_InterestDoesNotExists() throws Throwable {
        // Mocking the validation service for HashTag not found
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.hashTagExistsById(idHashTag)).thenReturn(true);
        when(validationService.existsInterest(idUserRequest,idHashTag)).thenReturn(false);

        // Mocking the HashTag not found response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteInterest(
                mock(ProceedingJoinPoint.class), idUserRequest, idHashTag);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Interest does not exist");
    }
    // endregion
}
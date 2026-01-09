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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.aspectj.lang.ProceedingJoinPoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/**
 * Test class for the {@link FriendsRelationshipServiceValidator}, focusing on testing individual methods and functionalities
 * for managing { The Validator data for Friends }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-17
 */
@ExtendWith(MockitoExtension.class)
class FriendsRelationshipServiceValidatorTest {
    @InjectMocks
    private FriendsRelationshipServiceValidator underTest;
    @Mock
    private ValidationService validationService;
    @Mock
    private ResponseService responseService;

    // variables
    String idUserRequest = "user1";
    String idUserTarget = "user2";
    // endregion

    // region aroundCreateFriendshipRequest
    @Test
    void aroundCreateFriendshipRequest_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(false);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // El retorno es Null porque esto garantiza que se ejecutará el metodo observado
        assertThat(result).isNull();

        // Verificación de interacciones con los servicios simulados (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).userExistsById(eq(idUserTarget));
        verify(validationService, times(1)).existsFriendRequest(eq(idUserRequest), eq(idUserTarget));
        verify(validationService, times(1)).existsFriendship(eq(idUserRequest), eq(idUserTarget));
        verify(responseService, never()).forbiddenDuplicateSocialUser();
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundCreateFriendshipRequest_NotFound_UserRequest() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void aroundCreateFriendshipRequest_NotFound_UserToBeFriend() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.userExistsById(idUserTarget)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserTarget));
    }

    @Test
    void aroundCreateFriendshipRequest_Forbidden_SameUser() throws Throwable {
        // Mocking the success response
        when(responseService.forbiddenDuplicateSocialUser()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserRequest);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user cannot be the same.");
    }

    @Test
    void aroundCreateFriendshipRequest_Conflict_FriendRequest_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friend Request already exists");
    }

    @Test
    void aroundCreateFriendshipRequest_Conflict_Friendship_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(false);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCreateFriendshipRequest(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friendship already exists");
    }
    // endregion

    // region aroundCancelRequestFriendship
    @Test
    void aroundCancelRequestFriendship_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // El retorno es Null porque esto garantiza que se ejecutará el metodo observado
        assertThat(result).isNull();

        // Verificación de interacciones con los servicios simulados (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).userExistsById(eq(idUserTarget));
        verify(validationService, times(1)).existsFriendRequest(eq(idUserRequest), eq(idUserTarget));
        verify(validationService, times(1)).existsFriendship(eq(idUserRequest), eq(idUserTarget));
        verify(responseService, never()).forbiddenDuplicateSocialUser();
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).notFoundWithMessageResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void aroundCancelRequestFriendship_Forbidden_SameUser() throws Throwable {
        // Mocking the success response
        when(responseService.forbiddenDuplicateSocialUser()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserRequest);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user cannot be the same.");
    }

    @Test
    void aroundCancelRequestFriendship_NotFound_UserRequest() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void cancelRequestFriendship_NotFound_UserToBeFriend() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.userExistsById(idUserTarget)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserTarget));
    }

    @Test
    void cancelRequestFriendship_Conflict_FriendRequest_DoesNotExist() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(false);

        // Mocking the success response
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo("The Friend Request does not exist");
    }

    @Test
    void cancelRequestFriendship_Conflict_Friendship_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequest(anyString(), anyString())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundCancelRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friendship already exists");
    }
    // endregion

    // region aroundAcceptedRequestFriendship
    @Test
    void aroundAcceptedRequestFriendship_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsFriendRequestByUserToAccept(anyString(), anyString())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(false);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // El retorno es Null porque esto garantiza que se ejecutará el metodo observado
        assertThat(result).isNull();

        // Verificación de interacciones con los servicios simulados (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).userExistsById(eq(idUserTarget));
        verify(validationService, times(1)).existsFriendRequestByUserToAccept(eq(idUserRequest), eq(idUserTarget));
        verify(validationService, times(1)).existsFriendship(eq(idUserRequest), eq(idUserTarget));
        verify(responseService, never()).forbiddenDuplicateSocialUser();
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).notFoundWithMessageResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }


    @Test
    void acceptedRequestFriendship_Forbidden_SameUser() throws Throwable {
        // Mocking the success response
        when(responseService.forbiddenDuplicateSocialUser()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserRequest);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user cannot be the same.");
    }

    @Test
    void acceptedRequestFriendship_NotFound_UserRequest() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void acceptedRequestFriendship_NotFound_UserToBeFriend() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.userExistsById(idUserTarget)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserTarget));
    }

    @Test
    void acceptedRequestFriendship_Conflict_FriendshipRequest_DoesNotExist() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequestByUserToAccept(anyString(), anyString())).thenReturn(false);

        // Mocking the success response
        when(responseService.notFoundWithMessageResponse(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo("The Friendship Request does not exist.");
    }

    @Test
    void acceptedRequestFriendship_Conflict_Friendship_AlreadyExists() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendRequestByUserToAccept(anyString(), anyString())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundAcceptedRequestFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Friendship already exists.");
    }
    // endregion

    // region aroundDeleteFriendship
    @Test
    void aroundDeleteFriendship_Success() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(anyString())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(true);

        // call the test method
        ResponseEntity<Object> result = underTest.aroundDeleteFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // El retorno es Null porque esto garantiza que se ejecutará el metodo observado
        assertThat(result).isNull();

        // Verificación de interacciones con los servicios simulados (Mock)
        verify(validationService, times(1)).userExistsById(eq(idUserRequest));
        verify(validationService, times(1)).userExistsById(eq(idUserTarget));
        verify(validationService, times(1)).existsFriendship(eq(idUserRequest), eq(idUserTarget));
        verify(responseService, never()).forbiddenDuplicateSocialUser();
        verify(responseService, never()).userNotFoundResponse(anyString());
        verify(responseService, never()).conflictResponseWithMessage(anyString());
    }

    @Test
    void deleteFriendship_Forbidden_SameUser() throws Throwable {
        // Mocking the success response
        when(responseService.forbiddenDuplicateSocialUser()).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserRequest);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.message()).isEqualTo("The user cannot be the same.");
    }

    @Test
    void deleteFriendship_NotFound_UserRequest() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserRequest));
    }

    @Test
    void deleteFriendship_NotFound_ToDeleteFriendship() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(idUserRequest)).thenReturn(true);
        when(validationService.userExistsById(idUserTarget)).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", idUserTarget));
    }

    @Test
    void deleteFriendship_Conflict_FriendshipRelationship_DoesNotExist() throws Throwable {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.existsFriendship(anyString(), anyString())).thenReturn(false);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(any())).thenCallRealMethod();

        // call the test method
        ResponseEntity<Object> responseEntity = underTest.aroundDeleteFriendship(
                mock(ProceedingJoinPoint.class), idUserRequest, idUserTarget);

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("There is no friendship relationship between users.");
    }
    // endregion
}

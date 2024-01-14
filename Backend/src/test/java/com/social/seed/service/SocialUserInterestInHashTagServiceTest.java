package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserInterestInHashTagRepository;
import com.social.seed.repository.SocialUserRepository;
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
 * Test class for the {@link SocialUserInterestInHashTagService}, focusing on testing individual methods and functionalities
 * for managing { The {@link com.social.seed.model.SocialUser} interest in {@link com.social.seed.model.HashTag} }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-01-13
 */
@ExtendWith(MockitoExtension.class)
class SocialUserInterestInHashTagServiceTest {
    // Class under test
    @InjectMocks
    private SocialUserInterestInHashTagService underTest;
    // region Mock Dependencies
    @Mock
    private SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository;
    @Mock
    private SocialUserRepository socialUserRepository;
    @Mock
    private ResponseService responseService;
    @Mock
    private ValidationService validationService;
    // endregion

    // region Sample Data for testing
    private SocialUser socialUser1;
    private HashTag hashTag1;
    // endregion

    @BeforeEach
    void setUp() {
        socialUser1 = TestUtils.createSocialUser("user1", "user1@gmail.com", "1992-01-04T00:00:00", "User Number One", "ES");
        socialUser1.setId("1");
        hashTag1 = TestUtils.createHashTag("1h","FirstHashTag",0,0);
    }

    // region Add Interest
    @Test
    void addInterest_Success() {
        // Mock validationService
        when(validationService.hashTagExistsById(any())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(false);
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(socialUserRepository.findById(socialUser1.getId())).thenReturn(Optional.of(socialUser1));

        // Mock repository
        doNothing().when(socialUserInterestInHashTagRepository).addInterest(any(),any(),any());

        // Mocking the success response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.addInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }

    @Test
    void addInterest_NotFound_UserRequest() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.addInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void addInterest_NotFound_HashTag() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.hashTagExistsById(any())).thenReturn(false);

        // Mocking the success response
        when(responseService.hashTagNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.addInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", hashTag1.getId()));
    }

    @Test
    void addInterest_Conflict_InterestAlreadyExists() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.hashTagExistsById(any())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(true);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.addInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.message()).isEqualTo("The Interest already exists");
    }

    // endregion

    // region Delete Interest
    @Test
    void deleteInterest_Success() {
        // Mock validationService
        when(validationService.hashTagExistsById(any())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(true);
        when(validationService.userExistsById(socialUser1.getId())).thenReturn(true);
        when(socialUserRepository.findById(socialUser1.getId())).thenReturn(Optional.of(socialUser1));

        // Mock repository
        doNothing().when(socialUserInterestInHashTagRepository).deleteInterest(any(),any());

        // Mocking the success response
        when(responseService.successResponse(any())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }


    @Test
    void deleteInterest_NotFound_UserRequest() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(false);

        // Mocking the success response
        when(responseService.userNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("The user with id: [ %s ] was not found.", socialUser1.getId()));
    }

    @Test
    void deleteInterest_NotFound_HashTag() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.hashTagExistsById(any())).thenReturn(false);

        // Mocking the success response
        when(responseService.hashTagNotFoundResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteInterest(socialUser1.getId(), hashTag1.getId());

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", hashTag1.getId()));
    }

    @Test
    void deleteInterest_Conflict_InterestDoesNotExist() {
        // Mock validationService
        when(validationService.userExistsById(any())).thenReturn(true);
        when(validationService.hashTagExistsById(any())).thenReturn(true);
        when(validationService.existsInterest(anyString(),anyString())).thenReturn(false);

        // Mocking the success response
        when(responseService.conflictResponseWithMessage(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.deleteInterest(socialUser1.getId(), hashTag1.getId());

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
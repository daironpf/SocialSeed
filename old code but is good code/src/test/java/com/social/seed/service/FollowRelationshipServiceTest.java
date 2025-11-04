package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.FollowRelationshipRepository;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
/**
 * Test class for the {@link FollowRelationshipService}, focusing on testing individual methods and functionalities
 * for managing { The Follows to SocialUsers }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-03-27
 */
@ExtendWith(MockitoExtension.class)
class FollowRelationshipServiceTest {
    // Class under test
    @InjectMocks
    private FollowRelationshipService underTest;
    // region dependencies
    @Mock
    private FollowRelationshipRepository followRelationshipRepository;
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void followSocialUser_Success() {
        // Mocking the repository createUserBFollowUserA method
        doNothing().when(followRelationshipRepository).createUserBFollowUserA(anyString(), anyString(), any());

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
    void unfollowSocialUser_Success() {
        // Mocking the repository unFollowTheUserA method
        doNothing().when(followRelationshipRepository).unFollowTheUserA(anyString(), anyString());

        // Mocking the success response
        when(responseService.successResponse(anyString())).thenCallRealMethod();

        // Calling the actual service method
        ResponseEntity<Object> responseEntity = underTest.unfollowSocialUser(socialUser1.getId(), "userIdToUnfollow");

        // Assertions
        assertThat(responseEntity).isNotNull();
        ResponseDTO response = (ResponseDTO) responseEntity.getBody();

        // Verify response details
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");
    }
}
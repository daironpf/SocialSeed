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
import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserInterestInHashTagRepository;
import com.social.seed.repository.SocialUserRepository;
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

    @Test
    void addInterest_Success() {
        // Mock repository
        when(socialUserRepository.findById(socialUser1.getId())).thenReturn(Optional.of(socialUser1));
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
    void deleteInterest_Success() {
        // Mock validationService
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
}
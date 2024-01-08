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
import com.social.seed.util.ValidationService;
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
    @Mock
    private ValidationService validationService;
    // endregion

    // region variables
    // Sample social user for testing
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
    void shouldReturnSuccessResponseWithSocialUserDetailsForValidUserName() {
        // Arrange
        when(responseService.successResponse(any())).thenCallRealMethod();
        when(socialUserRepository.findByUserName("maria1")).thenReturn(Optional.of(socialUser1));
        when(validationService.userExistByUserName("maria1")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = underTest.getSocialUserByUserName("maria1");

        // Assert
        verify(socialUserRepository, times(1)).findByUserName("maria1");

        assertThat(responseEntity).isNotNull();

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        SocialUser socialUserResponse = ((Optional<SocialUser>) response.response()).orElse(null);

        // Verify response details
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.message()).isEqualTo("Successful");

        // Verify social user details
        TestUtils.assertSocialUserEquals(socialUserResponse, socialUser1);
    }
    // endregion
}
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
package com.social.seed.utils;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

    /**
     * Creates a SocialUser object with the specified properties.
     *
     * @param userName   The username of the social user.
     * @param email      The email of the social user.
     * @param dateBorn   The date of birth of the social user in the format "yyyy-MM-dd'T'HH:mm:ss".
     * @param fullName   The full name of the social user.
     * @param language   The language preference of the social user.
     * @return A SocialUser object with the specified properties.
     */
    public static SocialUser createSocialUser(String userName, String email, String dateBorn, String fullName, String language) {
        return SocialUser.builder()
                .userName(userName)
                .email(email)
                .dateBorn(LocalDateTime.parse(dateBorn))
                .fullName(fullName)
                .language(language)
                .registrationDate(LocalDateTime.now())
                .isActive(true)
                .isDeleted(false)
                .onVacation(false)
                .followersCount(0)
                .friendCount(0)
                .followingCount(0)
                .friendRequestCount(0)
                .build();
    }

    /**
     * Creates a new HashTag instance with the given properties.
     *
     * @param id                    The unique identifier of the hashtag.
     * @param name                  The name of the hashtag.
     * @param socialUserInterestIn The count of social users interested in this hashtag.
     * @param postTaggedIn          The count of posts tagged with this hashtag.
     * @return A new HashTag instance with the specified properties.
     */
    public static HashTag createHashTag(String id, String name, int socialUserInterestIn, int postTaggedIn) {
        return HashTag.builder()
                .id(id)
                .name(name)
                .socialUserInterestIn(socialUserInterestIn)
                .postTaggedIn(postTaggedIn)
                .build();
    }

    /**
     * Asserts that two instances of {@link SocialUser} are equal in terms of their properties.
     *
     * @param actual   The actual SocialUser instance obtained during testing.
     * @param expected The expected SocialUser instance with the predefined values for comparison.
     *
     * @throws AssertionError If the actual and expected SocialUser instances are not equal.
     *                        The error details in the assertions can help identify which properties differ.
     */
    public static void assertSocialUserEquals(SocialUser actual, SocialUser expected) {
        // Ensure that the actual SocialUser instance is not null
        assertThat(actual).as("Actual SocialUser instance should not be null").isNotNull();

        // Compare individual properties for equality

        // Check for potential errors in the 'id' property comparison
        assertThat(actual.getId())
                .as("Error in 'id' property comparison")
                .isEqualTo(expected.getId());

        // Check for potential errors in the 'dateBorn' property comparison
        assertThat(actual.getDateBorn())
                .as("Error in 'dateBorn' property comparison")
                .isEqualTo(expected.getDateBorn());

        // Check for potential errors in the 'registrationDate' property comparison
        assertThat(actual.getRegistrationDate())
                .as("Error in 'registrationDate' property comparison")
                .isEqualTo(expected.getRegistrationDate());

        // Check for potential errors in the 'fullName' property comparison
        assertThat(actual.getFullName())
                .as("Error in 'fullName' property comparison")
                .isEqualTo(expected.getFullName());

        // Check for potential errors in the 'userName' property comparison
        assertThat(actual.getUserName())
                .as("Error in 'userName' property comparison")
                .isEqualTo(expected.getUserName());

        // Check for potential errors in the 'email' property comparison
        assertThat(actual.getEmail())
                .as("Error in 'email' property comparison")
                .isEqualTo(expected.getEmail());

        // Check for potential errors in the 'language' property comparison
        assertThat(actual.getLanguage())
                .as("Error in 'language' property comparison")
                .isEqualTo(expected.getLanguage());

        // Check for potential errors in the 'onVacation' property comparison
        assertThat(actual.getOnVacation())
                .as("Error in 'onVacation' property comparison")
                .isEqualTo(expected.getOnVacation());

        // Check for potential errors in the 'isActive' property comparison
        assertThat(actual.getIsActive())
                .as("Error in 'isActive' property comparison")
                .isEqualTo(expected.getIsActive());

        // Check for potential errors in the 'isDeleted' property comparison
        assertThat(actual.getIsDeleted())
                .as("Error in 'isDeleted' property comparison")
                .isEqualTo(expected.getIsDeleted());

        // Check for potential errors in the 'friendCount' property comparison
        assertThat(actual.getFriendCount())
                .as("Error in 'friendCount' property comparison")
                .isEqualTo(expected.getFriendCount());

        // Check for potential errors in the 'followersCount' property comparison
        assertThat(actual.getFollowersCount())
                .as("Error in 'followersCount' property comparison")
                .isEqualTo(expected.getFollowersCount());

        // Check for potential errors in the 'followingCount' property comparison
        assertThat(actual.getFollowingCount())
                .as("Error in 'followingCount' property comparison")
                .isEqualTo(expected.getFollowingCount());

        // Check for potential errors in the 'friendRequestCount' property comparison
        assertThat(actual.getFriendRequestCount())
                .as("Error in 'friendRequestCount' property comparison")
                .isEqualTo(expected.getFriendRequestCount());
    }

    /**
     * Asserts that two instances of {@link HashTag} are equal in terms of their properties.
     *
     * @param actual   The actual HashTag instance obtained during testing.
     * @param expected The expected HashTag instance with the predefined values for comparison.
     *
     * @throws AssertionError If the actual and expected HashTag instances are not equal.
     *                        The error details in the assertions can help identify which properties differ.
     */
    public static void assertHashTagEquals(HashTag actual, HashTag expected) {
        // Ensure that the actual SocialUser instance is not null
        assertThat(actual).as("Actual HashTag instance should not be null").isNotNull();

        // Check for potential errors in the 'id' property comparison
        assertThat(actual.getId())
                .as("Error in 'id' property comparison")
                .isEqualTo(expected.getId());

        // Check for potential errors in the 'name' property comparison
        assertThat(actual.getName())
                .as("Error in 'name' property comparison")
                .isEqualTo(expected.getName());

        // Check for potential errors in the 'socialUserInterestIn' property comparison
        assertThat(actual.getSocialUserInterestIn())
                .as("Error in 'socialUserInterestIn' property comparison")
                .isEqualTo(expected.getSocialUserInterestIn());

        // Check for potential errors in the 'postTaggedIn' property comparison
        assertThat(actual.getPostTaggedIn())
                .as("Error in 'postTaggedIn' property comparison")
                .isEqualTo(expected.getPostTaggedIn());
    }

    /**
     * Asserts that two instances of {@link com.social.seed.model.Post} are equal in terms of their properties.
     *
     * @param actual   The actual Post instance obtained during testing.
     * @param expected The expected Post instance with the predefined values for comparison.
     *
     * @throws AssertionError If the actual and expected Post instances are not equal.
     *                        The error details in the assertions can help identify which properties differ.
     */
    public static void assertPostEquals(Post actual, Post expected) {
        // Ensure that the actual Post instance is not null
        assertThat(actual).as("Actual Post instance should not be null").isNotNull();

        // Check for potential errors in the 'id' property comparison
        assertThat(actual.getId())
                .as("Error in 'id' property comparison")
                .isEqualTo(expected.getId());

        // Check for potential errors in the 'content' property comparison
        assertThat(actual.getContent())
                .as("Error in 'content' property comparison")
                .isEqualTo(expected.getContent());

        // Check for potential errors in the 'updateDate' property comparison
        assertThat(actual.getUpdateDate())
                .as("Error in 'updateDate' property comparison")
                .isEqualTo(expected.getUpdateDate());

        // Check for potential errors in the 'imageUrl' property comparison
        assertThat(actual.getImageUrl())
                .as("Error in 'imageUrl' property comparison")
                .isEqualTo(expected.getImageUrl());

        // Check for potential errors in the 'isActive' property comparison
        assertThat(actual.getIsActive())
                .as("Error in 'isActive' property comparison")
                .isEqualTo(expected.getIsActive());

        // Check for potential errors in the 'likedCount' property comparison
        assertThat(actual.getLikedCount())
                .as("Error in 'likedCount' property comparison")
                .isEqualTo(expected.getLikedCount());
    }
}
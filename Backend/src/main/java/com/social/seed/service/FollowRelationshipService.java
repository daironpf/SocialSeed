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
import com.social.seed.repository.FollowRelationshipRepository;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class handling operations related to Follow Relationship.
 */
@Service
public class FollowRelationshipService {

    // region Dependencies
    private final FollowRelationshipRepository followRelationshipRepository;
    private final ResponseService responseService;
    @Autowired
    public FollowRelationshipService(
            FollowRelationshipRepository followRelationshipRepository,
            ResponseService responseService
    ) {
        this.followRelationshipRepository = followRelationshipRepository;
        this.responseService = responseService;
    }
    // endregion

    // region Relationship Management
    /**
     * Allows a user to follow another user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToFollow The ID of the user to be followed.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> followSocialUser(String idUserRequest, String idUserToFollow) {
        // Create a Follow relationship
        followRelationshipRepository.createUserBFollowUserA(idUserRequest, idUserToFollow, LocalDateTime.now());
        return responseService.successResponse("The user was followed successfully.");
    }

    /**
     * Allows a user to unfollow another user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToUnFollow The ID of the user to be unfollowed.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> unfollowSocialUser(String idUserRequest, String idUserToUnFollow) {
        // Unfollow User
        followRelationshipRepository.unFollowTheUserA(idUserRequest, idUserToUnFollow);
        return responseService.successResponse("The user was unfollowed successfully.");
    }
    // endregion

    //region Gets
    public ResponseEntity<Object> getLiteFollowRecommendationsForUserById(String idUserRequest) {
        List<SocialUser> recommendations = followRelationshipRepository.getLiteFriendRecommendationsForUserById(idUserRequest);

        return responseService.successResponse(recommendations);
    }
    //endregion
}

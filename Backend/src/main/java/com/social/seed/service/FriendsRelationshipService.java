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
import com.social.seed.repository.FriendsRelationshipRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class focusing on operations related to managing Friends Relationship.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-16
 */
@Service
public class FriendsRelationshipService {
    //region dependencies
    private final FriendsRelationshipRepository friendsRelationshipRepository;
    private final ResponseService responseService;

    @Autowired
    public FriendsRelationshipService(FriendsRelationshipRepository friendsRelationshipRepository, ResponseService responseService) {
        this.friendsRelationshipRepository = friendsRelationshipRepository;
        this.responseService = responseService;
    }
    //endregion

    @Transactional
    public ResponseEntity<Object> createRequestFriendship(String idUserRequest, String idUserToBeFriend) {
        friendsRelationshipRepository.createFriendRequest(idUserRequest, idUserToBeFriend, LocalDateTime.now());

        return responseService.successResponse("The friendship request was created successfully.");
    }

    @Transactional
    public ResponseEntity<Object> cancelRequestFriendship(String idUserRequest, String idUserToCancelFriendRequest) {
        friendsRelationshipRepository.cancelRequestFriendship(idUserRequest, idUserToCancelFriendRequest);

        return responseService.successResponse("The friendship request was canceled successfully.");
    }

    @Transactional
    public ResponseEntity<Object> acceptedRequestFriendship(String idUserRequest, String idUserToAcceptedFriendRequest) {
        friendsRelationshipRepository.acceptedRequestFriendship(idUserRequest, idUserToAcceptedFriendRequest, LocalDateTime.now());

        return responseService.successResponse("The friendship request was accepted successfully.");
    }

    @Transactional
    public ResponseEntity<Object> deleteFriendship(String idUserRequest, String idUserToDeleteFriendship) {
        friendsRelationshipRepository.deleteFriendship(idUserRequest, idUserToDeleteFriendship);

        return responseService.successResponse("The Friendship Relationship was deleted successfully.");
    }

    public ResponseEntity<Object> getLiteFriendRecommendationsForUserById(String idUserRequest) {
        List<SocialUser> recommendations = friendsRelationshipRepository.getLiteFriendRecommendationsForUserById(idUserRequest);

        return responseService.successResponse(recommendations);
    }

    public ResponseEntity<Object> getFriendRecommendationsForUserById(String idUserRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SocialUser> recommendations = friendsRelationshipRepository.getFriendRecommendationsForUserById(idUserRequest, pageable);

        if (recommendations.isEmpty()) return responseService.notFoundWithMessageResponse("No SocialUsers available.");

        return responseService.successResponse(recommendations);
    }
}
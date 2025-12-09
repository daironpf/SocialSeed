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
package com.social.seed.controller;

import com.social.seed.service.FollowRelationshipService;
import com.social.seed.util.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle operations related to Follow Relationship in SocialUser in the application.
 * @author Dairon Pérez Frías
 */
@Tag(name = "FollowRelationship", description = "Follow Relationship Module")
@RestController
@RequestMapping("/api/v0.0.1/follow")
public class FollowRelationshipController {
    //region Dependencies
    private final FollowRelationshipService followRelationshipService;

    @Autowired
    public FollowRelationshipController(FollowRelationshipService followRelationshipService) {
        this.followRelationshipService = followRelationshipService;
    }
    //endregion

    //region Gets
    /**
     * Retrieves lite recommendations to the SocialUser of the idUserRequest.
     *
     * @param idUserRequest The ID of the user to recommendation SocialUser to follow.
     * @param page the number of page.
     * @param size the size of the page or the amount of elements in the page.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/follow-recommendations/")
    public ResponseEntity<ResponseDTO> getFollowRecommendationsForUserById(
            @RequestHeader("userId") String idUserRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ResponseEntity<Object> response = followRelationshipService.getFollowRecommendationsForUserById(idUserRequest, page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Retrieves a paginated list of users followed by the specified user.
     *
     * @param idUserRequest The ID of the user to retrieve the list of followed users.
     * @param userId The ID of the authenticated user making the request.
     * @param page The page number for pagination (default is 0).
     * @param size The size of each page or the number of elements per page (default is 10).
     * @return ResponseEntity containing a ResponseDTO with the list of followed users.
     */
    @GetMapping("/following/{idUserRequest}")
    public ResponseEntity<ResponseDTO> getFollowingForUserById(
            @PathVariable String idUserRequest,
            @RequestHeader("userId") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ResponseEntity<Object> response = followRelationshipService.getFollowingForUserById(userId, idUserRequest, page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Retrieves a paginated list of followers for the specified user.
     *
     * @param idUserRequest The ID of the user for whom to retrieve the list of followers.
     * @param userId The ID of the authenticated user making the request.
     * @param page The page number for pagination (default is 0).
     * @param size The size of each page or the number of elements per page (default is 10).
     * @return ResponseEntity containing a ResponseDTO with the paginated list of followers.
     */
    @GetMapping("/followers/{idUserRequest}")
    public ResponseEntity<ResponseDTO> getFollowersBySocialUserId(
            @PathVariable String idUserRequest,
            @RequestHeader("userId") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        // Retrieves a paginated list of followers for the specified user.
        ResponseEntity<Object> response = followRelationshipService.getFollowersBySocialUserId(userId, idUserRequest, page, size);

        // Constructs and returns a ResponseEntity containing a ResponseDTO with the paginated list of followers.
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    /**
     * Follow another Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToFollow The ID of the Social User to follow.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/follow/{idUserToFollow}")
    public ResponseEntity<ResponseDTO> followSocialUser(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToFollow) {
        ResponseEntity<Object> response = followRelationshipService.followSocialUser(idUserRequest, idUserToFollow);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Unfollow another Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToUnFollow The ID of the Social User to unfollow.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/unfollow/{idUserToUnFollow}")
    public ResponseEntity<ResponseDTO> unfollowSocialUser(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUnFollow) {
        ResponseEntity<Object> response = followRelationshipService.unfollowSocialUser(idUserRequest, idUserToUnFollow);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
}
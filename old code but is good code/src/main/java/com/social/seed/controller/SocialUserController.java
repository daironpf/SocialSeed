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

import com.social.seed.model.SocialUser;
import com.social.seed.util.ResponseDTO;
import com.social.seed.service.SocialUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle operations related to Social Users in the application.
 * @author Dairon Pérez Frías
 */
@Tag(name = "SocialUser", description = "SocialUser Module")
@RestController
@RequestMapping("/api/v0.0.1/socialUser")
public class SocialUserController {
    //region Dependencies
    private final SocialUserService socialUserService;

    @Autowired
    public SocialUserController(SocialUserService socialUserService) {
        this.socialUserService = socialUserService;
    }
    //endregion

    // region Gets
    /**
     * Retrieve a Social User by UserName.
     *
     * @param userName The userName of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserByUserName/{userName}")
    public ResponseEntity<ResponseDTO> getSocialUserByUserName(@PathVariable String userName) {
        ResponseEntity<Object> response = socialUserService.getSocialUserByUserName(userName);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Retrieve a Social User by Email.
     *
     * @param email The Email of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserByEmail/{email}")
    public ResponseEntity<ResponseDTO> getSocialUserByEmail(@PathVariable String email) {
        ResponseEntity<Object> response = socialUserService.getSocialUserByEmail(email);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region CRUD
    /**
     * Retrieve a Social User by ID.
     *
     * @param id The ID of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<ResponseDTO> getSocialUserById(@PathVariable String id) {
        ResponseEntity<Object> response = socialUserService.getSocialUserById(id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Create a new Social User.
     *
     * @param socialUser The Social User object to create.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/createSocialUser")
    public ResponseEntity<ResponseDTO> createSocialUser(@RequestBody SocialUser socialUser) {
        ResponseEntity<Object> response = socialUserService.createNewSocialUser(socialUser);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Update an existing Social User.
     *
     * @param userId     The ID of the user making the request.
     * @param socialUser The updated Social User object.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PutMapping("/updateSocialUser")
    public ResponseEntity<ResponseDTO> updateSocialUser(
            @RequestHeader("userId") String userId,
            @RequestBody SocialUser socialUser) {
        ResponseEntity<Object> response = socialUserService.updateSocialUser(userId, socialUser);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Delete a Social User by ID.
     *
     * @param userId The ID of the user making the request.
     * @param id     The ID of the Social User to delete.
     * @return ResponseEntity with a ResponseDTO.
     */
    @DeleteMapping("/deleteSocialUser/{id}")
    public ResponseEntity<ResponseDTO> deleteSocialUser(
            @RequestHeader("userId") String userId,
            @PathVariable String id) {
        ResponseEntity<Object> response = socialUserService.deleteSocialUser(userId, id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region update uniques properties
    /**
     * Update the username of a Social User.
     *
     * @param idUserRequest  The ID of the user making the request.
     * @param idUserToUpdate The ID of the Social User to update.
     * @param newUserName    The new username to set.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/updateSocialUserName/{idUserToUpdate}")
    public ResponseEntity<ResponseDTO> updateSocialUserName(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUpdate,
            @RequestParam String newUserName) {
        ResponseEntity<Object> response = socialUserService.updateSocialUserName(idUserRequest, idUserToUpdate, newUserName);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Update the email of a Social User.
     *
     * @param idUserRequest  The ID of the user making the request.
     * @param idUserToUpdate The ID of the Social User to update.
     * @param newEmail       The new email to set.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/updateSocialUserEmail/{idUserToUpdate}")
    public ResponseEntity<ResponseDTO> updateSocialUserEmail(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUpdate,
            @RequestParam String newEmail) {
        ResponseEntity<Object> response = socialUserService.updateSocialUserEmail(idUserRequest, idUserToUpdate, newEmail);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion



    //region VacationMode
    /**
     * Activate vacation mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/activateVacationMode")
    public ResponseEntity<ResponseDTO> activateVacationMode(
            @RequestHeader("userId") String idUserRequest) {
        ResponseEntity<Object> response = socialUserService.activateVacationMode(idUserRequest);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deactivate vacation mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/deactivateVacationMode")
    public ResponseEntity<ResponseDTO> deactivateVacationMode(
            @RequestHeader("userId") String idUserRequest) {
        ResponseEntity<Object> response = socialUserService.deactivateVacationMode(idUserRequest);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region Activate
    /**
     * Activate a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/activateSocialUser")
    public ResponseEntity<ResponseDTO> activateSocialUser(
            @RequestHeader("userId") String idUserRequest) {
        ResponseEntity<Object> response = socialUserService.activateSocialUser(idUserRequest);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deactivate a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/deactivateSocialUser")
    public ResponseEntity<ResponseDTO> deactivateSocialUser(
            @RequestHeader("userId") String idUserRequest) {
        ResponseEntity<Object> response = socialUserService.deactivateSocialUser(idUserRequest);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion
}
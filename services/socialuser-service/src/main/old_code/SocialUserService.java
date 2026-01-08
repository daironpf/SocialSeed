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
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service class handling operations related to Social Users.
 * @author Dairon Pérez Frías
 */
@Service
public class SocialUserService {

    // region Dependencies
    private final SocialUserRepository socialUserRepository;
    private final ResponseService responseService;
    @Autowired
    public SocialUserService(SocialUserRepository socialUserRepository, ResponseService responseService) {
        this.socialUserRepository = socialUserRepository;
        this.responseService = responseService;
    }
    // endregion

    // region Find by
    /**
     * Retrieves a Social User by UserName.
     *
     * @param userName The UserName of the Social User to retrieve.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getSocialUserByUserName(String userName) {
        return responseService.successResponse(socialUserRepository.findByUserName(userName));
    }

    /**
     * Retrieves a Social User by Email.
     *
     * @param email The Email of the Social User to retrieve.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getSocialUserByEmail(String email) {
        return responseService.successResponse(socialUserRepository.findByEmail(email));
    }
    // endregion

    // region CRUD
    /**
     * Retrieves a Social User by ID.
     *
     * @param userId The ID of the Social User to retrieve.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getSocialUserById(String userId) {
        return responseService.successResponse(socialUserRepository.findById(userId));
    }

    /**
     * Creates a new Social User.
     *
     * @param socialUser The Social User to be created.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> createNewSocialUser(SocialUser socialUser) {
        // Save the SocialUser Node
        SocialUser newSocialUser = socialUserRepository.save(
                SocialUser.builder()
                        .userName(socialUser.getUserName())
                        .email(socialUser.getEmail())
                        .dateBorn(socialUser.getDateBorn())
                        .fullName(socialUser.getFullName())
                        .language(socialUser.getLanguage())
                        .registrationDate(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .onVacation(false)
                        .followersCount(0)
                        .friendCount(0)
                        .followingCount(0)
                        .friendRequestCount(0)
                        .build()
        );

        return responseService.successCreatedResponse(newSocialUser);
    }

    /**
     * Updates a Social User.
     *
     * @param userId        The ID of the Social User to update.
     * @param newSocialUser The updated Social User information.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> updateSocialUser(String userId, SocialUser newSocialUser) {
        // Update the SocialUser Node properties
        socialUserRepository.update(
                newSocialUser.getId(),
                newSocialUser.getFullName(),
                newSocialUser.getDateBorn(),
                newSocialUser.getLanguage()
        );

        SocialUser response = socialUserRepository.findById(userId).get();
        return responseService.successResponse(response);
    }

    /**
     * Deletes a Social User.
     *
     * @param id The ID of the Social User to delete.
     * @param userId The ID of the user making the delete request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deleteSocialUser(String userId, String id) {
        // Delete the SocialUser
        socialUserRepository.deleteById(id);
        return responseService.successResponse("The user was deleted.");
    }
    // endregion

    // region Update Operations
    /**
     * Updates the username of a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToUpdate The ID of the user whose username is to be updated.
     * @param newUserName The new username to set.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> updateSocialUserName(String idUserRequest, String idUserToUpdate, String newUserName) {
        // Update the UserName in the SocialUser
        socialUserRepository.updateSocialUserName(idUserToUpdate, newUserName);
        return responseService.successResponseWithMessage(socialUserRepository.findById(idUserToUpdate).get(),
                "The username was updated successfully.");
    }

    /**
     * Updates the email of a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToUpdate The ID of the user whose email is to be updated.
     * @param newEmail The new email to set.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> updateSocialUserEmail(String idUserRequest, String idUserToUpdate, String newEmail) {
        // Update the Email in the SocialUser
        socialUserRepository.updateSocialUserEmail(idUserToUpdate, newEmail);
        return responseService.successResponseWithMessage(socialUserRepository.findById(idUserToUpdate).get(),
                "The email was updated successfully.");
    }
    // endregion

    // region Vacation Mode Operations
    /**
     * Activates Vacation Mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> activateVacationMode(String idUserRequest) {
        // Activate the Vacation Mode
        socialUserRepository.activateVacationMode(idUserRequest);
        return responseService.successResponse("The vacation mode was Activated successfully.");
    }

    /**
     * Deactivates Vacation Mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deactivateVacationMode(String idUserRequest) {
        // Deactivate the Vacation Mode
        socialUserRepository.deactivateVacationMode(idUserRequest);
        return responseService.successResponse("The vacation mode was Deactivated successfully.");
    }
    // endregion

    // region Activation Operations
    /**
     * Activates a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> activateSocialUser(String idUserRequest) {
        // Activate the SocialUser
        socialUserRepository.activateSocialUser(idUserRequest);
        return responseService.successResponse("The Social User was Activated successfully.");
    }

    /**
     * Deactivates a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deactivateSocialUser(String idUserRequest) {
        // Deactivate the SocialUser
        socialUserRepository.deactivateSocialUser(idUserRequest);
        return responseService.successResponse("The Social User was Deactivated successfully.");
    }
    // endregion
}
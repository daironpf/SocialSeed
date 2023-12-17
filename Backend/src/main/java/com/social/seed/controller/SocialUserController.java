package com.social.seed.controller;

import com.social.seed.model.SocialUser;
import com.social.seed.util.ResponseDTO;
import com.social.seed.service.SocialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle operations related to Social Users in the application.
 */
@RestController
@RequestMapping("/api/v0.0.1/socialUser")
public class SocialUserController {
    //region Dependencies
    @Autowired
    SocialUserService socialUserService;
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

    //region FOLLOW
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
        ResponseEntity<Object> response = socialUserService.followSocialUser(idUserRequest, idUserToFollow);
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
        ResponseEntity<Object> response = socialUserService.unfollowSocialUser(idUserRequest, idUserToUnFollow);
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
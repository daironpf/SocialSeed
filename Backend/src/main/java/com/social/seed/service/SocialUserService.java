package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service class handling operations related to Social Users.
 */
@Service
public class SocialUserService {

    //region Dependencies
    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private ValidationService validationService;
    //endregion

    /**
     * Retrieves a Social User by ID.
     *
     * @param userId The ID of the Social User to retrieve.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getSocialUserById(String userId) {
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);

        return responseService.successResponse(socialUserRepository.findById(userId), "Successful");
    }

    /**
     * Creates a new Social User.
     *
     * @param socialUser The Social User to be created.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> createNewSocialUser(SocialUser socialUser) {
        if (validationService.userExistByUserName(socialUser.getUserName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The userName [ %s ] already exists", socialUser.getUserName()));
        }
        if (validationService.userExistByEmail(socialUser.getEmail())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The Email [ %s ] already exists", socialUser.getEmail()));
        }

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
        if (!userId.equals(newSocialUser.getId())) {
            return responseService.forbiddenResponseWithMessage(
                    "The user making the update request is not the owner of this.");
        }
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);

        socialUserRepository.update(
                newSocialUser.getId(),
                newSocialUser.getFullName(),
                newSocialUser.getDateBorn(),
                newSocialUser.getLanguage()
        );

        return responseService.successResponse(socialUserRepository.findById(userId).get(), "Successful");
    }

    /**
     * Deletes a Social User.
     *
     * @param id The ID of the Social User to delete.
     * @param userId The ID of the user making the delete request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deleteSocialUser(String userId, String id) {
        if (!userId.equals(id)) return responseService.forbiddenResponseWithMessage("The user making the delete request is not the owner of this.");
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);

        socialUserRepository.deleteById(id);

        return responseService.successResponse("The user was deleted.", "Successful");
    }

    /**
     * Updates the username of a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToUpdate The ID of the user whose username is to be updated.
     * @param newUserName The new username to set.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> updateSocialUserName(String idUserRequest, String idUserToUpdate, String newUserName) {
        if (!idUserToUpdate.equals(idUserRequest)) {
            return responseService.forbiddenResponseWithMessage(
                    "The user who is requesting the userName change is not the owner of this");
        }
        if (!validationService.userExistsById(idUserToUpdate)) return responseService.userNotFoundResponse(idUserRequest);
        if (validationService.userExistByUserName(newUserName)) return responseService.conflictResponseWithMessage(String.format("The userName [ %s ] already exists", newUserName));

        socialUserRepository.updateSocialUserName(idUserToUpdate, newUserName);

        return responseService.successResponse(socialUserRepository.findById(idUserToUpdate).get(),
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
        if (!idUserToUpdate.equals(idUserRequest)) {
            return responseService.forbiddenResponseWithMessage("The user who is requesting the Email change is not the owner of this");
        }
        if (!validationService.userExistsById(idUserToUpdate)) return responseService.userNotFoundResponse(idUserRequest);
        if (validationService.userExistByEmail(newEmail)) return responseService.conflictResponseWithMessage( String.format("The Email [ %s ] already exists", newEmail));

        socialUserRepository.updateSocialUserEmail(idUserToUpdate, newEmail);

        return responseService.successResponse(socialUserRepository.findById(idUserToUpdate).get(),
                "The email was updated successfully.");
    }

    /**
     * Allows a user to follow another user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idUserToFollow The ID of the user to be followed.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> followSocialUser(String idUserRequest, String idUserToFollow) {
        if (idUserRequest.equals(idUserToFollow)) {
            return responseService.forbiddenResponseWithMessage(
                    "the user to be followed cannot be the same");
        }
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToFollow)) return responseService.NotFoundWithMessageResponse("The User to be followed has not been found");
        if (validationService.isUserBFollowerOfUserA(idUserRequest, idUserToFollow)) return responseService.alreadyFollow(idUserToFollow);

        socialUserRepository.createUserBFollowUserA(idUserRequest, idUserToFollow, LocalDateTime.now());

        return responseService.successResponse("The user was followed successfully.", "Successful");
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
        if (idUserRequest.equals(idUserToUnFollow)) return responseService.forbiddenResponseWithMessage("the user to be unfollowed cannot be the same");
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToUnFollow)) return responseService.NotFoundWithMessageResponse("The User to unfollow has not been found");
        if (!validationService.isUserBFollowerOfUserA(idUserRequest, idUserToUnFollow)) return responseService.dontUnFollow(idUserToUnFollow);

        socialUserRepository.unFollowTheUserA(idUserRequest, idUserToUnFollow);

        return responseService.successResponse("The user was unfollowed successfully.", "Successful");
    }

    /**
     * Activates Vacation Mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> activateVacationMode(String idUserRequest) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (validationService.isVacationModeActivated(idUserRequest)) return responseService.conflictResponseWithMessage("The Vacation Mode is already Active");

        socialUserRepository.activateVacationMode(idUserRequest);

        return responseService.successResponse("The vacation mode was Activated successfully.", "Successful");
    }

    /**
     * Deactivates Vacation Mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deactivateVacationMode(String idUserRequest) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.isVacationModeActivated(idUserRequest)) return responseService.conflictResponseWithMessage("The Vacation Mode is already Deactivated");

        socialUserRepository.deactivateVacationMode(idUserRequest);

        return responseService.successResponse("The vacation mode was Deactivated successfully.", "Successful");
    }

    /**
     * Activates a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> activateSocialUser(String idUserRequest) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (validationService.isSocialUserActivated(idUserRequest)) return responseService.conflictResponseWithMessage("The Social User is already Active");

        socialUserRepository.activateSocialUser(idUserRequest);

        return responseService.successResponse("The Social User was Activated successfully.", "Successful");
    }

    /**
     * Deactivates a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deactivateSocialUser(String idUserRequest) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.isSocialUserActivated(idUserRequest)) return responseService.conflictResponseWithMessage("The Social User is already Deactivated");

        socialUserRepository.deactivateSocialUser(idUserRequest);

        return responseService.successResponse("The Social User was Deactivated successfully.", "Successful");
    }
}

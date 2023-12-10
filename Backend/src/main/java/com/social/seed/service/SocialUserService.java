package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SocialUserService {
    //region dependencies
    @Autowired
    SocialUserRepository socialUserRepository;
    @Autowired
    ResponseService responseService;
    //endregion

    //region CRUD
    public ResponseEntity<Object> getSocialUserById(String userId) {
        if (!userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        return responseService.successResponse(socialUserRepository.findById(userId));
    }

    public ResponseEntity<Object> createNewSocialUser(SocialUser socialUser) {
        if (userExistByUserName(socialUser.getUserName())) return responseService.conflictResponseWithMessage(String.format("The userName [ %s ] already exists", socialUser.getUserName()));
        if (userExistByEmail(socialUser.getEmail())) return responseService.conflictResponseWithMessage(String.format("The Email [ %s ] already exists", socialUser.getEmail()));

        SocialUser newSocialUser = socialUserRepository.save(
                    SocialUser.builder()
                            .userName(socialUser.getUserName())
                            .email(socialUser.getEmail())
                            .dateBorn(socialUser.getDateBorn())
                            .fullName(socialUser.getFullName())
                            .language(socialUser.getLanguage())
                            .registrationDate(LocalDateTime.now())
                            .isActive(true)
                            .onVacation(false)
                            .followersCount(0)
                            .friendCount(0)
                            .followingCount(0)
                            .build()
        );

        return responseService.successCreatedResponse(newSocialUser);
    }

    public ResponseEntity<Object> updateSocialUser(String userId, SocialUser newSocialUser) {
        if (!userId.equals(newSocialUser.getId())) return responseService.forbiddenResponseWithMessage("The user making the update request is not the owner of this.");
        if (!userExistsById(userId)) return responseService.userNotFoundResponse(userId);

        socialUserRepository.update(
            newSocialUser.getId(),
            newSocialUser.getFullName(),
            newSocialUser.getDateBorn(),
            newSocialUser.getLanguage()
        );
        return responseService.successResponse(socialUserRepository.findById(userId).get());
    }
    public ResponseEntity<Object> deleteSocialUser(String userId, String id) {
        if (!userId.equals(id)) return responseService.forbiddenResponseWithMessage("The user making the delete request is not the owner of this.");
        if (!userExistsById(userId)) return responseService.userNotFoundResponse(userId);

        socialUserRepository.deleteById(id);
        return responseService.successResponse(null);
    }
    //endregion

    //region Update Special Props
    public ResponseEntity<Object> updateSocialUserName(String idUserRequest, String idUserToUpdate, String newUserName) {
        if (!idUserToUpdate.equals(idUserRequest)) return responseService.forbiddenResponseWithMessage("The user who is requesting the userName change is not the owner of this");
        if (!userExistsById(idUserToUpdate)) return responseService.userNotFoundResponse(idUserRequest);
        if (userExistByUserName(newUserName)) return responseService.conflictResponseWithMessage(String.format("The userName [ %s ] already exists", newUserName));

        socialUserRepository.updateSocialUserName(idUserToUpdate, newUserName);
        return responseService.successResponse(socialUserRepository.findById(idUserToUpdate).get());
    }

    public ResponseEntity<Object> updateSocialUserEmail(String idUserRequest, String idUserToUpdate, String newEmail) {
        if (!idUserToUpdate.equals(idUserRequest)) return responseService.forbiddenResponseWithMessage("The user who is requesting the Email change is not the owner of this");
        if (!userExistsById(idUserToUpdate)) return responseService.userNotFoundResponse(idUserRequest);
        if (userExistByEmail(newEmail)) return responseService.conflictResponseWithMessage(String.format("The Email [ %s ] already exists", newEmail));

        socialUserRepository.updateSocialUserEmail(idUserToUpdate, newEmail);
        return responseService.successResponse(socialUserRepository.findById(idUserToUpdate).get());
    }
    //endregion

    //region FOLLOWED_BY
    @Transactional
    public ResponseEntity<Object> followSocialUser(String idUserRequest, String idUserToFollow) {
        if (idUserRequest.equals(idUserToFollow)) return responseService.forbiddenResponseWithMessage("the user to be followed cannot be the same");
        if (!userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!userExistsById(idUserToFollow)) return responseService.NotFoundWithMessageResponse("The User to be followed has not been found");
        if (isUserBFollowerOfUserA(idUserRequest, idUserToFollow)) return responseService.alreadyFollow(idUserToFollow);

        socialUserRepository.createUserBFollowUserA(idUserRequest,idUserToFollow,LocalDateTime.now());
        return responseService.successResponse("The user was followed successfully.");
    }

    @Transactional
    public ResponseEntity<Object> unfollowSocialUser(String idUserRequest, String idUserToUnFollow) {
        if (idUserRequest.equals(idUserToUnFollow)) return responseService.forbiddenResponseWithMessage("the user to be unfollowed cannot be the same");
        if (!userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!userExistsById(idUserToUnFollow)) return responseService.NotFoundWithMessageResponse("The User to unfollow has not been found");
        if (!isUserBFollowerOfUserA(idUserRequest, idUserToUnFollow)) return responseService.dontUnFollow(idUserToUnFollow);

        socialUserRepository.unFollowTheUserA(idUserRequest, idUserToUnFollow);
        return responseService.successResponse("The user was unfollowed successfully.");
    }
    //endregion

    //region util validate
    private boolean userExistByEmail(String email) {
        return socialUserRepository.existByEmail(email);
    }
    private boolean userExistByUserName(String userName) {
        return socialUserRepository.existByUserName(userName);
    }

    private boolean userExistsById(String userId) {
        return socialUserRepository.existsById(userId);
    }

    private boolean isUserBFollowerOfUserA(String idUserRequest, String idUserToUnFollow){
        return socialUserRepository.IsUserBFollowerOfUserA(idUserRequest, idUserToUnFollow);
    }
    //endregion
}

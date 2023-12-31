package com.social.seed.service;

import com.social.seed.repository.FriendsRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FriendsService {
    //region dependencies
    private final FriendsRepository friendsRepository;
    private final ResponseService responseService;
    private final ValidationService validationService;

    @Autowired
    public FriendsService(FriendsRepository friendsRepository, ResponseService responseService, ValidationService validationService) {
        this.friendsRepository = friendsRepository;
        this.responseService = responseService;
        this.validationService = validationService;
    }

    //endregion

    @Transactional
    public ResponseEntity<Object> createRequestFriendship(String idUserRequest, String idUserToBeFriend) {
        if (idUserRequest.equals(idUserToBeFriend)) {
            return responseService.forbiddenResponseWithMessage("The user to be Friend cannot be the same.");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToBeFriend)) {
            return responseService.userNotFoundResponse(idUserToBeFriend);
        }
        if (validationService.existsFriendRequest(idUserRequest, idUserToBeFriend)) {
            return responseService.conflictResponseWithMessage("The Friend Request already exists");
        }
        if (validationService.existsFriendship(idUserRequest, idUserToBeFriend)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists");
        }

        friendsRepository.createFriendRequest(idUserRequest, idUserToBeFriend, LocalDateTime.now());
        return responseService.successResponse("The friendship request was created successfully.","Successful");
    }

    @Transactional
    public ResponseEntity<Object> cancelRequestFriendship(String idUserRequest, String idUserToCancelFriendRequest) {
        if (idUserRequest.equals(idUserToCancelFriendRequest)) {
            return responseService.forbiddenResponseWithMessage("The user cannot be the same.");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToCancelFriendRequest)) {
            return responseService.userNotFoundResponse(idUserToCancelFriendRequest);
        }
        if (!validationService.existsFriendRequest(idUserRequest, idUserToCancelFriendRequest)) {
            return responseService.NotFoundWithMessageResponse("The Friend Request does not exist");
        }
        if (validationService.existsFriendship(idUserRequest, idUserToCancelFriendRequest)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists");
        }

        friendsRepository.cancelRequestFriendship(idUserRequest, idUserToCancelFriendRequest);
        return responseService.successResponse("The friendship request was canceled successfully.","Successful");
    }

    @Transactional
    public ResponseEntity<Object> acceptedRequestFriendship(String idUserRequest, String idUserToAcceptedFriendRequest) {
        if (idUserRequest.equals(idUserToAcceptedFriendRequest)) {
            return responseService.forbiddenResponseWithMessage("The user cannot be the same.");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToAcceptedFriendRequest)) {
            return responseService.userNotFoundResponse(idUserToAcceptedFriendRequest);
        }
        if (!validationService.existsFriendRequestByUserToAccept(idUserRequest, idUserToAcceptedFriendRequest)) {
            return responseService.NotFoundWithMessageResponse("The Friendship Request does not exist.");
        }
        if (validationService.existsFriendship(idUserRequest, idUserToAcceptedFriendRequest)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists.");
        }

        friendsRepository.acceptedRequestFriendship(idUserRequest, idUserToAcceptedFriendRequest, LocalDateTime.now());
        return responseService.successResponse("The friendship request was accepted successfully.","Successful");
    }

    @Transactional
    public ResponseEntity<Object> deleteFriendship(String idUserRequest, String idUserToDeleteFriendship) {
        if (idUserRequest.equals(idUserToDeleteFriendship)) {
            return responseService.forbiddenResponseWithMessage("The user cannot be the same.");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToDeleteFriendship)) {
            return responseService.userNotFoundResponse(idUserToDeleteFriendship);
        }
        if (!validationService.existsFriendship(idUserRequest, idUserToDeleteFriendship)) {
            return responseService.conflictResponseWithMessage("There is no friendship relationship between users.");
        }

        friendsRepository.deleteFriendship(idUserRequest, idUserToDeleteFriendship);
        return responseService.successResponse("The Friendship Relationship was deleted successfully.","Successful");
    }
}

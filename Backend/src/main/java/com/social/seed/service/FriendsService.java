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
    @Autowired
    FriendsRepository friendsRepository;
    @Autowired
    ResponseService responseService;
    @Autowired
    ValidationService validationService;
    //endregion

    @Transactional
    public ResponseEntity<Object> createRequestFriendship(String idUserRequest, String idUserToBeFriend) {
        if (idUserRequest.equals(idUserToBeFriend)) return responseService.forbiddenResponseWithMessage("the user to be Friend cannot be the same.");
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToBeFriend)) return responseService.NotFoundWithMessageResponse("The User to Create Friend Request has not been found");
        if (validationService.existsFriendRequest(idUserRequest, idUserToBeFriend)) return responseService.existsFriendRequest(idUserToBeFriend);
        if (validationService.existsFriendship(idUserRequest, idUserToBeFriend)) return responseService.existsFriendship();

        friendsRepository.createFriendRequest(idUserRequest, idUserToBeFriend, LocalDateTime.now());
        return responseService.successResponse("The friendship request was Created successfully.");
    }

    @Transactional
    public ResponseEntity<Object> cancelRequestFriendship(String idUserRequest, String idUserToCancelFriendRequest) {
        if (idUserRequest.equals(idUserToCancelFriendRequest)) return responseService.forbiddenResponseWithMessage("the user cannot be the same.");
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToCancelFriendRequest)) return responseService.NotFoundWithMessageResponse("The User to Cancel Friend Request has not been found");
        if (!validationService.existsFriendRequest(idUserRequest, idUserToCancelFriendRequest)) return responseService.dontExistsFriendRequest(idUserToCancelFriendRequest);
        if (validationService.existsFriendship(idUserRequest, idUserToCancelFriendRequest)) return responseService.existsFriendship();

        friendsRepository.cancelRequestFriendship(idUserRequest, idUserToCancelFriendRequest);
        return responseService.successResponse("The friendship request was Cancel successfully.");
    }

    @Transactional
    public ResponseEntity<Object> acceptedRequestFriendship(String idUserRequest, String idUserToAcceptedFriendRequest) {
        if (idUserRequest.equals(idUserToAcceptedFriendRequest)) return responseService.forbiddenResponseWithMessage("the user cannot be the same.");
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToAcceptedFriendRequest)) return responseService.NotFoundWithMessageResponse("The User to Accepted Friend Request has not been found.");
        if (!validationService.existsFriendRequestByUserToAccept(idUserRequest, idUserToAcceptedFriendRequest)) return responseService.NotFoundWithMessageResponse("The Friendship Request does not Exist.");
        if (validationService.existsFriendship(idUserRequest, idUserToAcceptedFriendRequest)) return responseService.existsFriendship();

        friendsRepository.acceptedRequestFriendship(idUserRequest, idUserToAcceptedFriendRequest, LocalDateTime.now());
        return responseService.successResponse("The friendship request was Accepted successfully.");
    }

    @Transactional
    public ResponseEntity<Object> deleteFriendship(String idUserRequest, String idUserToDeleteFriendship) {
        if (idUserRequest.equals(idUserToDeleteFriendship)) return responseService.forbiddenResponseWithMessage("the user cannot be the same.");
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.userExistsById(idUserToDeleteFriendship)) return responseService.NotFoundWithMessageResponse("The User to Delete Friendship has not been found.");
        if (!validationService.existsFriendship(idUserRequest, idUserToDeleteFriendship)) return responseService.conflictResponseWithMessage("There is no friendship Relationship between users.");

        friendsRepository.deleteFriendship(idUserRequest, idUserToDeleteFriendship);
        return responseService.successResponse("The Friendship Relationship was Deleted successfully.");
    }
}

package com.social.seed.util;

import com.social.seed.repository.FriendsRepository;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.repository.PostRepository;
import com.social.seed.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    //region dependencies
    @Autowired
    SocialUserRepository socialUserRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashTagRepository hashTagRepository;
    @Autowired
    FriendsRepository friendsRepository;
    //endregion

    //region SocialUser
    public boolean userExistByEmail(String email) {
        return socialUserRepository.existByEmail(email);
    }
    public boolean userExistByUserName(String userName) {
        return socialUserRepository.existByUserName(userName);
    }
    public boolean userExistsById(String userId) {
        return socialUserRepository.existsById(userId);
    }
    public boolean isUserBFollowerOfUserA(String idUserRequest, String idUserToUnFollow){
        return socialUserRepository.IsUserBFollowerOfUserA(idUserRequest, idUserToUnFollow);
    }
    public boolean isVacationModeActivated(String idUserRequest) {
        return socialUserRepository.isVacationModeActivated(idUserRequest);
    }
    //endregion

    //region Post
    public boolean postExistsById(String postId) {
        return postRepository.existsById(postId);
    }
    public boolean userLikedPost(String userId, String postId) {
        return postRepository.isUserByIdLikedPostById(userId, postId);
    }
    public boolean userAuthorOfThePostById(String userId, String postId) {
        return postRepository.isUserAuthorOfThePostById(userId, postId);
    }
    //endregion

    //region HashTag
    public boolean hashTagExistsByName(String name) {
        return hashTagRepository.existByName(name);
    }
    public boolean hashTagExistsById(String tagId) {
        return hashTagRepository.existsById(tagId);
    }
    //endregion

    //region Friends
    public boolean existsFriendship(String idUserRequest, String idUserToAcceptedFriendRequest) {
        return friendsRepository.existsFriendship(idUserRequest, idUserToAcceptedFriendRequest);
    }
    public boolean existsFriendRequest(String idUserRequest, String idUserToBeFriend){
        return friendsRepository.existsFriendRequest(idUserRequest, idUserToBeFriend);
    }
    public boolean existsFriendRequestByUserToAccept(String idUserRequest, String idUserToAcceptedFriendRequest) {
        return friendsRepository.existsFriendRequestByUserToAccept(idUserRequest, idUserToAcceptedFriendRequest);
    }
    //endregion

}

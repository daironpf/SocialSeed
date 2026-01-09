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
package com.social.seed.util;

import com.social.seed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Service to handle operations related to Validate Data in the application.
 */
@Component
public class ValidationService {
    //region dependencies
    private final SocialUserRepository socialUserRepository;
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final FriendsRelationshipRepository friendsRelationshipRepository;
    private final SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository;
    private final FollowRelationshipRepository followRelationshipRepository;

    @Autowired
    public ValidationService(
            SocialUserRepository socialUserRepository,
            PostRepository postRepository,
            HashTagRepository hashTagRepository,
            FriendsRelationshipRepository friendsRelationshipRepository,
            SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository,
            FollowRelationshipRepository followRelationshipRepository) {
        this.socialUserRepository = socialUserRepository;
        this.postRepository = postRepository;
        this.hashTagRepository = hashTagRepository;
        this.friendsRelationshipRepository = friendsRelationshipRepository;
        this.socialUserInterestInHashTagRepository = socialUserInterestInHashTagRepository;
        this.followRelationshipRepository = followRelationshipRepository;
    }

    //endregion

    //region SocialUser
    public boolean userExistByEmail(String email) {
        return socialUserRepository.existByEmail(email);
    }
    public boolean userExistByUserName(String userName) {
        return socialUserRepository.existByUserName(userName);
    }
    public boolean userExistsById(String userId) { return socialUserRepository.existsById(userId); }
    public boolean isVacationModeActivated(String idUserRequest) {
        return socialUserRepository.isVacationModeActivated(idUserRequest);
    }
    public boolean isSocialUserActivated(String idUserRequest) {
        return socialUserRepository.isSocialUserActivated(idUserRequest);
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
        return friendsRelationshipRepository.existsFriendship(idUserRequest, idUserToAcceptedFriendRequest);
    }
    public boolean existsFriendRequest(String idUserRequest, String idUserToBeFriend){
        return friendsRelationshipRepository.existsFriendRequest(idUserRequest, idUserToBeFriend);
    }
    public boolean existsFriendRequestByUserToAccept(String idUserRequest, String idUserToAcceptedFriendRequest) {
        return friendsRelationshipRepository.existsFriendRequestByUserToAccept(idUserRequest, idUserToAcceptedFriendRequest);
    }
    //endregion

    //region Follow
    public boolean isUserBFollowerOfUserA(String idUserRequest, String idUserToUnFollow){
        return followRelationshipRepository.isUserBFollowerOfUserA(idUserRequest, idUserToUnFollow);
    }
    //endregion

    //region SocialUserInterestInHashTag
    public boolean existsInterest(String idUserRequest, String idHashTag) {
        return socialUserInterestInHashTagRepository.existsInterest(idUserRequest, idHashTag);
    }
    //endregion
}

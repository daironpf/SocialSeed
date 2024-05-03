package com.social.seed.dto;

import com.social.seed.model.SocialUserInterestInHashTagRelationShip;

import java.time.LocalDateTime;
import java.util.List;

public record SocialUserCard(
        String id,

        LocalDateTime dateBorn,
        LocalDateTime registrationDate,

        String fullName,
        String userName,
        String email,
        String language,
        String profileImage,
        String bio,

        Boolean onVacation,
        Boolean isActive,
        Boolean isDeleted,

        Integer friendCount,
        Integer followersCount,
        Integer followingCount,
        Integer friendRequestCount,

        Boolean isFriend,
        Boolean isRequestFriendshipSending,
        Boolean isRequestFriendshipReceived,
        Boolean isFollower,
        Boolean isFollow,
        Integer mutualFriends,

        List<SocialUserInterestInHashTagRelationShip>hashTags
) {
}

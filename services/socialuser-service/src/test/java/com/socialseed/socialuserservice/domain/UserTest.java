package com.socialseed.socialuserservice.domain;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

class UserTest {

    @Test
    void should_update_profile_fields() {
        User user = User.rehydrate(
                UUID.randomUUID(),
                "user",
                "user@mail.com",
                "Old Name",
                null,
                UserLanguage.EN,
                null,
                null,
                UserStatus.ACTIVE,
                null
        );

        user.updateProfile(
                "New Name",
                "Bio",
                "https://image.com/img.png",
                LocalDate.of(2000, 1, 1),
                UserLanguage.ES
        );

        assertEquals("New Name", user.getFullName());
        assertEquals("Bio", user.getBio());
        assertEquals(UserLanguage.ES, user.getLanguage());
    }
}
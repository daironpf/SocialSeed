package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SocialUserServiceTest {

    @Mock
    private SocialUserRepository socialUserRepository;

    @InjectMocks
    private SocialUserService underTest;

    @Test
    void canCreateNewSocialUser() {
        // given
        String email = "maria1@gmail.com";
        SocialUser user1 = SocialUser.builder()
                .userName("maria1")
                .email(email)
                .dateBorn(LocalDateTime.parse("1992-01-04T00:00:00"))
                .fullName("Maria del Laurel Perez")
                .language("ES")
                .registrationDate(LocalDateTime.now())
                .isActive(true)
                .isDeleted(false)
                .onVacation(false)
                .followersCount(0)
                .friendCount(0)
                .followingCount(0)
                .friendRequestCount(0)
                .build();

        // when
        underTest.createNewSocialUser(user1);

        // then
        verify(socialUserRepository).save(user1);
    }
}

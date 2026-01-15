package com.socialseed.socialuserservice.usecases;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.socialuserservice.user.application.usecase.UpdateUserProfile;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UpdateUserProfileDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserProfile useCase;

    @Test
    void should_update_user_profile_successfully() {
        // given
        UUID userId = UUID.randomUUID();

        User user = User.rehydrate(
                userId,
                "john_doe",
                "john@mail.com",
                "John",
                null,
                UserLanguage.EN,
                null,
                null,
                UserStatus.ACTIVE,
                null
        );

        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(
                userId,
                "John Updated",
                "New bio",
                "https://image.com/profile.png",
                LocalDate.of(1995, 1, 1),
                UserLanguage.ES
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // when
        useCase.execute(dto);

        // then
        verify(userRepository).updateProfile(user);

        assertEquals("John Updated", user.getFullName());
        assertEquals("New bio", user.getBio());
        assertEquals(UserLanguage.ES, user.getLanguage());
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        UUID userId = UUID.randomUUID();

        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(
                userId,
                "John",
                null,
                null,
                null,
                UserLanguage.EN
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> useCase.execute(dto)
        );

        verify(userRepository, never()).updateProfile(any());
    }

    @Test
    void should_not_allow_update_if_user_deleted() {
        User user = User.rehydrate(
                UUID.randomUUID(),
                "user",
                "user@mail.com",
                "Name",
                null,
                UserLanguage.EN,
                null,
                null,
                UserStatus.DELETED,
                null
        );

        assertThrows(
                IllegalStateException.class,
                () -> user.updateProfile(
                        "New",
                        null,
                        null,
                        null,
                        UserLanguage.EN
                )
        );
    }


}

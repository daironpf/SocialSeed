package com.socialseed.socialuserservice.usecases;

import com.socialseed.socialuserservice.user.application.usecase.EndVacation;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import com.socialseed.socialuserservice.user.domain.model.valueobject.VacationPeriod;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndVacationUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EndVacation useCase;

    @Test
    void should_end_vacation_successfully() {
        // given
        UUID userId = UUID.randomUUID();
        VacationPeriod period = VacationPeriod.of(LocalDate.now(), LocalDate.now().plusDays(1));
        User user = User.rehydrate(
                userId, "user", "user@mail.com", "Name", null, UserLanguage.EN, null, null, UserStatus.ON_VACATION, period
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        useCase.execute(userId);

        // then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertNull(user.getVacationPeriod());
        verify(userRepository).updateProfile(user);
    }

    @Test
    void should_throw_exception_if_user_not_on_vacation() {
        // given
        UUID userId = UUID.randomUUID();
        User user = User.rehydrate(
                userId, "user", "user@mail.com", "Name", null, UserLanguage.EN, null, null, UserStatus.ACTIVE, null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId));
    }
}

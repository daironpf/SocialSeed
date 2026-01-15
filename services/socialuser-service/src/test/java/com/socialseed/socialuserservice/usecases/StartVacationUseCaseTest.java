package com.socialseed.socialuserservice.usecases;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.socialuserservice.user.application.usecase.StartVacation;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.StartVacationRequestDTO;
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
class StartVacationUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StartVacation useCase;

    @Test
    void should_start_vacation_successfully() {
        // given
        UUID userId = UUID.randomUUID();
        User user = User.rehydrate(
                userId, "user", "user@mail.com", "Name", null, UserLanguage.EN, null, null, UserStatus.ACTIVE, null
        );

        StartVacationRequestDTO dto = new StartVacationRequestDTO(
                userId,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                "Trip to Japan"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        useCase.execute(dto);

        // then
        assertEquals(UserStatus.ON_VACATION, user.getStatus());
        assertNotNull(user.getVacationPeriod());
        assertEquals("Trip to Japan", user.getVacationPeriod().note());
        verify(userRepository).updateProfile(user);
    }

    @Test
    void should_throw_exception_if_user_already_on_vacation() {
        // given
        UUID userId = UUID.randomUUID();
        User user = User.rehydrate(
                userId, "user", "user@mail.com", "Name", null, UserLanguage.EN, null, null, UserStatus.ON_VACATION, null
        );

        StartVacationRequestDTO dto = new StartVacationRequestDTO(
                userId, LocalDate.now(), LocalDate.now().plusDays(1), "Note"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(dto));
    }
}

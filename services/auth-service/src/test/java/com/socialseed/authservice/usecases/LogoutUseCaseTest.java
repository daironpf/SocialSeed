package com.socialseed.authservice.usecases;

import com.socialseed.authservice.auth.application.usecase.Logout;
import com.socialseed.authservice.auth.domain.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseTest {

    @Mock
    private AuthService authService;

    private Logout logout;

    @BeforeEach
    void setUp() {
        logout = new Logout(authService);
    }

    @Test
    void shouldCallAuthServiceLogout() {
        String accessToken = "Bearer valid-token";
        String refreshToken = "valid-refresh-token";

        logout.execute(accessToken, refreshToken);

        verify(authService).logout(accessToken, refreshToken);
    }
}

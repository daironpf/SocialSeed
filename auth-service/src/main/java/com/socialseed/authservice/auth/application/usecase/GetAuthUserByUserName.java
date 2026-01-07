package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.platform.error.BusinessException;
import com.socialseed.authservice.platform.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAuthUserByUserName {
    private final AuthService authService;

    public GetAuthUserByUserName(AuthService authService) {
        this.authService = authService;
    }

    public Optional<AuthUser> execute(String username) {
        return Optional.ofNullable(authService.getUserByUserName(username)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_BY_USER_NAME_NOT_FOUND,
                        username
                )));
    }
}

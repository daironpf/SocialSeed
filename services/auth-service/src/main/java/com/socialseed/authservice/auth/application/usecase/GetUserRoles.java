package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class GetUserRoles {
    private final AuthService authService;

    public GetUserRoles(AuthService authService) {
        this.authService = authService;
    }

    public Set<String> execute(UUID userId) {
        AuthUser user = authService.getUserById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return user.getRoles();
    }
}

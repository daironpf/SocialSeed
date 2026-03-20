package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.event.RoleAssignedEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.RoleAssignedEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class AssignRoleToUser {
    private final AuthService authService;
    private final RoleAssignedEventPublisher eventPublisher;

    public AssignRoleToUser(AuthService authService, RoleAssignedEventPublisher eventPublisher) {
        this.authService = authService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Set<String> execute(UUID userId, String role, UUID adminId) {
        // Validate user exists
        AuthUser user = authService.getUserById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Validate role is not already assigned
        if (user.getRoles().contains(role)) {
            throw new BusinessException(ErrorCode.ROLE_ALREADY_ASSIGNED);
        }

        // Assign role and save
        user.getRoles().add(role);
        authService.saveUser(user);

        // Invalidate all existing refresh tokens to enforce new role permissions
        authService.revokeAllTokensForUser(userId);

        // Emit RoleAssigned event
        RoleAssignedEvent event = new RoleAssignedEvent(
                userId,
                user.getEmail(),
                user.getUsername(),
                role,
                adminId,
                Instant.now()
        );
        eventPublisher.publish(event);

        return user.getRoles();
    }
}
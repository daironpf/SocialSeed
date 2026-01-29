package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.event.RoleRemovedEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.RoleRemovedEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class RemoveRoleFromUser {
    private final AuthService authService;
    private final RoleRemovedEventPublisher eventPublisher;

    public RemoveRoleFromUser(AuthService authService, RoleRemovedEventPublisher eventPublisher) {
        this.authService = authService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Set<String> execute(UUID userId, String role, UUID adminId) {
        // Validate user exists
        AuthUser user = authService.getUserById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Validate role is assigned
        if (!user.getRoles().contains(role)) {
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }

        // Prevent removing ROLE_USER from self
        if (userId.equals(adminId) && "ROLE_USER".equals(role)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_OWN_USER_ROLE);
        }

        // Prevent removing last admin role if there are no other admins
        if ("ROLE_ADMIN".equals(role)) {
            long adminCount = authService.countUsersWithRole("ROLE_ADMIN");
            if (adminCount <= 1) {
                throw new BusinessException(ErrorCode.CANNOT_REMOVE_LAST_ADMIN);
            }
        }

        // Remove role and save
        user.getRoles().remove(role);
        authService.saveUser(user);

        // Revoke all tokens for the user to reflect role changes immediately
        authService.revokeAllTokensForUser(userId);

        // Emit RoleRemoved event
        RoleRemovedEvent event = new RoleRemovedEvent(
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
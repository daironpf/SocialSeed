package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.event.RoleRemovedEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.RoleRemovedEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveRoleFromUserTest {

    @Mock
    private AuthService authService;

    @Mock
    private RoleRemovedEventPublisher eventPublisher;

    private RemoveRoleFromUser removeRoleFromUser;
    private AuthUser testUser;
    private UUID userId;
    private UUID adminId;

    @BeforeEach
    void setUp() {
        removeRoleFromUser = new RemoveRoleFromUser(authService, eventPublisher);
        
        userId = UUID.randomUUID();
        adminId = UUID.randomUUID();
        
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        
        testUser = new AuthUser(userId, "testuser", "test@example.com", "password");
        testUser.setRoles(roles);
    }

    @Test
    void execute_SuccessfulRoleRemoval_ShouldRemoveRoleAndPublishEvent() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(authService.countUsersWithRole("ROLE_ADMIN")).thenReturn(2L); // Another admin exists

        // Act
        Set<String> result = removeRoleFromUser.execute(userId, "ROLE_ADMIN", adminId);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("ROLE_USER"));
        assertFalse(result.contains("ROLE_ADMIN"));
        
        verify(authService).saveUser(testUser);
        verify(authService).revokeAllTokensForUser(userId);
        verify(eventPublisher).publish(any(RoleRemovedEvent.class));
    }

    @Test
    void execute_UserNotFound_ShouldThrowException() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> removeRoleFromUser.execute(userId, "ROLE_ADMIN", adminId));
        
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_RoleNotAssigned_ShouldThrowException() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> removeRoleFromUser.execute(userId, "ROLE_MODERATOR", adminId));
        
        assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void execute_RemovingOwnUserRole_ShouldThrowException() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> removeRoleFromUser.execute(userId, "ROLE_USER", userId)); // Same user
        
        assertEquals(ErrorCode.CANNOT_REMOVE_OWN_USER_ROLE, exception.getErrorCode());
    }

    @Test
    void execute_RemovingLastAdmin_ShouldThrowException() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(authService.countUsersWithRole("ROLE_ADMIN")).thenReturn(1L); // Only this admin

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> removeRoleFromUser.execute(userId, "ROLE_ADMIN", adminId));
        
        assertEquals(ErrorCode.CANNOT_REMOVE_LAST_ADMIN, exception.getErrorCode());
    }

    @Test
    void execute_RemovingAdminRoleWhenMultipleAdminsExist_ShouldSucceed() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(authService.countUsersWithRole("ROLE_ADMIN")).thenReturn(2L); // Multiple admins

        // Act
        Set<String> result = removeRoleFromUser.execute(userId, "ROLE_ADMIN", adminId);

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.contains("ROLE_ADMIN"));
        verify(authService).saveUser(testUser);
        verify(authService).revokeAllTokensForUser(userId);
        verify(eventPublisher).publish(any(RoleRemovedEvent.class));
    }

    @Test
    void execute_RemovingUserRoleFromDifferentUser_ShouldSucceed() {
        // Arrange
        UUID differentUser = UUID.randomUUID();
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));

        // Act
        Set<String> result = removeRoleFromUser.execute(userId, "ROLE_USER", differentUser);

        // Assert
        assertEquals(1, result.size()); // ROLE_ADMIN remains
        assertTrue(result.contains("ROLE_ADMIN"));
        verify(authService).saveUser(testUser);
        verify(authService).revokeAllTokensForUser(userId);
        verify(eventPublisher).publish(any(RoleRemovedEvent.class));
    }

    @Test
    void execute_EventPublisherCalledWithCorrectData() {
        // Arrange
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(authService.countUsersWithRole("ROLE_ADMIN")).thenReturn(2L);

        // Act
        removeRoleFromUser.execute(userId, "ROLE_ADMIN", adminId);

        // Assert
        verify(eventPublisher).publish(argThat(event -> 
            event.userId().equals(userId) &&
            event.email().equals("test@example.com") &&
            event.username().equals("testuser") &&
            event.role().equals("ROLE_ADMIN") &&
            event.removedBy().equals(adminId) &&
            event.removedAt() != null
        ));
    }
}
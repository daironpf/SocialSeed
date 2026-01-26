package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.event.RoleAssignedEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.RoleAssignedEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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
class AssignRoleToUserTest {

    @Mock
    private AuthService authService;

    @Mock
    private RoleAssignedEventPublisher eventPublisher;

    @InjectMocks
    private AssignRoleToUser assignRoleToUser;

    private AuthUser testUser;
    private UUID userId;
    private UUID adminId;
    private static final String TEST_ROLE = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        adminId = UUID.randomUUID();
        
        Set<String> initialRoles = new HashSet<>();
        initialRoles.add("ROLE_USER");
        
        testUser = new AuthUser(userId, "testuser", "test@example.com", "password");
        testUser.setRoles(initialRoles);
    }

    @Test
    void execute_ShouldAssignRoleAndPublishEvent_WhenValidRequest() {
        // Given
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        
        // When
        Set<String> result = assignRoleToUser.execute(userId, TEST_ROLE, adminId);
        
        // Then
        assertTrue(result.contains(TEST_ROLE));
        verify(authService).saveUser(testUser);
        verify(eventPublisher).publish(any(RoleAssignedEvent.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(authService.getUserById(userId)).thenReturn(Optional.empty());
        
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> assignRoleToUser.execute(userId, TEST_ROLE, adminId));
        
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(authService, never()).saveUser(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_ShouldThrowException_WhenRoleAlreadyAssigned() {
        // Given
        testUser.getRoles().add(TEST_ROLE);
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> assignRoleToUser.execute(userId, TEST_ROLE, adminId));
        
        assertEquals(ErrorCode.ROLE_ALREADY_ASSIGNED, exception.getErrorCode());
        verify(authService, never()).saveUser(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_ShouldPublishCorrectEvent_WhenRoleAssigned() {
        // Given
        when(authService.getUserById(userId)).thenReturn(Optional.of(testUser));
        ArgumentCaptor<RoleAssignedEvent> eventCaptor = ArgumentCaptor.forClass(RoleAssignedEvent.class);
        
        // When
        assignRoleToUser.execute(userId, TEST_ROLE, adminId);
        
        // Then
        verify(eventPublisher).publish(eventCaptor.capture());
        RoleAssignedEvent publishedEvent = eventCaptor.getValue();
        
        assertEquals(userId, publishedEvent.userId());
        assertEquals(testUser.getEmail(), publishedEvent.email());
        assertEquals(testUser.getUsername(), publishedEvent.username());
        assertEquals(TEST_ROLE, publishedEvent.role());
        assertEquals(adminId, publishedEvent.assignedBy());
        assertNotNull(publishedEvent.assignedAt());
    }
}
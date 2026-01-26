package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.authservice.auth.application.usecase.AssignRoleToUser;
import com.socialseed.authservice.auth.entry.rest.dto.AssignRoleRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class RoleAssignmentController {

    private final AssignRoleToUser assignRoleToUser;

    public RoleAssignmentController(AssignRoleToUser assignRoleToUser) {
        this.assignRoleToUser = assignRoleToUser;
    }

    @PostMapping("roles/assign")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Set<String>>> assignRole(
            @Valid @RequestBody AssignRoleRequestDTO request,
            @AuthenticationPrincipal UserDetails adminUser) {

        UUID userId = UUID.fromString(request.userId());
        UUID adminId = UUID.fromString(adminUser.getUsername());

        Set<String> updatedRoles = assignRoleToUser.execute(userId, request.role(), adminId);

        ApiResponse<Set<String>> response = ApiResponse.success(updatedRoles, "auth.role.assign.success");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.authservice.auth.application.usecase.AuthUseCases; // Asegúrate de importar el UseCase correcto
import com.socialseed.authservice.auth.application.usecase.AssignRoleToUser;
import com.socialseed.authservice.auth.application.usecase.RemoveRoleFromUser;
import com.socialseed.authservice.auth.entry.rest.dto.AssignRoleRequestDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RemoveRoleRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("auth/roles") // Simplificamos la ruta base para roles
public class RoleController {

    private final AssignRoleToUser assignRoleToUser;
    private final RemoveRoleFromUser removeRoleFromUser;

    private final AuthUseCases authUseCases;
    private final MessageSource messageSource;
    private final UUID defaultAdminId;

    public RoleController(AssignRoleToUser assignRoleToUser,
            RemoveRoleFromUser removeRoleFromUser,
            AuthUseCases authUseCases,
            MessageSource messageSource,
            @Value("${security.admin.default-id:00000000-0000-0000-0000-000000000001}") UUID defaultAdminId) {
        this.assignRoleToUser = assignRoleToUser;
        this.removeRoleFromUser = removeRoleFromUser;
        this.authUseCases = authUseCases;
        this.messageSource = messageSource;
        this.defaultAdminId = defaultAdminId;
    }

    /**
     * Lista los roles de un usuario.
     * Acceso: ADMIN puede ver cualquiera, USER solo puede ver los suyos.
     * Se usa SpEL para verificar si es el dueño del ID o si es Admin.
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id.toString() == authentication.name")
    public ResponseEntity<ApiResponse<Set<String>>> getUserRoles(
            @PathVariable UUID id,
            Locale locale) {

        Set<String> roles = authUseCases.getUserRoles(id);

        String message = messageSource.getMessage("auth.roles.list.success", null, locale);
        return ResponseEntity.ok(ApiResponse.success(roles, message));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Set<String>>> assignRole(
            @Valid @RequestBody AssignRoleRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails adminUser) {

        UUID userId = UUID.fromString(request.userId());
        // Extraer el adminId del UserDetails - asumimos que el username contiene el
        // UUID
        UUID adminId;
        try {
            adminId = UUID.fromString(adminUser.getUsername());
        } catch (IllegalArgumentException e) {
            adminId = defaultAdminId;
        }

        Set<String> updatedRoles = assignRoleToUser.execute(userId, request.role(), adminId);

        return ResponseEntity.ok(ApiResponse.success(updatedRoles, "auth.role.assign.success"));
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Set<String>>> removeRole(
            @Valid @RequestBody RemoveRoleRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails adminUser) {

        UUID userId = UUID.fromString(request.userId());
        UUID adminId;
        try {
            adminId = UUID.fromString(adminUser.getUsername());
        } catch (IllegalArgumentException e) {
            adminId = defaultAdminId;
        }

        Set<String> updatedRoles = removeRoleFromUser.execute(userId, request.role(), adminId);

        return ResponseEntity.ok(ApiResponse.success(updatedRoles, "auth.role.remove.success"));

    }
}
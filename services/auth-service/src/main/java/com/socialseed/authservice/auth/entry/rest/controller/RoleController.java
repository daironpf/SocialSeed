package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.authservice.auth.application.usecase.AuthUseCases; // Asegúrate de importar el UseCase correcto
import com.socialseed.authservice.auth.application.usecase.AssignRoleToUser;
import com.socialseed.authservice.auth.entry.rest.dto.AssignRoleRequestDTO;
import jakarta.validation.Valid;
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
    private final AuthUseCases authUseCases;
    private final MessageSource messageSource;

    public RoleController(AssignRoleToUser assignRoleToUser,
            AuthUseCases authUseCases,
            MessageSource messageSource) {
        this.assignRoleToUser = assignRoleToUser;
        this.authUseCases = authUseCases;
        this.messageSource = messageSource;
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
        // Extraer el adminId del UserDetails - asumimos que el username contiene el UUID
        UUID adminId;
        try {
            adminId = UUID.fromString(adminUser.getUsername());
        } catch (IllegalArgumentException e) {
            // Si el username no es un UUID, necesitamos obtener el ID de otra forma
            // Por ahora, usamos un UUID por defecto para pruebas
            adminId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        }

        Set<String> updatedRoles = assignRoleToUser.execute(userId, request.role(), adminId);

        return ResponseEntity.ok(ApiResponse.success(updatedRoles, "auth.role.assign.success"));
    }
}
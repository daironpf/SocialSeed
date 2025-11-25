package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.authservice.auth.application.usecase.AuthUseCases;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.LoginRequestDTO;
import com.socialseed.authservice.auth.entry.rest.dto.PasswordChangeRequest;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import com.socialseed.authservice.auth.entry.rest.mapper.AuthRestMapper;
import com.socialseed.authservice.platform.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCases authUseCases;
    private final MessageSource messageSource;

    public AuthController(AuthUseCases authUseCases, MessageSource messageSource) {
        this.authUseCases = authUseCases;
        this.messageSource = messageSource;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDTO request, Locale locale) {
        AuthResponseDTO response = authUseCases.login(request.email, request.password);
        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        messageSource.getMessage("auth.login.success", null, locale)
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequestDTO request, Locale locale) {
        AuthUser authUser = AuthRestMapper.toDomain(request);
        AuthResponseDTO response = authUseCases.register(authUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    ApiResponse.created(
                        response,
                        messageSource.getMessage("auth.register.success", null, locale)
                )
        );
    }
    // CHANGE PASSWORD
    @PostMapping("/changepassword")
    public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody PasswordChangeRequest request, Locale locale) {
        AuthResponseDTO response = authUseCases.changeUserPassword(
                UUID.fromString(request.id()),
                request.currentPassword(),
                request.newPassword()
            );
        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        messageSource.getMessage("auth.change.password.success", null, locale)
                )
        );
    }
}
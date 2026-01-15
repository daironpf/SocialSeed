package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.authservice.auth.application.usecase.AuthUseCases;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.*;
import com.socialseed.authservice.auth.entry.rest.mapper.AuthRestMapper;
import com.socialseed.authservice.platform.common.response.ApiResponse;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("auth")
@Validated
public class AuthController {
        // region Dependencies
        private final AuthUseCases authUseCases;
        private final MessageSource messageSource;
        // endregion

        public AuthController(AuthUseCases authUseCases, MessageSource messageSource) {
                this.authUseCases = authUseCases;
                this.messageSource = messageSource;
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDTO request, Locale locale) {
                AuthResponseDTO response = authUseCases.login(request.email(), request.password());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                response,
                                                messageSource.getMessage("auth.login.success", null, locale)));
        }

        // region Gets
        @GetMapping("/getUserById/{id}")
        public ResponseEntity<ApiResponse<?>> getAuthUserById(@Valid @PathVariable UUID id) {
                Optional<AuthUser> authUser = authUseCases.getAuthUserById(id);
                if (authUser.isPresent()) {
                        AuthUserResponseDTO response = AuthRestMapper.toResponse(authUser.get());
                        return ResponseEntity.ok(
                                        ApiResponse.success(
                                                        response,
                                                        "AuthUser By Id"));
                }
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.message(HttpStatus.NOT_FOUND.value(), "User by ID not found"));
        }

        @GetMapping("/getUserByEmail/{email}")
        public ResponseEntity<ApiResponse<?>> getAuthUserByEmail(
                        @PathVariable @Email(message = "{email.invalid}") String email) {
                Optional<AuthUser> authUser = authUseCases.getAuthUserByEmail(email);
                if (authUser.isPresent()) {
                        AuthUserResponseDTO response = AuthRestMapper.toResponse(authUser.get());
                        return ResponseEntity.ok(
                                        ApiResponse.success(
                                                        response,
                                                        "AuthUser By Email"));
                }
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.message(HttpStatus.NOT_FOUND.value(), "User by Email not found"));
        }

        @GetMapping("/getUserByUserName/{username}")
        public ResponseEntity<ApiResponse<?>> getUserByUserName(
                        @PathVariable("username") @ValidUsername String username) {
                Optional<AuthUser> authUser = authUseCases.getUserByUserName(username);

                if (authUser.isPresent()) {
                        AuthUserResponseDTO response = AuthRestMapper.toResponse(authUser.get());
                        return ResponseEntity.ok(
                                        ApiResponse.success(
                                                        response,
                                                        "AuthUser By UserName"));
                }

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(
                                                ApiResponse.message(
                                                                HttpStatus.NOT_FOUND.value(),
                                                                "User by UserName not found"));
        }

        // endregion

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequestDTO request, Locale locale) {
                AuthUser authUser = AuthRestMapper.toDomain(request);
                AuthResponseDTO response = authUseCases.register(authUser);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                response,
                                                                messageSource.getMessage("auth.register.success", null,
                                                                                locale)));
        }

        // region Change
        @PostMapping("/changepassword")
        public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody PasswordChangeRequest request,
                        Locale locale) {
                AuthResponseDTO response = authUseCases.changeUserPassword(
                                UUID.fromString(request.id()),
                                request.currentPassword(),
                                request.newPassword());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                response,
                                                messageSource.getMessage("auth.change.password.success", null,
                                                                locale)));
        }
        // endregion

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<?>> logout(
                        @RequestHeader(value = "Authorization", required = false) String accessToken,
                        @Valid @RequestBody LogoutRequestDTO request,
                        Locale locale) {
                authUseCases.logout(accessToken, request.refreshToken());
                return ResponseEntity
                                .status(HttpStatus.NO_CONTENT)
                                .build();
        }
}
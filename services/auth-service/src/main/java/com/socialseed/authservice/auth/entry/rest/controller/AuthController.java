package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.authservice.auth.application.usecase.AuthUseCases;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.*;
import com.socialseed.authservice.auth.entry.rest.mapper.AuthRestMapper;
import com.socialseed.apiresponse.model.ApiResponse;

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
                AuthResponseDTO response = AuthRestMapper.toResponse(authUseCases.register(authUser));
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                response,
                                                                messageSource.getMessage("auth.register.success", null,
                                                                                locale)));
        }

        // region Change
        @PostMapping("/{id}/change-password")
        public ResponseEntity<ApiResponse<?>> changePassword(
                        @PathVariable UUID id,
                        @Valid @RequestBody ChangePasswordRequestDTO request,
                        Locale locale) {
                authUseCases.changeUserPassword(
                                id,
                                request.currentPassword(),
                                request.newPassword());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                null,
                                                messageSource.getMessage("auth.change.password.success", null,
                                                                locale)));
        }
        // endregion

        // region Log IN/OUT
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(
                        @Valid @RequestBody LoginRequestDTO request,
                        jakarta.servlet.http.HttpServletRequest httpRequest,
                        Locale locale) {
                String ip = httpRequest.getRemoteAddr();
                AuthResponseDTO response = AuthRestMapper.toResponse(authUseCases.login(request.email(), request.password(), ip));
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                response,
                                                messageSource.getMessage("auth.login.success", null, locale)));
        }

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

    @PostMapping("/token/refresh")
        public ResponseEntity<ApiResponse<?>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request,
                        Locale locale) {
                AuthResponseDTO response = AuthRestMapper.toResponse(authUseCases.refreshToken(request.refreshToken()));
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                response,
                                                messageSource.getMessage("auth.token.refresh.success", null, locale)));
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request,
                                                             Locale locale) {
            authUseCases.forgotPassword(request.email());
            return ResponseEntity.ok(
                    ApiResponse.success(
                            null,
                            messageSource.getMessage("auth.forgot.password.success", null, locale)));
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request,
                                                            Locale locale) {
            authUseCases.resetPassword(request.token(), request.newPassword());
            return ResponseEntity.ok(
                    ApiResponse.success(
                            null,
                            messageSource.getMessage("auth.reset.password.success", null, locale)));
        }
        // endregion

        @PostMapping("/verify-email")
        public ResponseEntity<ApiResponse<?>> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO request,
                                                           Locale locale) {
            authUseCases.verifyEmail(request.token());
            return ResponseEntity.ok(
                    ApiResponse.success(
                            null,
                            messageSource.getMessage("auth.verify.email.success", null, locale)));
        }

        @PostMapping("/resend-verification")
        public ResponseEntity<ApiResponse<?>> resendVerificationEmail(@Valid @RequestBody ResendVerificationEmailRequestDTO request,
                                                                       Locale locale) {
            authUseCases.resendVerificationEmail(request.email());
            return ResponseEntity.ok(
                    ApiResponse.success(
                            null,
                            messageSource.getMessage("auth.resend.verification.success", null, locale)));
        }
}

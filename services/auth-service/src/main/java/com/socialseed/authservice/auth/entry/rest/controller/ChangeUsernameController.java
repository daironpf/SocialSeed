package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.authservice.auth.application.usecase.ChangeUsernameUseCase;
import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.ChangeUsernameRequestDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class ChangeUsernameController {
  
  private static final Logger log = LoggerFactory.getLogger(ChangeUsernameController.class);

  private final ChangeUsernameUseCase changeUsernameUseCase;
  private final AuthService authService;
  private final JWTProvider jwtProvider;

  public ChangeUsernameController(ChangeUsernameUseCase changeUsernameUseCase, AuthService authService, JWTProvider jwtProvider) {
    this.changeUsernameUseCase = changeUsernameUseCase;
    this.authService = authService;
    this.jwtProvider = jwtProvider;
  }

  @PatchMapping("/username")
  public ResponseEntity<ApiResponse<Void>> changeUsername(
      @Valid @RequestBody ChangeUsernameRequestDTO request,
      @RequestHeader(value = "Authorization", required = false) String authHeader) {
    
    log.info("ChangeUsernameController - PATCH /auth/username called");
    log.info("Auth header: {}", authHeader);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn("No auth header, returning 401");
      return ResponseEntity.status(401).body(ApiResponse.message(401, "Authorization header required"));
    }

    String token = authHeader.substring(7);
    if (!jwtProvider.validateToken(token)) {
      log.warn("Invalid token, returning 401");
      return ResponseEntity.status(401).body(ApiResponse.message(401, "Invalid token"));
    }

    String username = jwtProvider.getUsernameFromToken(token);
    log.info("Username from token: {}", username);
    
    AuthUser currentUser = authService.getUserByUserName(username)
        .orElseThrow(() -> new com.socialseed.errorhandling.exception.BusinessException(
            com.socialseed.errorhandling.exception.ErrorCode.USER_NOT_FOUND));

    changeUsernameUseCase.execute(currentUser.getId(), request.newUsername());

    return ResponseEntity.ok(ApiResponse.message(200, ApiResponse.msg("auth.username.change.success")));
  }
}

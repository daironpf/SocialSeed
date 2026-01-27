package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.authservice.auth.application.usecase.ChangeUsernameUseCase;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.ChangeUsernameRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class ChangeUsernameController {

  private final ChangeUsernameUseCase changeUsernameUseCase;
  private final AuthService authService;

  public ChangeUsernameController(ChangeUsernameUseCase changeUsernameUseCase, AuthService authService) {
    this.changeUsernameUseCase = changeUsernameUseCase;
    this.authService = authService;
  }

  @PatchMapping("/username")
  public ResponseEntity<ApiResponse<Void>> changeUsername(
      @Valid @RequestBody ChangeUsernameRequestDTO request,
      org.springframework.security.core.Authentication authentication) {

    String currentUsername = authentication.getName();
    AuthUser currentUser = authService.getUserByUserName(currentUsername)
        .orElseThrow(() -> new com.socialseed.errorhandling.exception.BusinessException(
            com.socialseed.errorhandling.exception.ErrorCode.USER_NOT_FOUND));

    changeUsernameUseCase.execute(currentUser.getId(), request.newUsername());

    return ResponseEntity.ok(ApiResponse.message(200, ApiResponse.msg("auth.username.change.success")));
  }
}

package com.socialseed.socialuserservice.user.entry.rest.controller;
import com.socialseed.socialuserservice.user.application.usecase.UserUseCases;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UserUpdateRequestDTO;
import com.socialseed.socialuserservice.user.entry.rest.mapper.UserRestMapper;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UserCreateRequestDTO;
import com.socialseed.socialuserservice.user.entry.rest.dto.response.UserResponseDTO;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("socialusers")
public class UserController {
    private final UserUseCases userUseCases;

    public UserController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        User user = UserRestMapper.toDomain(dto);
        User saved = userUseCases.createUser().execute(user);
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return userUseCases.getUserById().execute(id)
                .map(user -> ResponseEntity.ok(UserRestMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userUseCases.getAllUsers().execute();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        List<UserResponseDTO> dtos = users.stream()
                .map(UserRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateRequestDTO request) {
        User updated = UserRestMapper.UpdatetoDomain(request);
        userUseCases.updateUser().execute(id, updated);
        return ResponseEntity.noContent().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userUseCases.deleteUser().execute(id);
        return ResponseEntity.noContent().build();
    }

    // CHANGE PASSWORD
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id,
                                               @RequestParam String currentPassword,
                                               @RequestParam String newPassword) {
        userUseCases.changeUserPassword().execute(id, currentPassword, newPassword);
        return ResponseEntity.noContent().build();
    }
}
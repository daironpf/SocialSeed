package com.socialseed.socialuserservice.user.entry.rest.controller;
import com.socialseed.socialuserservice.platform.common.response.ResponseDTO;
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
    //region Dependencies
    private final UserUseCases userUseCases;

    public UserController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }
    //endregion

    // region Gets
    // LIST
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userUseCases.getAllUsers();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        List<UserResponseDTO> dtos = users.stream()
                .map(UserRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    /**
     * Retrieve a Social User by UserName.
     *
     * @param userName The userName of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @GetMapping("/getSocialUserByUserName/{userName}")
//    public ResponseEntity<ResponseDTO> getSocialUserByUserName(@PathVariable String userName) {
//        ResponseEntity<Object> response = socialUserService.getSocialUserByUserName(userName);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }

    /**
     * Retrieve a Social User by Email.
     *
     * @param email The Email of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @GetMapping("/getSocialUserByEmail/{email}")
//    public ResponseEntity<ResponseDTO> getSocialUserByEmail(@PathVariable String email) {
//        ResponseEntity<Object> response = socialUserService.getSocialUserByEmail(email);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }
    //endregion

    //region CRUD
    // CREATE
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        User user = UserRestMapper.toDomain(dto);
        User saved = userUseCases.createUser(user);
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    // GET BY ID
    /**
     * Retrieve a Social User by ID.
     *
     * @param id The ID of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return userUseCases.getUserById(id)
                .map(user -> ResponseEntity.ok(UserRestMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    /**
     * Update an existing Social User.
     *
     * @param id     The ID of the user making the request.
     * @param request The updated Social User object.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UserUpdateRequestDTO request) {
        User updated = UserRestMapper.UpdatetoDomain(request);
        userUseCases.updateUser(id, updated);
        return ResponseEntity.noContent().build();
    }

    // DELETE
    /**
     * Delete a Social User by ID.
     *
     * @param id     The ID of the Social User to delete.
     * @return ResponseEntity with a ResponseDTO.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userUseCases.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region VacationMode
    /**
     * Activate vacation mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @PostMapping("/activateVacationMode")
//    public ResponseEntity<ResponseDTO> activateVacationMode(
//            @RequestHeader("userId") String idUserRequest) {
//        ResponseEntity<Object> response = socialUserService.activateVacationMode(idUserRequest);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }

    /**
     * Deactivate vacation mode for a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @PostMapping("/deactivateVacationMode")
//    public ResponseEntity<ResponseDTO> deactivateVacationMode(
//            @RequestHeader("userId") String idUserRequest) {
//        ResponseEntity<Object> response = socialUserService.deactivateVacationMode(idUserRequest);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }
    //endregion

    //region Activate
    /**
     * Activate a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @PostMapping("/activateSocialUser")
//    public ResponseEntity<ResponseDTO> activateSocialUser(
//            @RequestHeader("userId") String idUserRequest) {
//        ResponseEntity<Object> response = socialUserService.activateSocialUser(idUserRequest);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }

    /**
     * Deactivate a Social User.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with a ResponseDTO.
     */
//    @PostMapping("/deactivateSocialUser")
//    public ResponseEntity<ResponseDTO> deactivateSocialUser(
//            @RequestHeader("userId") String idUserRequest) {
//        ResponseEntity<Object> response = socialUserService.deactivateSocialUser(idUserRequest);
//        return ResponseEntity
//                .status(response.getStatusCode())
//                .body((ResponseDTO) response.getBody());
//    }
    //endregion
}
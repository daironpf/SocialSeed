package com.socialseed.socialuserservice.user.entry.rest.controller;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.socialuserservice.user.application.usecase.UserUseCases;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.StartVacationRequestDTO;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UpdateUserProfileDTO;
import com.socialseed.socialuserservice.user.entry.rest.mapper.UserRestMapper;
import com.socialseed.socialuserservice.user.entry.rest.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        List<User> users = userUseCases.getAllUsers();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        List<UserResponseDTO> response = users.stream()
                .map(UserRestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Retrieve a Social User by ID.
     *
     * @param id The ID of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @Valid @PathVariable UUID id
    ) {
        User user = userUseCases.getUserById(id).get();
                //.orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        return ResponseEntity.ok(
                ApiResponse.success(UserRestMapper.toResponse(user))
        );
    }


    /**
     * Retrieve a Social User by UserName.
     *
     * @param userName The userName of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserByUserName/{userName}")
    public ResponseEntity<ApiResponse<?>> getSocialUserByUserName(@PathVariable String userName) {
        Optional<User> user = userUseCases.getUserByName(userName);
        if (user.isPresent()){
            UserResponseDTO response =  UserRestMapper.toResponse(user.get());
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.ok(ApiResponse.success(null));
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ApiResponse.message(HttpStatus.NOT_FOUND.value(), "User by Username not found"));

    }

    /**
     * Retrieve a Social User by Email.
     *
     * @param email The Email of the Social User to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getSocialUserByEmail/{email}")
    public ResponseEntity<ApiResponse<?>> getSocialUserByEmail(@PathVariable String email) {
        Optional<User> user = userUseCases.getUserByEmail(email);
        if (user.isPresent()){
            UserResponseDTO response =  UserRestMapper.toResponse(user.get());
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.ok(ApiResponse.success(null));
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ApiResponse.message(HttpStatus.NOT_FOUND.value(), "User by Email not found"));

    }
    //endregion

    //region CRUD
    // CREATE this only take action in admin mode, then now will be commented out
//     @PostMapping
//     public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
//         User user = UserRestMapper.toDomain(dto);
//         User saved = userUseCases.createUser(user);
//         return ResponseEntity.ok(UserRestMapper.toResponse(saved));
//     }

    // UPDATE
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @Valid @RequestBody UpdateUserProfileDTO request
    ) {
        userUseCases.updateUserProfile(request);

        return ResponseEntity.ok(ApiResponse.success("User Profile updated successful"));
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

    // VACATIONS
    @PostMapping("/vacation/start")
    public ResponseEntity<ApiResponse<?>> startVacation(
            @Valid @RequestBody StartVacationRequestDTO request
    ) {
        userUseCases.startVacation(request);
        return ResponseEntity.ok(ApiResponse.success("User is now on vacation"));
    }

    @PostMapping("/vacation/end/{id}")
    public ResponseEntity<ApiResponse<?>> endVacation(
            @PathVariable UUID id
    ) {
        userUseCases.endVacation(id);
        return ResponseEntity.ok(ApiResponse.success("User has returned from vacation"));
    }
    //endregion
}
package com.social.seed.controller;

import com.social.seed.model.SocialUser;
import com.social.seed.service.SocialUserService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/socialUser")
public class SocialUserController {
    @Autowired
    SocialUserService socialUserService;

    //region CRUD
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<ResponseDTO> getSocialUserById(@PathVariable String id) {

        ResponseEntity<Object> response = socialUserService.getSocialUserById(id);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, response.getBody(), "Successful");
            case NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/createSocialUser")
    public ResponseEntity<ResponseDTO> createSocialUser(@RequestBody SocialUser socialUser){

        ResponseEntity<Object> responseCreate = socialUserService.createNewSocialUser(socialUser);
        HttpStatus status = (HttpStatus) responseCreate.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case CREATED -> new ResponseDTO(status, responseCreate.getBody(),"The User was Created");
            case CONFLICT -> new ResponseDTO(status, "Error", (String) responseCreate.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PutMapping("/updateSocialUser")
    public ResponseEntity<ResponseDTO> updateSocialUser(
            @RequestHeader("userId") String userId,
            @RequestBody SocialUser socialUser){

        ResponseEntity<Object> response = socialUserService.updateSocialUser(userId, socialUser);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, response.getBody(), String.format("The User with the id [ %s ] was Updated", socialUser.getId()));
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @DeleteMapping("/deleteSocialUser/{id}")
    public ResponseEntity<ResponseDTO> deleteSocialUser(
            @RequestHeader("userId") String userId,
            @PathVariable String id){

        ResponseEntity<Object> response = socialUserService.deleteSocialUser(userId, id);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", String.format("The User with the id [ %s ] was deleted", id));
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion

    //region Update Special Props
    @PostMapping("/updateSocialUserName/{idUserToUpdate}")
    public ResponseEntity<ResponseDTO> updateSocialUserName(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUpdate,
            @RequestParam String newUserName){

        ResponseEntity<Object> response = socialUserService.updateSocialUserName(idUserRequest, idUserToUpdate, newUserName);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, response.getBody(), String.format("The User Name was changed to [ %s ]", newUserName));
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/updateSocialUserEmail/{idUserToUpdate}")
    public ResponseEntity<ResponseDTO> updateSocialUserEmail(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUpdate,
            @RequestParam String newEmail){

        ResponseEntity<Object> response = socialUserService.updateSocialUserEmail(idUserRequest, idUserToUpdate, newEmail);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, response.getBody(), String.format("The Email was changed to [ %s ]", newEmail));
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion

    //region FOLLOWED_BY
    @PostMapping("/follow/{idUserToFollow}")
    public ResponseEntity<ResponseDTO> followSocialUser(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToFollow){

        ResponseEntity<Object> response = socialUserService.followSocialUser(idUserRequest, idUserToFollow);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/unfollow/{idUserToUnFollow}")
    public ResponseEntity<ResponseDTO> unfollowSocialUser(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToUnFollow){

        ResponseEntity<Object> response = socialUserService.unfollowSocialUser(idUserRequest, idUserToUnFollow);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion

    //region Vacation Mode
    @PostMapping("/activateVacationMode")
    public ResponseEntity<ResponseDTO> activateVacationMode(
            @RequestHeader("userId") String idUserRequest){

        ResponseEntity<Object> response = socialUserService.activateVacationMode(idUserRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/deactivateVacationMode")
    public ResponseEntity<ResponseDTO> deactivateVacationMode(
            @RequestHeader("userId") String idUserRequest){

        ResponseEntity<Object> response = socialUserService.deactivateVacationMode(idUserRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion

    //region Activated Mode
    @PostMapping("/activateSocialUser")
    public ResponseEntity<ResponseDTO> activateSocialUser(
            @RequestHeader("userId") String idUserRequest){

        ResponseEntity<Object> response = socialUserService.activateSocialUser(idUserRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/deactivateSocialUser")
    public ResponseEntity<ResponseDTO> deactivateSocialUser(
            @RequestHeader("userId") String idUserRequest){

        ResponseEntity<Object> response = socialUserService.deactivateSocialUser(idUserRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion
}
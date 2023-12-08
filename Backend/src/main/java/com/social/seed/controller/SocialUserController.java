package com.social.seed.controller;

import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import com.social.seed.service.SocialUserService;
import com.social.seed.util.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/socialUser")
public class SocialUserController {
    @Autowired
    SocialUserService socialUserService;

    //region crud
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<ResponseDTO> getSocialUserById(@PathVariable String id) {

        return ResponseEntity.status(OK)
                .body(socialUserService.getSocialUserById(id)
                        .map(user -> new ResponseDTO(OK, user, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", String.format("The User with the id [ %s ] was not found", id))));
    }


    @PostMapping("/createSocialUser")
    public ResponseEntity<ResponseDTO> createSocialUser(@RequestBody SocialUser socialUser){

        ResponseEntity<Object> responseCreate = socialUserService.createNewSocialUser(socialUser);
        HttpStatus status = (HttpStatus) responseCreate.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case CREATED -> new ResponseDTO(status, responseCreate.getBody(), String.format("The User with the email [ %s ] was Created", socialUser.getEmail()));
            case CONFLICT -> new ResponseDTO(status, "Error", (String) responseCreate.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PutMapping("/updateSocialUser")
    public ResponseEntity<ResponseDTO> updateSocialUser(
            @RequestHeader("userId") String userId,
            @RequestBody SocialUser socialUser){

        ResponseEntity<SocialUser> userResponseEntity = socialUserService.updateSocialUser(userId, socialUser);
        HttpStatus status = (HttpStatus) userResponseEntity.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, userResponseEntity.getBody(), String.format("The User with the id [ %s ] was Updated", socialUser.getId()));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The User with the id [ %s ] was not found", socialUser.getId()));
            case CONFLICT -> new ResponseDTO(status, "Error", String.format("The User with the id [ %s ] is not the same to modify", userId));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @DeleteMapping("/deleteSocialUser/{id}")
    public ResponseEntity<ResponseDTO> deleteSocialUser(
            @RequestHeader("userId") String userId,
            @PathVariable String id){

        HttpStatus status = socialUserService.deleteSocialUser(userId, id);

        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", String.format("The User with the id [ %s ] was deleted", id));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The User with the id [ %s ] was not found", id));
            case CONFLICT -> new ResponseDTO(status, "Error", String.format("The User with the id [ %s ] is not the same to modify", userId));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion
}
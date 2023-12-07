package com.social.seed.controller;

import com.social.seed.model.SocialUser;
import com.social.seed.service.SocialUserService;
import com.social.seed.util.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/socialUser")
public class SocialUserController {
    @Autowired
    SocialUserService socialUserService;

    //region crud
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<ResponseDTO> getSocialUserById(@PathVariable String id) {

        ResponseEntity<SocialUser> socialUserResponse = socialUserService.getSocialUserById(id);

        HttpStatus httpStatus = (HttpStatus) socialUserResponse.getStatusCode();
        Object response = httpStatus == HttpStatus.OK ? socialUserResponse.getBody() : String.format("The User with the id [ %s ] was not found", id);
        String message = httpStatus == HttpStatus.OK ? "Successfull" : "Error";

        ResponseDTO responseDTO = new ResponseDTO(
                httpStatus,
                response,
                message
        );

        return ResponseEntity.status(httpStatus).body(responseDTO);
    }


    @PostMapping("/createSocialUser")
    public ResponseEntity<ResponseDTO> createSocialUser(@RequestBody SocialUser socialUser){

        ResponseEntity<String> responseCreate = socialUserService.createNewSocialUser(socialUser);

        String message = responseCreate.getStatusCode() == HttpStatus.CREATED ? "Successfull" : "Error";

        ResponseDTO responseDTO = new ResponseDTO(
                (HttpStatus) responseCreate.getStatusCode(),
                responseCreate.getBody(),
                message
        );

        return ResponseEntity.status(responseCreate.getStatusCode()).body(responseDTO);
    }

    @DeleteMapping("/deleteSocialUser/{id}")
    public ResponseEntity<ResponseDTO> deleteSocialUser(
            HttpServletRequest request,
            @PathVariable String id){

        HttpStatus responseDeleted = socialUserService.deleteSocialUser(
                request.getHeader("userId"),
                id);

        String message = responseDeleted == HttpStatus.OK ? "Successfull" : "Error";

        String response = switch (responseDeleted) {
            case OK -> String.format("The User with the id [ %s ] was deleted", id);
            case NOT_FOUND -> String.format("The User with the id [ %s ] was not found", id);
            case CONFLICT ->
                    String.format("The User with the id [ %s ] is not the same to modify", request.getHeader("userId"));
            default -> "Unexpected response";
        };

        ResponseDTO responseDTO = new ResponseDTO(
                responseDeleted,
                response,
                message
        );

        return ResponseEntity.status(responseDeleted).body(responseDTO);
    }

    @PutMapping("/updateSocialUser")
    public ResponseEntity<ResponseDTO> updateSocialUser(
            HttpServletRequest request,
            @RequestBody SocialUser socialUser){

        HttpStatus updateResponse = socialUserService.updateSocialUser(
                request.getHeader("userId"),
                socialUser);

        String message = updateResponse == HttpStatus.OK ? "Successfull" : "Error";

        String response = switch (updateResponse) {
            case OK -> String.format("The User with the id [ %s ] was Updated", socialUser.getId());
            case NOT_FOUND -> String.format("The User with the id [ %s ] was not found", socialUser.getId());
            case CONFLICT ->
                    String.format("The User with the id [ %s ] is not the same to modify", request.getHeader("userId"));
            default -> "Unexpected response";
        };

        ResponseDTO responseDTO = new ResponseDTO(
                updateResponse,
                response,
                message
        );

        return ResponseEntity.status(updateResponse).body(responseDTO);
    }
    //endregion
}

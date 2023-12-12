package com.social.seed.controller;

import com.social.seed.service.FriendsService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
public class FriendsController {
    @Autowired
    FriendsService friendsService;

    //request friendship
    @PostMapping("/createRequest/{idUserToBeFriend}")
    public ResponseEntity<ResponseDTO> createRequestFriendship(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToBeFriend){

        ResponseEntity<Object> response = friendsService.createRequestFriendship(idUserRequest, idUserToBeFriend);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //cancel friendship
    @PostMapping("/cancelRequest/{idUserToCancelFriendRequest}")
    public ResponseEntity<ResponseDTO> cancelRequestFriendship(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToCancelFriendRequest){

        ResponseEntity<Object> response = friendsService.cancelRequestFriendship(idUserRequest, idUserToCancelFriendRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //accept friendship
    @PostMapping("/acceptedRequest/{idUserToAcceptedFriendRequest}")
    public ResponseEntity<ResponseDTO> acceptedRequestFriendship(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToAcceptedFriendRequest){

        ResponseEntity<Object> response = friendsService.acceptedRequestFriendship(idUserRequest, idUserToAcceptedFriendRequest);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //deleted friendship
    @PostMapping("/deleteFriendship/{idUserToDeleteFriendship}")
    public ResponseEntity<ResponseDTO> deleteFriendship(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idUserToDeleteFriendship){

        ResponseEntity<Object> response = friendsService.deleteFriendship(idUserRequest, idUserToDeleteFriendship);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND,FORBIDDEN -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
}
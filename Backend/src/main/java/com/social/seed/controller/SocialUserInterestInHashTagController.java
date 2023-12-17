package com.social.seed.controller;

import com.social.seed.service.SocialUserInterestInHashTagService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle operations related to Social User interests in Hash Tags.
 */
@RestController
@RequestMapping("/api/v0.0.1/interestsInHashTagBySocialUser")
public class SocialUserInterestInHashTagController {

    //region Dependencies
    @Autowired
    private SocialUserInterestInHashTagService socialUserInterestInHashTagService;
    //endregion

    /**
     * Adds an interest in a Hash Tag for a specific user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idHashTag     The ID of the Hash Tag to add interest in.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/addInterest/{idHashTag}")
    public ResponseEntity<ResponseDTO> addInterest(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idHashTag) {

        ResponseEntity<Object> response = socialUserInterestInHashTagService.addInterest(idUserRequest, idHashTag);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deletes an interest in a Hash Tag for a specific user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idHashTag     The ID of the Hash Tag to delete interest from.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/deleteInterest/{idHashTag}")
    public ResponseEntity<ResponseDTO> deleteInterest(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idHashTag) {

        ResponseEntity<Object> response = socialUserInterestInHashTagService.deleteInterest(idUserRequest, idHashTag);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
}
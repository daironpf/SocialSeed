package com.social.seed.controller;

import com.social.seed.service.SocialUserInterestInHashTagService;
import com.social.seed.util.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle operations related to Social User interests in Hash Tags.
 */
@RestController
@Tag(name = "Interest In Hashtag", description = "Interest In Hashtag Module")
@RequestMapping("/api/v0.0.1/interestsInHashTagBySocialUser")
public class SocialUserInterestInHashTagController {

    //region Dependencies
    private final SocialUserInterestInHashTagService socialUserInterestInHashTagService;

    @Autowired
    public SocialUserInterestInHashTagController(SocialUserInterestInHashTagService socialUserInterestInHashTagService) {
        this.socialUserInterestInHashTagService = socialUserInterestInHashTagService;
    }

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

    /**
     * Retrieve a Data of Interest for HashTag by SocialUser ID.
     *
     * @param userId The ID of the Social User to find.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getInterestBySocialUserId/{userId}")
    public ResponseEntity<ResponseDTO> getInterestBySocialUserId(@PathVariable String userId) {
        ResponseEntity<Object> response = socialUserInterestInHashTagService.getInterestBySocialUserId(userId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
}
package com.social.seed.service;

import com.social.seed.repository.SocialUserInterestInHashTagRepository;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service class responsible for managing interests of Social Users in Hash Tags.
 */
@Service
public class SocialUserInterestInHashTagService {

    //region Dependencies
    @Autowired
    private SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository;
    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ResponseService responseService;
    //endregion

    /**
     * Adds an interest in a Hash Tag for a specific user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idHashTag     The ID of the Hash Tag to add interest in.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> addInterest(String idUserRequest, String idHashTag) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.hashTagExistsById(idHashTag)) return responseService.hashTagNotFoundResponse(idHashTag);
        if (validationService.existsInterest(idUserRequest, idHashTag)) return responseService.conflictResponseWithMessage("The Interest already exists");

        // Add the interest and timestamp
        socialUserInterestInHashTagRepository.addInterest(
                idUserRequest,
                idHashTag,
                LocalDateTime.now()
        );

        return responseService.successResponse(socialUserRepository.findById(idUserRequest).get(), "Successful");
    }

    /**
     * Deletes an interest in a Hash Tag for a specific user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idHashTag     The ID of the Hash Tag to delete interest from.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> deleteInterest(String idUserRequest, String idHashTag) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.hashTagExistsById(idHashTag)) return responseService.hashTagNotFoundResponse(idHashTag);
        if (!validationService.existsInterest(idUserRequest, idHashTag)) return responseService.conflictResponseWithMessage("The Interest does not exist");

        // Delete the interest
        socialUserInterestInHashTagRepository.deleteInterest(idUserRequest, idHashTag);

        return responseService.successResponse(socialUserRepository.findById(idUserRequest).get(), "Successful");
    }
}

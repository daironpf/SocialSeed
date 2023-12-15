package com.social.seed.service;

import com.social.seed.repository.SocialUserInterestInHashTagRepository;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SocialUserInterestInHashTagService {
    //region dependencies
    @Autowired
    SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository;
    @Autowired
    SocialUserRepository socialUserRepository;
    @Autowired
    ValidationService validationService;
    @Autowired
    ResponseService responseService;
    //endregion

    public ResponseEntity<Object> addInterest(String idUserRequest, String idHashTag) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.hashTagExistsById(idHashTag)) return responseService.postNotFoundResponse(idHashTag);
        if (validationService.existsInterest(idUserRequest, idHashTag)) return responseService.conflictResponseWithMessage("The Interest already Exist.");

        socialUserInterestInHashTagRepository.addInterest(
                idUserRequest,
                idHashTag,
                LocalDateTime.now()
        );

        return responseService.successResponse(socialUserRepository.findById(idUserRequest).get());
    }

    public ResponseEntity<Object> deleteInterest(String idUserRequest, String idHashTag) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.hashTagExistsById(idHashTag)) return responseService.postNotFoundResponse(idHashTag);
        if (!validationService.existsInterest(idUserRequest, idHashTag)) return responseService.conflictResponseWithMessage("The Interest does not Exist.");

        socialUserInterestInHashTagRepository.deleteInterest(idUserRequest, idHashTag);

        return responseService.successResponse(socialUserRepository.findById(idUserRequest).get());
    }
}

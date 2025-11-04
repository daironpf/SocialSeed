/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserInterestInHashTagRepository;
import com.social.seed.repository.SocialUserRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing interests of Social Users in Hash Tags.
 */
@Service
public class SocialUserInterestInHashTagService {

    //region Dependencies
    private final SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository;
    private final SocialUserRepository socialUserRepository;
    private final ResponseService responseService;

    @Autowired
    public SocialUserInterestInHashTagService(SocialUserInterestInHashTagRepository socialUserInterestInHashTagRepository, SocialUserRepository socialUserRepository, ResponseService responseService) {
        this.socialUserInterestInHashTagRepository = socialUserInterestInHashTagRepository;
        this.socialUserRepository = socialUserRepository;
        this.responseService = responseService;
    }
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
        // Add the interest and timestamp
        socialUserInterestInHashTagRepository.addInterest(
                idUserRequest,
                idHashTag,
                LocalDateTime.now()
        );

        SocialUser response = socialUserRepository.findById(idUserRequest).get();
        return responseService.successResponse(response);
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
        // Delete the interest
        socialUserInterestInHashTagRepository.deleteInterest(idUserRequest, idHashTag);

        return responseService.successResponse(socialUserRepository.findById(idUserRequest).get());
    }

    /**
     * Get interests in a Hash Tag for a specific user.
     *
     * @param idUserRequest The ID of the user making the request.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getInterestBySocialUserId(String idUserRequest) {
        Optional<List<HashTag>> reponse = socialUserInterestInHashTagRepository.getInterestBySocialUserId(idUserRequest);

        return responseService.successResponse(reponse.get());
    }
}

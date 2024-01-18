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
import com.social.seed.repository.HashTagRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/**
 * Service class for the {@link HashTag}, focusing on operations related to managing
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2023-12-08
 */
@Service
public class HashTagService {
    //region dependencies
    private final HashTagRepository hashTagRepository;
    private final ResponseService responseService;

    @Autowired
    public HashTagService(HashTagRepository hashTagRepository, ResponseService responseService) {
        this.hashTagRepository = hashTagRepository;
        this.responseService = responseService;
    }
    //endregion

    //region Gets
    public ResponseEntity<Object> getAllHashTag(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HashTag> hashTags = hashTagRepository.findAll(pageable);

        if (hashTags.isEmpty()) return responseService.notFoundWithMessageResponse("No posts available.");
        return responseService.successResponse(hashTags);
    }
    //endregion

    //region CRUD
    public ResponseEntity<Object> getHashTagById(String id) {
        return responseService.successResponse(hashTagRepository.findById(id));
    }

    @Transactional
    public ResponseEntity<Object> createNewHashTag(HashTag newHashTag) {

        HashTag savedNewHashTag = hashTagRepository.save(
                HashTag.builder()
                        .name(newHashTag.getName())
                        .socialUserInterestIn(0)
                        .postTaggedIn(0)
                        .build()
        );

        return responseService.successCreatedResponse(savedNewHashTag);
    }

    public ResponseEntity<Object> updateHashTag(HashTag updatedHashTag) {

        hashTagRepository.update(updatedHashTag.getId(), updatedHashTag.getName());

        Optional<HashTag> hashTag = hashTagRepository.findById(updatedHashTag.getId());
        return hashTag.map(tag -> responseService.successResponseWithMessage(tag, "Updated")).orElse(null);
    }

    @Transactional
    public ResponseEntity<Object> deleteHashTag(String id) {

        hashTagRepository.deleteById(id);

        return responseService.successResponseWithMessage("The HashTag was Deleted.","Successful");
    }
    //endregion
}
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
package com.social.seed.controller;

import com.social.seed.model.HashTag;
import com.social.seed.service.HashTagService;
import com.social.seed.util.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "HashTag", description = "HashTag Module")
@RequestMapping("/api/v0.0.1/hashTag")
public class HashTagController {
    //region Dependencies
    private final HashTagService hashTagService;

    @Autowired
    public HashTagController(HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }
    //endregion

    //region Gets
    /**
     * Retrieves all hashtags with pagination support.
     *
     * @param page The page number (default is 0).
     * @param size The number of items per page (default is 10).
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getAllHashTag")
    public ResponseEntity<ResponseDTO> getAllHashTag(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Object> response = hashTagService.getAllHashTag(page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region CRUD
    /**
     * Retrieves a hashtag by its ID.
     *
     * @param id The ID of the hashtag.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getHashTagById/{id}")
    public ResponseEntity<ResponseDTO> getHashTagById(@PathVariable String id) {
        ResponseEntity<Object> response = hashTagService.getHashTagById(id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Creates a new hashtag.
     *
     * @param newHashTag The hashtag to be created.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/createHashTag")
    public ResponseEntity<ResponseDTO> createHashTag(@RequestBody HashTag newHashTag) {
        ResponseEntity<Object> response = hashTagService.createNewHashTag(newHashTag);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Updates an existing hashtag.
     *
     * @param updateHashTag The updated hashtag.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PutMapping("/updateHashTag")
    public ResponseEntity<ResponseDTO> updateHashTag(@RequestBody HashTag updateHashTag) {
        ResponseEntity<Object> response = hashTagService.updateHashTag(updateHashTag);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deletes a hashtag by its ID.
     *
     * @param id The ID of the hashtag to be deleted.
     * @return ResponseEntity with a ResponseDTO.
     */
    @DeleteMapping("/deleteHashTag/{id}")
    public ResponseEntity<ResponseDTO> deleteHashTag(@PathVariable String id) {
        ResponseEntity<Object> response = hashTagService.deleteHashTag(id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion
}
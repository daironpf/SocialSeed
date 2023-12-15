package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HashTagService {
    //region dependencies
    @Autowired
    HashTagRepository hashTagRepository;
    @Autowired
    ResponseService responseService;
    @Autowired
    ValidationService validationService;
    //endregion

    //region CRUD
    public ResponseEntity<Object> getHashTagById(String id) {
        if (!validationService.hashTagExistsById(id)) return responseService.hashTagNotFoundResponse(id);
        return responseService.successResponse(hashTagRepository.findById(id).get());
    }

    @Transactional
    public ResponseEntity<Object> createNewHashTag(HashTag newHashTag) {
        if (validationService.hashTagExistsByName(newHashTag.getName())) return responseService.conflictResponseWithMessage(String.format("The HashTag with the name [ %s ] already exists ", newHashTag.getName()));

        HashTag savedNewHashTag = hashTagRepository.save(
                    HashTag.builder()
                            .name(newHashTag.getName())
                            .socialUserInterestIn(0)
                            .postTaggedIn(0)
                            .build()
        );
        return responseService.successCreatedResponse(savedNewHashTag);
    }

    public ResponseEntity<Object> updateHashTag(HashTag updateHashTag) {
        if (!validationService.hashTagExistsById(updateHashTag.getId())) return responseService.hashTagNotFoundResponse(updateHashTag.getId());
        if (validationService.hashTagExistsByName(updateHashTag.getName())) return responseService.conflictResponseWithMessage("The HashTag with the name [ %s ] already exists");

        hashTagRepository.update(updateHashTag.getId(), updateHashTag.getName());
        HashTag savedHashTag = hashTagRepository.findById(updateHashTag.getId()).get();
        return responseService.successResponse(savedHashTag);
    }

    @Transactional
    public ResponseEntity<Object> deleteHashTag(String id) {
        if (!validationService.hashTagExistsById(id)) return responseService.hashTagNotFoundResponse(id);

        hashTagRepository.deleteById(id);
        return responseService.successResponse(null);
    }
    //endregion

    //region Gets
    public Optional<Object> getAllHashTag(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(hashTagRepository.findAll(pageable));
    }
    //endregion
}

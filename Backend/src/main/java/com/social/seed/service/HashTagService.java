package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //region Gets
    public ResponseEntity<Object> getAllHashTag(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HashTag> hashTags = hashTagRepository.findAll(pageable);

        if (hashTags.isEmpty()) return responseService.NotFoundWithMessageResponse("No posts available.");
        return responseService.successResponse(hashTags, "Successful");
    }
    //endregion

    //region CRUD
    public ResponseEntity<Object> getHashTagById(String id) {
        if (!validationService.hashTagExistsById(id)) return responseService.hashTagNotFoundResponse(id);
        return responseService.successResponse(hashTagRepository.findById(id).get(), "Successful");
    }

    @Transactional
    public ResponseEntity<Object> createNewHashTag(HashTag newHashTag) {
        if (validationService.hashTagExistsByName(newHashTag.getName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The HashTag with the name [ %s ] already exists", newHashTag.getName())
            );
        }

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
        if (!validationService.hashTagExistsById(updatedHashTag.getId())) return responseService.hashTagNotFoundResponse(updatedHashTag.getId());
        if (validationService.hashTagExistsByName(updatedHashTag.getName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The HashTag with the name [ %s ] already exists", updatedHashTag.getName())
            );
        }

        hashTagRepository.update(updatedHashTag.getId(), updatedHashTag.getName());
        return responseService.successResponse(hashTagRepository.findById(updatedHashTag.getId()).get(), "Updated");
    }

    @Transactional
    public ResponseEntity<Object> deleteHashTag(String id) {
        if (!validationService.hashTagExistsById(id)) return responseService.hashTagNotFoundResponse(id);

        hashTagRepository.deleteById(id);
        return responseService.successResponse("The HashTag was Deleted.","Successful");
    }
    //endregion
}
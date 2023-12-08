package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.repository.HashTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HashTagService {
    @Autowired
    HashTagRepository hashTagRepository;

    @Transactional
    public ResponseEntity<Object> createNewHashTag(HashTag newHashTag) {
        Object response;

        if (!hashTagRepository.existByName(newHashTag.getName())){
            response = hashTagRepository.save(
                    HashTag.builder()
                            .name(newHashTag.getName())
                            .socialUserInterestIn(0)
                            .postTaggedIn(0)
                            .build()
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(String.format("The HashTag with the name [ %s ] already exists ", newHashTag.getName()), HttpStatus.CONFLICT);
        }
    }

    public Optional<HashTag> getHashTagById(String id) {
        return hashTagRepository.findById(id);
    }

    public HttpStatus deleteHashTag(String id) {
        Optional<HashTag> hashTag = hashTagRepository.findById(id);

        if (hashTag.isPresent()) {
                hashTagRepository.deleteById(id);
                return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    public ResponseEntity<HashTag> updateHashTag(HashTag updateHashTag) {
        if (hashTagRepository.existsById(updateHashTag.getId())){
            if (!hashTagRepository.existByName(updateHashTag.getName())){

                hashTagRepository.update(
                        updateHashTag.getId(),
                        updateHashTag.getName()
                );

                HashTag savedHashTag = hashTagRepository.findById(updateHashTag.getId()).get();
                return ResponseEntity.status(HttpStatus.OK).body(savedHashTag);
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //region gets
    public Optional<Object> getAllHashTag(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(hashTagRepository.findAll(pageable));
    }
    //endregion
}

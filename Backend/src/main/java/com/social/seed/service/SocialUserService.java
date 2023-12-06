package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialUserService {
    @Autowired
    SocialUserRepository socialUserRepository;

    //region crud
    public ResponseEntity<SocialUser> getSocialUserById(String id){

        HttpStatus httpStatus = HttpStatus.OK;
        Optional<SocialUser> socialUser = socialUserRepository.findById(id);

        if (socialUser.isEmpty()){
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<SocialUser>(socialUser.get(), httpStatus);
    }
    //endregion

}

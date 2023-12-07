package com.social.seed.service;

import com.social.seed.model.SocialUser;
import com.social.seed.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SocialUserService {
    @Autowired
    SocialUserRepository socialUserRepository;

    //region crud
    public ResponseEntity<SocialUser> getSocialUserById(String id) {
        return socialUserRepository.findById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> createNewSocialUser(SocialUser socialUser) {

        HttpStatus httpStatus;
        String response = "";

        boolean existByEmail = socialUserRepository.existByEmail(socialUser.getEmail());
        boolean existByUserName = socialUserRepository.existByUserName(socialUser.getUserName());

        //si no existe el email y el username entonces se procede a crear el usuario
        if (!existByEmail && !existByUserName){
            //create the social user with the base data
            socialUserRepository.save(
                    SocialUser.builder()
                            .userName(socialUser.getUserName())
                            .email(socialUser.getEmail())
                            .dateBorn(socialUser.getDateBorn())
                            .fullName(socialUser.getFullName())
                            .language(socialUser.getLanguage())
                            .registrationDate(LocalDateTime.now())
                            .isActive(true)
                            .onVacation(false)
                            .followersCount(0)
                            .friendCount(0)
                            .followingCount(0)
                            .build()
            );

            httpStatus = HttpStatus.CREATED;
            response = "The User was created successfully";

        }else {
            httpStatus = HttpStatus.CONFLICT;

            if (existByUserName && existByEmail){
                response = "The email and the username already exists";
            } else if (existByEmail) {
                response = "The email already exists";
            } else if (existByUserName) {
                response = "The username already exists";
            } else {
                response = "Unknown conflict";
            }
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    public HttpStatus deleteSocialUser(String userId, String id) {
        if (!userId.equals(id)) {
            return HttpStatus.CONFLICT;
        }

        return socialUserRepository.findById(id)
                .map(user -> {
                    socialUserRepository.deleteById(id);
                    return HttpStatus.OK;
                })
                .orElse(HttpStatus.NOT_FOUND);
    }

    public HttpStatus updateSocialUser(String userId, SocialUser newSocialUser) {
        if (!userId.equals(newSocialUser.getId())) {
            return HttpStatus.CONFLICT;
        }

        Optional<SocialUser> existingUser = socialUserRepository.findById(newSocialUser.getId());

        if (existingUser.isPresent()) {
            socialUserRepository.update(
                newSocialUser.getId(),
                newSocialUser.getFullName(),
                newSocialUser.getDateBorn(),
                newSocialUser.getLanguage()
            );
            return HttpStatus.OK;
        }else {
            return HttpStatus.NOT_FOUND;
        }
    }
    //endregion
}

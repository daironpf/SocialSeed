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

    //region CRUD
    public Optional<SocialUser> getSocialUserById(String id) {
        return socialUserRepository.findById(id);
    }


    public ResponseEntity<Object> createNewSocialUser(SocialUser socialUser) {

        HttpStatus httpStatus;
        Object response;

        boolean existByEmail = socialUserRepository.existByEmail(socialUser.getEmail());
        boolean existByUserName = socialUserRepository.existByUserName(socialUser.getUserName());

        //si no existe el email y el username entonces se procede a crear el usuario
        if (!existByEmail && !existByUserName){
            //create the social user with the base data
            response = socialUserRepository.save(
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
        }else {
            httpStatus = HttpStatus.CONFLICT;

            if (existByUserName && existByEmail){
                response = "The email and the username already exists";
            } else if (existByEmail) {
                response = "The email already exists";
            } else {
                response = "The username already exists";
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

    public ResponseEntity<SocialUser> updateSocialUser(String userId, SocialUser newSocialUser) {

        if (!userId.equals(newSocialUser.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }else {
            Optional<SocialUser> socialUser = socialUserRepository.findById(newSocialUser.getId());

            if (socialUser.isPresent()){
                socialUserRepository.update(
                        newSocialUser.getId(),
                        newSocialUser.getFullName(),
                        newSocialUser.getDateBorn(),
                        newSocialUser.getLanguage()
                );

                SocialUser savedSocialUser = socialUserRepository.findById(userId).get();
                return ResponseEntity.status(HttpStatus.OK).body(savedSocialUser);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

    }
    //endregion
}

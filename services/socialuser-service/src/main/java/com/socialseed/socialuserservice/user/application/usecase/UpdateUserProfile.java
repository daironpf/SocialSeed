package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UpdateUserProfileDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserProfile {
    private final UserRepository userRepository;

    public UpdateUserProfile(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(UpdateUserProfileDTO dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        dto.userId()
                ));

        user.updateProfile(
                dto.fullName(),
                dto.bio(),
                dto.profileImage(),
                dto.birthDate(),
                dto.language()
        );

        userRepository.updateProfile(user);
    }
}
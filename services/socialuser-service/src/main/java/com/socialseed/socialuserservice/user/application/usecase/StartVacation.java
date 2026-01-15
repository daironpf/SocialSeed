package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.valueobject.VacationPeriod;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.StartVacationRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StartVacation {
    private final UserRepository userRepository;

    public StartVacation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(StartVacationRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        dto.userId()
                ));

        VacationPeriod period = new VacationPeriod(dto.startDate(), dto.endDate(), dto.note());
        
        user.goOnVacation(period);

        userRepository.updateProfile(user);
    }
}

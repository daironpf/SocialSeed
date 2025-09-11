package com.our.socialseed.user.config;

import com.our.socialseed.user.application.usecase.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockUserUseCasesConfig {

    @Bean
    public UserUseCases userUseCases() {
        return Mockito.mock(UserUseCases.class);
    }

    @Bean
    public CreateUser createUser() {
        return Mockito.mock(CreateUser.class);
    }

    @Bean
    public GetUserById getUserById() {
        return Mockito.mock(GetUserById.class);
    }

    @Bean
    public GetAllUsers getAllUsers() {
        return Mockito.mock(GetAllUsers.class);
    }

    @Bean
    public UpdateUser updateUser() {
        return Mockito.mock(UpdateUser.class);
    }

    @Bean
    public DeleteUser deleteUser() {
        return Mockito.mock(DeleteUser.class);
    }

    @Bean
    public ChangeUserPassword changeUserPassword() {
        return Mockito.mock(ChangeUserPassword.class);
    }
}

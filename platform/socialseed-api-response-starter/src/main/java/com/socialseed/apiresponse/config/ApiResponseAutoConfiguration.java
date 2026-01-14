package com.socialseed.apiresponse.config;

import com.socialseed.apiresponse.i18n.MessageResolver;
import com.socialseed.apiresponse.model.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ApiResponseAutoConfiguration {

    private final MessageSource messageSource;

    public ApiResponseAutoConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    void init() {
        ApiResponse.configure(new MessageResolver(messageSource));
    }
}

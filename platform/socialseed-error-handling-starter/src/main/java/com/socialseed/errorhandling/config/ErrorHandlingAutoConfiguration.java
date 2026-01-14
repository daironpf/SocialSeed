package com.socialseed.errorhandling.config;

import com.socialseed.errorhandling.handler.GlobalErrorHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ErrorHandlingAutoConfiguration {

    @Bean
    GlobalErrorHandler globalErrorHandler() {
        return new GlobalErrorHandler();
    }
}

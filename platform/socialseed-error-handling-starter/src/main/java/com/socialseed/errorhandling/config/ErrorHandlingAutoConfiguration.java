package com.socialseed.errorhandling.config;

import com.socialseed.errorhandling.handler.GlobalErrorHandler;
import com.socialseed.errorhandling.handler.PGSQLExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ErrorHandlingAutoConfiguration {

    @Bean
    GlobalErrorHandler globalErrorHandler() {
        return new GlobalErrorHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.dao.DataIntegrityViolationException")
    PGSQLExceptionHandler pgsqlExceptionHandler() {
        return new PGSQLExceptionHandler();
    }
}

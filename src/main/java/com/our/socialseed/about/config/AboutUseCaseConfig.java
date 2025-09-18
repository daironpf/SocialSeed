package com.our.socialseed.about.config;

import com.our.socialseed.about.application.usecase.AboutUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AboutUseCaseConfig {
    @Bean
    public AboutUseCases aboutUseCases(){
        return new AboutUseCases();
    }
}

package com.our.socialseed.about.application.usecase;

import com.our.socialseed.about.domain.model.About;
import com.our.socialseed.config.AppProperties;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAbout {
    private final AppProperties appProperties;

    public GetAbout(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public Optional<About> execute() {
        return Optional.of(new About(
                appProperties.getVersion(),
                "base_seed",
                appProperties.getName(),
                appProperties.getIcon()
        ));
    }
}


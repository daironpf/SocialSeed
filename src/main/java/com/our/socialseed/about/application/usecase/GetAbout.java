package com.our.socialseed.about.application.usecase;

import com.our.socialseed.about.domain.model.About;
import com.our.socialseed.config.AppInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAbout {

    public GetAbout() {}

    public Optional<About> execute() {
        return Optional.of(new About(
                AppInfo.VERSION,
                "base_seed",
                "Test Social",
                "https://www.iconos.com/socialseed.png",
                "Red Social Base con Social Seed"
        ));
    }
}
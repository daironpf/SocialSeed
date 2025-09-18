package com.our.socialseed.about.application.usecase;

import com.our.socialseed.about.domain.model.About;
import java.util.Optional;

public class GetAbout {

    public Optional<About> execute() {
        return Optional.of(new About(
                "0.0.1",
                "base_seed",
                "Social Seed",
                "https://www.iconos.com/socialseed.png"
        ));
    }
}


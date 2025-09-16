package com.our.socialseed.about.domain.service;

import com.our.socialseed.about.domain.model.About;
import java.util.Optional;

public interface AboutService {
    Optional<About> getAbout();
}

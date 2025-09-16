package com.our.socialseed.about.domain.repository;

import com.our.socialseed.about.domain.model.About;

import java.util.Optional;

public interface AboutRepository {
    Optional<About> getAbout();
}

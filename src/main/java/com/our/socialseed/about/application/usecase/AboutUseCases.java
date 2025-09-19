package com.our.socialseed.about.application.usecase;

import org.springframework.stereotype.Service;

@Service
public class AboutUseCases {
    private final GetAbout getAbout;

    public AboutUseCases(GetAbout getAbout) {
        this.getAbout = getAbout;
    }

    public GetAbout getAbout() {
        return getAbout;
    }
}

package com.our.socialseed.about.application.usecase;


import com.our.socialseed.user.application.usecase.GetUserById;

public class AboutUseCases {
    private final GetAbout getAbout;

    public AboutUseCases() {
        this.getAbout = new GetAbout();
    }

    public GetAbout getAbout() {
        return getAbout;
    }
}

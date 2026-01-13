package com.socialseed.apiresponse.model;

public record ApiError(
        String code,
        String message
) {}

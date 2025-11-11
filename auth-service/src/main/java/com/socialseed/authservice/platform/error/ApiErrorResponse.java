package com.socialseed.authservice.platform.error;

public record ApiErrorResponse(
        String code,
        String message
) {}

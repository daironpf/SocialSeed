package com.socialseed.socialuserservice.platform.error;

public record ApiErrorResponse(
        String code,
        String message
) {}

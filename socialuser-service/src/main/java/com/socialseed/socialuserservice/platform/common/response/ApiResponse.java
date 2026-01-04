package com.socialseed.socialuserservice.platform.common.response;

import com.socialseed.socialuserservice.platform.config.AppInfo;

import java.time.Instant;

/**
 * Standardized API response for REST controllers.
 */
public record ApiResponse<T>(
        int status,
        T data,
        String message,
        String version,
        Instant timestamp
) {

    public ApiResponse(int status, T data, String message) {
        this(status, data, message, AppInfo.VERSION, Instant.now());
    }

    /* ===== Factories neutrales ===== */
    public static <T> ApiResponse<T> of(int status, T data, String message) {
        return new ApiResponse<>(status, data, message);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, data, message);
    }

    public static ApiResponse<Void> message(int status, String message) {
        return new ApiResponse<>(status, null, message);
    }
}
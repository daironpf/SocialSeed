package com.our.socialseed.shared.response;

import com.our.socialseed.config.AppInfo;

import java.time.Instant;

/**
 * Standardized API response for REST controllers.
 */
public record ApiResponse<T>(
        int status,       // HTTP status code
        T data,           // Actual payload (can be null on error)
        String message,   // Human-readable message
        String version,   // API version
        Instant timestamp // Response time
) {
    public ApiResponse(int status, T data, String message) {
        this(status, data, message, AppInfo.VERSION, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, data, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, null, message);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(409, null, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, null, message);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, null, message);
    }
}


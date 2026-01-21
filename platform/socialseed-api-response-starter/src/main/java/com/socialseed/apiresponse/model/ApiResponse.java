package com.socialseed.apiresponse.model;

import com.socialseed.apiresponse.config.AppInfo;
import com.socialseed.apiresponse.i18n.MessageResolver;

import java.time.Instant;

public record ApiResponse<T>(
        int status,
        T data,
        String message,
        String version,
        Instant timestamp) {

    private static MessageResolver messageResolver;

    // Inyectado por configuración
    public static void configure(MessageResolver resolver) {
        messageResolver = resolver;
    }

    public static String msg(String key, Object... params) {
        return messageResolver.resolve(key, params);
    }

    private static String msg(ApiMessageKey key, Object... params) {
        return messageResolver.resolve(key.key(), params);
    }

    /* ===== Factories públicas ===== */

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                200,
                data,
                msg(ApiMessageKey.SUCCESS_DEFAULT),
                AppInfo.VERSION,
                Instant.now());
    }

    public static <T> ApiResponse<T> success(T data, ApiMessageKey key, Object... params) {
        return new ApiResponse<>(
                200,
                data,
                msg(key, params),
                AppInfo.VERSION,
                Instant.now());
    }

    public static ApiResponse<Void> message(ApiMessageKey key) {
        return new ApiResponse<>(
                200,
                null,
                msg(key),
                AppInfo.VERSION,
                Instant.now());
    }

    public static ApiResponse<Void> error(ApiError error, int status) {
        return new ApiResponse<>(
                status,
                null,
                error.message(),
                AppInfo.VERSION,
                Instant.now());
    }

    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(
                status,
                null,
                message,
                AppInfo.VERSION,
                Instant.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                200,
                data,
                message,
                AppInfo.VERSION,
                Instant.now());
    }

    public static ApiResponse<Void> message(int status, String message) {
        return new ApiResponse<>(
                status,
                null,
                message,
                AppInfo.VERSION,
                Instant.now());
    }

    public static <T> ApiResponse<T> error(int status, T data, String message) {
        return new ApiResponse<>(
                status,
                data,
                message,
                AppInfo.VERSION,
                Instant.now());
    }
}

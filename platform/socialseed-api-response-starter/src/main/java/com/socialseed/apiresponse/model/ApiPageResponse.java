package com.socialseed.apiresponse.model;

import com.socialseed.apiresponse.config.AppInfo;
import java.time.Instant;
import java.util.List;

public record ApiPageResponse<T>(
        int status,
        List<T> data,
        String message,
        String version,
        Instant timestamp,
        PageMetadata metadata
) {

    public record PageMetadata(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last
    ) {}

    public static <T> ApiPageResponse<T> success(
            List<T> data,
            int page,
            int size,
            long totalElements,
            String message
    ) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new ApiPageResponse<>(
                200,
                data,
                message,
                AppInfo.VERSION,
                Instant.now(),
                new PageMetadata(
                        page,
                        size,
                        totalElements,
                        totalPages,
                        page == 0,
                        page >= totalPages - 1
                )
        );
    }

    public static <T> ApiPageResponse<T> success(
            List<T> data,
            int page,
            int size,
            long totalElements
    ) {
        return success(data, page, size, totalElements, ApiResponse.msg("api.success.default"));
    }
}

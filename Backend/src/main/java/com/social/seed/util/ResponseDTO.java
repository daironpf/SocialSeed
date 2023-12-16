package com.social.seed.util;

import org.springframework.http.HttpStatus;

/**
 * Represents a response from the REST API.
 */
public record ResponseDTO(
        HttpStatus status,
        Object response,
        String message,
        String version,
        Throwable error
) {
    /**
     * Main constructor to create an instance of ResponseDTO.
     *
     * @param status   The HTTP status of the response.
     * @param response The API response.
     * @param message  A descriptive message.
     * @param error    The associated exception (can be null).
     */
    private ResponseDTO(HttpStatus status, Object response, String message, Throwable error) {
        this(status, response, message, "v0.0.1", error);
    }

    /**
     * Convenience constructor to create an instance of ResponseDTO without handling exceptions.
     *
     * @param status   The HTTP status of the response.
     * @param response The API response.
     * @param message  A descriptive message.
     */
    public ResponseDTO(HttpStatus status, Object response, String message) {
        this(status, response, message, null);
    }

    /**
     * Creates an instance of ResponseDTO with SUCCESS status.
     *
     * @param response The API response.
     * @param message  A descriptive message.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO success(Object response, String message) {
        return new ResponseDTO(HttpStatus.OK, response, message, null);
    }

    /**
     * Creates an instance of ResponseDTO with ERROR status.
     *
     * @param message The error message.
     * @param error   The associated exception.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO error(String message, Throwable error) {
        return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", message, error);
    }
}

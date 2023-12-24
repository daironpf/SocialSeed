package com.social.seed.util;

import org.springframework.http.HttpStatus;

/**
 * Represents a response from the REST API.
 */
public record ResponseDTO(
        HttpStatus status,
        Object response,
        String message,
        String version
) {
    /**
     * Main constructor to create an instance of ResponseDTO.
     *
     * @param status   The HTTP status of the response.
     * @param response The API response.
     * @param message  A descriptive message.
     */
    public ResponseDTO(HttpStatus status, Object response, String message) {
        this(status, response, message, "v0.0.1");
    }

    /**
     * Creates an instance of ResponseDTO with SUCCESS status.
     *
     * @param response The API response.
     * @param message  A descriptive message.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO success(Object response, String message) {
        return new ResponseDTO(HttpStatus.OK, response, message);
    }

    /**
     * Creates an instance of ResponseDTO with NOT_FOUND status.
     *
     * @param message The error message.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO notFound(String message) {
        return new ResponseDTO(HttpStatus.NOT_FOUND,"Error", message);
    }

    /**
     * Creates an instance of ResponseDTO with CONFLICT status.
     *
     * @param message The error message.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO conflict(String message) {
        return new ResponseDTO(HttpStatus.CONFLICT, "Error", message);
    }

    /**
     * Creates an instance of ResponseDTO with FORBIDDEN status.
     *
     * @param message The error message.
     * @return The ResponseDTO instance.
     */
    public static ResponseDTO forbidden(String message) {
        return new ResponseDTO(HttpStatus.FORBIDDEN, "Error", message);
    }
}
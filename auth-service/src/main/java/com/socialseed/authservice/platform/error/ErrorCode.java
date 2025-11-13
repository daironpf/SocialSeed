package com.socialseed.authservice.platform.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enumeración central de códigos de error y sus estados HTTP asociados.
 * Compatible con internacionalización usando MessageSource.
 */
@Getter
public enum ErrorCode {

    // --- HTTP Standard Errors ---
    BAD_REQUEST("error.bad_request", HttpStatus.BAD_REQUEST),
    INVALID_ID("error.invalid.id", HttpStatus.BAD_REQUEST),
    VALIDATION("error.validation", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("error.unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("error.forbidden", HttpStatus.FORBIDDEN),
    CONFLICT("error.conflict", HttpStatus.CONFLICT),
    NOT_FOUND("error.notfound", HttpStatus.NOT_FOUND),
    INTERNAL_ERROR("error.internal", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- Domain Generic Errors ---
    RESOURCE_ALREADY_EXISTS("error.resource_already_exists", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND("error.resource_not_found", HttpStatus.NOT_FOUND),

    // --- Domain Specific Errors ---
    USER_EMAIL_EXISTS("error.user.email_exists", HttpStatus.CONFLICT),
    USERNAME_EXISTS("error.user.username_exists", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("error.user.password_mismatch", HttpStatus.BAD_REQUEST),
    USER_CREATION_FAILED("error.user.creation_failed", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus httpStatus;

    ErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

}

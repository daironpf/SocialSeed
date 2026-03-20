package com.socialseed.apiresponse.model;

/**
 * Standard message keys used by the platform.
 */
public enum ApiMessageKey {

    SUCCESS_DEFAULT("api.success.default"),
    NO_CONTENT("api.success.no_content"),
    CREATED("api.success.created"),
    UPDATED("api.success.updated"),
    DELETED("api.success.deleted"),
    BAD_REQUEST("error.bad_request"),
    UNAUTHORIZED("error.unauthorized"),
    FORBIDDEN("error.forbidden"),
    NOT_FOUND("error.notfound"),
    CONFLICT("error.conflict"),
    INTERNAL_ERROR("error.internal");

    private final String key;

    ApiMessageKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

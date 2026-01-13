package com.socialseed.apiresponse.model;

/**
 * Standard message keys used by the platform.
 */
public enum ApiMessageKey {

    SUCCESS_DEFAULT("api.success.default"),
    NO_CONTENT("api.success.no_content"),
    CREATED("api.success.created");

    private final String key;

    ApiMessageKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

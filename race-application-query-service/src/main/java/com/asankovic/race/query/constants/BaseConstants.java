package com.asankovic.race.query.constants;

public final class BaseConstants {

    public static final String NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be null";
    public static final String BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be blank";

    public static final String RUNNERS_GET_ALL_LIMIT_PROPERTY = "runners.get.all.limit";
    public static final int RUNNERS_GET_ALL_LIMIT_DEFAULT = 100;

    public static final String FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY = "feature.get.all.runners.enabled";
    public static final boolean FEATURE_GET_ALL_RUNNERS_ENABLED_DEFAULT = false;

    public static final String ALLOWED_TEXT_REGEX = "^[a-zA-Z0-9\\s-]+$";
    public static final String ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE = "Must contain only alphanumeric and allowed characters";

    private BaseConstants() {
        throw new IllegalStateException("Utility class");
    }
}

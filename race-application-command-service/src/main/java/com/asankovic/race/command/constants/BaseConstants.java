package com.asankovic.race.command.constants;

public final class BaseConstants {

    public static final String NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be null";
    public static final String BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be blank";

    public static final String ALLOWED_TEXT_REGEX = "^[a-zA-Z0-9\\s-]+$";
    public static final String ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE = "Must contain only alphanumeric and allowed characters";

    private BaseConstants() {
        throw new IllegalStateException("Utility class");
    }
}

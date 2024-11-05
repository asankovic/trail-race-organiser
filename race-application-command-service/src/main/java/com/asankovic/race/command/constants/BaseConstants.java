package com.asankovic.race.command.constants;

public final class BaseConstants {

    public static final String NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be null";
    public static final String BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE = "%s must not be blank";

    private BaseConstants() {
        throw new IllegalStateException("Utility class");
    }
}

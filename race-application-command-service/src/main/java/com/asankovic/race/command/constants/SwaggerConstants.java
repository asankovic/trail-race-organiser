package com.asankovic.race.command.constants;

public final class SwaggerConstants {

    public static final String OK = "200";
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    private SwaggerConstants() {
        throw new IllegalStateException("Utility class");
    }
}
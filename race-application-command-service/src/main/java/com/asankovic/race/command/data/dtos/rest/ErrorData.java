package com.asankovic.race.command.data.dtos.rest;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorData(
        @Schema(description = "The error message description", example = "Invalid request")
        String error,
        @Schema(description = "In case of failed validation, name of the field that failed to get validated",
                example = "publicRunnerID")
        String field) {

    public ErrorData(final String error) {
        this(error, null);
    }
}

package com.asankovic.race.query.data.dtos.rest;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorData(@Schema(description = "The error message", example = "Invalid request") String error) {
}
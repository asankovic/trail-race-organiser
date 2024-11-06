package com.asankovic.race.query.data.dtos.rest;

import io.swagger.v3.oas.annotations.media.Schema;

public record RunnerData(
        @Schema(
                description = "Public ID of the runner",
                type = "string",
                format = "uuid",
                defaultValue = "d6d5d8f4-33e8-4a73-bf0f-b925d915e1c1"
        )
        String publicId,
        @Schema(
                description = "First name of the runner",
                defaultValue = "John"
        )
        String firstName,

        @Schema(
                description = "Last name of the runner",
                defaultValue = "Doe"
        )
        String lastName,

        @Schema(
                description = "Club name associated with the runner",
                defaultValue = "Generic Club"
        )
        String club,

        @Schema(
                description = "Distance name associated with the runner",
                defaultValue = "5k"
        )
        String distance
) {
}

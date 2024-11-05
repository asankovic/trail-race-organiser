package com.asankovic.race.query.data.dtos.rest;

public record RunnerData(
        String publicId,
        String firstName,
        String lastName,
        String club,
        String distance
) {
}

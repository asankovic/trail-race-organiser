package com.asankovic.race.query.data.dtos.rest;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public record RunnerData(
        String publicId,
        String firstName,
        String lastName,
        String club,
        String distance
) {
    public static RunnerData empty() {
        return new RunnerData(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
    }
}

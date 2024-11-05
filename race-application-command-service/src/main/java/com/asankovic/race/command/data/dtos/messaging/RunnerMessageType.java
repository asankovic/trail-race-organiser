package com.asankovic.race.command.data.dtos.messaging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RunnerMessageType {

    CREATE("runner-create"),
    PARTIAL_UPDATE("runner-partial-update"),
    DELETE("runner-delete");

    private final String value;
}

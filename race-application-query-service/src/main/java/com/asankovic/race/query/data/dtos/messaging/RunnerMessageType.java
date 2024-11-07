package com.asankovic.race.query.data.dtos.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RunnerMessageType {

    CREATE("runner-create"),
    PARTIAL_UPDATE("runner-partial-update"),
    DELETE("runner-delete");

    public static final String CREATE_VALUE = "runner-create";
    public static final String PARTIAL_UPDATE_VALUE = "runner-partial-update";
    public static final String DELETE_VALUE = "runner-delete";

    private final String value;

    @JsonCreator
    public static RunnerMessageType fromValue(String value) {
        for (final RunnerMessageType type : RunnerMessageType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RunnerMessageType: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}


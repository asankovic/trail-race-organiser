package com.asankovic.race.query.data.dtos.messaging;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO move this and related classes to shared module/library
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseRunnerMessageData {

    @NotNull(message = "Message type must be specified")
    protected RunnerMessageType messageType;
}

package com.asankovic.race.command.data.dtos.messaging;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO move this and related classes to shared module/library
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseRunnerMessageData {

    protected RunnerMessageType messageType;
}

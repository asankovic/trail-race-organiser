package com.asankovic.race.command.services;

import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;

public interface RunnerMessageService {

    void publishCreationEvent(CreateRunnerData createRunnerData);

    void publishUpdateEvent(UpdateRunnerData updateRunnerData, String publicRunnerID);

    void publishDeletionEvent(String publicRunnerID);
}

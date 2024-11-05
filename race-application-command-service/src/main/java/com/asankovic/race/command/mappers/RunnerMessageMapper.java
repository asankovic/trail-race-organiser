package com.asankovic.race.command.mappers;

import com.asankovic.race.command.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;

public interface RunnerMessageMapper {

    CreateRunnerMessageData mapToCreationMessage(CreateRunnerData createRunnerData);

    UpdateRunnerMessageData mapToUpdateMessage(UpdateRunnerData updateRunnerMessageData, String publicRunnerID);

    DeleteRunnerMessageData mapToDeleteMessage(String publicRunnerID);
}

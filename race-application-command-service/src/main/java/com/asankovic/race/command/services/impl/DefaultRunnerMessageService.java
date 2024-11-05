package com.asankovic.race.command.services.impl;

import com.asankovic.race.command.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import com.asankovic.race.command.mappers.RunnerMessageMapper;
import com.asankovic.race.command.services.RunnerMessageService;
import org.springframework.stereotype.Service;

@Service
public class DefaultRunnerMessageService implements RunnerMessageService {

    private final RunnerMessageMapper messageMapper;

    public DefaultRunnerMessageService(final RunnerMessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public void publishCreationEvent(final CreateRunnerData createRunnerData) {
        final CreateRunnerMessageData creationMessage = messageMapper.mapToCreationMessage(createRunnerData);
        //TODO publish
    }

    @Override
    public void publishUpdateEvent(final UpdateRunnerData updateRunnerData, final String publicRunnerID) {
        final UpdateRunnerMessageData updateMessage = messageMapper.mapToUpdateMessage(updateRunnerData, publicRunnerID);
        //TODO publish
    }

    @Override
    public void publishDeletionEvent(final String publicRunnerID) {
        final DeleteRunnerMessageData deleteMessage = messageMapper.mapToDeleteMessage(publicRunnerID);
        //TODO publish
    }
}

package com.asankovic.race.command.mappers.impl;

import com.asankovic.race.command.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import com.asankovic.race.command.mappers.RunnerMessageMapper;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import static com.asankovic.race.command.constants.BaseConstants.BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE;
import static com.asankovic.race.command.constants.BaseConstants.NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE;

@Component
public class DefaultRunnerMessageMapper implements RunnerMessageMapper {

    @Override
    public CreateRunnerMessageData mapToCreationMessage(final CreateRunnerData createRunnerData) {
        Validate.notNull(createRunnerData, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("CreateRunnerData"));

        return new CreateRunnerMessageData(
                createRunnerData.getFirstName(),
                createRunnerData.getLastName(),
                createRunnerData.getClub(),
                createRunnerData.getDistance()
        );
    }

    @Override
    public UpdateRunnerMessageData mapToUpdateMessage(final UpdateRunnerData updateRunnerData,
                                                      final String publicRunnerID) {
        Validate.notNull(updateRunnerData, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("UpdateRunnerData"));
        Validate.notNull(publicRunnerID, BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("publicRunnerID"));

        return new UpdateRunnerMessageData(
                publicRunnerID,
                updateRunnerData.getFirstName(),
                updateRunnerData.getLastName(),
                updateRunnerData.getClub(),
                updateRunnerData.getDistanceCode()
        );
    }

    @Override
    public DeleteRunnerMessageData mapToDeleteMessage(final String publicRunnerID) {
        Validate.notNull(publicRunnerID, BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("publicRunnerID"));

        return new DeleteRunnerMessageData(publicRunnerID);
    }
}

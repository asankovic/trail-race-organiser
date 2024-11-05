package com.asankovic.race.query.mappers.impl;

import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.data.models.DistanceModel;
import com.asankovic.race.query.data.models.RunnerModel;
import com.asankovic.race.query.mappers.RunnerMapper;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import static com.asankovic.race.query.constants.BaseConstants.NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE;

@Component
public class DefaultRunnerMapper implements RunnerMapper {

    @Override
    public RunnerData mapToData(final RunnerModel runner) {
        Validate.notNull(runner, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("RunnerModel"));

        return new RunnerData(
                runner.getPublicId().toString(),
                runner.getFirstName(),
                runner.getLastName(),
                runner.getClub(),
                runner.getDistance().getName()
        );
    }

    @Override
    public RunnerModel mapToModel(final CreateRunnerMessageData runnerMessage, final DistanceModel distance) {
        Validate.notNull(runnerMessage, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("CreateRunnerMessageData"));
        Validate.notNull(distance, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("DistanceModel"));

        return new RunnerModel(
                runnerMessage.getFirstName(),
                runnerMessage.getLastName(),
                runnerMessage.getClub(),
                distance
        );
    }
}

package com.asankovic.race.query.mappers;

import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.data.models.DistanceModel;
import com.asankovic.race.query.data.models.RunnerModel;

public interface RunnerMapper {

    RunnerData mapToData(RunnerModel runner);

    RunnerModel mapToModel(CreateRunnerMessageData runner, DistanceModel distance);
}

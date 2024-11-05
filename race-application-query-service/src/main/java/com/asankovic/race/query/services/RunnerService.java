package com.asankovic.race.query.services;

import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.exceptions.UnknownDistanceException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;

import java.util.List;

public interface RunnerService {

    String MISSING_DISTANCE_ERROR_MESSAGE = "Distance for name %s does not exist";

    RunnerData createRunner(CreateRunnerMessageData createRunnerMessageData) throws UnknownDistanceException;

    void updateRunner(UpdateRunnerMessageData updateRunnerMessageData) throws UnknownRunnerIdException, UnknownDistanceException;

    void deleteRunner(DeleteRunnerMessageData deleteRunnerMessageData);

    RunnerData getRunner(String publicId) throws UnknownRunnerIdException;

    List<RunnerData> getLimitedRunners();

    List<RunnerData> getAllRunners();
}

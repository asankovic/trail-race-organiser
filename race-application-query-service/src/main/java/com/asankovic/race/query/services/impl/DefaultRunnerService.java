package com.asankovic.race.query.services.impl;

import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.data.models.DistanceModel;
import com.asankovic.race.query.data.models.RunnerModel;
import com.asankovic.race.query.exceptions.UnknownDistanceException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.mappers.RunnerMapper;
import com.asankovic.race.query.repositories.DistanceRepository;
import com.asankovic.race.query.repositories.RunnerRepository;
import com.asankovic.race.query.services.RunnerService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static com.asankovic.race.query.constants.BaseConstants.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class DefaultRunnerService implements RunnerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRunnerService.class);
    private static final String RUNNER_STATE_LOG_MESSAGE_TEMPLATE =
            "Saved values for runner: firstName={}, lastName={}, club={}, distance={}";

    private final RunnerRepository runnerRepository;
    private final DistanceRepository distanceRepository;
    private final RunnerMapper runnerMapper;
    private final Environment environment;

    public DefaultRunnerService(final RunnerRepository runnerRepository,
                                final DistanceRepository distanceRepository,
                                final RunnerMapper runnerMapper,
                                final Environment environment) {
        this.runnerRepository = runnerRepository;
        this.distanceRepository = distanceRepository;
        this.runnerMapper = runnerMapper;
        this.environment = environment;
    }

    //TODO should this be ignored or fallback/retry logic created
    //TODO extend messages with unique IDs to improve logging
    @Override
    public RunnerData createRunner(final CreateRunnerMessageData createRunnerMessageData) throws UnknownDistanceException {
        Validate.notNull(createRunnerMessageData, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("CreateRunnerMessageData"));

        final DistanceModel foundDistance = distanceRepository.getDistanceByName(createRunnerMessageData.getDistanceName())
                .orElseThrow(() -> new UnknownDistanceException(
                        MISSING_DISTANCE_ERROR_MESSAGE.formatted(createRunnerMessageData.getDistanceName())
                ));

        final RunnerModel newRunner = runnerMapper.mapToModel(createRunnerMessageData, foundDistance);
        final RunnerModel savedRunner = runnerRepository.save(newRunner);

        LOG.info("Saved new runner with public ID '{}'", savedRunner.getPublicId());
        logRunnerState(savedRunner);

        return runnerMapper.mapToData(savedRunner);
    }

    @Override
    public void updateRunner(final UpdateRunnerMessageData updateRunnerMessageData) throws UnknownRunnerIdException, UnknownDistanceException {
        Validate.notNull(updateRunnerMessageData, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("UpdateRunnerMessageData"));

        final var runnerID = UUID.fromString(updateRunnerMessageData.getRunnerPublicID());
        final Optional<RunnerModel> existingRunner = runnerRepository.getRunnerByPublicId(runnerID);

        if (existingRunner.isEmpty()) {
            throw new UnknownRunnerIdException("Cannot update runner for ID ('%s') because it does not exist"
                    .formatted(updateRunnerMessageData.getRunnerPublicID())
            );
        }

        final RunnerModel runner = existingRunner.get();
        LOG.info("Updating runner with public ID '{}'", runner.getPublicId());
        logRunnerState(runner);

        Optional.ofNullable(updateRunnerMessageData.getFirstName()).ifPresent(runner::setFirstName);
        Optional.ofNullable(updateRunnerMessageData.getLastName()).ifPresent(runner::setLastName);
        Optional.ofNullable(updateRunnerMessageData.getClub()).ifPresent(runner::setClub);

        if (isNotBlank(updateRunnerMessageData.getDistanceCode())) {
            final DistanceModel distance = distanceRepository.getDistanceByName(updateRunnerMessageData.getDistanceCode()).orElseThrow(
                    () -> {
                        final var errorMessage = "Requested update for non existing club name ('%s'), skipping club update for runner '%s'"
                                .formatted(updateRunnerMessageData.getClub(), runnerID);
                        return new UnknownDistanceException(errorMessage);
                    }
            );
            runner.setDistance(distance);
        }

        final RunnerModel updatedRunner = runnerRepository.save(runner);

        LOG.info("Updated runner with public ID '{}'", updatedRunner.getPublicId());
        logRunnerState(updatedRunner);
    }

    @Transactional
    @Override
    public void deleteRunner(final DeleteRunnerMessageData deleteRunnerMessageData) {
        Validate.notNull(deleteRunnerMessageData, NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("DeleteRunnerMessageData"));

        final var runnerID = UUID.fromString(deleteRunnerMessageData.getRunnerPublicID());

        LOG.info("Deleting runner with public ID '{}'", runnerID);

        runnerRepository.deleteRunnerByPublicId(runnerID);
    }

    @Override
    public RunnerData getRunner(final String publicId) throws UnknownRunnerIdException {
        Validate.notBlank(publicId, BLANK_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("publicId"));

        final var runnerID = UUID.fromString(publicId);
        final RunnerModel runner = runnerRepository.getRunnerByPublicId(runnerID).orElseThrow(() ->
                new UnknownRunnerIdException("Runner with public ID '%s' does not exist".formatted(publicId))
        );

        return runnerMapper.mapToData(runner);
    }

    @Override
    public List<RunnerData> getLimitedRunners() {
        final int queryLimit = environment.getProperty(
                RUNNERS_GET_ALL_LIMIT_PROPERTY,
                Integer.class,
                RUNNERS_GET_ALL_LIMIT_DEFAULT
        );

        return runnerRepository.getLatestRunners(queryLimit)
                .stream()
                .map(runnerMapper::mapToData)
                .toList();
    }

    @Override
    public List<RunnerData> getAllRunners() {
        //TODO remove, risky to use in production
        return StreamSupport.stream(runnerRepository.findAll().spliterator(), false)
                .map(runnerMapper::mapToData)
                .toList();
    }

    private void logRunnerState(final RunnerModel runner) {
        LOG.debug(RUNNER_STATE_LOG_MESSAGE_TEMPLATE, runner.getFirstName(), runner.getLastName(),
                runner.getClub(), runner.getDistance().getName());
    }
}

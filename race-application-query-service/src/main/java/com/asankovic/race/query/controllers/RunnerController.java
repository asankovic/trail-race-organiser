package com.asankovic.race.query.controllers;

import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.services.RunnerService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.asankovic.race.query.constants.BaseConstants.FEATURE_GET_ALL_RUNNERS_ENABLED_DEFAULT;
import static com.asankovic.race.query.constants.BaseConstants.FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY;

@RestController
@RequestMapping(RunnerController.ENDPOINT)
public class RunnerController {
    protected static final String ENDPOINT = "/v1/runners";

    private final RunnerService runnerService;
    private final Environment environment;

    public RunnerController(final RunnerService runnerService, Environment environment) {
        this.runnerService = runnerService;
        this.environment = environment;
    }

    @GetMapping
    public List<RunnerData> getRunners() {
        // TODO add pagination instead of risking performance when fetching all from DB
        final boolean isGetAllAllowed = environment.getProperty(
                FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY,
                Boolean.class,
                FEATURE_GET_ALL_RUNNERS_ENABLED_DEFAULT
        );

        if (isGetAllAllowed) {
            return runnerService.getAllRunners();
        }

        return runnerService.getLimitedRunners();
    }

    @GetMapping("/{publicRunnerId}")
    public RunnerData getRunner(@PathVariable String publicRunnerId) throws UnknownRunnerIdException {
        return runnerService.getRunner(publicRunnerId);
    }
}

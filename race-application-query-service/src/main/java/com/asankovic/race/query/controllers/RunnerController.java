package com.asankovic.race.query.controllers;

import com.asankovic.race.query.constants.SwaggerConstants;
import com.asankovic.race.query.data.dtos.rest.ErrorData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.exceptions.InvalidPublicRunnerIdException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.services.RunnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.asankovic.race.query.constants.BaseConstants.FEATURE_GET_ALL_RUNNERS_ENABLED_DEFAULT;
import static com.asankovic.race.query.constants.BaseConstants.FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY;

@RestController
@RequestMapping(RunnerController.ENDPOINT)
@Tag(name = "Track Runner", description = "Track Runner API")
public class RunnerController {

    protected static final String ENDPOINT = "/v1/runners";

    private final RunnerService runnerService;
    private final Environment environment;

    public RunnerController(final RunnerService runnerService, Environment environment) {
        this.runnerService = runnerService;
        this.environment = environment;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Gets all registered track runners.",
            description = "If feature is enabled, it will get all possible saved runners, otherwise to save performance, " +
                    "only a limited amount will be retrieved.",
            method = SwaggerConstants.GET
    )
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successfully fetched all track runners.",
                    responseCode = SwaggerConstants.OK
            ),
            @ApiResponse(
                    description = "Issue occurred when trying to fetch all track runners.",
                    responseCode = SwaggerConstants.INTERNAL_SERVER_ERROR,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            )
    })
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

    @GetMapping(value = "/{publicRunnerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Gets track runner for given public ID",
            description = "If a track runner with given ID exists, it will be returned.",
            method = SwaggerConstants.GET
    )
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successfully found and returned track runner with provided public ID.",
                    responseCode = SwaggerConstants.OK
            ),
            @ApiResponse(
                    description = "Given public ID is not in valid UUID format",
                    responseCode = SwaggerConstants.BAD_REQUEST,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            ),
            @ApiResponse(
                    description = "Track runner with provided public ID does not exist.",
                    responseCode = SwaggerConstants.NOT_FOUND,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            ),
            @ApiResponse(
                    description = "Issue occurred when trying to fetch track runner with provided public ID.",
                    responseCode = SwaggerConstants.INTERNAL_SERVER_ERROR,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            )
    })
    public RunnerData getRunner(@Parameter(description = "Existing public ID of the runner")
                                    @PathVariable String publicRunnerID) throws UnknownRunnerIdException {
        //TODO switch to annotation
        // in general, a lot of code is shared across services DTOs, handlers, etc and should be extracted to a shared lib
        try {
            UUID.fromString(publicRunnerID);
        } catch (final IllegalArgumentException e) {
            throw new InvalidPublicRunnerIdException(publicRunnerID + " is not a valid UUID");
        }
        return runnerService.getRunner(publicRunnerID);
    }
}

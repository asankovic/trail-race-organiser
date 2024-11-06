package com.asankovic.race.command.controllers;

import com.asankovic.race.command.constants.SwaggerConstants;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.ErrorData;
import com.asankovic.race.command.data.dtos.rest.ErrorDataList;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import com.asankovic.race.command.exceptions.InvalidPublicRunnerIdException;
import com.asankovic.race.command.services.RunnerMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(RunnerMutatingController.ENDPOINT)
@Tag(name = "Track Runner Mutations", description = "Track Runner Mutating API")
public class RunnerMutatingController {
//    todo add tests for jwt as well

    public static final String ENDPOINT = "/v1/runners";

    private final RunnerMessageService runnerMessageService;

    public RunnerMutatingController(final RunnerMessageService runnerMessageService) {
        this.runnerMessageService = runnerMessageService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Creates a new track runner",
            description = "Publishes an event for creating a new track runner with provided data",
            method = SwaggerConstants.POST
    )
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successfully published an event for creating a new track runner",
                    responseCode = SwaggerConstants.OK
            ),
            @ApiResponse(
                    description = "Payload validation has failed.",
                    responseCode = SwaggerConstants.BAD_REQUEST,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDataList.class)
                    )
            ),
            @ApiResponse(
                    description = "Issue occurred when trying to publish a new creation event",
                    responseCode = SwaggerConstants.INTERNAL_SERVER_ERROR,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            )
    })
    public void createRunner(@Valid @RequestBody CreateRunnerData createRunnerData) {
        runnerMessageService.publishCreationEvent(createRunnerData);
    }

    @PatchMapping(
            value = "/{publicRunnerID}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Updates an existing runner with provided data",
            description = "Publishes an event for updating an existing track runner with provided data",
            method = SwaggerConstants.PATCH
    )
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successfully published an event for updating an existing track runner",
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
                    description = "Payload validation has failed.",
                    responseCode = SwaggerConstants.BAD_REQUEST,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDataList.class)
                    )
            ),
            @ApiResponse(
                    description = "Issue occurred when trying to publish a new update event",
                    responseCode = SwaggerConstants.INTERNAL_SERVER_ERROR,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            )
    })
    public void updateRunner(@Valid @RequestBody UpdateRunnerData updateRunnerData,
                             @Parameter(description = "Existing public ID of the runner")
                             @PathVariable String publicRunnerID) {
        validatePublicRunnerID(publicRunnerID);
        runnerMessageService.publishUpdateEvent(updateRunnerData, publicRunnerID);
    }

    @DeleteMapping("/{publicRunnerID}")
    @Operation(
            summary = "Deletes an existing runner by provided public ID",
            description = "Publishes an event for deleting an existing track runner by provided public ID",
            method = SwaggerConstants.DELETE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successfully published an event for deleting an existing track runner by provided public ID",
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
                    description = "Issue occurred when trying to publish a new deletion event",
                    responseCode = SwaggerConstants.INTERNAL_SERVER_ERROR,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorData.class)
                    )
            )
    })
    public void deleteRunner(@Parameter(description = "Existing public ID of the runner")
                             @PathVariable String publicRunnerID) {
        validatePublicRunnerID(publicRunnerID);
        runnerMessageService.publishDeletionEvent(publicRunnerID);
    }

    private void validatePublicRunnerID(final String publicRunnerID) {
        try {
            UUID.fromString(publicRunnerID);
        } catch (final IllegalArgumentException e) {
            throw new InvalidPublicRunnerIdException(publicRunnerID + " is not a valid UUID");
        }
    }
}

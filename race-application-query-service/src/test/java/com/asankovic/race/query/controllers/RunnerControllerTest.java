package com.asankovic.race.query.controllers;

import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.services.RunnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.asankovic.race.query.constants.BaseConstants.FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RunnerController.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {FEATURE_GET_ALL_RUNNERS_ENABLED_PROPERTY + "=true"})
class RunnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RunnerService runnerService;

    @Test
    void shouldReturnAllRunners() throws Exception {
        final var firstRunner = new RunnerData("firstUUID", "firstName", "lastName", "testClub", "testDistance");
        final var secondRunner = new RunnerData("secondUUID", "secondName", "lastName", "testClub", "testDistance");

        when(runnerService.getAllRunners()).thenReturn(List.of(firstRunner, secondRunner));

        mockMvc.perform(get(RunnerController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].publicId").value(firstRunner.publicId()))
                .andExpect(jsonPath("$[1].publicId").value(secondRunner.publicId()));
    }

    @Test
    void shouldReturnInternalServerError_whenRequestingAllResultsInRuntimeException() throws Exception {
        when(runnerService.getAllRunners()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(RunnerController.ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnRunnerForId() throws Exception {
        final var runnerData = new RunnerData("testID", "firstName", "lastName", "testClub", "testDistance");

        when(runnerService.getRunner(anyString())).thenReturn(runnerData);

        mockMvc.perform(get(RunnerController.ENDPOINT + "/" + runnerData.publicId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.publicId").value(runnerData.publicId()))
                .andExpect(jsonPath("$.firstName").value(runnerData.firstName()))
                .andExpect(jsonPath("$.lastName").value(runnerData.lastName()))
                .andExpect(jsonPath("$.club").value(runnerData.club()))
                .andExpect(jsonPath("$.distance").value(runnerData.distance()));
    }

    @Test
    void shouldReturnNotFound_whenRunnerWithIdDoesNotExist() throws Exception {
        when(runnerService.getRunner(anyString())).thenThrow(UnknownRunnerIdException.class);

        mockMvc.perform(get(RunnerController.ENDPOINT + "/nonexistentId"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }
}
package com.asankovic.race.query.mappers.impl;

import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.data.models.DistanceModel;
import com.asankovic.race.query.data.models.RunnerModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.asankovic.race.query.constants.BaseConstants.NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DefaultRunnerMapperUnitTest {

    private final DefaultRunnerMapper runnerMapper = new DefaultRunnerMapper();

    @Test
    void shouldMapRunnerModelToDataClass() {
        final var testDistance = new DistanceModel("testDistance");
        final var runner = new RunnerModel("testFirstName", "testLastName", "testClub", testDistance);

        final RunnerData mappedRunner = runnerMapper.mapToData(runner);

        assertThat(mappedRunner.publicId()).isEqualTo(runner.getPublicId().toString());
        assertThat(mappedRunner.firstName()).isEqualTo(runner.getFirstName());
        assertThat(mappedRunner.lastName()).isEqualTo(runner.getLastName());
        assertThat(mappedRunner.club()).isEqualTo(runner.getClub());
        assertThat(mappedRunner.distance()).isEqualTo(testDistance.getName());
    }

    @Test
    void shouldFailFast_whenPassedModelIsNull() {
        assertThatThrownBy(() -> runnerMapper.mapToData(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("RunnerModel"));
    }

    @Test
    void shouldMapCreateMessageToModel() {
        final var runnerCreate = new CreateRunnerMessageData("testFirstName", "testLastName", "testClub", "testDistance");
        final var testDistance = new DistanceModel("testDistance");

        final RunnerModel mappedRunnerModel = runnerMapper.mapToModel(runnerCreate, testDistance);

        assertThat(mappedRunnerModel.getPublicId()).isNotNull();
        assertThat(mappedRunnerModel.getFirstName()).isEqualTo(runnerCreate.getFirstName());
        assertThat(mappedRunnerModel.getLastName()).isEqualTo(runnerCreate.getLastName());
        assertThat(mappedRunnerModel.getClub()).isEqualTo(runnerCreate.getClub());
        assertThat(mappedRunnerModel.getDistance().getName()).isEqualTo(testDistance.getName());
    }

    @Test
    void shouldFailFast_whenPassedCreationDataIsNull() {
        assertThatThrownBy(() -> runnerMapper.mapToModel(null, new DistanceModel()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("CreateRunnerMessageData"));
    }

    @Test
    void shouldFailFast_whenPassedDistanceModelIsNull() {
        assertThatThrownBy(() -> runnerMapper.mapToModel(new CreateRunnerMessageData(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(NULL_PARAMETER_ERROR_MESSAGE_TEMPLATE.formatted("DistanceModel"));
    }
}
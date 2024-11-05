package com.asankovic.race.command.mappers.impl;

import com.asankovic.race.command.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultRunnerMessageMapperUnitTest {

    private final DefaultRunnerMessageMapper runnerMessageMapper = new DefaultRunnerMessageMapper();

    @Test
    void shouldMapCreationMessage() {
        final var createRunnerData = new CreateRunnerData("testFirstName", "testLastName", "testClub", "testDistance");

        final CreateRunnerMessageData mappedCreateData = runnerMessageMapper.mapToCreationMessage(createRunnerData);

        assertThat(mappedCreateData.getFirstName()).isEqualTo(createRunnerData.getFirstName());
        assertThat(mappedCreateData.getLastName()).isEqualTo(createRunnerData.getLastName());
        assertThat(mappedCreateData.getClub()).isEqualTo(createRunnerData.getClub());
        assertThat(mappedCreateData.getDistanceName()).isEqualTo(createRunnerData.getDistance());
    }

    @Test
    void shouldMapUpdateMessage() {
        final var updateRunnerData = new UpdateRunnerData("testFirstName", "testLastName", "testClub", "testDistance");
        final String updateRunnerID = "updateID";

        final UpdateRunnerMessageData updateRunnerMessageData = runnerMessageMapper.mapToUpdateMessage(updateRunnerData, updateRunnerID);

        assertThat(updateRunnerMessageData.getRunnerPublicID()).isEqualTo(updateRunnerID);
        assertThat(updateRunnerMessageData.getFirstName()).isEqualTo(updateRunnerData.getFirstName());
        assertThat(updateRunnerMessageData.getLastName()).isEqualTo(updateRunnerData.getLastName());
        assertThat(updateRunnerMessageData.getClub()).isEqualTo(updateRunnerData.getClub());
        assertThat(updateRunnerMessageData.getDistanceCode()).isEqualTo(updateRunnerData.getDistanceCode());
    }

    @Test
    void shouldMapToDeleteMessage() {
        final String removeRunnerID = "removeID";

        final DeleteRunnerMessageData deleteRunnerMessageData = runnerMessageMapper.mapToDeleteMessage(removeRunnerID);

        assertThat(deleteRunnerMessageData.getRunnerPublicID()).isEqualTo(removeRunnerID);
    }
}
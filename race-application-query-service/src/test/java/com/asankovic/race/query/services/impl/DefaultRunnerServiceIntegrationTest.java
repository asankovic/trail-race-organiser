package com.asankovic.race.query.services.impl;

import com.asankovic.race.query.IntegrationTestBase;
import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.query.data.dtos.rest.RunnerData;
import com.asankovic.race.query.data.models.RunnerModel;
import com.asankovic.race.query.exceptions.UnknownDistanceException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.repositories.RunnerRepository;
import com.asankovic.race.query.services.RunnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static com.asankovic.race.query.services.impl.DefaultRunnerService.MISSING_DISTANCE_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class DefaultRunnerServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    @Qualifier("defaultRunnerService")
    private RunnerService defaultRunnerService;

    @Autowired
    private RunnerRepository runnerRepository;

    @Test
    void shouldCreateNewRunner() throws UnknownDistanceException {
        final var runnerCreate = new CreateRunnerMessageData("testFirstName", "testLastName", "testClub", "5k");

        final RunnerData createdRunner = defaultRunnerService.createRunner(runnerCreate);

        final RunnerModel savedRunner = runnerRepository.getRunnerByPublicId(UUID.fromString(createdRunner.publicId())).orElseThrow();

        assertThat(savedRunner).isNotNull();
        assertThat(savedRunner.getPublicId()).isNotNull();
        assertThat(savedRunner.getFirstName()).isEqualTo(runnerCreate.getFirstName());
        assertThat(savedRunner.getLastName()).isEqualTo(runnerCreate.getLastName());
        assertThat(savedRunner.getClub()).isEqualTo(runnerCreate.getClub());
        assertThat(savedRunner.getDistance().getName()).isEqualTo(runnerCreate.getDistanceName());
        assertThat(savedRunner.getCreationTime()).isAfterOrEqualTo(Instant.now().minus(1, ChronoUnit.MINUTES));
    }

    @Test
    void shouldNotCreateNewRunner_whenPassedDistanceIsNotValid() {
        final var runnerCreate = new CreateRunnerMessageData("testFirstName", "testLastName", "testClub", "nonExistentDistance");

        assertThatThrownBy(() -> defaultRunnerService.createRunner(runnerCreate))
                .isInstanceOf(UnknownDistanceException.class)
                .hasMessage(MISSING_DISTANCE_ERROR_MESSAGE.formatted(runnerCreate.getDistanceName()));

        final Iterable<RunnerModel> allSavedRunners = runnerRepository.findAll();
        assertThat(allSavedRunners).isEmpty();
    }

    @Test
    void shouldThrowUnknownRunnerException_whenTryingToUpdateRunnerForMissingId() {
        final var runnerUpdate = new UpdateRunnerMessageData();
        runnerUpdate.setRunnerPublicID(UUID.randomUUID().toString());

        assertThatThrownBy(() -> defaultRunnerService.updateRunner(runnerUpdate))
                .isInstanceOf(UnknownRunnerIdException.class);
    }

    @Test
    void shouldUpdateOnlyFirstName() throws UnknownRunnerIdException, UnknownDistanceException {
        final var runnerCreate = new CreateRunnerMessageData("testFirstName", "testLastName", "testClub", "5k");
        final RunnerData createdRunner = defaultRunnerService.createRunner(runnerCreate);

        final var runnerUpdate = new UpdateRunnerMessageData();
        runnerUpdate.setRunnerPublicID(createdRunner.publicId());
        runnerUpdate.setFirstName("changedFirstName");

        defaultRunnerService.updateRunner(runnerUpdate);

        final Optional<RunnerModel> savedRunner = runnerRepository.getRunnerByPublicId(UUID.fromString(createdRunner.publicId()));

        assertThat(savedRunner).isPresent().hasValueSatisfying(runner -> {
            assertThat(runner.getPublicId()).hasToString(createdRunner.publicId());
            assertThat(runner.getFirstName()).isEqualTo(runnerUpdate.getFirstName());
            assertThat(runner.getLastName()).isEqualTo(createdRunner.lastName());
            assertThat(runner.getClub()).isEqualTo(createdRunner.club());
            assertThat(runner.getDistance().getName()).isEqualTo(createdRunner.distance());
        });
    }

    @Test
    void shouldUpdateOnlyLastName() throws UnknownRunnerIdException, UnknownDistanceException {
        final RunnerData createdRunner = createTestRunner();
        final var runnerUpdate = new UpdateRunnerMessageData();
        runnerUpdate.setRunnerPublicID(createdRunner.publicId());
        runnerUpdate.setLastName("changedLastName");

        defaultRunnerService.updateRunner(runnerUpdate);

        final Optional<RunnerModel> savedRunner = runnerRepository.getRunnerByPublicId(UUID.fromString(createdRunner.publicId()));

        assertThat(savedRunner).isPresent().hasValueSatisfying(runner -> {
            assertThat(runner.getPublicId()).hasToString(createdRunner.publicId());
            assertThat(runner.getFirstName()).isEqualTo(createdRunner.firstName());
            assertThat(runner.getLastName()).isEqualTo(runnerUpdate.getLastName());
            assertThat(runner.getClub()).isEqualTo(createdRunner.club());
            assertThat(runner.getDistance().getName()).isEqualTo(createdRunner.distance());
        });
    }

    @Test
    void shouldUpdateOnlyDistance() throws UnknownRunnerIdException, UnknownDistanceException {
        final RunnerData createdRunner = createTestRunner();
        final var runnerUpdate = new UpdateRunnerMessageData();
        runnerUpdate.setRunnerPublicID(createdRunner.publicId());
        runnerUpdate.setDistanceCode("10k");

        defaultRunnerService.updateRunner(runnerUpdate);

        final Optional<RunnerModel> savedRunner = runnerRepository.getRunnerByPublicId(UUID.fromString(createdRunner.publicId()));

        assertThat(savedRunner).isPresent().hasValueSatisfying(runner -> {
            assertThat(runner.getPublicId()).hasToString(createdRunner.publicId());
            assertThat(runner.getFirstName()).isEqualTo(createdRunner.firstName());
            assertThat(runner.getLastName()).isEqualTo(createdRunner.lastName());
            assertThat(runner.getClub()).isEqualTo(createdRunner.club());
            assertThat(runner.getDistance().getName()).isEqualTo(runnerUpdate.getDistanceCode());
        });
    }

    @Test
    void shouldThrowUnknownDistanceException_whenUpdatingToAnUnknownDistance() throws UnknownDistanceException {
        final RunnerData createdRunner = createTestRunner();
        final var runnerUpdate = new UpdateRunnerMessageData();
        runnerUpdate.setRunnerPublicID(createdRunner.publicId());
        runnerUpdate.setDistanceCode("nonExistentDistance");

        assertThatThrownBy(() -> defaultRunnerService.updateRunner(runnerUpdate))
                .isInstanceOf(UnknownDistanceException.class);
    }

    @Test
    void shouldDeleteRunnerByPublicId() throws UnknownDistanceException {
        final RunnerData createdRunner = createTestRunner();
        final var runnerDelete = new DeleteRunnerMessageData(createdRunner.publicId());

        defaultRunnerService.deleteRunner(runnerDelete);

        final Optional<RunnerModel> savedRunner = runnerRepository.getRunnerByPublicId(UUID.fromString(createdRunner.publicId()));
        assertThat(savedRunner).isEmpty();
    }

    @Test
    void shouldThrowUnknownRunnerException_whenRunnerWithPublicIdDoesNodExist() throws UnknownDistanceException {
        createTestRunner();

        assertThatThrownBy(() -> defaultRunnerService.getRunner(UUID.randomUUID().toString()))
                .isInstanceOf(UnknownRunnerIdException.class);
    }

    private RunnerData createTestRunner() throws UnknownDistanceException {
        final var runnerCreate = new CreateRunnerMessageData("testFirstName", "testLastName", "testClub", "5k");
        return defaultRunnerService.createRunner(runnerCreate);
    }
}
package com.asankovic.race.query.repositories;

import com.asankovic.race.query.IntegrationTestBase;
import com.asankovic.race.query.data.models.DistanceModel;
import com.asankovic.race.query.data.models.RunnerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
class RunnerRepositoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private DistanceRepository distanceRepository;

    private DistanceModel testDistance;

    @BeforeEach
    void setUp() {
        testDistance = distanceRepository.getDistanceByName("5k").orElseThrow();
    }

    @Test
    void shouldReturnRunner_whenExistingUUIDIsPassed() {
        final var testRunner = new RunnerModel("testFirstName", "testLastName", "testClub", testDistance);
        runnerRepository.save(testRunner);

        final Optional<RunnerModel> foundRunner = runnerRepository.getRunnerByPublicId(testRunner.getPublicId());

        assertThat(foundRunner).isPresent().hasValueSatisfying(runner -> {
            assertThat(runner.getPublicId()).isEqualTo(testRunner.getPublicId());
            assertThat(runner.getFirstName()).isEqualTo(testRunner.getFirstName());
            assertThat(runner.getLastName()).isEqualTo(testRunner.getLastName());

            assertThat(runner.getDistance()).isEqualTo(testRunner.getDistance());
            assertThat(runner.getLastModifiedTime()).isAfterOrEqualTo(Instant.now().minus(1, ChronoUnit.MINUTES));
        });
    }

    @Test
    void shouldReturnEmptyRunner_whenNullUUIDIsPassed() {
        final Optional<RunnerModel> foundRunner = runnerRepository.getRunnerByPublicId(null);

        assertThat(foundRunner).isEmpty();
    }

    @Test
    void shouldReturnLimitedNumberOfRunnersOrderedByPk_whenMultipleExist() {
        final RunnerModel firstRunner = new RunnerModel("firstFirstName", "firstLastName", "firstClub", testDistance);
        final RunnerModel secondRunner = new RunnerModel("secondFirstName", "secondLastName", "secondClub", testDistance);
        runnerRepository.save(firstRunner);
        runnerRepository.save(secondRunner);

        final int runnersToGet = 1;
        final List<RunnerModel> lastModifiedRunners = runnerRepository.getLatestRunners(runnersToGet);

        assertThat(lastModifiedRunners).hasSize(runnersToGet);
        final RunnerModel lastModifiedRunner = lastModifiedRunners.getFirst();
        assertThat(lastModifiedRunner.getPublicId()).isEqualTo(secondRunner.getPublicId());
        assertThat(lastModifiedRunner.getFirstName()).isEqualTo(secondRunner.getFirstName());
        assertThat(lastModifiedRunner.getLastName()).isEqualTo(secondRunner.getLastName());
        assertThat(lastModifiedRunner.getClub()).isEqualTo(secondRunner.getClub());
    }
}
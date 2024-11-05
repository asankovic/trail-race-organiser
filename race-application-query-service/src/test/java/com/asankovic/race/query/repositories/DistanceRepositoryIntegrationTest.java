package com.asankovic.race.query.repositories;

import com.asankovic.race.query.IntegrationTestBase;
import com.asankovic.race.query.data.models.DistanceModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
class DistanceRepositoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    private DistanceRepository distanceRepository;

    @Test
    void shouldReturnDistanceByName() {
        final DistanceModel newDistance = new DistanceModel("testDistance");
        distanceRepository.save(newDistance);

        final Optional<DistanceModel> foundDistance = distanceRepository.getDistanceByName(newDistance.getName());

        assertThat(foundDistance).isPresent().hasValueSatisfying(distance ->
                assertThat(distance.getName()).isEqualTo(newDistance.getName())
        );
    }
}
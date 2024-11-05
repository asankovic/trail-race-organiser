package com.asankovic.race.query.repositories;

import com.asankovic.race.query.data.models.RunnerModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RunnerRepository extends CrudRepository<RunnerModel, Long> {

    Optional<RunnerModel> getRunnerByPublicId(UUID uuid);

    void deleteRunnerByPublicId(UUID uuid);

    @Query("SELECT r FROM " + RunnerModel.TYPE_CODE + " r ORDER BY r.pk DESC LIMIT :limit")
    List<RunnerModel> getLatestRunners(@Param("limit") int limit);
}

package com.asankovic.race.query.repositories;

import com.asankovic.race.query.data.models.DistanceModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DistanceRepository extends CrudRepository<DistanceModel, Integer> {

    Optional<DistanceModel> getDistanceByName(String name);
}

package com.programmers.handyV.station.repository;

import java.util.List;
import java.util.Optional;

import com.programmers.handyV.station.domain.Station;

public interface StationRepository {
    Station save(Station station);

    List<Station> findAll();

    List<Station> findByPartialName(String partialName);

    Optional<Station> findByName(String name);

    default boolean existsByName(String name) {
        return findByName(name).isPresent();
    }
}

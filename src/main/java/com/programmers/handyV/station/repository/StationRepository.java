package com.programmers.handyV.station.repository;

import java.util.Optional;

import com.programmers.handyV.station.domain.Station;

public interface StationRepository {
    Station save(Station station);
    Optional<Station> findByName(String name);
    default boolean existsByName(String name) {
        return findByName(name).isPresent();
    }
}

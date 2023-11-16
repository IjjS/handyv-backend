package com.programmers.handyV.charger.repository;

import com.programmers.handyV.charger.domain.Charger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChargerRepository {
    Charger save(Charger charger);

    List<Charger> findAll();

    List<Charger> findByStationId(UUID stationId);

    Optional<Charger> findById(UUID chargerId);

    void refreshStatus();
}

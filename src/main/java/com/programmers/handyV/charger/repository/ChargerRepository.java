package com.programmers.handyV.charger.repository;

import java.util.List;
import java.util.UUID;

import com.programmers.handyV.charger.domain.Charger;

public interface ChargerRepository {
    Charger save(Charger charger);

    List<Charger> findByStationId(UUID stationId);

    void refreshStatus();
}

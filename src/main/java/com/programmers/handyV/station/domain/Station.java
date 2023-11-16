package com.programmers.handyV.station.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Station {
    private final UUID stationId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String name;

    public Station(String name) {
        //TODO: validation
        this(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), name);
    }

    public Station(UUID stationId, LocalDateTime createdAt, LocalDateTime updatedAt, String name) {
        this.stationId = stationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
    }

    public UUID getStationId() {
        return stationId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }
}

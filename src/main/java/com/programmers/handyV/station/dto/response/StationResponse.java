package com.programmers.handyV.station.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.programmers.handyV.station.domain.Station;

public record StationResponse(UUID stationId, LocalDateTime createdAt, LocalDateTime updatedAt, String name) {
    public static StationResponse from(Station station) {
        return new StationResponse(station.getStationId(), station.getCreatedAt(), station.getUpdatedAt(), station.getName());
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .toList();
    }
}

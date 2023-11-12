package com.programmers.handyV.station.dto.response;

import java.util.UUID;

import com.programmers.handyV.station.domain.Station;

public record CreateStationResponse(UUID stationId, String name) {
    public static CreateStationResponse from(Station station) {
        return new CreateStationResponse(station.getStationId(), station.getName());
    }
}

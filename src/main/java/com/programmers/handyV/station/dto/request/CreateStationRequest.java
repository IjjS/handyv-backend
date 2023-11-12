package com.programmers.handyV.station.dto.request;

import com.programmers.handyV.station.domain.Station;

public record CreateStationRequest(String name) {
    public Station toStation() {
        return new Station(name);
    }
}

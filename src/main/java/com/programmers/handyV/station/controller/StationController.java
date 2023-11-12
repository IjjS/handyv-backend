package com.programmers.handyV.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmers.handyV.station.dto.request.CreateStationRequest;
import com.programmers.handyV.station.dto.response.CreateStationResponse;
import com.programmers.handyV.station.service.StationService;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<CreateStationResponse> create(CreateStationRequest request) {
        return ResponseEntity.ok(stationService.create(request));
    }
}

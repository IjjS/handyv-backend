package com.programmers.handyV.station.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmers.handyV.station.dto.request.CreateStationRequest;
import com.programmers.handyV.station.dto.response.StationResponse;
import com.programmers.handyV.station.service.StationService;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findAll(Optional<String> partialName) {
        return ResponseEntity.ok(stationService.findAll(partialName));
    }

    @PostMapping
    public ResponseEntity<StationResponse> create(CreateStationRequest request) {
        return ResponseEntity.ok(stationService.create(request));
    }
}

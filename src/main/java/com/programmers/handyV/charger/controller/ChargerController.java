package com.programmers.handyV.charger.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programmers.handyV.charger.dto.request.ConductBookingRequest;
import com.programmers.handyV.charger.dto.request.CreateChargerRequest;
import com.programmers.handyV.charger.dto.response.ChargerResponse;
import com.programmers.handyV.charger.dto.response.ConductBookingResponse;
import com.programmers.handyV.charger.dto.response.CreateChargerResponse;
import com.programmers.handyV.charger.service.ChargerService;

@RestController
@RequestMapping("/api/v1/chargers")
public class ChargerController {
    private final ChargerService chargerService;

    public ChargerController(ChargerService chargerService) {
        this.chargerService = chargerService;
    }

    @PostMapping
    public ResponseEntity<CreateChargerResponse> create(CreateChargerRequest request) {
        return ResponseEntity.ok(chargerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ChargerResponse>> findAll(@RequestParam Optional<UUID> stationId) {
        return ResponseEntity.ok(chargerService.enterFindAll(stationId));
    }

    @PostMapping("/{chargerId}")
    public ResponseEntity<ConductBookingResponse> conductBooking(@PathVariable UUID chargerId, @ModelAttribute ConductBookingRequest request) {
        return ResponseEntity.ok(chargerService.conductBooking(chargerId, request));
    }
}

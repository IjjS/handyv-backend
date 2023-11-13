package com.programmers.handyV.charger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmers.handyV.charger.dto.request.CreateChargerRequest;
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
}

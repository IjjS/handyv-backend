package com.programmers.handyV.charger.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.charger.domain.ChargerStatus;

public record ChargerResponse(UUID chargerId, LocalDateTime createdAt, LocalDateTime updatedAt, String hashName, ChargerStatus chargerStatus, LocalDateTime bookedAt) {
    public static ChargerResponse from(Charger charger) {
        return new ChargerResponse(charger.getChargerId(), charger.getCreatedAt(), charger.getUpdatedAt(), charger.getHashName(), charger.getChargerStatus(), charger.getBookedAt());
    }

    public static List<ChargerResponse> listOf(List<Charger> chargers) {
        return chargers.stream()
                .map(ChargerResponse::from)
                .toList();
    }
}

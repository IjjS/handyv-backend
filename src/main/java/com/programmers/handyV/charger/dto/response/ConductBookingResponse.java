package com.programmers.handyV.charger.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.user.domain.CarNumber;

public record ConductBookingResponse(UUID chargerId, String hashName, LocalDateTime bookedAt, String carFullNumber) {
    public static ConductBookingResponse of(Charger charger, CarNumber carNumber) {
        return new ConductBookingResponse(
                charger.getChargerId(), charger.getHashName(), charger.getBookedAt(), carNumber.getFullNumber());
    }
}

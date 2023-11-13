package com.programmers.handyV.charger.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Charger {
    private final UUID chargerId;
    private final LocalDateTime createdAt;
    private final ChargerType chargerType;
    private final UUID stationId;
    private LocalDateTime updatedAt;
    private ChargerStatus chargerStatus;
    private LocalDateTime bookedAt;
    private UUID userId;

    public Charger(UUID chargerId, LocalDateTime createdAt, LocalDateTime updatedAt, ChargerType chargerType, ChargerStatus chargerStatus, LocalDateTime bookedAt, UUID stationId, UUID userId) {
        this.chargerId = chargerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.chargerType = chargerType;
        this.chargerStatus = chargerStatus;
        this.bookedAt = bookedAt;
        this.stationId = stationId;
        this.userId = userId;
    }

    public static Charger createCharger(String chargerTypeInput, UUID stationId) {
        ChargerType chargerType = ChargerType.findChargerTypeByName(chargerTypeInput);
        return new Charger(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), chargerType, ChargerStatus.AVAILABLE, null, stationId, null);
    }

    public UUID getChargerId() {
        return chargerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getChargerTypeName() {
        return chargerType.displayName();
    }

    public ChargerStatus getChargerStatus() {
        return chargerStatus;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public UUID getStationId() {
        return stationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getHashName() {
        return getChargerTypeName() + " " + chargerId.toString().substring(0, 4);
    }

    public void conductBooking(UUID userId) {
        chargerStatus = ChargerStatus.BOOKED;
        this.userId = userId;
        bookedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}

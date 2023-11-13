package com.programmers.handyV.charger.dto.request;

import java.util.UUID;

public record CreateChargerRequest(String chargerType, UUID stationId) {
}

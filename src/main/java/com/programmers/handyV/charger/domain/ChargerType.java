package com.programmers.handyV.charger.domain;

import com.programmers.handyV.common.exception.BadRequestException;
import java.util.Arrays;
import java.util.Objects;

public enum ChargerType {
    STANDARD("완속"),
    QUICK("급속"),
    HI("초고속");

    private final String chargerTypeName;

    ChargerType(String chargerTypeName) {
        this.chargerTypeName = chargerTypeName;
    }

    public static ChargerType findChargerTypeByName(String name) {
        return Arrays.stream(ChargerType.values())
                .filter(chargerType -> chargerType.isMatching(name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("잘못된 충전기 타입입니다."));
    }

    public String displayName() {
        return chargerTypeName;
    }

    private boolean isMatching(String name) {
        return Objects.equals(chargerTypeName, name);
    }
}

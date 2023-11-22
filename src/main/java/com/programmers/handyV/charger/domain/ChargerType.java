package com.programmers.handyV.charger.domain;

import com.programmers.handyV.common.exception.BadRequestException;
import java.util.Arrays;
import java.util.Objects;

public enum ChargerType {
    STANDARD_AC("완속 AC단상"),
    QUICK_AC3("급속 AC3상"),
    QUICK_DC_CHADEMO("급속 DC차데모"),
    QUICK_DC_COMBO("급속 DC콤보"),
    HI_DC_COMBO("초급속 DC콤보");

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

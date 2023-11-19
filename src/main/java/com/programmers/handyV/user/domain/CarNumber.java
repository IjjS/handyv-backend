package com.programmers.handyV.user.domain;

import com.programmers.handyV.common.exception.BadRequestException;

public class CarNumber {
    private final String frontNumber;
    private final String backNumber;

    public CarNumber(String fullNumber) {
        String[] carNumber = fullNumber.split(" ");
        validateFullNumber(carNumber);
        this.frontNumber = carNumber[0];
        this.backNumber = carNumber[1];
    }

    public CarNumber(String frontNumber, String backNumber) {
        this.frontNumber = frontNumber;
        this.backNumber = backNumber;
    }

    public String getFullNumber() {
        return frontNumber + " " + backNumber;
    }

    public void validateFullNumber(String[] carNumber) {
        if (carNumber.length != 2) {
            throw new BadRequestException("차 번호 형식이 잘못되었습니다. 띄어쓰기로 구분해주세요 (ex.11가 1111)");
        }
    }
}

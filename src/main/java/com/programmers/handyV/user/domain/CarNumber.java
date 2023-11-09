package com.programmers.handyV.user.domain;

public class CarNumber {
    private final String frontNumber;
    private final String backNumber;

    public CarNumber(String frontNumber, String backNumber) {
        this.frontNumber = frontNumber;
        this.backNumber = backNumber;
    }

    public String getFullNumber() {
        return frontNumber + " " + backNumber;
    }
}

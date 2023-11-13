package com.programmers.handyV.user.domain;

public class CarNumber {
    private final String frontNumber;
    private final String backNumber;

    public CarNumber(String fullNumber) {
        String[] carNumber = fullNumber.split(" ");
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
}

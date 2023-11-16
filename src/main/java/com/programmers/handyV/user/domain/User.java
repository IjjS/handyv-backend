package com.programmers.handyV.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final CarNumber carNumber;
    private final UserAuthority authority;

    public User(UUID userId,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                CarNumber carNumber,
                UserAuthority authority) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.carNumber = carNumber;
        this.authority = authority;
    }

    public static User createNormalUser(String frontNumber, String backNumber) {
        //TODO: validation (car number reg ex)
        CarNumber carNumber = new CarNumber(frontNumber, backNumber);
        return new User(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), carNumber, UserAuthority.NORMAL);
    }

    public static User createAdminUser(CarNumber carNumber) {
        return new User(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), carNumber, UserAuthority.ADMIN);
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCarFullNumber() {
        return carNumber.getFullNumber();
    }

    public UserAuthority getAuthority() {
        return authority;
    }
}

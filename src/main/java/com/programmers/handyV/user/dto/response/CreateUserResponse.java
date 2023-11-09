package com.programmers.handyV.user.dto.response;

import java.util.UUID;

import com.programmers.handyV.user.domain.User;

public record CreateUserResponse(UUID userId, String carNumber) {
    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(user.getUserId(), user.getCarFullNumber());
    }
}

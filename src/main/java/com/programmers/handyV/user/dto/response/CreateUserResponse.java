package com.programmers.handyV.user.dto.response;

import com.programmers.handyV.user.domain.User;
import java.util.UUID;

public record CreateUserResponse(UUID userId, String carNumber) {
    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(user.getUserId(), user.getCarFullNumber());
    }
}

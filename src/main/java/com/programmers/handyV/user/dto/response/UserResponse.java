package com.programmers.handyV.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.domain.UserAuthority;

public record UserResponse(UUID userId, LocalDateTime createdAt, LocalDateTime updatedAt, String carFullNumber, UserAuthority userAuthority) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getCreatedAt(), user.getUpdatedAt(), user.getCarFullNumber(), user.getAuthority());
    }

    public static List<UserResponse> listOf(List<User> users) {
        return users.stream()
                .map(UserResponse::from)
                .toList();
    }
}

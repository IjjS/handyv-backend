package com.programmers.handyV.user.dto.request;

public record CreateUserRequest(Boolean isAdmin, String frontNumber, String backNumber) {
}

package com.programmers.handyV.common.advice;

public class ErrorResponse {
    private final String message;

    public ErrorResponse(RuntimeException exception) {
        this.message = exception.getMessage();
    }

    public String getMessage() {
        return message;
    }
}

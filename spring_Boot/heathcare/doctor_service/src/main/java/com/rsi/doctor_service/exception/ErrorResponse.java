package com.rsi.doctor_service.exception;

public record ErrorResponse(
        String code,
        String message) {
}

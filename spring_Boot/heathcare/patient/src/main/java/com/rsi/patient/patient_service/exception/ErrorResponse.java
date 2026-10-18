package com.rsi.patient.patient_service.exception;

public record ErrorResponse(
        String code,
        String message) {}

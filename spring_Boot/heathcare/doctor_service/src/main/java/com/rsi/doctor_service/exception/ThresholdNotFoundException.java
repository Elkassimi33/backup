package com.rsi.doctor_service.exception;

public class ThresholdNotFoundException extends RuntimeException {
    public ThresholdNotFoundException(String msg) {
        super(msg);
    }
}

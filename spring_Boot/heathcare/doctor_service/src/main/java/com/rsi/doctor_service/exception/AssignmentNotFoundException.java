package com.rsi.doctor_service.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String msg) {
        super(msg);
    }
}

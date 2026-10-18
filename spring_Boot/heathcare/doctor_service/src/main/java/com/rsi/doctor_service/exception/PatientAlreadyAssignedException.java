package com.rsi.doctor_service.exception;

public class PatientAlreadyAssignedException extends RuntimeException {
    public PatientAlreadyAssignedException(String msg) {
        super(msg);
    }
}

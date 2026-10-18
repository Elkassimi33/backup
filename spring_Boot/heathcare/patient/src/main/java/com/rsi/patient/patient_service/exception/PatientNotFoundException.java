package com.rsi.patient.patient_service.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String msg) {
        super(msg);
    }

}

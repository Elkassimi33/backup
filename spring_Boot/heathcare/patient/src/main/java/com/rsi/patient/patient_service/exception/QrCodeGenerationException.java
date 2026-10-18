package com.rsi.patient.patient_service.exception;

public class QrCodeGenerationException extends RuntimeException {

    public QrCodeGenerationException(String msg) {
        super(msg);
    }
}

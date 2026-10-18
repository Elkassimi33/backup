package com.rsi.doctor_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DoctorExceptionHandler {

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(DoctorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("DOCTOR_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(PatientAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyAssigned(
            PatientAlreadyAssignedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("PATIENT_ALREADY_ASSIGNED", ex.getMessage()));
    }

    @ExceptionHandler(ThresholdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleThresholdNotFound(
            ThresholdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("THRESHOLD_NOT_FOUND", ex.getMessage()));
    }
}

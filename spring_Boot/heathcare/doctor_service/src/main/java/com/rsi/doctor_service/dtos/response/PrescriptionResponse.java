package com.rsi.doctor_service.dtos.response;

import java.time.LocalDate;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.PrescriptionStatus;

public record PrescriptionResponse(
        UUID id,
        UUID patientId,
        UUID doctorId,
        String doctorName,
        String medication,
        String dosage,
        String frequency,
        String instructions,
        LocalDate startDate,
        LocalDate endDate,
        PrescriptionStatus status,
        boolean isActive,
        long remainingDays) {
}

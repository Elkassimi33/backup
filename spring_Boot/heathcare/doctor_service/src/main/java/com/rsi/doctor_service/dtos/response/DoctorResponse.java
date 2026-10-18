package com.rsi.doctor_service.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.DoctorStatus;

import lombok.Builder;

@Builder
public record DoctorResponse(
        UUID id,
        String fullName,
        String specialization,
        String licenseNumber,
        String department,
        int yearsOfExperience,
        BigDecimal consultationFee,
        double rating,
        boolean isAvailable,
        DoctorStatus status,
        long activePatientCount) {
}

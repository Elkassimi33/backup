package com.rsi.patient.patient_service.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import lombok.Builder;

@Builder
public record AllergyDto(
        UUID id,
        String substance,
        UUID patientId,
        AllergySeverity severity,
        String reaction,
        LocalDate diagnosedOn) {
}

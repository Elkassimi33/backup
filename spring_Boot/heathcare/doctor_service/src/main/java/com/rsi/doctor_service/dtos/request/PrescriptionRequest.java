package com.rsi.doctor_service.dtos.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrescriptionRequest(
        @NotNull UUID patientId,
        @NotBlank String medication,
        @NotBlank String dosage,
        @NotBlank String frequency,
        String instructions,
        @NotNull LocalDate startDate,
        LocalDate endDate) {
}

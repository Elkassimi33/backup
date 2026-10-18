package com.rsi.doctor_service.dtos.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AssignmentRequest(
        @NotNull UUID patientId,
        @NotNull UUID doctorId,
        String patientCondition,
        UUID nurseId) {
}

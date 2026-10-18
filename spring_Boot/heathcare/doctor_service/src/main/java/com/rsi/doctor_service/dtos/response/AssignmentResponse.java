package com.rsi.doctor_service.dtos.response;

import java.time.LocalDate;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.AssignmentStatus;

import lombok.Builder;

@Builder
public record AssignmentResponse(
        UUID id,
        UUID patientId,
        UUID doctorId,
        String doctorName,
        UUID nurseId,
        LocalDate assignmentDate,
        AssignmentStatus status,
        long daysAdmitted) {
}

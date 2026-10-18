package com.rsi.patient.patient_service.dtos.requestDtos;

import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import com.rsi.patient.patient_service.model.enums.RecordType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicalHistoryRequest(
        @NotNull UUID doctorId,
        @NotBlank String diagnosis,
        @NotBlank String treatment,
        AllergySeverity condition,
        String clinicalNotes,
        RecordType recordType,
        LocalDate followUpDate,
        List<String> attachmentUrls) {
}

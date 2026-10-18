package com.rsi.patient.patient_service.dtos.requestDtos;

import com.rsi.patient.patient_service.model.embedded.GpsLocation;
import com.rsi.patient.patient_service.model.enums.AlertTrigger;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmergencyAlertRequest(
        
    @NotNull AlertTrigger triggeredBy,
    @NotNull(message = "Patient ID is required")
      UUID patientId,
      GpsLocation location,
      String roomNumber,
      String floor,
      Double heartRate,
      Double bloodPressureSystolic,
      Double bloodPressureDiastolic,
      Double oxygenLevel,
      Double temperature,

      String notes
){}
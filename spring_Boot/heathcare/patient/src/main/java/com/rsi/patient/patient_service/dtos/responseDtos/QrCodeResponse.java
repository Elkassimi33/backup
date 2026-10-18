package com.rsi.patient.patient_service.dtos.responseDtos;

import java.util.UUID;

public record QrCodeResponse(
        UUID patientId,
        String qrCodeBase64,
        String qrContent) {
}


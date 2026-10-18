package com.rsi.doctor_service.dtos.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record DischargeRequest(
        @NonNull UUID patientId,
        @NotBlank String summary,
        @NotBlank String instructions) {
}

package com.rsi.doctor_service.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ThresholdRequest(
        @NotNull @Min(20) @Max(60) int minHeartRate,
        @NotNull @Min(80) @Max(200) int maxHeartRate,
        @NotNull int minBPSystolic,
        @NotNull int maxBPSystolic,
        @NotNull int minBPDiastolic,
        @NotNull int maxBPDiastolic,
        @NotNull @Min(80) @Max(100) int minOxygen,
        @NotNull @Min(80) @Max(100) int maxOxygen,
        Double minGlucose,
        Double maxGlucose,
        Double minTemperature,
        Double maxTemperature,
        String notes) {
}

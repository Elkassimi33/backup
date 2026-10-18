package com.rsi.doctor_service.event.consumed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AIPredictionCompletedEvent(
        UUID patientId,
        String riskLevel,
        double riskScore,
        List<String> recommendations,
        String summary,
        String suggestedSpecialist,
        Boolean requiresImmediateAction,
        LocalDateTime occurredAt) {
}

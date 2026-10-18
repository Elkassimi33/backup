package com.rsi.doctor_service.event.consumed;

import java.time.LocalDateTime;
import java.util.UUID;

public record HighRiskAlertEvent(
        UUID patientId, String description,
        LocalDateTime occurredAt) {
}

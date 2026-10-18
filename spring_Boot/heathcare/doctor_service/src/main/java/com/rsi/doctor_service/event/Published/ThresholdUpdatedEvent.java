package com.rsi.doctor_service.event.Published;

import java.util.UUID;
import java.time.LocalDateTime;

public record ThresholdUpdatedEvent(
        UUID patientId, UUID doctorId, LocalDateTime occurredAt) {
}

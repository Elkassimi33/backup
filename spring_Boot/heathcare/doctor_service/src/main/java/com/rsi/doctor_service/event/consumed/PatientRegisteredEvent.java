package com.rsi.doctor_service.event.consumed;

import java.time.LocalDateTime;
import java.util.UUID;

public record PatientRegisteredEvent(
        UUID patientId, String name, LocalDateTime occurredAt) {
}

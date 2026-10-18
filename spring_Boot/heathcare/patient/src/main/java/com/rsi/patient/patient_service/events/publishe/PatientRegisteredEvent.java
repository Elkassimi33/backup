package com.rsi.patient.patient_service.events.publishe;

import java.time.LocalDateTime;
import java.util.UUID;

public record PatientRegisteredEvent(
        UUID eventId,
        UUID patientId,
        String name,
        LocalDateTime occurredAt) {
}

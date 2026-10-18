package com.rsi.doctor_service.event.Published;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorAssignedEvent(
        UUID patientId, UUID doctorId,
        UUID nurseId, LocalDateTime occurredAt) {
}

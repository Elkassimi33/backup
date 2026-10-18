package com.rsi.doctor_service.event.Published;

import java.time.LocalDateTime;
import java.util.UUID;

public record PrescriptionWrittenEvent(
        UUID prescriptionId, UUID patientId,
        UUID doctorId, String medication, LocalDateTime occurredAt) {
}

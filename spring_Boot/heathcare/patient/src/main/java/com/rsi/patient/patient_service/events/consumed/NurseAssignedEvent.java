package com.rsi.patient.patient_service.events.consumed;

import java.util.UUID;

public record NurseAssignedEvent(
        UUID patientId,
        UUID nurseId) {
}

package com.rsi.patient.patient_service.events.consumed;

import java.util.UUID;

public record DoctorAssignedEvent(
        UUID patientId,
        UUID doctorId) {
}

package com.rsi.patient.patient_service.events.publishe ;

import java.time.LocalDateTime ;
import java.util.UUID ;

import com.rsi.patient.patient_service.model.embedded.GpsLocation;
import com.rsi.patient.patient_service.model.enums.AlertTrigger ;
import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import lombok.Builder;

@Builder
public record EmergencyTriggeredEvent(

        String eventId,
        UUID patientId,
        UUID alertId,
        AlertTrigger trigger,
        GpsLocation location,
        AllergySeverity severity,
        String roomNumber,
        Double heartRate ,
        Double oxygenLevel ,
        LocalDateTime triggeredAt ,
        AlertTrigger triggerSource
) {}

package com.rsi.patient.patient_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.rsi.patient.patient_service.model.embedded.GpsLocation;
import com.rsi.patient.patient_service.model.enums.AlertStatus;
import com.rsi.patient.patient_service.model.enums.AlertTrigger;

import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * EmergencyAlert
 */

@Entity
@Table(name = "emergency_alerts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "trigger_source")
    private AlertTrigger triggeredBy;

    @Embedded
    private GpsLocation location;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private AllergySeverity severity; // CRITICAL, HIGH, MEDIUM, LOW

//    @Column(name = "device_id")
//    private UUID deviceId;

    private UUID acknowledgedBy;

    private LocalDateTime acknowledgedAt;

    private LocalDateTime resolvedAt;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "floor", length = 10)
    private String floor;

    // Vital Signs at Emergency
    @Column(name = "heart_rate")
    private Double heartRate;

    @Column(name = "blood_pressure_systolic")
    private Double bloodPressureSystolic;

    @Column(name = "blood_pressure_diastolic")
    private Double bloodPressureDiastolic;

    @Column(name = "oxygen_level")
    private Double oxygenLevel;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "is_false_alarm")
    private Boolean isFalseAlarm;

    @Column(name = "resolved_by")
    private UUID resolvedBy;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @CreationTimestamp
    @Column(name = "triggered_at", updatable = false)
    private LocalDateTime triggeredAt;

    public void acknowledge(UUID userId) {

        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedBy = userId;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public void resolve() {

        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    @PrePersist
    private void prePersist() {

        this.timestamp = LocalDateTime.now();
        this.status = AlertStatus.TRIGGERED;
    }


}

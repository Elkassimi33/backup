package com.rsi.doctor_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Vital_thresholds", indexes = {
        @Index(name = "idx_threshold_patient", columnList = "patient_id"),
        @Index(name = "idx_threshold_doctor", columnList = "doctor_id")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false, unique = true)
    private UUID patientId;

    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctorId;

    // ── Heart Rate ──────────────────────────────────────────
    @Column(nullable = false)
    @Builder.Default
    private int minHeartRate = 50;

    @Column(nullable = false)
    @Builder.Default
    private int maxHeartRate = 12;

    // ── Blood Pressure ──────────────────────────────────────
    @Column(nullable = false)
    @Builder.Default
    private int minBPSystolic = 90;

    @Column(nullable = false)
    @Builder.Default
    private int maxBPSystolic = 140;

    @Column(nullable = false)
    @Builder.Default
    private int minBPDiastolic = 60;

    @Column(nullable = false)
    @Builder.Default
    private int maxBPDiastolic = 90;

    // ── Oxygen Saturation ───────────────────────────────────
    @Column(nullable = false)
    @Builder.Default
    private int minOxygen = 92;

    @Column(nullable = false)
    @Builder.Default
    private int maxOxygen = 100;

    // ── Glucose ─────────────────────────────────────────────
    @Column
    @Builder.Default
    private Double minGlucose = 70.0; // mg/dL

    @Column
    @Builder.Default
    private Double maxGlucose = 180.0;

    // ── Temperature ─────────────────────────────────────────
    @Column
    @Builder.Default
    private Double minTemperature = 36.0; // Celsius

    @Column
    @Builder.Default
    private Double maxTemperature = 38.0;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // ── Business Logic ──────────────────────────────────────
    public boolean isHeartRateNormal(int value) {
        return value >= minHeartRate && value <= maxHeartRate;
    }

    public boolean isOxygenNormal(int value) {
        return value >= minOxygen && value <= maxOxygen;
    }

    public boolean isGlucoseNormal(double value) {
        return value >= minGlucose && value <= maxGlucose;
    }

    public boolean isBPNormal(int systolic, int diastolic) {
        return systolic >= minBPSystolic && systolic <= maxBPSystolic
                && diastolic >= minBPDiastolic && diastolic <= maxBPDiastolic;
    }

    @PrePersist
    @PreUpdate
    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

}

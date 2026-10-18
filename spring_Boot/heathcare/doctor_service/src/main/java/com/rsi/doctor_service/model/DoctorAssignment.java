package com.rsi.doctor_service.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.AssignmentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_assignments", indexes = {
        @Index(name = "idx_assignment_doctor", columnList = "doctor_id"),
        @Index(name = "idx_assignment_patient", columnList = "patient_id"),
        @Index(name = "idx_assignment_status", columnList = "status"),
        @Index(name = "idx_assignment_nurse", columnList = "nurse_id")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "nurse_id")
    private UUID nurseId;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    @Column(name = "patient_condition", columnDefinition = "TEXT")
    private String patientCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    @Builder.Default
    private boolean isDischarged = false;

    private LocalDate dischargeDate;

    @Column(columnDefinition = "TEXT")
    private String dischargeSummary;

    @Column(columnDefinition = "TEXT")
    private String dischargeInstructions;

    public void discharge(String summary, String instructions) {
        this.isDischarged = true;
        this.status = AssignmentStatus.DISCHARGED;
        this.dischargeDate = LocalDate.now();
        this.dischargeSummary = summary;
        this.dischargeInstructions = instructions;
    }

    public long getDaysAdmitted() {
        LocalDate end = isDischarged ? dischargeDate : LocalDate.now();
        return ChronoUnit.DAYS.between(assignmentDate, end);
    }

    @PrePersist
    private void prePersist() {
        this.assignmentDate = LocalDate.now();
        this.status = AssignmentStatus.ACTIVE;
    }
}

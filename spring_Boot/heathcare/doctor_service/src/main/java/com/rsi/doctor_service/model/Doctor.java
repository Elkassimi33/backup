package com.rsi.doctor_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.AssignmentStatus;
import com.rsi.doctor_service.model.enums.DoctorStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctor_specialization", columnList = "specialization"),
        @Index(name = "idx_doctor_department", columnList = "department")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private int yearsOfExperience;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(nullable = false)
    @Builder.Default
    private double rating = 0.0;

    @Builder.Default
    private int totalRatings = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAvailable = true;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DoctorAssignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void updateRating(double newRating) {
        this.totalRatings++;
        this.rating = ((this.rating * (totalRatings - 1)) + newRating) / totalRatings;
    }

    public long getActivePatientCount() {
        return assignments.stream()
                .filter(a -> a.getStatus() == AssignmentStatus.ACTIVE)
                .count();
    }

    public boolean isResponsibleFor(UUID patientId) {
        return assignments.stream()
                .filter(a -> a.getStatus() == AssignmentStatus.ACTIVE)
                .anyMatch(a -> a.getPatientId().equals(patientId));
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = DoctorStatus.ACTIVE;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

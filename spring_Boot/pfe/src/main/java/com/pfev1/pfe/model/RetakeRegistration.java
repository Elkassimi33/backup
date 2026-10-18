package com.pfev1.pfe.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RetakeRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private RetakeSession session;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "original_grade_id", nullable = false)
    private Grade originalGrade;

    @ManyToOne
    @JoinColumn(name = "final_grade_id")
    private Grade finalGrade;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.REGISTERED;

    public enum RegistrationStatus {
        REGISTERED, PASSED, FAILED
    }
}
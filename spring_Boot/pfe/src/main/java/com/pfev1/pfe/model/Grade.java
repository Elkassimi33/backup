package com.pfev1.pfe.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teaching_id", nullable = false)
    private Teaching teaching;

    @Column(nullable = false)
    private Double grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type", nullable = false)
    private GradeType gradeType;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Column(nullable = false)
    private Integer semester;

    @Lob
    private String comment;

    @ManyToOne
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    public enum GradeType {
        NORMAL, RETAKE
    }
}

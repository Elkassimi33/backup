package com.pfev1.pfe.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(unique = true, nullable = false)
    private String subjectName;

    @Lob
    private String description;

    private boolean isCore = true;
}

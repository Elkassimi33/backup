package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Module module;

    @ManyToOne
    private EmploiDuTemps emploiDuTemps;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean present;
    private String justification;
} 
package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "semester_notes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"etudiant_id", "semester", "annee_universitaire_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SemesterNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    @NotNull(message = "L'étudiant ne peut pas être null")
    private Etudiant etudiant;

    @DecimalMin(value = "0.0", message = "La note ne peut pas être inférieure à 0")
    @DecimalMax(value = "20.0", message = "La note ne peut pas être supérieure à 20")
    @Column(nullable = false)
    private Double note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSemester statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Module.Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    @NotNull(message = "L'année universitaire ne peut pas être null")
    private AnneeUniversitaire anneeUniversitaire;

    public enum StatutSemester {
        VALIDE, NON_VALIDE
    }
}
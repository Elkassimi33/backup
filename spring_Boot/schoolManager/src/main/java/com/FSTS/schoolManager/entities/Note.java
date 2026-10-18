package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_note")
    private Integer idNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    @NotNull(message = "L'enseignant ne peut pas être null")
    private Enseignant enseignant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    @NotNull(message = "L'étudiant ne peut pas être null")
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    @NotNull(message = "Le module ne peut pas être null")
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @DecimalMin(value = "0.0", message = "La note ne peut pas être inférieure à 0")
    @DecimalMax(value = "20.0", message = "La note ne peut pas être supérieure à 20")
    @Column(nullable = false)
    private Double note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Module.Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    @NotNull(message = "L'année universitaire ne peut pas être null")
    private AnneeUniversitaire anneeUniversitaire;

    @Column(name = "date_notation")
    private LocalDate dateNotation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeExamen typeExamen = TypeExamen.NORMAL;

    @Column(name = "est_tp")
    private Boolean estTp = false;

    @Enumerated(EnumType.STRING)
    private NoteFinal.StatutNote statut;

    public enum TypeExamen {
        NORMAL, RATTRAPAGE
    }
}
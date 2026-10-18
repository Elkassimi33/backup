package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "emplois_du_temps", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"salle_id", "jour_semaine", "heure_debut", "annee_universitaire_id"}),
        @UniqueConstraint(columnNames = {"enseignant_id", "jour_semaine", "heure_debut", "annee_universitaire_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmploiDuTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    @NotNull(message = "Le module ne peut pas être null")
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    @NotNull(message = "L'enseignant ne peut pas être null")
    private Enseignant enseignant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    @NotNull(message = "La filière ne peut pas être null")
    private Filiere filiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id")
    @NotNull(message = "La salle ne peut pas être null")
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    @NotNull(message = "L'année universitaire ne peut pas être null")
    private AnneeUniversitaire anneeUniversitaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "jour_semaine", nullable = false)
    private DayOfWeek jourSemaine;

    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Module.Semester semestre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCours typeCours;

    @Column(nullable = false)
    private Integer groupe = 1;

    @Column(name = "est_valide", nullable = false)
    private Boolean estValide = true;

    @Column(name = "commentaire")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek jour;

    public enum TypeCours {
        COURS, TD, TP, EXAMEN, RATTRAPAGE, CONTROLE_CONTINU
    }
}

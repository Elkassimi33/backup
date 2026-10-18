package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"etudiant_id", "annee_universitaire_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    @NotNull(message = "La filière ne peut pas être null")
    private Filiere filiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    @NotNull(message = "L'étudiant ne peut pas être null")
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    @NotNull(message = "L'année universitaire ne peut pas être null")
    private AnneeUniversitaire anneeUniversitaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    public enum StatutInscription {
        EN_COURS,
        VALIDE,
        ANNULE
    }
}

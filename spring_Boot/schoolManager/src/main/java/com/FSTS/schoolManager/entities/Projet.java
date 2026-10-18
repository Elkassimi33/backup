package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projets")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeProjet type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projet_etudiants",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    @Builder.Default
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encadrant_interne_id")
    @NotNull(message = "L'encadrant interne ne peut pas être null")
    private Enseignant encadrantInterne;

    @Column(name = "encadrant_externe")
    private String encadrantExterne;

    @Column(name = "entreprise")
    private String entreprise;

    @Column(name = "adresse_entreprise")
    private String adresseEntreprise;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "date_soutenance")
    private LocalDate dateSoutenance;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projet_jury",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "enseignant_id")
    )
    @Builder.Default
    private Set<Enseignant> jury = new HashSet<>();

    @Column(name = "note_finale")
    private Double noteFinale;

    @Column(name = "note_rapport")
    private Double noteRapport;

    @Column(name = "note_soutenance")
    private Double noteSoutenance;

    @Column(name = "note_encadrant")
    private Double noteEncadrant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutProjet statut = StatutProjet.EN_COURS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    @NotNull(message = "L'année universitaire ne peut pas être null")
    private AnneeUniversitaire anneeUniversitaire;

    @Column(name = "technologies_utilisees")
    private String technologiesUtilisees;

    @Column(name = "lien_github")
    private String lienGithub;

    @Column(name = "lien_rapport")
    private String lienRapport;

    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;

    public enum TypeProjet {
        PFE,
        STAGE_OUVRIER,
        STAGE_TECHNIQUE,
        STAGE_INGENIEUR,
        PROJET_TUTORE,
        PROJET_RECHERCHE
    }

    public enum StatutProjet {
        EN_RECHERCHE,
        EN_COURS,
        RAPPORT_DEPOSE,
        SOUTENANCE_PLANIFIEE,
        SOUTENANCE_EFFECTUEE,
        VALIDE,
        NON_VALIDE,
        ANNULE
    }
}
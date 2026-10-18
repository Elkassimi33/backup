package com.FSTS.schoolManager.entities;


import com.FSTS.schoolManager.auth.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "etudiants")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_apj")
    private Integer codeApj;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message = "L'utilisateur ne peut pas être null")
    private User userId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(unique = true)
    private String cne;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    private String adresse;

    private String pay;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Format de téléphone invalide")
    private String telephone;

    @Column(name = "annee_actuelle", nullable = false)
    private Integer anneeActuelle = 1;

    private String profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_inscription_id")
    private AnneeUniversitaire dateInscription;

    @Enumerated(EnumType.STRING)
    private Module.Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    private AnneeUniversitaire anneeUniversitaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEtudiant statut = StatutEtudiant.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    private Filiere filiereID;

    public enum StatutEtudiant {
        NORMAL, REDOUBLANT, REDOUBLANT_PARTIEL, RESERVISTE, ABANDON,VALIDE_NIVEAU
    }
}
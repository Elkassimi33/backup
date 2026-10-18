package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import com.FSTS.schoolManager.entities.Etudiant;
import com.FSTS.schoolManager.entities.Filiere;
import com.FSTS.schoolManager.entities.Module;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {

    private Integer codeApj;


    private Integer userId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    private String cne;

    private LocalDate dateNaissance;

    private String adresse;

    private String pay;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Format de téléphone invalide")
    private String telephone;

    private Integer anneeActuelleId ;

    private String profile;

    private String profileUrl;

    private AnneeUniversitaire dateInscription;

    private Module.Semester semester;

    private String  anneeUniversitaire;

    private Etudiant.StatutEtudiant statut ;

    @NotNull
    private Integer filierId;

}

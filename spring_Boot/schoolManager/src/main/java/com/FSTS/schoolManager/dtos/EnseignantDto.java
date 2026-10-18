package com.FSTS.schoolManager.dtos;


import com.FSTS.schoolManager.entities.Enseignant;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantDto {


    private Integer enseignantId;


    private Integer userId;


    private String nom;


    private String prenom;

    private String profil;

    private String profilUrl;

    private String specialite;


    private String grade;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Le numéro de téléphone doit être au format valide")

    private String telephone;



    private String departementNam;


    private Enseignant.StatutEnseignant statut;




}

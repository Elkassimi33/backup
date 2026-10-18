package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Filiere.NiveauFiliere;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiliereDTO {
    private Integer filiereId;

    @NotBlank(message = "Le nom de la filière est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom de la filière doit contenir entre 2 et 100 caractères")
    private String nom;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "La durée des études est obligatoire")
    @Positive(message = "La durée des études doit être positive")
    private Integer dureeEtudes;

    @NotNull(message = "Le niveau de la filière est obligatoire")
    private NiveauFiliere niveau;

    @NotNull(message = "Le département est obligatoire")
    private String departementNam;

    private Integer chefFiliereId;
    private Boolean active;
} 
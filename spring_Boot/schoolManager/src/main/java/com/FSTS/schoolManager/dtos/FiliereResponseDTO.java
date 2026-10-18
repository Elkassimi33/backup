package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Filiere.NiveauFiliere;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiliereResponseDTO {
    private Integer filiereId;
    private String nom;
    private String description;
    private Integer dureeEtudes;
    private NiveauFiliere niveau;
    private DepartementDto departement;
    private EnseignantDto chefFiliere;
    private Boolean active;
    private List<ModuleDto> modules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int nombreModules;
    private String nomComplet;
} 
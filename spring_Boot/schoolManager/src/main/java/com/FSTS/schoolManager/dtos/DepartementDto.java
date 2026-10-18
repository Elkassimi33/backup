package com.FSTS.schoolManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDto {
    private Integer departementId;
    private String nom;
    private String description;
    private String ChefDepartementUsername;
    private Integer nombreEnseignants;
    private Integer nombreFilieres;
} 
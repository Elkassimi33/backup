package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Salle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalleDto {
    private Integer id;
    private String nomSalle;
    private Integer capacite;
    private Salle.TypeSalle type;
    private boolean disponible;
} 
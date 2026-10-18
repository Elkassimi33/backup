package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.Salle;
import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploiDuTempsDto {
    private Long id;
    private Module module;
    private Salle salle;
    private DayOfWeek jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String typeSeance; // CM, TD, TP
    private Integer groupe; // For TD/TP sessions
    private String anneeUniversitaireNam;
} 
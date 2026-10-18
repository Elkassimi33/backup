package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {

    private static final long serialVersionUID = 3L;

    private Long idModule;

    private String nomModule;

    private String codeModule;

    private Integer volumeHoraire;

    private Module.Semester semester;

    private Integer filiere;

    private Integer enseignantid;


    private Integer anneeUniversitaire;

    private boolean hasTp;

}

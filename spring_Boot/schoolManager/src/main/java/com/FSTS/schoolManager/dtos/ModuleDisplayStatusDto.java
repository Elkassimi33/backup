package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Module;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleDisplayStatusDto {
    private Long moduleId;
    private String moduleName;
    private Module.Semester semester;
    private boolean isDisplayed;
    private boolean displayNormal;
    private boolean displayRattrapage;
} 
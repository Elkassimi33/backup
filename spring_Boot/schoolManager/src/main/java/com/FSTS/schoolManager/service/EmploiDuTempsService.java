package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.EmploiDuTempsDto;
import java.time.DayOfWeek;
import java.util.List;

public interface EmploiDuTempsService {
    EmploiDuTempsDto createEmploiDuTemps(EmploiDuTempsDto dto);
    EmploiDuTempsDto updateEmploiDuTemps(Long id, EmploiDuTempsDto dto);
    void deleteEmploiDuTemps(Long id);
    List<EmploiDuTempsDto> getEmploiDuTempsByModule(Long moduleId);
    List<EmploiDuTempsDto> getEmploiDuTempsBySalle(Integer salleId);
    List<EmploiDuTempsDto> getEmploiDuTempsByJour(DayOfWeek jour);
} 
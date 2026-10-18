package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.ModuleDto;
import java.util.List;

public interface ModuleSevice {
    void insertModule(ModuleDto moduleDto);
    void insertModules(List<ModuleDto> moduleDtos);
    
    /**
     * Update an existing module
     * @param moduleId the ID of the module to update
     * @param moduleDto the updated module data
     * @return the updated module
     */
    ModuleDto updateModule(Long moduleId, ModuleDto moduleDto);

    ModuleDto getModulesById(Long moduleId);
    
    /**
     * Delete a module by its ID
     * @param moduleId the ID of the module to delete
     */
    void deleteModule(Long moduleId);

    /**
     * Get all modules taught by a specific teacher in the current academic year
     * @param enseignantId the teacher's ID
     * @return list of modules taught by the teacher
     */
    List<ModuleDto> getModulesByEnseignant(Integer enseignantId);

    /**
     * Get all modules in a specific department for the current academic year
     * @param departementId the department's ID
     * @return list of modules in the department
     */
    List<ModuleDto> getModulesByDepartement(Integer departementId);

    /**
     * Get all modules in a specific filiere for the current academic year
     * @param filiereId the filiere's ID
     * @return list of modules in the filiere
     */
    List<ModuleDto> getModulesByFiliere(Integer filiereId);

    /**
     * Get all modules
     * @return list of all modules
     */
    List<ModuleDto> getAllModules();
}

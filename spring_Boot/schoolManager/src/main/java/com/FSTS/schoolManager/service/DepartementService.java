package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.DepartementDto;
import java.util.List;

public interface DepartementService {
    /**
     * Create a new department
     * @param departementDto the department data
     * @return the created department DTO
     */
    DepartementDto createDepartement(DepartementDto departementDto);

    /**
     * Update an existing department
     * @param departementId the department ID
     * @param departementDto the updated department data
     * @return the updated department DTO
     */
    DepartementDto updateDepartement(Integer departementId, DepartementDto departementDto);

    /**
     * Delete a department
     * @param departementId the department ID
     */
    void deleteDepartement(Integer departementId);

    /**
     * Get a department by ID
     * @param departementId the department ID
     * @return the department DTO
     */
    DepartementDto getDepartementById(Integer departementId);

    /**
     * Get all departments
     * @return list of all department DTOs
     */
    List<DepartementDto> getAllDepartements();

    /**
     * Get department by name
     * @param nom the department name
     * @return the department DTO
     */
    DepartementDto getDepartementByNom(String nom);
} 
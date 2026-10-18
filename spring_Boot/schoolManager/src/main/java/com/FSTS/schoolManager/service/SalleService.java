package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.SalleDto;
import com.FSTS.schoolManager.entities.Salle;

import java.util.List;

public interface SalleService {
    /**
     * Create a new classroom
     * @param salleDto the classroom data
     * @return the created classroom
     */
    Salle createSalle(SalleDto salleDto);

    /**
     * Update an existing classroom
     * @param salleId the classroom ID
     * @param salleDto the updated classroom data
     * @return the updated classroom
     */
    Salle updateSalle(Integer salleId, SalleDto salleDto);

    /**
     * Delete a classroom
     * @param salleId the classroom ID
     */
    void deleteSalle(Integer salleId);

    /**
     * Get a classroom by ID
     * @param salleId the classroom ID
     * @return the classroom
     */
    Salle getSalleById(Integer salleId);

    /**
     * Get all classrooms
     * @return list of all classrooms
     */
    List<Salle> getAllSalles();

    /**
     * Get available classrooms for a specific capacity
     * @param capacity the required capacity
     * @return list of available classrooms
     */
    List<Salle> getAvailableSallesByCapacity(Integer capacity);
} 
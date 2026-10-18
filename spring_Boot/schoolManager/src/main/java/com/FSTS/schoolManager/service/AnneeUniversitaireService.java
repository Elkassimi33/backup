package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import java.util.List;

public interface AnneeUniversitaireService {
    /**
     * Create a new academic year
     * @param anneeUniversitaire the academic year to create
     * @return the created academic year
     */
    AnneeUniversitaire createAnneeUniversitaire(AnneeUniversitaire anneeUniversitaire);

    /**
     * Update an existing academic year
     * @param anneeId the academic year ID
     * @param anneeUniversitaire the updated academic year data
     * @return the updated academic year
     */
    AnneeUniversitaire updateAnneeUniversitaire(Integer anneeId, AnneeUniversitaire anneeUniversitaire);

    /**
     * Delete an academic year
     * @param anneeId the academic year ID
     */
    void deleteAnneeUniversitaire(Integer anneeId);

    /**
     * Get an academic year by ID
     * @param anneeId the academic year ID
     * @return the academic year
     */
    AnneeUniversitaire getAnneeUniversitaireById(Integer anneeId);

    /**
     * Get all academic years
     * @return list of all academic years
     */
    List<AnneeUniversitaire> getAllAnneesUniversitaires();

    /**
     * Get the current academic year
     * @return the current academic year
     */
    AnneeUniversitaire getCurrentAnneeUniversitaire();

    /**
     * Set an academic year as current
     * @param anneeId the academic year ID to set as current
     */
    void setCurrentAnneeUniversitaire(Integer anneeId);
} 
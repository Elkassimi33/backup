package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.Inscription;
import java.util.List;

public interface InscriptionService {
    /**
     * Create a new inscription for a student
     * @param etudiantId the student ID
     * @param anneeId the academic year ID
     * @param filiereId the filiere ID
     * @return the created inscription
     */
    Inscription createInscription(Integer etudiantId, Integer anneeId, Integer filiereId);

    /**
     * Get an inscription by student ID and academic year
     * @param etudiantId the student ID
     * @param anneeId the academic year ID
     * @return the inscription if found
     */
    Inscription getInscription(Integer etudiantId, Integer anneeId);

    /**
     * Get all inscriptions for a student
     * @param etudiantId the student ID
     * @return list of inscriptions
     */
    List<Inscription> getStudentInscriptions(Integer etudiantId);

    /**
     * Get all inscriptions for an academic year
     * @param anneeId the academic year ID
     * @return list of inscriptions
     */
    List<Inscription> getYearInscriptions(Integer anneeId);

    /**
     * Check if a student is already enrolled for an academic year
     * @param etudiantId the student ID
     * @param anneeId the academic year ID
     * @return true if already enrolled
     */
    boolean isStudentEnrolled(Integer etudiantId, Integer anneeId);
} 
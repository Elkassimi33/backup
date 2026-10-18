package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.*;
import com.FSTS.schoolManager.entities.Filiere;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.Note;

import java.util.List;

/**
 * Service interface for managing department head (Chef Filiere) operations.
 * A department head is a teacher who manages a department (filiere).
 */
public interface ChefFiliereService {

    /**
     * Get all filieres managed by a specific teacher
     * @param enseignantId the teacher's ID
     * @return list of filieres managed by the teacher
     */
    List<Filiere> getManagedFilieres(Integer enseignantId);

    /**
     * Get all students in a specific filiere
     * @param filiereId the filiere ID
     * @return list of students in the filiere
     */
    List<EtudiantDto> getFiliereStudents(Integer filiereId);

    /**
     * Get grade statistics for a specific module in the filiere
     * @param moduleId the module ID
     * @return grade statistics for the module
     */
    GradeStatistics getModuleGradeStatistics(Long moduleId);

    /**
     * Get all modules in a specific filiere
     * @param filiereId the filiere ID
     * @return list of modules in the filiere
     */
    List<Module> getFiliereModules(Integer filiereId);

    /**
     * Get all grades for a specific module
     * @param moduleId the module ID
     * @return list of grades for the module
     */
    List<noteDto> getModuleGrades(Long moduleId);

    /**
     * Validate a grade for a module
     * @param noteId the note ID to validate
     * @param validated true if the grade is validated, false otherwise
     */
    void validateGrade(Integer noteId, boolean validated);

    /**
     * Get student statistics for a specific filiere
     * @param filiereId the filiere ID
     * @return list of student statistics
     */
    List<StudentStatistics> getFiliereStudentStatistics(Integer filiereId);

    /**
     * Get all failed students in a specific filiere
     * @param filiereId the filiere ID
     * @return list of failed students
     */
    List<EtudiantDto> getFailedStudents(Integer filiereId);

    /**
     * Get all students at risk of failing in a specific filiere
     * @param filiereId the filiere ID
     * @return list of students at risk
     */
    List<EtudiantDto> getAtRiskStudents(Integer filiereId);

    List<ModuleDisplayStatusDto> getModulesWithDisplayStatus(Integer filiereId);
} 
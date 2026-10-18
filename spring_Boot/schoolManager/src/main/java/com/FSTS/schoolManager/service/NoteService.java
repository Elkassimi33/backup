package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.*;
import com.FSTS.schoolManager.entities.Note;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.NoteFinal;
import com.FSTS.schoolManager.entities.SemesterNote;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing student grades and grade-related operations.
 * This interface defines all the operations for grade management including
 * insertion, retrieval, calculation, and validation of grades.
 */
public interface NoteService {

    /**
     * Insert a list of grades for students in a specific module
     * @param noteList the list of student grades to insert
     * @param idModule the module ID
     * @param idEnseignant the teacher ID
     * @param idFiliere the program/filiere ID
     * @param typeExamen the type of exam (NORMAL or RATTRAPAGE)
     */
    void insertNotes(List<EtudiantNoteDto> noteList, Long idModule, Integer idEnseignant, 
                    Integer idFiliere, Note.TypeExamen typeExamen);

    /**
     * Get all notes for a student in the current academic year
     * @param idEtudiant the student ID
     * @return list of notes for the student
     */
    List<Note> getEtudiantNotesByYear(Integer idEtudiant);

    /**
     * Get grade statistics for a specific module
     * @param moduleId the module ID
     * @return GradeStatistics object containing various statistics
     */
    GradeStatistics getModuleGradeStatistics(Long moduleId);

    /**
     * Get grade history for a student in a specific module
     * @param studentId the student ID
     * @param moduleId the module ID
     * @return list of grade history entries
     */
    List<GradeHistory> getStudentGradeHistory(Integer studentId, Long moduleId);

    /**
     * Validate and correct a grade if necessary
     * @param note the grade to validate
     * @return validated and potentially corrected grade
     */
    Note validateAndCorrectGrade(Note note);

    /**
     * Get student's GPA for a specific semester
     * @param studentId the student ID
     * @param semester the semester
     * @return the GPA
     */
    double getStudentGPA(Integer studentId, Module.Semester semester);

    /**
     * Get comprehensive statistics for a student
     * @param studentId the student ID
     * @return StudentStatistics object containing various student metrics
     */
    StudentStatistics getStudentStatistics(Integer studentId);

    /**
     * Get all notes for a student in a specific module
     * @param studentId the student ID
     * @param moduleId the module ID
     * @return list of notes for the student in the specified module
     */
    List<noteDto> getStudentModuleNotes(Integer studentId, Long moduleId);

    /**
     * Calculate student's GPA for a specific semester
     * @param studentId the student ID
     * @param semester the semester
     * @return the calculated GPA
     */
    double calculateStudentGPA(Integer studentId, Module.Semester semester);

    /**
     * Get all notes for a specific module
     * @param moduleId the module ID
     * @return list of all notes for the module
     */
    List<noteDto> getModuleNotes(Long moduleId);

    /**
     * Update a specific grade
     * @param noteId the note ID to update
     * @param newGrade the new grade value
     */
    void updateNote(Integer noteId, double newGrade);

    /**
     * Delete a specific grade
     * @param noteId the note ID to delete
     */
    void deleteNote(Integer noteId);

    /**
     * Get all failed modules for a student
     * @param studentId the student ID
     * @return list of notes for failed modules
     */
    List<NoteFinal> getStudentFailedModules(Integer studentId);

    /**
     * Get all passed modules for a student
     * @param studentId the student ID
     * @return list of notes for passed modules
     */
    List<NoteFinal> getStudentPassedModules(Integer studentId);

    /**
     * Get student's notes for each module and each semester
     * @param studentId the student ID
     * @return a map of module notes
     */
    Map<String, Map<String, Double>> getEtudiantNote(Integer studentId);

    /**
     * Updates a student's status based on their academic performance
     * @param studentId the student's ID
     */
    void updateStudentStatus(Integer studentId);
}
package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Etudiant;
import com.FSTS.schoolManager.entities.Etudiant.StatutEtudiant;
import com.FSTS.schoolManager.entities.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Etudiant (Student) entities.
 * Provides methods for finding, filtering, and managing student records.
 */
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    /**
     * Finds a student by their CNE (Code National d'Étudiant)
     * @param cne the CNE to search for
     * @return an Optional containing the student if found
     */
    Optional<Etudiant> findByCne(String cne);

    /**
     * Finds a student by their associated user ID
     * @param userId the ID of the associated user
     * @return an Optional containing the student if found
     */
    Optional<Etudiant> findByUserId_Id(Integer userId);

    /**
     * Finds a student by their associated user's email
     * @param email the email to search for
     * @return an Optional containing the student if found
     */
    Optional<Etudiant> findByUserId_Email(String email);

    /**
     * Finds all students in a specific filiere (program)
     * @param filiereId the ID of the filiere
     * @return a list of students in the filiere
     */
    List<Etudiant> findByFiliereID_FiliereId(Integer filiereId);

    /**
     * Finds all students in a specific filiere with pagination
     * @param filiereId the ID of the filiere
     * @param pageable pagination information
     * @return a page of students in the filiere
     */
    Page<Etudiant> findByFiliereID_FiliereId(Integer filiereId, Pageable pageable);

    /**
     * Finds all students by their current year of study
     * @param annee the year of study
     * @return a list of students in that year
     */
    List<Etudiant> findByAnneeActuelle(Integer annee);

    /**
     * Finds all students by their status
     * @param statut the student status to search for
     * @return a list of students with the specified status
     */
    List<Etudiant> findByStatut(StatutEtudiant statut);

    /**
     * Finds all students by their status and semester
     * @param statut the student status
     * @param semester the semester
     * @return a list of students matching both criteria
     */
    List<Etudiant> findByStatutAndSemester(StatutEtudiant statut, Module.Semester semester);

    /**
     * Finds all students by semester
     * @param semester the semester to search for
     * @return a list of students in that semester
     */
    List<Etudiant> findBySemester(Module.Semester semester);

    /**
     * Finds all students by semester and academic year
     * @param semester the semester
     * @param anneeUniversitaireId the academic year ID
     * @return a list of students matching both criteria
     */
    List<Etudiant> findBySemesterAndAnneeUniversitaire_AnneeId(Module.Semester semester, Integer anneeUniversitaireId);

    /**
     * Finds all students by filiere and academic year
     * @param filiereID the filiere ID
     * @param anneeUniversitaireId the academic year ID
     * @return a list of students matching both criteria
     */
    List<Etudiant> findByFiliereID_FiliereIdAndAnneeUniversitaire_AnneeId(Integer filiereID, Integer anneeUniversitaireId);

//    /**
//     * Finds all students by filiere, academic year, and semester
//     * @param idFiliere the filiere ID
//     * @param anneeUniversitaireId the academic year ID
//     * @param semester the semester
//     * @return a list of students matching all criteria
//     */
//    List<Etudiant> findByFiliereID_FiliereIdAndAnneeUniversitaire_AnneeIdAndSemester(
//        Integer idFiliere,
//        Integer anneeUniversitaireId,
//        Module.Semester semester
//    );

    /**
     * Finds students by name or surname containing the search term
     * @param searchTerm the term to search for in names
     * @return a list of students matching the search term
     */
    List<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String searchTerm, String searchTerm2);

    /**
     * Finds all students by academic year
     * @param anneeUniversitaireId the academic year ID
     * @return a list of students in that academic year
     */
    List<Etudiant> findByAnneeUniversitaire_AnneeId(Integer anneeUniversitaireId);

    /**
     * Finds students who are either normal or repeating students in a specific filiere and semester
     * @param idFiliere the filiere ID
     * @param semester the semester
     * @return a list of matching students
     */
    @Query("""
        SELECT e FROM Etudiant e 
        WHERE (e.statut = 'NORMAL' OR e.statut = 'REDOUBLANT')
        AND e.filiereID.filiereId = :filiereId 
        AND e.semester = :semester
        """)
    List<Etudiant> findNormalOrRepeatingStudentsInFiliereAndSemester(
        @Param("filiereId") Integer idFiliere,
        @Param("semester") Module.Semester semester
    );


    List<Etudiant> findByFiliereID_FiliereIdAndStatutAndAnneeUniversitaire_AnneeIdAndSemester(Integer filierId, StatutEtudiant statutEtudiant,Integer anneeId, Module.Semester semester);

    /**
     * Finds students who are either reserve or partial repeating students in a specific filiere and module
     * @param idFiliere the filiere ID
     * @param idModule the module ID
     * @return a list of matching students
     */
    @Query("""
        SELECT e FROM Etudiant e 
        WHERE e.codeApj NOT IN (
            SELECT nf.etudiant.codeApj 
            FROM NoteFinal nf 
            WHERE nf.statut IN ('VALIDE', 'ACQUIS_PAR_COMPENSATION')
            AND nf.module.idModule = :moduleId
        )
        AND e.statut IN ('RESERVISTE', 'REDOUBLANT_PARTIEL')
        AND e.filiereID.filiereId = :filiereId
        """)
    List<Etudiant> findReserveOrPartialRepeatingStudentsInFiliereAndModule(
        @Param("filiereId") Integer idFiliere,
        @Param("moduleId") Integer idModule
    );

    /**
     * Finds students by birth date range
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of students born in the specified date range
     */
    List<Etudiant> findByDateNaissanceBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Counts students by filiere and status
     * @param filiereId the filiere ID
     * @param statut the student status
     * @return the count of students matching the criteria
     */
    long countByFiliereID_FiliereIdAndStatut(Integer filiereId, StatutEtudiant statut);

    /**
     * Checks if a student exists with the given CNE
     * @param cne the CNE to check
     * @return true if a student exists with the given CNE
     */
    boolean existsByCne(String cne);

    /**
     * Find students by status, filiere, and module
     * @param statut the student status
     * @param filiereId the filiere ID
     * @param moduleId the module ID
     * @return list of students matching the criteria
     */
    // List<Etudiant> findByStatutAndFiliereID_FiliereIdAndModule_IdModule(
    //     StatutEtudiant statut, Integer filiereId, Integer moduleId);

    /**
     * Finds all students by filiere, academic year, and semester
     * @param filiereId the filiere ID
     * @param anneeId the academic year ID
     * @param semester the semester
     * @return a list of students matching all criteria
     */
    List<Etudiant> findByFiliereID_FiliereIdAndAnneeUniversitaire_AnneeIdAndSemester(Integer filiereId, Integer anneeId, Module.Semester semester);
}
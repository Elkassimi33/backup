package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.NoteFinal;
import com.FSTS.schoolManager.entities.NoteFinal.StatutNote;
import com.FSTS.schoolManager.entities.Module.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing NoteFinal (Final Grade) entities.
 * Provides methods for finding and managing final grades.
 */
@Repository
public interface NoteFinalRepository extends JpaRepository<NoteFinal, Integer> {

    /**
     * Trouve la note finale d'un étudiant pour un module, un semestre et une année universitaire spécifiques
     * @param etudiantId l'ID de l'étudiant
     * @param moduleId l'ID du module
     * @param semester le semestre
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return un Optional contenant la note finale si elle existe
     */
    Optional<NoteFinal> findByEtudiant_CodeApjAndModule_IdModuleAndSemesterAndAnneeUniversitaire_AnneeId(
            Integer etudiantId, Long moduleId, Semester semester, Integer anneeUniversitaireId);

    /**
     * Trouve toutes les notes finales d'un étudiant
     * @param etudiantId l'ID de l'étudiant
     * @return la liste des notes finales de cet étudiant
     */
    List<NoteFinal> findByEtudiant_CodeApj(Integer etudiantId);

    /**
     * Trouve toutes les notes finales attribuées par un enseignant
     * @param enseignantId l'ID de l'enseignant
     * @return la liste des notes finales attribuées par cet enseignant
     */
    List<NoteFinal> findByEnseignant_EnseignantId(Integer enseignantId);

    /**
     * Trouve toutes les notes finales pour un module
     * @param moduleId l'ID du module
     * @return la liste des notes finales pour ce module
     */
    List<NoteFinal> findByModule_IdModule(Long moduleId);

    List<NoteFinal> findByModule_IdModuleAndAnneeUniversitaire_AnneeId(Long moduleId,Integer anneeUniversitaireId);

    /**
     * Trouve toutes les notes finales d'un etudiant pour un module
     * @param moduleId l'ID du module
     * @param etudiantId l'ID d'etudiant
     * @return la liste des notes finales d'un etudiant pour ce module
     */
    List<NoteFinal> findByModule_IdModuleAndEtudiant_CodeApj(Long moduleId,Integer etudiantId);
    /**
     * Trouve toutes les notes finales pour un semestre
     * @param semester le semestre
     * @return la liste des notes finales pour ce semestre
     */
    List<NoteFinal> findBySemester(Semester semester);

    /**
     * Trouve toutes les notes finales pour une année universitaire
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des notes finales pour cette année universitaire
     */
    List<NoteFinal> findByAnneeUniversitaire_AnneeId(Integer anneeUniversitaireId);

    /**
     * Trouve toutes les notes finales par statut
     * @param statut le statut des notes
     * @return la liste des notes finales avec ce statut
     */
    List<NoteFinal> findByStatut(StatutNote statut);

    /**
     * Calcule la moyenne des notes finales pour un étudiant, un semestre et une année universitaire
     * @param etudiantId l'ID de l'étudiant
     * @param semester le semestre
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la moyenne des notes finales ou null s'il n'y a pas de notes
     */
    @Query("SELECT AVG(n.note) FROM NoteFinal n WHERE n.etudiant.codeApj = ?1 AND n.semester = ?2 AND n.anneeUniversitaire.anneeId = ?3")
    Double calculateAverageByEtudiantAndSemesterAndAnneeUniversitaire(Integer etudiantId, Semester semester, Integer anneeUniversitaireId);

    /**
     * Compte les modules validés (note >= 10) pour un étudiant dans un semestre et une année universitaire
     * @param etudiantId l'ID de l'étudiant
     * @param semester le semestre
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return le nombre de modules validés
     */
    @Query("SELECT COUNT(n) FROM NoteFinal n WHERE n.etudiant.codeApj = ?1 AND n.semester = ?2 AND n.anneeUniversitaire.anneeId = ?3 AND n.statut = 'VALIDE'")
    Long countValidatedModulesByEtudiantAndSemesterAndAnneeUniversitaire(Integer etudiantId, Semester semester, Integer anneeUniversitaireId);

    /**
     * Finds all final notes for a specific student and semester
     * @param codeApj the student ID
     * @param semester the semester
     * @return list of final notes
     */
    List<NoteFinal> findByEtudiant_CodeApjAndSemester(Integer codeApj, Semester semester);

    List<NoteFinal> findByEtudiant_CodeApjAndStatut(Integer studentId, StatutNote statutNote);
    List<NoteFinal> findByEtudiant_CodeApjAndStatutAndAnneeUniversitaire_AnneeId(
            Integer codeApj,
            StatutNote statut,
            Integer anneeId
    );
    List<NoteFinal> findByEtudiant_CodeApjAndModule_IdModuleAndStatutAndAnneeUniversitaire_AnneeId(
        Integer studentId, Long moduleId, NoteFinal.StatutNote statut, Integer anneeId);

    List<NoteFinal> findByEtudiant_CodeApjAndModule_IdModuleAndStatut(
        Integer studentId, Long moduleId, NoteFinal.StatutNote statut);

    long countByEtudiant_CodeApjAndStatutInAndAnneeUniversitaire_AnneeId(Integer codeApj, List<NoteFinal.StatutNote> statuts, Integer anneeId);
}
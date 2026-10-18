package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Note;
import com.FSTS.schoolManager.entities.NoteFinal;
import com.FSTS.schoolManager.entities.Note.TypeExamen;
import com.FSTS.schoolManager.entities.Module.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    /**
     * Trouve les notes d'un étudiant
     *
     * @param etudiantId l'ID de l'étudiant
     * @return la liste des notes de cet étudiant
     */
    List<Note> findByEtudiant_CodeApj(Integer etudiantId);

    /**
     * Trouve les notes attribuées par un enseignant
     *
     * @param enseignantId l'ID de l'enseignant
     * @return la liste des notes attribuées par cet enseignant
     */
    List<Note> findByEnseignant_EnseignantId(Integer enseignantId);

    /**
     * Trouve les notes pour un module
     *
     * @param moduleId l'ID du module
     * @return la liste des notes pour ce module
     */
    List<Note> findByModule_IdModule(Long moduleId);

    /**
     * Trouve les notes pour un semestre
     *
     * @param semester le semestre
     * @return la liste des notes pour ce semestre
     */
    List<Note> findBySemester(Semester semester);

    /**
     * Trouve les notes pour une année universitaire
     *
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des notes pour cette année universitaire
     */
    List<Note> findByAnneeUniversitaire_AnneeId(Integer anneeUniversitaireId);

    /**
     * Trouve les notes par type d'examen
     *
     * @param typeExamen le type d'examen
     * @return la liste des notes pour ce type d'examen
     */
    List<Note> findByTypeExamen(TypeExamen typeExamen);

    /**
     * Trouve les notes pour un étudiant, un module et un semestre spécifiques
     *
     * @param etudiantId l'ID de l'étudiant
     * @param moduleId   l'ID du module
     * @param semester   le semestre
     * @return la liste des notes correspondantes
     */
    List<Note> findByEtudiant_CodeApjAndModule_IdModuleAndSemester(Integer etudiantId, Long moduleId, Semester semester);

    /**
     * Trouve les notes pour un étudiant, un module, un semestre et une année universitaire spécifiques
     *
     * @param etudiantId           l'ID de l'étudiant
     * @param moduleId             l'ID du module
     * @param semester             le semestre
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des notes correspondantes
     */
    List<Note> findByEtudiant_CodeApjAndModule_IdModuleAndSemesterAndAnneeUniversitaire_AnneeId(
            Integer etudiantId, Long moduleId, Semester semester, Integer anneeUniversitaireId);

    /**
     * Trouve les notes pour un étudiant et une année universitaire spécifiques
     *
     * @param etudiantId           l'ID de l'étudiant
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des notes correspondantes
     */
    List<Note> findByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(Integer etudiantId, Integer anneeUniversitaireId);

    /**
     * Find notes by student and module, ordered by date notation in descending order
     *
     * @param etudiantId the student ID
     * @param moduleId   the module ID
     * @return list of notes ordered by date
     */
    List<Note> findByEtudiant_CodeApjAndModule_IdModuleOrderByDateNotationDesc(Integer etudiantId, Long moduleId);

    /**
     * Calcule la moyenne des notes pour un étudiant, un module et une année universitaire
     *
     * @param etudiantId           l'ID de l'étudiant
     * @param moduleId             l'ID du module
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la moyenne des notes ou null s'il n'y a pas de notes
     */
    @Query("SELECT AVG(n.note) FROM Note n WHERE n.etudiant.codeApj = ?1 AND n.module.idModule = ?2 AND n.anneeUniversitaire.anneeId = ?3")
    Double calculateAverageByEtudiantAndModuleAndAnneeUniversitaire(Integer etudiantId, Long moduleId, Integer anneeUniversitaireId);

    List<Note> findByModule_IdModuleOrderByDateNotationDesc(Long moduleId);

    List<Note> findByEtudiant_CodeApjAndStatut(Integer codeApj, NoteFinal.StatutNote statut);

    List<Note> findByModule_IdModuleAndAndAnneeUniversitaire_AnneeIdOrderByDateNotationDesc(Long moduleId, Integer anneeId);

    @Query("SELECT n FROM Note n WHERE n.etudiant.codeApj = :codeApj " +
            "AND n.module.idModule = :idModule " +
            "AND n.semester = :semester " +
            "AND n.anneeUniversitaire.anneeId = :anneeId " +
            "ORDER BY n.dateNotation DESC")
    List<Note> findNotesByStudentModuleSemesterAndYear(
            @Param("codeApj") Integer codeApj,
            @Param("idModule") Long idModule,
            @Param("semester") Semester semester,
            @Param("anneeId") Integer anneeId
    );
}
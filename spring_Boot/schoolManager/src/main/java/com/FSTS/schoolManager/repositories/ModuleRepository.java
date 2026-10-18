package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.Module.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {



    /**
     * Trouve un module par son code unique
     * @param codeModule le code du module
     * @return un Optional contenant le module s'il existe
     */
    Optional<Module> findByCodeModule(String codeModule);

    /**
     * Trouve des modules par nom
     * @param nomModule le nom du module
     * @return la liste des modules correspondants
     */
    List<Module> findByNomModuleContainingIgnoreCase(String nomModule);

    /**
     * Trouve des modules par filière
     * @param filiereId l'ID de la filière
     * @return la liste des modules de cette filière
     */
    List<Module> findByFiliere_FiliereId(Integer filiereId);

    /**
     * Trouve des modules par enseignant
     * @param enseignantId l'ID de l'enseignant
     * @return la liste des modules enseignés par cet enseignant
     */
    List<Module> findByEnseignant_EnseignantId(Integer enseignantId);

    /**
     * Trouve des modules par enseignant et année universitaire
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @param enseignantId l'ID de l'enseignant
     * @return la liste des modules enseignés par cet enseignant et cette année universitaire
     */
    List<Module> findByAnneeUniversitaire_AnneeIdAndEnseignant_EnseignantId(Integer anneeUniversitaireId, Integer enseignantId);

    /**
     * Trouve des modules par semestre
     * @param semester le semestre
     * @return la liste des modules de ce semestre
     */
    List<Module> findBySemester(Semester semester);

    /**
     * Trouve des modules par année universitaire et filière
     * @param anneeId l'ID de l'année universitaire
     * @param filiereId l'ID de la filière
     * @return la liste des modules de cette filière pour cette année
     */
    List<Module> findByAnneeUniversitaire_AnneeIdAndFiliere_FiliereId(Integer anneeId, Integer filiereId);

    /**
     * Trouve des modules par année universitaire et liste d'enseignants
     * @param anneeId l'ID de l'année universitaire
     * @param enseignantIds la liste des IDs des enseignants
     * @return la liste des modules enseignés par ces enseignants pour cette année
     */
    List<Module> findByAnneeUniversitaire_AnneeIdAndEnseignant_EnseignantIdIn(Integer anneeId, List<Integer> enseignantIds);

    /**
     * Trouve des modules par année universitaire
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des modules pour cette année universitaire
     */
    List<Module> findByAnneeUniversitaire_AnneeId(Integer anneeUniversitaireId);

    /**
     * Trouve des modules avec TP
     * @param hasTp true pour les modules avec TP, false sinon
     * @return la liste des modules selon leur statut TP
     */
    List<Module> findByHasTp(boolean hasTp);

    /**
     * Trouve des modules par semestre et filière
     * @param semester le semestre
     * @param filiereId l'ID de la filière
     * @return la liste des modules pour ce semestre et cette filière
     */
    List<Module> findBySemesterAndFiliere_FiliereId(Semester semester, Integer filiereId);

    List<Module> findBySemesterAndFiliere_FiliereIdAndAnneeUniversitaire_AnneeId(Semester semester, Integer filiereId,Integer anneeId);

    /**
     * Vérifie si un module avec le code spécifié existe
     * @param codeModule le code du module
     * @return true si un module avec ce code existe
     */
    boolean existsByCodeModule(String codeModule);
}
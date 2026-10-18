package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.EmploiDuTemps;
import com.FSTS.schoolManager.entities.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EmploiDuTempsRepository extends JpaRepository<EmploiDuTemps, Long> {
    List<EmploiDuTemps> findByModule_IdModuleAndAnneeUniversitaire_AnneeId(Long moduleId,Integer anneeUniversitaireId);
    List<EmploiDuTemps> findBySalle_IdAndAnneeUniversitaire_AnneeId(Integer salleId, Integer anneeUniversitaireId);
    List<EmploiDuTemps> findByJourAndAnneeUniversitaire_AnneeId(DayOfWeek jour, Integer anneeUniversitaireId);
    List<EmploiDuTemps> findBySalleAndJourAndHeureDebutLessThanEqualAndHeureFinGreaterThanEqual(
        Salle salle, DayOfWeek jour, LocalTime heureFin, LocalTime heureDebut);
}
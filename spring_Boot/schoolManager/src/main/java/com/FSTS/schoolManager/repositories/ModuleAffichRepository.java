package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.ModuleAffich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleAffichRepository extends JpaRepository<ModuleAffich,Integer> {

    @Query("SELECT m.module.idModule FROM ModuleAffich m WHERE m.afficheNormal = true AND m.anneeUniversitaire.anneeId = :anneeId")
    List<Long> ModuleAfficheNormal(@Param("anneeId") Integer anneeId);

    @Query("SELECT m.module.idModule FROM ModuleAffich m WHERE m.afficheRatrapage = true AND m.anneeUniversitaire.anneeId = :anneeId")
    List<Long> ModuleAfficheRatt(@Param("anneeId") Integer anneeId);

}

package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.FiliereSemester;
import com.FSTS.schoolManager.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiliereSemesterRepository extends JpaRepository<FiliereSemester, Integer> {
    Optional<FiliereSemester> findByFiliere_FiliereIdAndSemester(Integer filiereId, Module.Semester semester);
    Optional<FiliereSemester> findByFiliere_FiliereIdAndIsFirstSemesterTrue(Integer filiereId);
} 
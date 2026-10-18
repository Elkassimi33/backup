package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.SemesterNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterNoteRepository extends JpaRepository<SemesterNote, Integer> {
    Optional<SemesterNote> findByEtudiant_CodeApjAndSemesterAndAnneeUniversitaire_AnneeId(
        Integer studentId, Module.Semester semester, Integer anneeId);
    boolean existsByEtudiant_CodeApjAndStatut(Integer codeApj, SemesterNote.StatutSemester statut);
} 
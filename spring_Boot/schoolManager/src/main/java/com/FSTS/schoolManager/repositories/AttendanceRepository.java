package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEtudiant_CodeApj(Integer studentId);
    long countByEtudiant_CodeApj(Integer studentId);
    long countByEtudiant_CodeApjAndPresentTrue(Integer studentId);
    List<Attendance> findByEtudiant_CodeApjAndDateBetween(Integer studentId, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByModule_IdModuleAndDate(Long moduleId, LocalDate date);
} 
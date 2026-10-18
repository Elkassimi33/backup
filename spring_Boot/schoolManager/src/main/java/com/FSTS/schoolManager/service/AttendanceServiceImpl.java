package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.Attendance;
import com.FSTS.schoolManager.entities.Etudiant;
import com.FSTS.schoolManager.entities.EmploiDuTemps;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EtudiantRepository etudiantRepository;
    private final ModuleRepository moduleRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;

    @Override
    public Attendance markAttendance(Integer studentId, Long moduleId, Long emploiDuTempsId, 
            boolean present, String justification) {
        
        Etudiant etudiant = etudiantRepository.findById(studentId)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + studentId));
            
        Module module = moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + moduleId));
            
        EmploiDuTemps emploiDuTemps = emploiDuTempsRepository.findById(emploiDuTempsId)
            .orElseThrow(() -> new EmploiDuTempsNotFoundException("Schedule not found with id=" + emploiDuTempsId));

        Attendance attendance = Attendance.builder()
            .etudiant(etudiant)
            .module(module)
            .emploiDuTemps(emploiDuTemps)
            .date(LocalDate.now())
            .heureDebut(emploiDuTemps.getHeureDebut())
            .heureFin(emploiDuTemps.getHeureFin())
            .present(present)
            .justification(justification)
            .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendance(Integer studentId) {
        return attendanceRepository.findByEtudiant_CodeApj(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendanceByDateRange(Integer studentId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByEtudiant_CodeApjAndDateBetween(studentId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getModuleAttendance(Long moduleId, LocalDate date) {
        return attendanceRepository.findByModule_IdModuleAndDate(moduleId, date);
    }

    @Override
    @Transactional(readOnly = true)
    public double getStudentAttendanceRate(Integer studentId) {
        long totalSessions = attendanceRepository.countByEtudiant_CodeApj(studentId);
        if (totalSessions == 0) {
            return 0.0;
        }
        long attendedSessions = attendanceRepository.countByEtudiant_CodeApjAndPresentTrue(studentId);
        return (attendedSessions * 100.0) / totalSessions;
    }
} 
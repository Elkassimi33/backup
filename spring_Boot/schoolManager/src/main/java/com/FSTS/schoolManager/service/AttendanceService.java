package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.Attendance;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    Attendance markAttendance(Integer studentId, Long moduleId, Long emploiDuTempsId, boolean present, String justification);
    List<Attendance> getStudentAttendance(Integer studentId);
    List<Attendance> getStudentAttendanceByDateRange(Integer studentId, LocalDate startDate, LocalDate endDate);
    List<Attendance> getModuleAttendance(Long moduleId, LocalDate date);
    double getStudentAttendanceRate(Integer studentId);
} 
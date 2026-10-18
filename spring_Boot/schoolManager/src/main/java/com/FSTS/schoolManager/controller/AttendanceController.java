package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.entities.Attendance;
import com.FSTS.schoolManager.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(
            @RequestParam Integer studentId,
            @RequestParam Long moduleId,
            @RequestParam Long emploiDuTempsId,
            @RequestParam boolean present,
            @RequestParam(required = false) String justification) {
        return ResponseEntity.ok(attendanceService.markAttendance(
            studentId, moduleId, emploiDuTempsId, present, justification));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studentId));
    }

    @GetMapping("/student/{studentId}/range")
    public ResponseEntity<List<Attendance>> getStudentAttendanceByDateRange(
            @PathVariable Integer studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceByDateRange(
            studentId, startDate, endDate));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Attendance>> getModuleAttendance(
            @PathVariable Long moduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getModuleAttendance(moduleId, date));
    }

    @GetMapping("/student/{studentId}/rate")
    public ResponseEntity<Double> getStudentAttendanceRate(
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceRate(studentId));
    }
} 
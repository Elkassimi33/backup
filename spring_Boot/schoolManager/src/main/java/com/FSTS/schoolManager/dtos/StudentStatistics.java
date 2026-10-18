package com.FSTS.schoolManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentStatistics {
    private double gpa;
    private int totalModules;
    private int passedModules;
    private int failedModules;
    private double attendanceRate;
    private String currentSemester;
    private String status;
} 
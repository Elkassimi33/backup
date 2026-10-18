package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Module;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModuleDetails {
    private Long id;
    private String name;
    private String description;
    private Module.Semester semester;
    private int credits;
    private String teacherName;
    private boolean hasTp;
    private double averageGrade;
    private int enrolledStudents;
    private String filiereName;
} 
package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Note;
import com.FSTS.schoolManager.entities.NoteFinal;

import java.time.LocalDate;

/**
 * DTO class to hold grade history information
 */
public class GradeHistory {
    private final double grade;
    private final Note.TypeExamen typeExamen;
    private final LocalDate date;
    private final NoteFinal.StatutNote status;
    private final boolean isTp;

    public GradeHistory(double grade, Note.TypeExamen typeExamen, 
            LocalDate date, NoteFinal.StatutNote status, boolean isTp) {
        this.grade = grade;
        this.typeExamen = typeExamen;
        this.date = date;
        this.status = status;
        this.isTp = isTp;
    }

    public double getGrade() { return grade; }
    public Note.TypeExamen getTypeExamen() { return typeExamen; }
    public LocalDate getDate() { return date; }
    public NoteFinal.StatutNote getStatus() { return status; }
    public boolean isTp() { return isTp; }
} 
package com.FSTS.schoolManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GradeStatistics {
    private final double average;
    private final double highest;
    private final double lowest;
    private final long passedCount;
    private final long failedCount;
    private final double passRate;

    public static GradeStatistics empty() {
        return new GradeStatistics(0.0, 0.0, 0.0, 0L, 0L, 0.0);
    }
} 
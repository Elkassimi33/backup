package com.rsi.patient.patient_service.model.embedded;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Embeddable;

@Embeddable
public class DateRange {

    private LocalDate from;
    private LocalDate to;

    public Long getDurtionInDays() {

        return ChronoUnit.DAYS.between(from, to);
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}

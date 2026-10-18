package com.rsi.doctor_service.dtos.request;

import java.math.BigDecimal;

public record DoctroRegistrationRequest(

        String fullName,

        String specialization,

        String licenseNumber,

        String department,

        Integer yearsOfExperience,

        BigDecimal consultationFee

) {
}

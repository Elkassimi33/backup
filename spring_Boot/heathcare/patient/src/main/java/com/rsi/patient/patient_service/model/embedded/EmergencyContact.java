package com.rsi.patient.patient_service.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Embeddable
@Data
public class EmergencyContact {

    @Column(name = "emergency_name")
    @NotBlank
    private String emergencyName;
    @NotBlank
    private String relationShip;
    @Column(name = "emergency_phone_number")
    @NotBlank
    private String phoneNumber;

}

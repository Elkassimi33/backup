package com.rsi.patient.patient_service.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GpsLocation {

    @Column(name = "device_id")
    private UUID deviceId;
    private Double latitude;
    private Double longitude;
    private String resolveAddress;
}

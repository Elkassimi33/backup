package com.rsi.patient.patient_service.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * ContactInfo
 */

@Embeddable
@Data
public class ContactInfo {

    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

}

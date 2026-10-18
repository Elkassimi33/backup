package com.rsi.doctor_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rsi.doctor_service.model.Doctor;
import com.rsi.doctor_service.model.enums.DoctorStatus;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findByDepartmentAndIsAvailable(String dept, boolean available);

    List<Doctor> findBySpecializationAndStatus(String spec, DoctorStatus status);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByDepartment(String department);

    List<Doctor> findByIsAvailableTrue();

    // @Query("SELECT d FROM Doctor d WHERE d.isAvailable = true AND
    // d.currentPatientCount < d.maxPatientsPerDay")
    @Query("SELECT * FROM Doctor")
    List<Doctor> findAvailableDoctors();
}

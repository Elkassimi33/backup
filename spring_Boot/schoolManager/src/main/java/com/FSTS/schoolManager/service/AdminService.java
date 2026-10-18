package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.dtos.AdminDto;
import com.FSTS.schoolManager.entities.Admin;
import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Module;

import java.util.List;

public interface AdminService {
    /**
     * Add a new student to the system
     * @param email the student's email
     * @param nom the student's last name
     * @param prenom the student's first name
     * @param filiereId the filiere ID
     * @param semester the semester
     * @return the created user
     */
    User addEtudiant(String email, String nom, String prenom, Integer filiereId, Module.Semester semester);

    /**
     * Add a new teacher user
     * @param email the teacher's email
     * @param nom the teacher's last name
     * @param prenom the teacher's first name
     * @return the created user
     */
    User  addEnseignant(String email, String nom, String prenom, String departementNom, 
            Enseignant.StatutEnseignant statut, String grade, String specialite , Boolean isFiliereChef);

    /**
     * Add a new admin user
     * @param email the admin's email
     * @param nom the admin's last name
     * @param prenom the admin's first name
     * @param specialite the admin's specialty
     * @param grade the admin's grade
     * @return the created user
     */
    User addAdmin(String email, String nom, String prenom, String specialite, String grade);

    /**
     * Update an existing admin
     * @param adminId the admin's ID
     * @param adminDto the updated admin data
     * @return the updated admin DTO
     */
    AdminDto updateAdmin(Integer adminId, AdminDto adminDto);

    List<AdminDto> getAllAdmins();

     AdminDto getAdminBYUserId(Integer userId );
} 
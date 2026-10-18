package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.dtos.AdminDto;
import com.FSTS.schoolManager.entities.Admin;
import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public  ResponseEntity<List<AdminDto>>getAll(){
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PostMapping("/student")
    public ResponseEntity<User> addEtudiant(
            @RequestParam String email,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam Integer filiereId,
            @RequestParam Module.Semester semester) {
        return ResponseEntity.ok(adminService.addEtudiant(email, nom, prenom, filiereId, semester));
    }

    @PostMapping("/teacher")
    public ResponseEntity<User> addEnseignant(
            @RequestParam String email,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String departementNom,
            @RequestParam Enseignant.StatutEnseignant statut,
            @RequestParam String grade,
            @RequestParam String specialite,
            @RequestParam Boolean isFiliereChef ) {
        return ResponseEntity.ok(adminService.addEnseignant(email, nom, prenom, departementNom, statut, grade, specialite, isFiliereChef));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AdminDto> getAdminBYUserId(@PathVariable Integer userId){
        return  ResponseEntity.ok(adminService.getAdminBYUserId(userId));
    }

    @PostMapping("/admin")
    public ResponseEntity<User> addAdmin(
            @RequestParam String email,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String specialite,
            @RequestParam String grade) {
        return ResponseEntity.ok(adminService.addAdmin(email, nom, prenom, specialite, grade));
    }

} 
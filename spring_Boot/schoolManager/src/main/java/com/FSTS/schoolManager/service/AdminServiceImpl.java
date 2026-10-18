package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.entities.Role;
import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.auth.repositories.RoleRepository;
import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.exception.DepartementNotFoundException;
import com.FSTS.schoolManager.repositories.*;
import com.FSTS.schoolManager.dtos.EtudiantDto;
import com.FSTS.schoolManager.exception.FiliereNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.FSTS.schoolManager.dtos.AdminDto;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final FiliereRepository filiereRepository;
    private final DepartementRepository departementRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @Override
    @Transactional
    public User addEtudiant(String email, String nom, String prenom, Integer filiereId, Module.Semester semester) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists: " + email);
        }

        Filiere filiere = filiereRepository.findById(filiereId)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));

        // Generate credentials
        String username = generateUsername(nom, prenom);
        String password = generatePassword();
        String universityEmail = generateUniversityEmail(username);

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUniversityEmail(universityEmail);
        user.setUniversityPassword(password);
        user.setRole(User.UserRole.ROLE_STUDENT);

        // Set roles
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_ETUDIANT")
                .orElseThrow(() -> new RoleNotFoundException("Role ROLE_ETUDIANT not found")));
        user.setRoles(roles);

        AnneeUniversitaire anneeUniversitaireDB=anneeUniversitaireRepository.findByAnneeCouranteIsTrue().orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));

        // Create student entity
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setUserId(user);
        etudiant.setFiliereID(filiere);
        etudiant.setSemester(semester);
        etudiant.setStatut(Etudiant.StatutEtudiant.NORMAL);
        etudiant.setAnneeUniversitaire(anneeUniversitaireDB);

        // Save entities
        User savedUser = userRepository.save(user);
        etudiantRepository.save(etudiant);

        // Send email
        sendCredentialsEmail(email, username, password, universityEmail);

        return savedUser;
    }

    @Override
    public User addEnseignant(String email, String nom, String prenom, String departementNom,
            Enseignant.StatutEnseignant statut, String grade, String specialite ,Boolean isFiliereChef) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists: " + email);
        }

        // Find department by name
        Departement departement = departementRepository.findByNom(departementNom)
            .orElseThrow(() -> new DepartementNotFoundException("Department not found with name=" + departementNom));

        // Generate credentials
        String username = generateUsername(nom, prenom);
        String password = generatePassword();
        String universityEmail = generateUniversityEmail(username);

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUniversityEmail(universityEmail);
        user.setUniversityPassword(password);


        // Set roles
        Set<Role> roles = new HashSet<>();
        if(isFiliereChef){
            user.setRole(User.UserRole.ROLE_CHEF_FILIERE);
            roles.add(roleRepository.findByName("ROLE_CHEF_FILIERE")
                    .orElseThrow(() -> new RoleNotFoundException("Role ROLE_ENSEIGNANT not found")));
        }
        else{
            user.setRole(User.UserRole.ROLE_TEACHER);
            roles.add(roleRepository.findByName("ROLE_ENSEIGNANT")
                    .orElseThrow(() -> new RoleNotFoundException("Role ROLE_ENSEIGNANT not found")));
        }

        user.setRoles(roles);

        // Create teacher entity
        Enseignant enseignant = new Enseignant();
        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setUser(user);
        enseignant.setStatut(statut);
        enseignant.setGrade(grade);
        enseignant.setSpecialite(specialite);
        enseignant.setDepartement(departement);

        // Save entities
        User savedUser = userRepository.save(user);
        enseignantRepository.save(enseignant);

        // Send email
        sendCredentialsEmail(email, username, password, universityEmail);

        return savedUser;
    }

    @Override
    public User addAdmin(String email, String nom, String prenom, String specialite, String grade) {

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists: " + email);
        }

        // Generate credentials
        String username = generateUsername(nom, prenom);
        String password = generatePassword();
        String universityEmail = generateUniversityEmail(username);

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUniversityEmail(universityEmail);
        user.setUniversityPassword(password);
        user.setRole(User.UserRole.ROLE_ADMIN);

        // Set roles
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RoleNotFoundException("Role ROLE_ADMIN not found")));
        user.setRoles(roles);

        // Create admin entity
        Admin admin = new Admin();
        admin.setNom(nom);
        admin.setPrenom(prenom);
        admin.setUser(user);
        admin.setSpecialite(specialite);
        admin.setGrade(grade);

        // Save entities
        User savedUser = userRepository.save(user);
        adminRepository.save(admin);

        // Send email
        sendCredentialsEmail(email, username, password, universityEmail);

        return savedUser;
    }

    public List<AdminDto> getAllAdmins(){
        List<Admin> admins=  adminRepository.findAll();

        return  admins.stream().map(a->
                AdminDto.builder()
                        .id(a.getAdminId())
                        .email(a.getEmail())
                        .nom(a.getNom())
                        .prenom(a.getPrenom())
                        .specialite(a.getSpecialite())
                        .grade(a.getGrade())
                        .username(a.getGrade())
                        .universityEmail(a.getNom())
                        .build()).toList();
    }

    public AdminDto getAdminBYUserId(Integer userId ){
        Admin admin = adminRepository.findByUser_Id(userId)
                .orElseThrow(()-> new AdminNotFoundException("Admin not found with userId"+userId));
        return AdminDto.builder()
                        .id(admin.getAdminId())
                        .email(admin.getEmail())
                        .nom(admin.getNom())
                        .prenom(admin.getPrenom())
                        .specialite(admin.getSpecialite())
                        .grade(admin.getGrade())
                        .username(admin.getUser().getUsername())
                        .universityEmail(admin.getUser().getUniversityEmail())
                        .build();
    }
    @Override
    public AdminDto updateAdmin(Integer adminId, AdminDto adminDto) {
        Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new AdminNotFoundException("Admin not found with id=" + adminId));

        User user = admin.getUser();
        
        // Update user information
        user.setEmail(adminDto.getEmail());
        user.setUsername(adminDto.getUsername());
        user.setUniversityEmail(adminDto.getUniversityEmail());
        
        // Update admin information
        admin.setNom(adminDto.getNom());
        admin.setPrenom(adminDto.getPrenom());
        admin.setSpecialite(adminDto.getSpecialite());
        admin.setGrade(adminDto.getGrade());

        // Save changes
        User savedUser = userRepository.save(user);
        Admin savedAdmin = adminRepository.save(admin);

        // Convert to DTO and return
        return AdminDto.builder()
            .id(savedAdmin.getAdminId())
            .email(savedUser.getEmail())
            .nom(savedAdmin.getNom())
            .prenom(savedAdmin.getPrenom())
            .specialite(savedAdmin.getSpecialite())
            .grade(savedAdmin.getGrade())
            .username(savedUser.getUsername())
            .universityEmail(savedUser.getUniversityEmail())
            .build();
    }

    

    private String generateUsername(String nom, String prenom) {
        String baseUsername = (prenom + "." + nom).toLowerCase()
                .replaceAll("[^a-z0-9]", "");
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter++;
        }

        return username;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniversityEmail(String username) {
        return username + "@fsts.ac.ma";
    }

    private void sendCredentialsEmail(String email, String username, String password, String universityEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your University Account Credentials");
        message.setText(String.format(
            "Dear User,\n\n" +
            "Your account has been created successfully.\n\n" +
            "Login Credentials:\n" +
            "Username: %s\n" +
            "Password: %s\n\n" +
            "University Email: %s\n" +
            "University Email Password: %s\n\n" +
            "Please change your password after your first login.\n\n" +
            "Best regards,\n" +
            "University Administration",
            username, password, universityEmail, password
        ));

        mailSender.send(message);
    }
} 
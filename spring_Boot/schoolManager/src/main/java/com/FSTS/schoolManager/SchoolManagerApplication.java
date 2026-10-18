package com.FSTS.schoolManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.FSTS.schoolManager.service.AdminService;
import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.repositories.AdminRepository;
import com.FSTS.schoolManager.auth.repositories.RoleRepository;
import com.FSTS.schoolManager.auth.entities.Role;
import com.FSTS.schoolManager.auth.entities.User.UserRole;

@SpringBootApplication
public class SchoolManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagerApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner initAdmin(AdminService adminService, AdminRepository adminRepository, RoleRepository roleRepository) {
//		return args -> {
//			try {
//				// Check if admin role exists
//				if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
//					// Create ROLE_ADMIN if it doesn't exist
//					Role adminRole = new Role();
//					adminRole.setName("ROLE_ADMIN");
//					roleRepository.save(adminRole);
//					System.out.println("ROLE_ADMIN created.");
//				}
//
//				// Check if admin already exists
//				if (adminRepository.count() == 4) {
//
//					// Create default admin if none exists
//					User admin = adminService.addAdmin(
//							"elkassimiyoussef100@gmail.com",
//							"youssef",
//							"System",
//							"IT",
//							"Administrator"
//					);
//					// Set the role to ROLE_ADMIN
//					admin.setRole(UserRole.ROLE_ADMIN);
//					System.out.println("Default admin created with username: " + admin.getUsername());
//				}
//			} catch (Exception e) {
//				System.err.println("Error during admin initialization: " + e.getMessage());
//				System.err.println("The application will continue to run, but you may need to create an admin user manually.");
//			}
//		};
//	}
}

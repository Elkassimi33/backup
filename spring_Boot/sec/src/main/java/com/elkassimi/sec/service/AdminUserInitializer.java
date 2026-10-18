package com.elkassimi.sec.service;

import com.elkassimi.sec.entity.User;
import com.elkassimi.sec.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class AdminUserInitializer   {


    @Bean
    public CommandLineRunner commandLineRunner(CustomUserDetailsService userDetailsService , PasswordEncoder passwordEncoder,UserRepo userRepo){

        return args -> {
            if(userRepo.findByUsername("admin").isEmpty()){
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("1111"));
                user.setRole("ADMIN");

                userRepo.save(user);

                System.out.println("default admin user created");
            }
        };
    }
}

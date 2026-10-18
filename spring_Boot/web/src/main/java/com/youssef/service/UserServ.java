package com.youssef.service;

import com.youssef.entity.User;
import com.youssef.repository.UserRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserServ {

    private final UserRepo userRepo;

    public UserServ(UserRepo userRepo){

        this.userRepo =userRepo;
    }

    public User getUserById(Long id){
        return userRepo.findById(id).orElseThrow();
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }
    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }
    public User addUser(User user){
        return userRepo.save(user);
    }
}

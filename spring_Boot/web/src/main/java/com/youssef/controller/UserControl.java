package com.youssef.controller;

import com.youssef.entity.User;
import com.youssef.service.UserServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControl {

    private final UserServ  userServ;


    public UserControl(UserServ userServ) {
        this.userServ = userServ;
    }


    @GetMapping
    public User getUserById(Long id){
        return userServ.getUserById(id);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userServ.getAllUsers();
    }

    @PutMapping
    public User updateUser(User user){
        return userServ.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(Long id){
        userServ.deleteUser(id);
    }
    @PostMapping
    public User addUser(User user){
        return userServ.addUser(user);
    }
}

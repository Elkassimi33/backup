package com.youssef.confi;

import com.youssef.entity.Role;
import com.youssef.entity.User;
import com.youssef.service.RoleServ;
import com.youssef.service.UserServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AppStartUp implements CommandLineRunner {

    private final RoleServ  roleServ;

    private  final UserServ userServ;

    public AppStartUp(RoleServ roleServ, UserServ userServ) {
        this.roleServ = roleServ;
        this.userServ = userServ;
    }

    @Override
    public void run(String... args) throws Exception {

        Role role1 = new Role();
        role1.setRoleName("admin");
        Role role2 = new Role();
        role2.setRoleName("user");

        roleServ.insert(role1);
        roleServ.insert(role2);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(role1);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role2);


        User user1 = new User();
        user1.setUsername("user_admin");
        user1.setPassword("password_admin");
        user1.setRoles(adminRoles);

        User user2 = new User();
        user2.setUsername("user");
        user2.setPassword("password");
        user2.setRoles(userRoles);

        userServ.addUser(user1);
        userServ.addUser(user2);


    }
}

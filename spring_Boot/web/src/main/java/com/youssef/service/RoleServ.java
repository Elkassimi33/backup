package com.youssef.service;

import com.youssef.entity.Role;
import com.youssef.repository.RoleRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class RoleServ {

    private final RoleRepo roleRepo;

    public RoleServ(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> findAll(){
        return roleRepo.findAll();
    }
    public List<Role> findByName(String name){
        return roleRepo.findByRoleName(name);
    }
    public Role insert(Role role){
        return roleRepo.save(role);
    }
    public Role findById(Long id){
        return roleRepo.findById(id).orElseThrow();
    }
    public void deleteById(Long id){
        roleRepo.deleteById(id);
    }

}

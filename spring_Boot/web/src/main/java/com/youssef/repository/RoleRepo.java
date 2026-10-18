package com.youssef.repository;

import com.youssef.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    public  List<Role> findByRoleName(String name);
}

package com.youssef.service;

import com.youssef.entity.Department;
import com.youssef.repository.DepartmentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementServ {

    private  final DepartmentRepo departmentRepo;

    public DepartementServ(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public Department  getDepartment (Long id){

        return departmentRepo.findById(id).orElseThrow();
    }

    public Department addDepartment(Department department) {
        return departmentRepo.save(department);
    }

    public Department updateDepartment(Department department) {
        return  departmentRepo.save(department);
    }

    public List<Department> getAll() {

        return departmentRepo.findAll();
    }
}

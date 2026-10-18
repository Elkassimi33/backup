package com.youssef.controller;

import com.youssef.entity.Department;
import com.youssef.service.DepartementServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentControl {

    private final DepartementServ departementServ;

    public DepartmentControl(DepartementServ departementServ) {
        this.departementServ = departementServ;
    }

    @GetMapping()
    public List<Department> getDepartments() {
        return departementServ.getAll();
    }

    @GetMapping("/{id}")
    public Department getDepartment (@PathVariable Long id){

        return departementServ.getDepartment(id);
    }

    @PostMapping()
    public Department addDepartment(@RequestBody Department department){
        return departementServ.addDepartment(department);
    }
    @PutMapping()
    public Department updateDepartment (@RequestBody Department department){
        return departementServ.updateDepartment(department);
    }
}

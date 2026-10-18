package com.youssef.controller;

import com.youssef.entity.Employee;
import com.youssef.service.EmployeeServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeContr {

    private final EmployeeServ employeeServ;

    public EmployeeContr(EmployeeServ employeeServ) {
        this.employeeServ = employeeServ;
    }

    @GetMapping()
    public List<Employee> getEmployees() {

        return employeeServ.getAll();
    }
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id){
        return employeeServ.getEmployee(id);
    }


    @GetMapping("/{name}")
    public List<Employee> getEmployee(@PathVariable String name){
        return employeeServ.getEmployeeByName(name);
    }

    @PostMapping()
    public Employee addEmployee(@RequestBody Employee employee){
        return employeeServ.insertEmployee(employee);
    }

    @PutMapping()
    public Employee updateEmployee(@RequestBody Employee employee){
        return employeeServ.updateEmployee(employee);
    }

}

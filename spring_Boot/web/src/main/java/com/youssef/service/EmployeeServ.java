package com.youssef.service;

import com.youssef.entity.Employee;
import com.youssef.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServ {

    private final EmployeeRepo employeeRepo;

    public EmployeeServ(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee getEmployee (Long id){

        return employeeRepo.findById(id).orElseThrow();
    }

    public List<Employee> getEmployeeByName (String name){
        return employeeRepo.findByName(name);
    }

    public Employee insertEmployee(Employee employee) {

        return employeeRepo.save(employee);
    }

    public Employee updateEmployee(Employee employee) {

        return employeeRepo.save(employee);
    }

    public List<Employee> getAll() {

        return employeeRepo.findAll();
    }
}

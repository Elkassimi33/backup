package com.movie.movieApi.controller;

import com.movie.movieApi.impl.EmployeeJDBCRepo;
import com.movie.movieApi.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private  EmployeeJDBCRepo employeeJDBCRepo;


    @GetMapping
    public int countEmployee(){

        return employeeJDBCRepo.count();
    }

    @GetMapping("/employee/{id}")
    public Employee findById(@PathVariable Long id) {
        return employeeJDBCRepo.findById(id);
    }
    @GetMapping("/all")
    public List<Employee> findAll() {
        return employeeJDBCRepo.findAll();
    }

    @PostMapping("/insert")
    public int insert(@RequestBody Employee employee) {
        return employeeJDBCRepo.insert(employee);
    }

    @PostMapping("/update")
    public int update(@RequestBody Employee employee) {
        return employeeJDBCRepo.update(employee);
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable Long id) {
        return employeeJDBCRepo.delete(id);
    }
}

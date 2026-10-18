package com.movie.movieApi.impl;

import com.movie.movieApi.mapper.EmployeeMap;
import com.movie.movieApi.model.Employee;
import com.movie.movieApi.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeJDBCRepo  implements EmployeeRepo {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from employee",Integer.class);

    }

    @Override
    public Employee findById(Long id) {
        return jdbcTemplate.queryForObject("select * from `spring-boot-curse`.Employee where id =?", new Object[]{id}, new EmployeeMap());
    }

    @Override
    public List<Employee> findAll() {
        return jdbcTemplate.query("select * from employee",new EmployeeMap());
    }

    @Override
    public int insert(Employee employee) {
        return jdbcTemplate.update("insert into employee (id,name,salary) values (?,?,?)",new Object[]{employee.getId(),employee.getName(),employee.getSalary()});
    }

    @Override
    public int update(Employee employee) {
        return jdbcTemplate.update("update employee set name=? and salary=? where id=? ",employee.getName(),employee.getSalary(),employee.getId());
    }

    @Override
    public int delete(Long id) {
        return jdbcTemplate.update("delete from employee where id=?", new Long[]{id});
    }
}

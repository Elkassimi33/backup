package com.movie.movieApi.config;

import com.movie.movieApi.impl.EmployeeJDBCRepo;
import com.movie.movieApi.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupProject implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmployeeJDBCRepo  employeeJDBCRepo;
    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.execute("Drop table if exists employee");
        jdbcTemplate.execute("Create table if not exists employee(id SERIAL PRIMARY KEY NOT NULL, name VARCHAR(255) NOT NULL,salary DOUBLE)");

        employeeJDBCRepo.insert(new Employee(1L,"Mike",100.0));
        employeeJDBCRepo.insert(new Employee(2L,"youssef",110.0));
    }
}

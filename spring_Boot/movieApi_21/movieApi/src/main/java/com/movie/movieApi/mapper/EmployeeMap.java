package com.movie.movieApi.mapper;

import com.movie.movieApi.model.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMap implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(rs.getLong("id"),rs.getString("name"),rs.getDouble("salary"));
    }
}

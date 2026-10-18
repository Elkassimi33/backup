package com.youssef.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {

    @Id
    private Long id;
    private String name;
    private Double salary;
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;




}

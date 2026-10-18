package com.movie.movieApi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    private Long id;
    private String name;
    private Double salary;

}

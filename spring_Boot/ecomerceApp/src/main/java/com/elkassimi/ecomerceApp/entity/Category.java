package com.elkassimi.ecomerceApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(length = 1000)
    private String description;
    @OnToMany
    private Set<Product> products;


}

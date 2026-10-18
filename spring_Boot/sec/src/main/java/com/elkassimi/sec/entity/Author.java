package com.elkassimi.sec.entity;

import java.time.LocalDate;
import java.util.Set;

public class Author {
    private Long id;
    private String firstName;
    private String LastName;
    private String address;
    private LocalDate dateNissance;
    private LocalDate dateMort;
    private String email;
    private Set<Book> books;



}

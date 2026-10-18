package com.elkassimi.sec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private Long id;
    private String title;
    private String quote;
    private LocalDate datePublication;
    private Double prix;
    private Boolean disponible;
    private Integer quantity;
    private Integer pageNumber;
    private Author author;

    private Double discount;


}

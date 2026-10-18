package com.elkassimi.ecomerceApp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LigneCommand{

    @Id
    @GeneratedValue(strategy= GeneratedType.auto)
    private Long ligneCommandId;

    @Column(name = "productId")
    private Product product;

    @Column(nullabel=false )
    private Intrger quantity;


    private BigDecimal totale;

    private LocalDateTime date = LocalDateTime.now();

}

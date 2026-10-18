package com.pfev1.pfe.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    @Column(unique = true, nullable = false)
    private String trackName;

    @Lob
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
}
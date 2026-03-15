package com.learn.project.md3_ss12.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_supplies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MedicalSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String specification;
    private String provider;

    private String unit;

    private Integer quantity = 0;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}

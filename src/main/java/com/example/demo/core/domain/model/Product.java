package com.example.demo.core.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //TODO: das müsste dann UUID sein, oder man lässt die Datenbak das managen über sequence
    private Integer id;

    private String name;

    private String description;

    private String price;

    private String details;

    private int count;

    private String imageLink;



}

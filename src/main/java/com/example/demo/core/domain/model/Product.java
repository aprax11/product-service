package com.example.demo.core.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product", schema="product")
public class Product {
    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    private String name;

    private String description;

    private String price;

    private String details;

    private String image;

}

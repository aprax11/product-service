package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {
    @Bean
    CommandLineRunner commandLineRunner(IProductRepository repository){
        return args -> {
            Product product1 = new Product(
                    1,
                    "Ring",
                    "Das ist ein Ring.",
                    "22",
                    "details",
                    1,
                    "link"
            );
            Product product2 = new Product(
                    2,
                    "Lette",
                    "Das ist eine Kette.",
                    "220",
                    "details",
                    1,
                    "link"
            );

            repository.saveAll(
                    List.of(product1, product2)
            );
        };
    }
}
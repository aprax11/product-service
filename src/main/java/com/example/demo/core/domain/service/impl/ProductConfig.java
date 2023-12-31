package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.core.domain.service.interfaces.IProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class ProductConfig {
    @Bean
    CommandLineRunner commandLineRunner(IProductService productService){
        return args -> {
            Product product1 = new Product(
                    UUID.randomUUID(),
                    "Ring",
                    "Das ist ein Ring.",
                    "22€",
                    "details",
                    "1"
            );
            Product product2 = new Product(
                    UUID.randomUUID(),
                    "Kette",
                    "Das ist eine Kette.",
                    "220€",
                    "details",
                    "2"
            );

            productService.createProduct(product1);
            productService.createProduct(product2);
        };
    }
}

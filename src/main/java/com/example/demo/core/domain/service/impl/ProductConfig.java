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
                    "This is a default ring.",
                    "22",
                    "details",
                    "2"
            );
            Product product2 = new Product(
                    UUID.randomUUID(),
                    "Necklace",
                    "This is a default necklace.",
                    "220",
                    "details",
                    "1"
            );
            Product product3 = new Product(
                    UUID.randomUUID(),
                    "Earring",
                    "This is a default erring.",
                    "15",
                    "details",
                    "3"
            );

            productService.createProduct(product1);
            productService.createProduct(product2);
            productService.createProduct(product3);
        };
    }
}

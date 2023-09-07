package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

}



package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Product;

import java.util.UUID;

public interface IProductService
{
    Product createProduct (Product product);

    Product updateProduct (Product product);

    String deleteProduct (UUID id);

    Product getProduct(UUID id);

    Iterable<Product> getAllProducts();
}

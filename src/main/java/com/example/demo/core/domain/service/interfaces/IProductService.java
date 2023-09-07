package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Product;

public interface IProductService
{
    Product createProduct (Product product);

    void updateProduct (Product product);

    void deleteProduct (Product product);

    Product getProduct(int id);

    Iterable<Product> getAllProducts();
}

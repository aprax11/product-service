package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.core.domain.service.interfaces.IProductService;
import com.example.demo.exception.ProductDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    @Autowired
    ProductService(IProductRepository productRepository){

        this.productRepository = productRepository;
    }

    public Product createProduct (Product product) {

        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {

        if(existsProduct(product.getId())){
            productRepository.save(product);
            return product;
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    @Override
    public String deleteProduct(UUID id) {

        if(existsProduct(id)){
            productRepository.deleteById(id);
            return "deleting product";
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    private boolean existsProduct(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public Product getProduct(UUID id) {

        return productRepository.getReferenceById(id);
    }

    @Override
    public Iterable<Product> getAllProducts() {

        return productRepository.findAll();
    }
}

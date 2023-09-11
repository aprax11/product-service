package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.core.domain.service.interfaces.IProductService;
import com.example.demo.exception.ProductDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void updateProduct(Product product) {

        boolean productExists = productRepository.existsById(product.getId());

        if(productExists){
            productRepository.save(product);
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    @Override
    public void deleteProduct(Product product) {

        productRepository.delete(product);
    }

    @Override
    public Product getProduct(int id) {

        return productRepository.getReferenceById(id);
    }

    @Override
    public Iterable<Product> getAllProducts() {

        return productRepository.findAll();
    }

}

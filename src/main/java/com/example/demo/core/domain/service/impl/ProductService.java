package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.Statics;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.core.domain.service.interfaces.IProductService;
import com.example.demo.exception.ProductDoesNotExistException;
import com.example.demo.port.external.api.ExternalApi;
import com.example.demo.port.producer.interfaces.IBasketServiceProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    private final IBasketServiceProducer basketServiceProducer;
    @Autowired
    ProductService(IProductRepository productRepository, IBasketServiceProducer basketServiceProducer){

        this.productRepository = productRepository;
        this.basketServiceProducer = basketServiceProducer;
    }
    @Override
    public Product createProduct (Product product) {

        product.setId(ExternalApi.getRandomUUID());
        productRepository.save(product);
        basketServiceProducer.sendCreateProductRequest(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {

        if(existsProduct(product.getId())){
            productRepository.save(product);
            basketServiceProducer.sendUpdateProductMessage(product);
            return product;
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    @Override
    public String deleteProduct(UUID id) {

        if(existsProduct(id)){
            productRepository.deleteById(id);
            basketServiceProducer.sendDeleteProductMessage(id.toString());
            return Statics.DELETE_RESPONSE;
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    private boolean existsProduct(UUID id) {
        return productRepository.existsById(id);
    }
    @Cacheable(value = "ProductCache")
    @Override
    public Product getProduct(UUID id) {

        log.info("existsProduct: {}", existsProduct(id));
        if(existsProduct(id)){

            return productRepository.findProductById(id).get();
        }else{
            throw new ProductDoesNotExistException();
        }
    }

    @Override
    public Iterable<Product> getAllProducts() {

        return productRepository.findAll();
    }
}

package com.example.demo.port.producer.interfaces;

import com.example.demo.core.domain.model.Product;

public interface IBasketServiceProducer {
    Product sendCreateProductRequest(Product product);

    Product sendUpdateProductMessage(Product product);

    String sendDeleteProductMessage(String id);
}

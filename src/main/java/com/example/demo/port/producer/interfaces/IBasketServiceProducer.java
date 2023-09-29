package com.example.demo.port.producer.interfaces;

import com.example.demo.core.domain.model.Product;

public interface IBasketServiceProducer {
    void sendCreateProductRequest(Product product);

    void sendUpdateProductMessage(Product product);

    void sendDeleteProductMessage(String id);
}

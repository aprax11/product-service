package com.example.demo.port.producer.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.exception.ErrorResponseException;
import com.example.demo.port.producer.interfaces.IBasketServiceProducer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;

import static com.example.demo.port.MessageType.*;

@Slf4j
@Service
public class BasketServiceProducer implements IBasketServiceProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;

    @Value("basket-service.rpc.key")
    private String routingKey;

    @Override
    public void sendCreateProductRequest(Product product){

        log.info("Sending product to Basket-queue: {}", product);
        byte[] serializedProduct = new Gson().toJson(product).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, CREATE_PRODUCT.name());

         rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );


    }
    @Override
    public void sendUpdateProductMessage(Product product){

        byte[] serializedProduct = new Gson().toJson(product).getBytes();

        Message message = new Message(serializedProduct);
        setMessageType(message, UPDATE_PRODUCT.name());
        rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
    }
    @Override
    public void sendDeleteProductMessage(String id){

        Message message = new Message(id.getBytes());
        setMessageType(message, DELETE_PRODUCT.name());
        rabbitTemplate.sendAndReceive(
                directExchange.getName(),
                routingKey,
                message
        );
    }
    private void setMessageType(Message message, String type) {
        message.getMessageProperties()
                .setType(type);
    }
    private boolean messageIsNull(Message receivedMessage) {
        return receivedMessage == null ||
                receivedMessage.getBody() == null ||
                new String(receivedMessage.getBody(), StandardCharsets.UTF_8).equals("errorResponse");
    }
    private void logErrorFor(String taskName) {
        log.error("error while '{}', because received Message from Product Service via rabbitmq is empty", taskName);
    }
}

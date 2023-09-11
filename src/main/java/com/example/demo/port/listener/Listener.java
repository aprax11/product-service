package com.example.demo.port.listener;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductService;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponseException;

import java.nio.charset.StandardCharsets;


@Slf4j
@Data
@Controller
public class Listener {


    private final IProductService productService;


    @RabbitListener(queues = {"product-service.rpc.queue"})
    public String handleRequest(Message message){
        log.info("receiveMessage triggered");
        final MessageType messageType;
        try {
            messageType = MessageType.valueOf(message.getMessageProperties().getType());
        } catch (IllegalArgumentException e) {
            return logInvalidMessageType(message.getMessageProperties().getType());
        }
        try{
            switch (messageType){
                case CREATE_PRODUCT: {
                    var product = createProductFromMessage(message);
                    log.info("create call processed");
                    return createProduct(product);
                }
                case GET_ALL_PRODUCTS: {
                    log.info("get all products request processed");
                    return getAllProducts();
                }
                default: {
                    return errorResponse();
                }
            }
        } catch (ErrorResponseException e) {
            return errorResponse();
        }
    }

    private String getAllProducts() {
        return new Gson().toJson(productService.getAllProducts());
    }

    private String createProduct(Product product) {
        productService.createProduct(product);
        return new Gson().toJson(productService.createProduct(product));
    }

    private String getBodyOfMessage(Message message) {
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }
    private Product createProductFromMessage(Message message) {
        return new Gson().fromJson(getBodyOfMessage(message), Product.class);
    }

    private String errorResponse() {
        log.error("respond with message 'errorResponse'");
        return "errorResponse";
    }
    private String logInvalidMessageType(String type) {
        log.info("invalid message type: " + type);
        return errorResponse();
    }
}

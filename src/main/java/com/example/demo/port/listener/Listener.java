package com.example.demo.port.listener;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.interfaces.IProductService;
import com.example.demo.port.MessageType;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponseException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


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
                    Product product = createProductFromMessage(message);
                    log.info("create call processed");
                    return createProduct(product);
                }
                case UPDATE_PRODUCT: {
                    Product product = createProductFromMessage(message);
                    log.info("update product request processed");
                    return updateProduct(product);
                }
                case DELETE_PRODUCT: {
                    UUID id = extractIdFromMessage(message);
                    log.info("delete product request processed");
                    return deleteProduct(id);
                }
                case GET_PRODUCT: {
                    UUID id = extractIdFromMessage(message);
                    log.info("get product request processed");
                    return getProduct(id);
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
    private String getProduct(UUID id) {
        Product product = productService.getProduct(id);
        return new Gson().toJson(product);
    }
    private String deleteProduct(UUID id) {
        String answer = productService.deleteProduct(id);
        return answer;
    }
    private String updateProduct(Product product) {
        Product updatedProduct = productService.updateProduct(product);
        return new Gson().toJson(updatedProduct);
    }
    private String getAllProducts() {
        Iterable<Product> products = productService.getAllProducts();
        return new Gson().toJson(products);
    }
    private String createProduct(Product product) {
        Product createdProduct = productService.createProduct(product);
        return new Gson().toJson(createdProduct);
    }
    private String extractBodyOfMessage(Message message) {
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }
    private UUID extractIdFromMessage(Message message) {
        return UUID.fromString(extractBodyOfMessage(message));
    }
    private Product createProductFromMessage(Message message) {
        return new Gson().fromJson(extractBodyOfMessage(message), Product.class);
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

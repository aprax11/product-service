package com.example.demo.listener;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.impl.ProductService;
import com.example.demo.port.listener.Listener;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

import static com.example.demo.port.listener.MessageType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListenerTests {

    @Mock
    private ProductService productService;
    @Mock
    private Message message;
    @Mock
    private MessageProperties messageProperties;
    @InjectMocks
    private Listener listener;

    @BeforeEach
    void setUp() {
        when(message.getMessageProperties()).thenReturn(messageProperties);
    }
    @Test
    void handleRequestObtainAllProductsTest() {
        when(messageProperties.getType()).thenReturn(OBTAIN_ALL_PRODUCTS.name());

        listener.handleRequest(message);

        verify(productService).getAllProducts();
    }
    @Test
    void handleRequestObtainProductTest() {
        when(messageProperties.getType()).thenReturn(OBTAIN_PRODUCT.name());
        when(message.getBody()).thenReturn(UUID.randomUUID().toString().getBytes());

        listener.handleRequest(message);

        verify(productService).getProduct(any(UUID.class));
    }
    @Test
    void handleRequestDeleteProductTest() {
        when(messageProperties.getType()).thenReturn(DELETE_PRODUCT.name());
        when(message.getBody()).thenReturn(UUID.randomUUID().toString().getBytes());

        listener.handleRequest(message);

        verify(productService).deleteProduct(any(UUID.class));
    }
    @Test
    void handleRequestUpdateProductTest() {
        when(messageProperties.getType()).thenReturn(UPDATE_PRODUCT.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new Product()).getBytes());

        listener.handleRequest(message);

        verify(productService).updateProduct(any(Product.class));
    }
    @Test
    void handleRequestCreateProductTest() {
        when(messageProperties.getType()).thenReturn(CREATE_PRODUCT.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new Product()).getBytes());

        listener.handleRequest(message);

        verify(productService).createProduct(any(Product.class));
    }
    @Test
    void handleRequestInvalidType() {
        when(messageProperties.getType()).thenReturn("invalid type");

        listener.handleRequest(message);

        verifyNoInteractions(productService);
    }
}

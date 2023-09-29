package com.example.demo.port.producer.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.exception.ErrorResponseException;
import com.example.demo.port.MessageType;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.example.demo.port.MessageType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketServiceProducerTest {

    public static final String TEST = "test";
    public static final String TEST_PRODUCT_NAME_ONE = "Ring";
    public static final String TEST_PRODUCT_NAME_TWO = "Kette";
    public static final String TEST_ID = UUID.randomUUID().toString();
    public static final String ROUTING_KEY = "routingKey";

    public static final List<Product> ALL_PRODUCTS = List.of(
            new Product(
                    UUID.randomUUID(),
                    TEST_PRODUCT_NAME_ONE,
                    "Das ist ein Ring.",
                    "22",
                    "details",
                    "1"
            ),
            new Product(
                    UUID.randomUUID(),
                    TEST_PRODUCT_NAME_TWO,
                    "Das ist eine Kette.",
                    "220",
                    "details",
                    "2"
            )
    );
    @InjectMocks
    private BasketServiceProducer basketServiceProducer;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DirectExchange directExchange;

    @BeforeEach
    void setUp() {
        try {
            var field = basketServiceProducer.getClass().getDeclaredField("routingKey");
            field.setAccessible(true);
            field.set(basketServiceProducer, ROUTING_KEY);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }
    @Test
    void createProductTest() {

        Product testProduct = ALL_PRODUCTS.get(0);

        when(directExchange.getName()).thenReturn(TEST);

        basketServiceProducer.sendCreateProductRequest(testProduct);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());

        Product argumentProduct = new Gson().fromJson(
                capturedArgumentBody,
                Product.class
        );

        assertThat(argumentProduct).isEqualTo(testProduct);
        assertThat(capturedArgumentMessageType).isEqualTo(CREATE_PRODUCT);
    }
    @Test
    void updateProductTest() {

        Product testProduct = ALL_PRODUCTS.get(0);

        when(directExchange.getName()).thenReturn(TEST);

        basketServiceProducer.sendUpdateProductMessage(testProduct);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());

        Product argumentProduct = new Gson().fromJson(
                capturedArgumentBody,
                Product.class
        );

        assertThat(argumentProduct).isEqualTo(testProduct);
        assertThat(capturedArgumentMessageType).isEqualTo(UPDATE_PRODUCT);
    }
    @Test
    void deleteProductTest() {

        when(directExchange.getName()).thenReturn(TEST);

        basketServiceProducer.sendDeleteProductMessage(TEST_ID);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), argumentCaptor.capture());

        Message capturedArgument = argumentCaptor.getValue();

        String capturedArgumentBody = new String(capturedArgument.getBody(), StandardCharsets.UTF_8);

        MessageType capturedArgumentMessageType = MessageType.valueOf(capturedArgument
                .getMessageProperties()
                .getType());

        assertThat(capturedArgumentBody).isEqualTo(TEST_ID);
        assertThat(capturedArgumentMessageType).isEqualTo(DELETE_PRODUCT);
    }
}

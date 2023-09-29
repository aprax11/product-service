package com.example.demo.port.external.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class ExternalApiTest {

    @Test
    void getUuidTest(){

        UUID id = ExternalApi.getRandomUUID();

        assertNotNull(id);
    }
}

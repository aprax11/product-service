package com.example.demo.port.external.api;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@UtilityClass
public class ExternalApi {
    private static final WebClient webClient = WebClient.create("https://www.uuidgenerator.net/api/version4");

    public static UUID getRandomUUID() {

        try {
            var response = webClient
                    .get()
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("generated UUID with external API");

            return UUID.fromString(Objects.requireNonNull(response.block()));
        } catch (Exception e) {
            log.info("external API is broken - UUID without external Api generated");
            return UUID.randomUUID();
        }
    }
}

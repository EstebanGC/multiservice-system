package com.example.cart_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081/api/products").build();
    }

    public ProductDTO getProduct(Long productId) {
        return webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
    }

    public record ProductDTO(Long id, String name, Double price) {}
}




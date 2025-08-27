package com.example.cart_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {

    public ProductDTO getProduct(Long productId) {
        System.out.println("=== Requesting product with ID: " + productId + " ===");

        try {
            ProductDTO result = WebClient.create("http://localhost:8081")
                    .get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();

            System.out.println("Success: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public record ProductDTO(Long id, String name, Double price) {}
}




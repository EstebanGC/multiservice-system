package com.example.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String description;

    @Positive
    private double price;
}

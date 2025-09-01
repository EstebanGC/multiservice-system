package com.example.product_service.dto;

import lombok.Data;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Integer stock;

    private double price;
}

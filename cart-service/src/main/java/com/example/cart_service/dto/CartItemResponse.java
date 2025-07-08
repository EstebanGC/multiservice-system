package com.example.cart_service.dto;

import lombok.Data;

@Data
public class CartItemResponse {

    private Long productId;
    private String productName;
    private double price;
    private Integer quantity;
}

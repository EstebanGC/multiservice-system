package com.example.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private double price;
    private Integer quantity;
}

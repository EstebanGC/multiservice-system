package com.example.cart_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
}

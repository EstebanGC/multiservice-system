package com.example.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {

    private Long cartId;
    private String username;
    private List<CartItemResponse> items;
}

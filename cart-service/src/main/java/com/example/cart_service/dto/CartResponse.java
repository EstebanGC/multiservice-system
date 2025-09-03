package com.example.cart_service.dto;

import com.example.cart_service.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private String username;
    private String sessionId;
    private List<CartItemResponse> items;
}

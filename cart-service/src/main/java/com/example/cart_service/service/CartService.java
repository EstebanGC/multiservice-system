package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {
    void addToCart(String sessionId, AddToCartRequest request);
    void removeItem(String username, Long productId);
    void clearItem(String username);
    CartResponse getCartResponse(String username);
}

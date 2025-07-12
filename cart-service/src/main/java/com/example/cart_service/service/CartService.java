package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {
    void addToCart(Long userId, AddToCartRequest request);
    void removeItem(Long userId, Long productId);
    void clearItem(Long userId);
    CartResponse getCartResponse(Long userId);
}

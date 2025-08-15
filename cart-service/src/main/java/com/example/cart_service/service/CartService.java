package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartGuest;
import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {

    void addToCartGuest(String sessionId, AddToCartRequest request);

    void addToCartUser(String username, AddToCartRequest request);

    void removeItemBySession(String sessionId, Long productId);

    void removeItemByUser(String username, Long productId);

    void clearCartBySession(String sessionId);

    void clearCartByUser(String username);

    CartResponse getCartBySession(String sessionId);

    CartResponse getCartByUser(String username);
}

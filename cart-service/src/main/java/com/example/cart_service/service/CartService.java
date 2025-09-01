package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {

    CartResponse addToCartGuest(String sessionId, AddToCartRequest request);

    CartResponse addToCartUser(String username, AddToCartRequest request);

    CartResponse getCartResponse(String username);

//    void removeItemBySession(String sessionId, Long productId);
//
//    void removeItemByUser(String username, Long productId);
//
//    void clearCartBySession(String sessionId);
//
//    void clearCartByUser(String username);
//
//    CartResponse getCartBySession(String sessionId);
//
//    CartResponse getCartByUser(String username);
}

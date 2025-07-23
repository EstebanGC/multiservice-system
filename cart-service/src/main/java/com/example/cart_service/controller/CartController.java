package com.example.cart_service.controller;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public void addToCart(AddToCartRequest request, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        cartService.addToCart(username, request);
    }
}

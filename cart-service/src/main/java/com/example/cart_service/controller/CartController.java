package com.example.cart_service.controller;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody @Valid AddToCartRequest request,
            @CookieValue(value = "SESSIONID", required = false) String sessionId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response
    ) {
        String userId = null;

        // Case 1: user authed by jwt
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            userId = extractUserIdFromJwt(jwt); 
        }

        // Case 2: guest user by sessionid
        if (userId == null) {
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
                response.addHeader("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
            }
            cartService.addToCartGuest(sessionId, request);
        } else {
            cartService.addToCartUser(userId, request);
        }

        return ResponseEntity.ok().build();
    }
}

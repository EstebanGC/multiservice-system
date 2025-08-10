package com.example.cart_service.controller;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
            HttpServletResponse response
    ) {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            response.addHeader("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
        }

        cartService.addToCart(sessionId, request);
        return ResponseEntity.ok().build();
    }
}

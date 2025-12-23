package com.example.cart_service.controller;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody @Valid AddToCartRequest request,
            @CookieValue(value = "SESSIONID", required = false) String sessionId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response
    ) {
        try {
            String userId = extractUserIdFromAuthHeader(authHeader);
            CartResponse cartResponse;

            if (userId != null) {
                log.info("Authenticated user: {}", userId);
                cartResponse = cartService.addToCartUser(userId, request);
            } else {
                sessionId = getOrCreateSessionId(sessionId, response);
                log.info("Guest sessionId: {}", sessionId);
                cartResponse = cartService.addToCartGuest(sessionId, request);

                cartResponse.setSessionId(sessionId);
            }

            return ResponseEntity.ok(cartResponse);

        } catch (Exception e) {
            log.error("Error adding to cart", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private String getOrCreateSessionId(String sessionId, HttpServletResponse response) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = UUID.randomUUID().toString();

            String cookieValue = String.format(
                    "SESSIONID=%s; Path=/; HttpOnly; Max-Age=86400; SameSite=Lax",
                    sessionId
            );
            response.addHeader("Set-Cookie", cookieValue);
            log.info("Generated new sessionId: {}", sessionId);
        }
        return sessionId;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @CookieValue(value = "SESSIONID", required = false) String sessionId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response
    ) {
        try {
            String userId = extractUserIdFromAuthHeader(authHeader);
            CartResponse cartResponse;

            if (userId != null) {
                log.info("Fetching cart for user: {}", userId);
                cartResponse = cartService.getCartResponseByUsername(userId);
            } else {
                sessionId = getOrCreateSessionId(sessionId, response);
                log.info("Fetching cart for sessionId: {}", sessionId);
                cartResponse = cartService.getCartResponseBySession(sessionId);
            }

            return ResponseEntity.ok(cartResponse);

        } catch (IllegalStateException e) {
            log.warn("Cart not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting cart", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private String extractUserIdFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        try {
            String jwt = authHeader.substring(7);
            return extractUserIdFromJwt(jwt);
        } catch (Exception e) {
            log.warn("Failed to extract user from JWT: {}", e.getMessage());
            return null;
        }
    }

    private String extractUserIdFromJwt(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode payload = mapper.readTree(payloadJson);

            if (payload.has("sub")) {
                return payload.get("sub").asText();
            } else if (payload.has("username")) {
                return payload.get("username").asText();
            } else if (payload.has("email")) {
                return payload.get("email").asText();
            }

            throw new IllegalArgumentException("No user identifier found in JWT");

        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage(), e);
        }
    }
}

package com.example.cart_service.service;

import com.example.cart_service.client.ProductClient;
import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.dto.CartItemResponse;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    // ======================
    // GET CART
    // ======================

    @Override
    public CartResponse getCartResponseByUsername(String username) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user: " + username));

        return toCartResponse(cart);
    }

    @Override
    public CartResponse getCartResponseBySession(String sessionId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found for session: " + sessionId));

        return toCartResponse(cart);
    }

    // ======================
    // ADD TO CART
    // ======================

    @Override
    public CartResponse addToCartUser(String username, AddToCartRequest request) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> createNewCartForUser(username));

        addOrUpdateItems(cart, request.getProductId(), request.getQuantity());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    public CartResponse addToCartGuest(String sessionId, AddToCartRequest request) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> createNewCartForGuest(sessionId));

        addOrUpdateItems(cart, request.getProductId(), request.getQuantity());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    // ======================
    // REMOVE / CLEAR
    // ======================

    @Transactional
    public void removeItem(String sessionId, Long productId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
    }

    @Transactional
    public void clearItem(String sessionId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().clear();
    }

    // ======================
    // MAPPERS
    // ======================

    private CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .username(cart.getUsername())   // null si es guest (correcto)
                .sessionId(cart.getSessionId())
                .items(cart.getItems().stream()
                        .map(this::from)
                        .toList())
                .build();
    }

    private CartItemResponse from(CartItem item) {
        ProductClient.ProductDTO product = productClient.getProduct(item.getProductId());

        return CartItemResponse.builder()
                .productId(item.getProductId())
                .productName(product.name())
                .price(product.price())
                .quantity(item.getQuantity())
                .build();
    }

    // ======================
    // HELPERS
    // ======================

    private Cart createNewCartForGuest(String sessionId) {
        Cart cart = new Cart();
        cart.setSessionId(sessionId);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    private Cart createNewCartForUser(String username) {
        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    private void addOrUpdateItems(Cart cart, Long productId, int quantity) {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
    }
}

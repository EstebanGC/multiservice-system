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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    public void addToCartGuest(String sessionId, AddToCartRequest request){
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> createNewCartForGuest(sessionId));

    }

    @Transactional
    public void addToCartUser(String username, AddToCartRequest request) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> createNewCartForUser(username));

    }

    @Transactional
    public void removeItem(String sessionId, Long productId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearItem(String sessionId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCartResponse(String username) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .sessionId(cart.getSessionId())
                .items(cart.getItems().stream()
                        .map(this::from) // 👈 convierte cada CartItem -> CartItemResponse
                        .toList())
                .build();
    }

    private CartItemResponse from(CartItem item) {
        ProductClient.ProductDTO product = productClient.getProduct(item.getProductId());

        return CartItemResponse.builder()
                .productId(item.getProductId())
                .productName(product.name())   // record getter
                .price(product.price())        // record getter
                .quantity(item.getQuantity())
                .build();
    }

    public Cart createNewCartForGuest(String sessionId) {
        Cart cart = new Cart();
        cart.setSessionId(sessionId);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    public Cart createNewCartForUser(String username) {
        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    public void addOrUpdateItems(Cart cart, Long productId, int quantity) {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
    }
}

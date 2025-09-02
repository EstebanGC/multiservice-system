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

    @Override
    public CartResponse addToCartGuest(String sessionId, AddToCartRequest request){
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> createNewCartForGuest(sessionId));

        addOrUpdateItems(cart, request.getProductId(), request.getQuantity());
        cartRepository.save(cart);

        return toCartResponse(cart);

    }

    @Override
    public CartResponse addToCartUser(String username, AddToCartRequest request) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> createNewCartForUser(username));

        addOrUpdateItems(cart, request.getProductId(), request.getQuantity());
        cartRepository.save(cart);

        return toCartResponse(cart);
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
    public CartResponse getCartResponseBySession(String sessionId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .sessionId(cart.getSessionId())
                .items(cart.getItems().stream()
                        .map(this::from)
                        .toList())
                .build();
    }

    @Override
    public CartResponse getCartResponseByUsername(String username) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .sessionId(cart.getSessionId()) // This will be null for user carts
                .username(cart.getUsername())   // Add username to response
                .items(cart.getItems().stream()
                        .map(this::from)
                        .toList())
                .build();
    }

    private CartItemResponse from(CartItem item) {

        System.out.println("=== from() Debug ===");
        System.out.println("CartItem ID: " + item.getId());
        System.out.println("CartItem ProductId: " + item.getProductId());
        ProductClient.ProductDTO product = productClient.getProduct(item.getProductId());

        return CartItemResponse.builder()
                .productId(item.getProductId())
                .productName(product.name())
                .price(product.price())
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
        System.out.println("=== addOrUpdateItems Debug ===");
        System.out.println("ProductId received: " + productId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            System.out.println("Updating existing item with productId: " + existingItem.get().getProductId());
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            System.out.println("Creating new CartItem");
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            System.out.println("New CartItem productId set to: " + newItem.getProductId());
        }

        System.out.println("Cart items after operation:");
        cart.getItems().forEach(item ->
                System.out.println("Item ID: " + item.getId() + ", ProductId: " + item.getProductId())
        );
    }

    private CartResponse toCartResponse(Cart cart) {
        System.out.println("=== toCartResponse Debug ===");
        System.out.println("Cart ID: " + cart.getId());
        System.out.println("Number of items: " + cart.getItems().size());

        cart.getItems().forEach(item ->
                System.out.println("Processing item - ID: " + item.getId() + ", ProductId: " + item.getProductId())
        );

        return CartResponse.builder()
                .cartId(cart.getId())
                .sessionId(cart.getSessionId())
                .items(cart.getItems().stream()
                        .map(this::from)
                        .toList())
                .build();
    }
}

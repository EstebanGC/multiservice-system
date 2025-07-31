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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
    private final ProductClient productClient;

    @Transactional
    public void addToCart(String sessionId, AddToCartRequest request) {
        System.out.println("Request received - productId: " + request.getProductId() + ", quantity: " + request.getQuantity());

        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> {
                    System.out.println("Cart not found - creating new one for: " + sessionId);
                    return cartRepository.save(Cart.createEmptyForSession(sessionId));
                });

        Optional<CartItem> maybeItem = cart.getItems().stream()
                .filter(ci -> request.getProductId().equals(ci.getProductId()))
                .findFirst();

        if (maybeItem.isPresent()) {
            CartItem item = maybeItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            System.out.println("Product already in the cart - New quantity: " + item.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            System.out.println("New product added to cart - productId: " + newItem.getProductId() + ", quantity: " + newItem.getQuantity());
        }

        cartRepository.save(cart);
        System.out.println("Cart saved successfully");
    }

    @Transactional
    public void removeItem(String sessionId, Long productId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().removeIf(item ->item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearItem(String sessionId) {
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCartResponse(String sessionId){
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> cartRepository.save(Cart.createEmptyForSession(sessionId)));

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map( item -> {
                    ProductClient.ProductDTO p = productClient.getProduct(item.getProductId());
                    return new CartItemResponse(p.id(), p.name(), p.price(), item.getQuantity());
                })
                .collect(Collectors.toList());

        return new CartResponse(cart.getId(), sessionId, itemResponses);
    }
}

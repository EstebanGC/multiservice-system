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

    public void addToCartGuest(String sessionId, AddToCartRequest request){
        Cart cart = cartRepository.findUserBySessionId(sessionId)
                .orElseGet(() -> cartRepository.save(Cart.createEmptyForSession(sessionId)));
        addOrUpdateItem(cart, request);
    }

    @Transactional
    public void addToCartUser(String username, AddToCartRequest request) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> cartRepository.save(Cart.createEmptyForUser(username)));
        addOrUpdateItem(cart, request);
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

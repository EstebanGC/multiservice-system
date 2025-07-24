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
    public void addToCart(String username, AddToCartRequest request) {
        System.out.println("Request recibido - productId: " + request.getProductId() + ", quantity: " + request.getQuantity());

        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> {
                    System.out.println("No se encontró carrito, se crea uno nuevo para: " + username);
                    return cartRepository.save(Cart.createEmptyForUser(username));
                });

        Optional<CartItem> maybeItem = cart.getItems().stream()
                .filter(ci -> ci.getProductId().equals(request.getProductId()))
                .findFirst();

        if (maybeItem.isPresent()) {
            CartItem item = maybeItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            System.out.println("Producto ya estaba en el carrito. Nueva cantidad: " + item.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            System.out.println("Producto nuevo agregado al carrito - productId: " + newItem.getProductId() + ", quantity: " + newItem.getQuantity());
        }

        cartRepository.save(cart);
        System.out.println("Carrito guardado exitosamente");
    }

    @Transactional
    public void removeItem(String username, Long productId) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().removeIf(item ->item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearItem(String username) {
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCartResponse(String username){
        Cart cart = cartRepository.findUserByUsername(username)
                .orElseGet(() -> cartRepository.save(Cart.createEmptyForUser(username)));

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map( item -> {
                    ProductClient.ProductDTO p = productClient.getProduct(item.getProductId());
                    return new CartItemResponse(p.id(), p.name(), p.price(), item.getQuantity());
                })
                .collect(Collectors.toList());

        return new CartResponse(cart.getId(), username, itemResponses);
    }
    
}

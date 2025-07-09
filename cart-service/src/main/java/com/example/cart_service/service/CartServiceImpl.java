package com.example.cart_service.service;

import com.example.cart_service.dto.AddToCartRequest;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
//    private final ProductClient productClient;

    @Transactional
    public void addToCart(Long userId, AddToCartRequest request){
        Cart cart = cartRepository.findUserById(userId)
                .orElseGet(() -> cartRepository.save(Cart.createEmptyForUser(userId)));

        Optional<CartItem> maybeItem = cart.getItems().stream()
                .filter(ci -> ci.getProductId().equals(request.getProductId()))
                .findFirst();

        if (maybeItem.isPresent()) {
            CartItem item = maybeItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }else{
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }


}

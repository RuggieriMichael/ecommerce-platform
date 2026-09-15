package com.ecommerce.platform.service;

import com.ecommerce.platform.model.CartItem;
import com.ecommerce.platform.model.Product;
import com.ecommerce.platform.model.User;
import com.ecommerce.platform.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addToCart(User user, Long productId, int quantity) {
        Product product = productService.findById(productId);
        CartItem item = cartItemRepository.findByUserAndProductId(user, productId)
                .orElse(new CartItem(user, product, 0));
        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }

    public BigDecimal getCartTotal(User user) {
        return getCartItems(user).stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.ecommerce.platform.controller;

import com.ecommerce.platform.model.User;
import com.ecommerce.platform.repository.UserRepository;
import com.ecommerce.platform.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // Spring Security stores the logged-in username in the Authentication object;
    // we look up our full User entity from it whenever we need cart/order data.
    private User currentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found in DB"));
    }

    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        User user = currentUser(authentication);
        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("total", cartService.getCartTotal(user));
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             Authentication authentication) {
        cartService.addToCart(currentUser(authentication), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String remove(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "redirect:/cart";
    }
}

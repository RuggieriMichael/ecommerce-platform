package com.ecommerce.platform.controller;

import com.ecommerce.platform.model.Order;
import com.ecommerce.platform.model.User;
import com.ecommerce.platform.repository.UserRepository;
import com.ecommerce.platform.service.CartService;
import com.ecommerce.platform.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public CheckoutController(CartService cartService, OrderService orderService, UserRepository userRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found in DB"));
    }

    @GetMapping
    public String checkoutForm(Model model, Authentication authentication) {
        User user = currentUser(authentication);
        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("total", cartService.getCartTotal(user));
        return "checkout/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                              Authentication authentication,
                              Model model) {
        User user = currentUser(authentication);
        try {
            Order order = orderService.placeOrder(user, shippingAddress);
            return "redirect:/checkout/confirmation/" + order.getId();
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartItems", cartService.getCartItems(user));
            model.addAttribute("total", cartService.getCartTotal(user));
            return "checkout/checkout";
        }
    }

    @GetMapping("/confirmation/{orderId}")
    public String confirmation(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        return "checkout/confirmation";
    }
}

package com.ecommerce.platform.controller;

import com.ecommerce.platform.model.Order;
import com.ecommerce.platform.model.User;
import com.ecommerce.platform.repository.UserRepository;
import com.ecommerce.platform.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String myOrders(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("orders", orderService.getOrdersForUser(user));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication authentication) {
        Order order = orderService.findById(id);
        // Simple ownership check: users may only view their own orders (admins bypass via /admin/orders).
        if (!order.getUser().getUsername().equals(authentication.getName())
                && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new org.springframework.security.access.AccessDeniedException("Not your order");
        }
        model.addAttribute("order", order);
        return "orders/detail";
    }
}

package com.ecommerce.platform.service;

import com.ecommerce.platform.model.*;
import com.ecommerce.platform.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Turns the user's current cart into a real Order.
     * @Transactional means: if anything in this method throws, ALL database
     * changes made so far in the method are rolled back -- we never end up
     * with a half-created order or stock decremented without an order to match.
     */
    @Transactional
    public Order placeOrder(User user, String shippingAddress) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        Order order = new Order(user, cartService.getCartTotal(user), shippingAddress);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Not enough stock for " + product.getName());
            }
            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), product.getPrice());
            order.addItem(orderItem);
            productService.decreaseStock(product, cartItem.getQuantity());
        }

        // In a real app, a payment gateway (Stripe, PayPal, etc.) would be
        // charged right here. This is a portfolio demo, so we simply mark
        // the order as successfully placed.
        order.setStatus(OrderStatus.PROCESSING);

        Order saved = orderRepository.save(order);
        cartService.clearCart(user);
        return saved;
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = findById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }
}

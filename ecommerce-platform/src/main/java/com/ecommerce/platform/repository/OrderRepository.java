package com.ecommerce.platform.repository;

import com.ecommerce.platform.model.Order;
import com.ecommerce.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findAllByOrderByOrderDateDesc();
}

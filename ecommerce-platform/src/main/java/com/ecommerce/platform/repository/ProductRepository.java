package com.ecommerce.platform.repository;

import com.ecommerce.platform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    // Spring turns this into: WHERE LOWER(name) LIKE LOWER('%keyword%')
    List<Product> findByNameContainingIgnoreCase(String keyword);
}

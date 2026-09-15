package com.ecommerce.platform.controller;

import com.ecommerce.platform.model.OrderStatus;
import com.ecommerce.platform.model.Product;
import com.ecommerce.platform.repository.CategoryRepository;
import com.ecommerce.platform.service.OrderService;
import com.ecommerce.platform.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Every method here is only reachable by ROLE_ADMIN -- enforced centrally
 * in SecurityConfig ("/admin/**" -> hasRole("ADMIN")), not per-method.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final OrderService orderService;

    public AdminController(ProductService productService, CategoryRepository categoryRepository, OrderService orderService) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderService = orderService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam Long categoryId) {
        product.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders";
    }
}

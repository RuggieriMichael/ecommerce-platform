package com.ecommerce.platform.controller;

import com.ecommerce.platform.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Show the first 6 products as "featured" on the landing page.
        var featured = productService.findAll().stream().limit(6).toList();
        model.addAttribute("featuredProducts", featured);
        return "home"; // resolves to templates/home.html
    }
}

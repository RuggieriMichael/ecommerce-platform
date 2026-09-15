package com.ecommerce.platform.controller;

import com.ecommerce.platform.model.Product;
import com.ecommerce.platform.repository.CategoryRepository;
import com.ecommerce.platform.service.ProductService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long categoryId,
                        Model model) {
        List<Product> products;
        if (keyword != null && !keyword.isBlank()) {
            products = productService.search(keyword);
        } else if (categoryId != null) {
            products = productService.findByCategory(categoryId);
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "products/detail";
    }
}

package com.ecommerce.platform.config;

import com.ecommerce.platform.model.*;
import com.ecommerce.platform.repository.CategoryRepository;
import com.ecommerce.platform.repository.ProductRepository;
import com.ecommerce.platform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * CommandLineRunner.run() executes automatically once, right after the app starts.
 * We use it here to populate the database with sample categories, products,
 * and a ready-made admin account -- so the demo looks populated immediately
 * instead of showing an empty store.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository,
                            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // already seeded on a previous run
        }

        Category electronics = categoryRepository.save(new Category("Electronics", "Gadgets and devices"));
        Category books = categoryRepository.save(new Category("Books", "Fiction and non-fiction"));
        Category home = categoryRepository.save(new Category("Home & Kitchen", "Everyday essentials"));
        Category sports = categoryRepository.save(new Category("Sports & Outdoors", "Gear for staying active"));

        productRepository.saveAll(List.of(
            new Product("Wireless Headphones", "Over-ear, noise-cancelling, 30hr battery.",
                    new BigDecimal("89.99"), "https://picsum.photos/seed/headphones/400/400", 25, electronics),
            new Product("Mechanical Keyboard", "Hot-swappable switches, RGB backlight.",
                    new BigDecimal("129.99"), "https://picsum.photos/seed/keyboard/400/400", 15, electronics),
            new Product("4K Monitor", "27-inch IPS panel, 144Hz refresh rate.",
                    new BigDecimal("349.99"), "https://picsum.photos/seed/monitor/400/400", 8, electronics),
            new Product("The Pragmatic Programmer", "A classic guide to software craftsmanship.",
                    new BigDecimal("34.99"), "https://picsum.photos/seed/book1/400/400", 40, books),
            new Product("Clean Code", "Writing readable, maintainable software.",
                    new BigDecimal("29.99"), "https://picsum.photos/seed/book2/400/400", 30, books),
            new Product("Ceramic Cookware Set", "10-piece non-stick, oven safe.",
                    new BigDecimal("149.99"), "https://picsum.photos/seed/cookware/400/400", 12, home),
            new Product("French Press", "Borosilicate glass, 34oz capacity.",
                    new BigDecimal("24.99"), "https://picsum.photos/seed/frenchpress/400/400", 50, home),
            new Product("Yoga Mat", "Non-slip, 6mm thick, includes carry strap.",
                    new BigDecimal("19.99"), "https://picsum.photos/seed/yogamat/400/400", 60, sports),
            new Product("Trail Running Shoes", "Lightweight, breathable, grippy sole.",
                    new BigDecimal("79.99"), "https://picsum.photos/seed/shoes/400/400", 20, sports)
        ));

        User admin = new User("admin", "admin@demo-store.com", passwordEncoder.encode("admin123"));
        admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        userRepository.save(admin);

        User demoUser = new User("demo", "demo@demo-store.com", passwordEncoder.encode("demo123"));
        demoUser.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(demoUser);

        System.out.println("=========================================");
        System.out.println(" Demo data loaded.");
        System.out.println(" Admin login  -> username: admin  password: admin123");
        System.out.println(" User login   -> username: demo   password: demo123");
        System.out.println("=========================================");
    }
}

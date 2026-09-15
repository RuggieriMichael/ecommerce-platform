package com.ecommerce.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. Spring Boot scans this package (and sub-packages) for
 * @Component/@Service/@Repository/@Controller classes, wires them together
 * (dependency injection), and starts an embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class EcommercePlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommercePlatformApplication.class, args);
    }
}

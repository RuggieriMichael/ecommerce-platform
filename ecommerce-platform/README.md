# ShopSphere — Full-Stack E-Commerce Platform

A demo e-commerce web application built with **Java + Spring Boot**. Users can browse a
product catalog, search/filter, add items to a cart, and check out (no real payment is
processed). Admins get a separate panel to manage products and order statuses.

> Built as a portfolio project. No real money moves — "Place Order" simulates a successful
> checkout so the full purchase flow can be demonstrated end-to-end.

## Tech Stack

| Layer          | Technology                                   |
|----------------|-----------------------------------------------|
| Language       | Java 17                                       |
| Framework      | Spring Boot 3 (Spring MVC, Spring Data JPA, Spring Security) |
| Templating     | Thymeleaf (server-rendered HTML)              |
| Database       | H2 (file-based, embedded — no separate DB server needed) |
| Build Tool     | Maven                                         |

## Getting Started

### Prerequisites
- Java 17+ installed (`java -version` to check)
- Maven (or use the included `mvnw` wrapper if you add one — see note below)
- VS Code with the **"Extension Pack for Java"** and **"Spring Boot Extension Pack"** (both from Microsoft/VMware, install from the Extensions tab)

### Run it
1. Open the `ecommerce-platform` folder in VS Code.
2. VS Code should detect it as a Maven project automatically. If prompted, click "Yes" to import.
3. Open `src/main/java/com/ecommerce/platform/EcommercePlatformApplication.java` and click **Run** above the `main` method (or press `F5`).
   - Alternatively, from a terminal in the project root: `mvn spring-boot:run`
4. Visit **http://localhost:8080**

The app seeds itself with demo data on first run — you'll see this in the console:
```
Admin login  -> username: admin  password: admin123
User login   -> username: demo   password: demo123
```

### Explore
- **http://localhost:8080/** — storefront
- **http://localhost:8080/products** — catalog with search/filter
- **http://localhost:8080/h2-console** — inspect the live database (JDBC URL: `jdbc:h2:file:./data/ecommerce`, user `sa`, blank password)

## Architecture

This follows the standard **layered MVC architecture** used in real Spring Boot backends:

```
Browser
   │  HTTP request
   ▼
Controller     (handles the request, talks to services, picks a view)
   │
   ▼
Service        (business logic: "can this order be placed?", "compute cart total")
   │
   ▼
Repository     (Spring Data JPA — turns Java method calls into SQL)
   │
   ▼
Database       (H2, holds users/products/orders as tables)
```

- **`model/`** — JPA entities (`User`, `Product`, `Category`, `CartItem`, `Order`, `OrderItem`). Each class maps to one database table.
- **`repository/`** — interfaces extending `JpaRepository`. You declare method signatures like `findByUsername(String)` and Spring Data generates the SQL for you — no hand-written queries needed for basic operations.
- **`service/`** — business logic sits here, not in controllers. e.g. `OrderService.placeOrder()` checks stock, creates the order, decrements inventory, and clears the cart — all inside one `@Transactional` method so it can't half-succeed.
- **`controller/`** — thin classes that map URLs (`GET /products/{id}`) to Java methods, and hand data to Thymeleaf templates via a `Model`.
- **`config/SecurityConfig.java`** — defines which URLs need login (`/cart`, `/checkout`, `/orders`) vs admin rights (`/admin/**`) vs are fully public (`/products`).
- **`templates/`** — Thymeleaf HTML files. `fragments/navbar.html` is included on every page so the nav bar is written once.

## Key Features to Highlight in an Interview

- **Authentication & authorization** — Spring Security with BCrypt password hashing and role-based access control (`ROLE_USER` vs `ROLE_ADMIN`).
- **Relational data modeling** — one-to-many (`Category → Product`, `Order → OrderItem`) and many-to-one relationships mapped with JPA annotations.
- **Transactional integrity** — `placeOrder()` is wrapped in `@Transactional`; if inventory runs out mid-checkout, the whole operation rolls back instead of corrupting data.
- **Separation of concerns** — controllers never touch the database directly; that's the service/repository layers' job. This is the pattern almost every Java backend job will expect you to know.
- **Server-side rendering** — no separate frontend build step or API layer needed, which kept the project achievable solo while still being a "real" web app.

## Possible Extensions (good "future work" talking points)

- REST API endpoints (`@RestController`) alongside the HTML views, so a React/mobile frontend could consume the same backend
- Product reviews & ratings
- Pagination on the product list (`Pageable` in Spring Data)
- Real payment integration (Stripe test mode)
- Unit/integration tests with JUnit + Spring's `@SpringBootTest` and `MockMvc`
- Dockerfile + deployment to Render/Railway/AWS so it's live at a public URL for your resume link

## Putting This on Your Resume

1. Push this folder to a GitHub repo (`git init`, `git add .`, `git commit -m "Initial commit"`, then create a repo on GitHub and `git push`).
2. Resume bullet example:
   > *Built a full-stack e-commerce web application (Java, Spring Boot, Spring Security, JPA/Hibernate, Thymeleaf) implementing user authentication, role-based access control, a shopping cart, and a transactional checkout flow.*
3. Link the GitHub repo. If you want it live (not just runnable locally), consider deploying to Railway or Render's free tier — ask if you'd like help with that next.

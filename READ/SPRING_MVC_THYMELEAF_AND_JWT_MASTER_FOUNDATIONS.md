# 📚 Master Foundations: Spring MVC, Thymeleaf, JWT & Refresh Tokens (MVC vs REST) + Complete CRUD Blueprint

---

## 🎯 Purpose of This Guide
If you feel you know nothing about Spring Boot Controllers, how Thymeleaf talks to Java backend, how JWT authentication works with Cookies vs REST headers, or how to build CRUD operations from scratch, **this document is your complete, zero-assumption masterclass**.

---

## 📑 Table of Contents
1. [Core Spring MVC Controller Architecture & Lifecycle](#1-core-spring-mvc-controller-architecture--lifecycle)
2. [How Spring MVC Controller Works with Thymeleaf (Step-by-Step)](#2-how-spring-mvc-controller-works-with-thymeleaf-step-by-step)
3. [Thymeleaf Syntax & Essential Directives Reference](#3-thymeleaf-syntax--essential-directives-reference)
4. [The PRG Pattern: Model vs RedirectAttributes (Flash Attributes)](#4-the-prg-pattern-model-vs-redirectattributes-flash-attributes)
5. [JWT & Refresh Token Architecture: MVC (Thymeleaf) vs REST API](#5-jwt--refresh-token-architecture-mvc-thymeleaf-vs-rest-api)
   - [5.1 JWT in REST API (Stateless Client / React / Mobile)](#51-jwt-in-rest-api-stateless-client--react--mobile)
   - [5.2 JWT in Spring MVC + Thymeleaf (HttpOnly Cookie Session)](#52-jwt-in-spring-mvc--thymeleaf-httponly-cookie-session)
   - [5.3 Side-by-Side Comparison Matrix](#53-side-by-side-comparison-matrix)
   - [5.4 Refresh Token Mechanics & Rotation (RTR)](#54-refresh-token-mechanics--rotation-rtr)
6. [Complete Step-by-Step CRUD Blueprint in Spring MVC + Thymeleaf](#6-complete-step-by-step-crud-blueprint-in-spring-mvc--thymeleaf)
   - [Step 1: Entity & DTO](#step-1-entity--dto)
   - [Step 2: Repository & Service](#step-2-repository--service)
   - [Step 3: Controller (GET List, POST Create, GET/POST Edit, POST Delete)](#step-3-controller-get-list-post-create-getpost-edit-post-delete)
   - [Step 4: Thymeleaf HTML Template (Table, Modals, Forms, Alerts)](#step-4-thymeleaf-html-template-table-modals-forms-alerts)
7. [Common Pitfalls, Gotchas & Interview Questions](#7-common-pitfalls-gotchas--interview-questions)

---

## 1. Core Spring MVC Controller Architecture & Lifecycle

### 1.1 `@Controller` vs `@RestController`

| Feature | `@Controller` (Spring MVC) | `@RestController` (REST API) |
| :--- | :--- | :--- |
| **Primary Goal** | Returns **HTML Views / Webpages** | Returns **Raw Data (JSON / XML)** |
| **Return Value** | Returns a String representing the **View Name** (e.g., `"admin/rfq-list"`) or a redirect string (e.g., `"redirect:/admin/rfqs"`). | Returns Java Objects directly (e.g., `ResponseEntity<UserResponse>`), which Jackson converts to JSON. |
| **Target Client** | Web Browsers requesting full HTML pages. | SPAs (React, Angular, Vue), Mobile Apps, Third-party APIs. |
| **Template Engine** | Thymeleaf, JSP, Freemarker. | None (No UI engine). |
| **Annotation Makeup** | `@Component` $\rightarrow$ `@Controller` | `@Controller` + `@ResponseBody` |

---

### 1.2 The Complete Spring MVC Request-Response Lifecycle

When a user clicks a link or submits a form in their browser:

```
[ Web Browser ]
      │
      │ 1. HTTP GET /admin/rfqs
      ▼
┌──────────────────────────────────────────────────────────────┐
│ DispatcherServlet (Spring's Front Controller)                │
└──────────────────────────────┬───────────────────────────────┘
                               │ 2. Asks "Who handles this URL?"
                               ▼
┌──────────────────────────────────────────────────────────────┐
│ HandlerMapping (finds @GetMapping("/admin/rfqs"))             │
└──────────────────────────────┬───────────────────────────────┘
                               │ 3. Executes controller method
                               ▼
┌──────────────────────────────────────────────────────────────┐
│ AdminMvcController                                           │
│ ├─ Calls Service / Repository for data                       │
│ ├─ Adds data to Model: model.addAttribute("rfqs", rfqList)   │
│ └─ Returns View Name String: "admin/rfq-list"                │
└──────────────────────────────┬───────────────────────────────┘
                               │ 4. Passes Model + "admin/rfq-list"
                               ▼
┌──────────────────────────────────────────────────────────────┐
│ ThymeleafViewResolver (Template Engine)                      │
│ ├─ Finds src/main/resources/templates/admin/rfq-list.html    │
│ ├─ Evaluates Thymeleaf expressions (${rfqs}, th:each, etc.)  │
│ └─ Produces Final Pure HTML String                           │
└──────────────────────────────┬───────────────────────────────┘
                               │ 5. Returns Pure HTML + CSS
                               ▼
[ Web Browser renders the webpage ]
```

---

## 2. How Spring MVC Controller Works with Thymeleaf (Step-by-Step)

### The Three Fundamental Actors:
1. **The Controller**: Prepares the data and names the template.
2. **The Model (`org.springframework.ui.Model`)**: A key-value container (like a Java `Map`) that transports data from the Controller to the HTML page.
3. **Thymeleaf Template (`.html`)**: Reads keys from the Model and fills in the HTML structure dynamically.

### Visual Example:

```
Java Controller:
model.addAttribute("companyName", "Masstech Corp");
model.addAttribute("totalCount", 25);
return "dashboard";
         │
         │  (Transfers keys: "companyName" and "totalCount")
         ▼
Thymeleaf HTML (dashboard.html):
<h1 th:text="${companyName}">Default Name</h1>   ===> Renders: <h1>Masstech Corp</h1>
<p th:text="${totalCount}">0</p>                 ===> Renders: <p>25</p>
```

---

## 3. Thymeleaf Syntax & Essential Directives Reference

Thymeleaf uses special HTML attributes starting with `th:`. When the browser visits the page directly without Spring, it sees normal fallback HTML. When rendered by Spring, Thymeleaf replaces the content.

| Directive | Purpose | Real Code Example | Rendered HTML Output |
| :--- | :--- | :--- | :--- |
| `th:text` | Replaces inner text with dynamic value (escapes HTML). | `<span th:text="${rfq.rfqNo}">RFQ-001</span>` | `<span>RFQ-20261005-AB12</span>` |
| `th:utext` | Replaces inner text without escaping HTML tags. | `<div th:utext="${rawHtmlContent}"></div>` | Renders HTML elements inside. |
| `th:each` | Loops through collections/lists. | `<tr th:each="item : ${items}"><td th:text="${item.itemCode}"></td></tr>` | Generates a `<tr>` for every item in list. |
| `th:if` | Renders element **only if condition is true**. | `<span th:if="${rfq.status == 'OPEN'}" class="badge bg-success">Open</span>` | Badge rendered if status is OPEN. |
| `th:unless` | Renders element **only if condition is false** (inverse of `th:if`). | `<div th:unless="${isInvited}" class="alert">Not Invited</div>` | Alert shown if `isInvited == false`. |
| `th:href` | Creates dynamic URLs (handles context path & params). | `<a th:href="@{/admin/rfqs/{id}(id=${rfq.rfqNo})}">View</a>` | `<a href="/admin/rfqs/RFQ-20261005-AB12">View</a>` |
| `th:action` | Specifies dynamic form submission target. | `<form th:action="@{/admin/rfqs/{id}/edit(id=${rfq.rfqNo})}" method="post">` | `<form action="/admin/rfqs/RFQ-001/edit" method="post">` |
| `th:value` | Pre-populates form `<input>` value. | `<input type="text" name="title" th:value="${rfq.title}">` | `<input type="text" name="title" value="Raw Steel RFQ">` |
| `th:classappend` | Conditionally appends CSS classes. | `<a th:classappend="${activePage == 'rfqs' ? 'active' : ''}">` | `<a class="nav-link active">` |
| `th:replace` / `th:insert` | Embeds reusable reusable layout fragments (e.g. sidebars, navbars, headers). | `<div th:replace="~{fragments/sidebar :: sidebar(${role}, ${activePage})}"></div>` | Replaces `<div>` with complete sidebar HTML. |

---

## 4. The PRG Pattern: Model vs RedirectAttributes (Flash Attributes)

### Why PRG (Post / Redirect / Get) Is Critical:

If a user submits a form (`POST /admin/rfqs/create`) and the controller directly returns a template name (`return "admin/rfq-list"`), then if the user refreshes their browser (**F5**), the browser will prompt:
> *"Confirm Form Resubmission: Do you want to submit this form again?"*

This causes **duplicate database records**!

### The Solution: The PRG Pattern
```
Browser                     Controller                      Browser
   │                             │                             │
   │ 1. POST /rfqs/create (Data) │                             │
   ├────────────────────────────>│                             │
   │                             │ 2. Saves to DB              │
   │                             │ 3. Sets Flash Attribute     │
   │                             │ 4. Sends HTTP 302 Redirect  │
   │<────────────────────────────┤    ("redirect:/admin/rfqs") │
   │                                                           │
   │ 5. GET /admin/rfqs (Safe idempotent request)              │
   ├──────────────────────────────────────────────────────────>│
   │                                                           │
   │ 6. Displays HTML with Flash Message (Disappears on refresh)
   │<──────────────────────────────────────────────────────────┤
```

### `Model` vs `RedirectAttributes`

```java
// ❌ WRONG for POST operations:
@PostMapping("/rfqs/create")
public String createRfq(Model model) {
    model.addAttribute("successMessage", "Created!"); // LOST on redirect!
    return "redirect:/admin/rfqs"; 
}

// ✅ CORRECT: Flash Attributes survive exactly ONE redirect via temporary session:
@PostMapping("/rfqs/create")
public String createRfq(RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("successMessage", "RFQ Created Successfully!");
    return "redirect:/admin/rfqs";
}
```

---

## 5. JWT & Refresh Token Architecture: MVC (Thymeleaf) vs REST API

Both architectures can use **JSON Web Tokens (JWT)**, but they store and transmit tokens differently.

---

### 5.1 JWT in REST API (Stateless Client / React / Mobile)

```
[ React / Mobile App ]
      │
      │ 1. POST /api/auth/login { "email": "...", "password": "..." }
      ▼
[ AuthController (@RestController) ]
      │
      │ 2. Returns JSON Body: { "accessToken": "eyJ...", "refreshToken": "uuid-..." }
      ▼
[ React saves tokens in localStorage / Memory ]
      │
      │ 3. Every subsequent API call sends Header:
      │    Authorization: Bearer eyJhbGciOi...
      ▼
[ Backend JwtAuthenticationFilter validates header ]
```

* **Where is token stored?** In client JavaScript memory, LocalStorage, or SessionStorage.
* **How is it transmitted?** In the `Authorization: Bearer <token>` HTTP header.
* **When authentication fails?** Backend returns **HTTP 401 Unauthorized** with a JSON body (`{ "error": "Unauthorized" }`).

---

### 5.2 JWT in Spring MVC + Thymeleaf (HttpOnly Cookie Session)

Since HTML form submissions and standard browser clicks (`<a href="...">`) cannot easily inject custom headers like `Authorization: Bearer`, Spring MVC utilizes **Secure, HttpOnly Cookies**:

```
[ Web Browser ]
      │
      │ 1. User submits form: POST /verify-2fa (preAuthToken, otpCode)
      ▼
[ AuthMvcController (@Controller) ]
      │
      │ 2. Verifies 2FA OTP -> Generates Access Token (JWT)
      │ 3. Attaches HttpOnly Cookie to HttpServletResponse:
      │    Cookie: jwtToken=eyJhbGci...; Path=/; HttpOnly; MaxAge=604800;
      ▼
[ Browser automatically stores cookie and sends it with EVERY subsequent page request ]
      │
      │ 4. User clicks link: GET /admin/rfqs
      │    Browser automatically sends Cookie: jwtToken=eyJ...
      ▼
[ JwtAuthenticationFilter inspects request.getCookies() ]
      │
      │ 5. Validates JWT signature -> Populates SecurityContextHolder
      ▼
[ AdminMvcController renders requested page ]
```

#### Why is `HttpOnly` Cookie Secure?
JavaScript (`document.cookie`) **cannot read** `HttpOnly` cookies. This makes the system **immune to Cross-Site Scripting (XSS) token theft**!

#### What happens during Logout?
To log out in Spring MVC, the controller overwrites the cookie with an empty value and `MaxAge=0`:
```java
Cookie jwtCookie = new Cookie("jwtToken", "");
jwtCookie.setHttpOnly(true);
jwtCookie.setPath("/");
jwtCookie.setMaxAge(0); // Immediately deletes cookie from browser
response.addCookie(jwtCookie);
```

---

### 5.3 Side-by-Side Comparison Matrix

| Dimension | REST API (`@RestController`) | Spring MVC (`@Controller` + Thymeleaf) |
| :--- | :--- | :--- |
| **Token Storage** | LocalStorage, Memory, or Secure Storage. | `HttpOnly` Browser Cookie (`jwtToken`). |
| **Token Transmission** | `Authorization: Bearer <token>` Header. | Sent automatically by browser in `Cookie` Header. |
| **Login Response** | JSON Object (`{ "accessToken": "...", ... }`). | Redirect (`redirect:/dashboard`) + Set-Cookie header. |
| **Unauthenticated Handling** | Returns **401 Unauthorized JSON**. | Redirects browser to **`/login?unauthorized=true`**. |
| **XSS Vulnerability** | Higher if stored in `localStorage`. | Protected by `HttpOnly` cookie flag. |
| **CSRF Consideration** | Stateless APIs typically disable CSRF. | Protected via SameSite cookies or CSRF tokens. |
| **Logout Mechanism** | Client discards token from storage. | Server sends `MaxAge=0` cookie to clear browser storage. |

---

### 5.4 Refresh Token Mechanics & Rotation (RTR)

* **Access Token**: Short-lived (e.g., 15 minutes to 24 hours). Contains user identity and role.
* **Refresh Token**: Long-lived (e.g., 7 days). Stored in the database (`User.refreshToken`) with an expiry timestamp (`User.refreshTokenExpiry`).

#### Refresh Token Rotation (RTR) Flow:
When an access token expires:
1. Client sends the `refreshToken` to `/auth/refresh-token`.
2. Server verifies `user.refreshTokenExpiry.isAfter(Instant.now())`.
3. Server generates a **new Access Token** AND a **new Refresh Token**.
4. The old refresh token is invalidated in the database.
5. This prevents replay attacks if a refresh token is intercepted!

---

## 6. Complete Step-by-Step CRUD Blueprint in Spring MVC + Thymeleaf

Here is the exact blueprint for implementing any CRUD feature from scratch.
Let's use a complete, working example: **Managing Products (`Product`)**.

---

### Step 1: Entity & DTO

```java
// Product.java (Entity)
package com.ampta.rfq.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String name;

    private BigDecimal price;
    private Integer stockQty;
}
```

```java
// ProductRequest.java (DTO)
package com.ampta.rfq.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String productCode;
    private String name;
    private BigDecimal price;
    private Integer stockQty;
}
```

---

### Step 2: Repository & Service

```java
// ProductRepository.java
package com.ampta.rfq.repository;

import com.ampta.rfq.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    boolean existsByProductCode(String productCode);
}
```

```java
// ProductService.java & ProductServiceImpl.java
package com.ampta.rfq.service.impl;

import com.ampta.rfq.dto.ProductRequest;
import com.ampta.rfq.entity.Product;
import com.ampta.rfq.exception.ResourceNotFoundException;
import com.ampta.rfq.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(ProductRequest req) {
        Product p = new Product();
        p.setProductCode(req.getProductCode());
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setStockQty(req.getStockQty());
        return productRepository.save(p);
    }

    public Product updateProduct(Long id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setStockQty(req.getStockQty());
        return productRepository.save(p);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
```

---

### Step 3: Controller (Complete CRUD)

```java
package com.ampta.rfq.controller;

import com.ampta.rfq.dto.ProductRequest;
import com.ampta.rfq.entity.Product;
import com.ampta.rfq.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductMvcController {

    private final ProductServiceImpl productService;

    // 1. READ (List all products)
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("activePage", "products");
        return "products/list"; // templates/products/list.html
    }

    // 2. CREATE (Process form submission)
    @PostMapping("/create")
    public String createProduct(@RequestParam String productCode,
                                @RequestParam String name,
                                @RequestParam BigDecimal price,
                                @RequestParam Integer stockQty,
                                RedirectAttributes redirectAttributes) {
        try {
            ProductRequest request = new ProductRequest();
            request.setProductCode(productCode);
            request.setName(name);
            request.setPrice(price);
            request.setStockQty(stockQty);

            productService.createProduct(request);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // 3. UPDATE (Process edit submission)
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer stockQty,
                              RedirectAttributes redirectAttributes) {
        try {
            ProductRequest request = new ProductRequest();
            request.setName(name);
            request.setPrice(price);
            request.setStockQty(stockQty);

            productService.updateProduct(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // 4. DELETE (Process deletion)
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
```

---

### Step 4: Thymeleaf HTML Template (Table, Modals, Forms, Alerts)

`src/main/resources/templates/products/list.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Product Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-4">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Product Catalog</h2>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createProductModal">
            + Add New Product
        </button>
    </div>

    <!-- Alert Messages -->
    <div th:if="${successMessage}" class="alert alert-success alert-dismissible fade show" role="alert">
        <span th:text="${successMessage}"></span>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <div th:if="${errorMessage}" class="alert alert-danger alert-dismissible fade show" role="alert">
        <span th:text="${errorMessage}"></span>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>

    <!-- READ Table -->
    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Product Code</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Stock Qty</th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr th:if="${#lists.isEmpty(products)}">
                        <td colspan="6" class="text-center py-4 text-muted">No products found.</td>
                    </tr>
                    <tr th:each="prod : ${products}">
                        <td th:text="${prod.id}">1</td>
                        <td><strong th:text="${prod.productCode}">PRD-001</strong></td>
                        <td th:text="${prod.name}">Steel Valve</td>
                        <td th:text="'$' + ${#numbers.formatDecimal(prod.price, 1, 2)}">$45.00</td>
                        <td>
                            <span class="badge" th:classappend="${prod.stockQty > 10 ? 'bg-success' : 'bg-warning'}"
                                  th:text="${prod.stockQty} + ' in stock'">25 in stock</span>
                        </td>
                        <td class="text-end">
                            <!-- EDIT Modal Trigger Button -->
                            <button class="btn btn-sm btn-outline-primary me-1" 
                                    data-bs-toggle="modal" 
                                    th:data-bs-target="'#editModal_' + ${prod.id}">
                                Edit
                            </button>

                            <!-- DELETE Form Button -->
                            <form th:action="@{/products/{id}/delete(id=${prod.id})}" method="post" class="d-inline"
                                  onsubmit="return confirm('Are you sure you want to delete this product?');">
                                <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                            </form>

                            <!-- EDIT MODAL (One per row) -->
                            <div class="modal fade text-start" th:id="'editModal_' + ${prod.id}" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <form th:action="@{/products/{id}/edit(id=${prod.id})}" method="post">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Edit Product: <span th:text="${prod.productCode}"></span></h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label class="form-label">Product Name</label>
                                                    <input type="text" name="name" class="form-control" th:value="${prod.name}" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Price ($)</label>
                                                    <input type="number" step="0.01" name="price" class="form-control" th:value="${prod.price}" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Stock Quantity</label>
                                                    <input type="number" name="stockQty" class="form-control" th:value="${prod.stockQty}" required>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                <button type="submit" class="btn btn-primary">Save Changes</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <!-- END EDIT MODAL -->
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- CREATE PRODUCT MODAL -->
<div class="modal fade" id="createProductModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form th:action="@{/products/create}" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Create New Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Product Code</label>
                        <input type="text" name="productCode" class="form-control" placeholder="e.g. PRD-1001" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Product Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Industrial Valve" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Price ($)</label>
                        <input type="number" step="0.01" name="price" class="form-control" placeholder="0.00" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Stock Quantity</label>
                        <input type="number" name="stockQty" class="form-control" placeholder="10" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Create Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

---

## 7. Common Pitfalls, Gotchas & Interview Questions

### Q1: Why do forms in HTML only support `GET` and `POST`?
* **Answer**: Standard HTML5 `<form>` tags only natively support `method="get"` and `method="post"`. To perform `PUT` or `DELETE` in pure HTML forms, developers either:
  1. Use `POST` routes (e.g. `POST /products/{id}/delete` or `POST /products/{id}/edit`), or
  2. Use Spring's `HiddenHttpMethodFilter` with `<input type="hidden" name="_method" value="DELETE">`.

### Q2: Why does refreshing the page duplicate data if you don't redirect?
* **Answer**: Because a browser refresh re-sends the exact last HTTP request. If the last request was a `POST` form submission, the browser executes the `POST` again. By using the **Post/Redirect/Get (PRG)** pattern, the last request becomes a safe `GET` request.

### Q3: What is the difference between `th:replace` and `th:insert`?
* **`th:replace`**: Replaces the host tag completely with the fragment tag.
* **`th:insert`**: Inserts the fragment content **inside** the host tag as a child.

### Q4: How does Spring Security authenticate every Thymeleaf request using JWT?
* **Answer**: The custom filter `JwtAuthenticationFilter` extends `OncePerRequestFilter`. On every incoming HTTP request, it searches the `request.getCookies()` array for a cookie named `jwtToken`. If found and cryptographically valid, it extracts the username and roles, creates a `UsernamePasswordAuthenticationToken`, and loads it into `SecurityContextHolder`.

---

🎉 *You now have the complete foundational knowledge of Spring MVC, Thymeleaf, JWT architecture, and CRUD patterns!*

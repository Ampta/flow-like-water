# Spring MVC & Spring Boot Master Interview Guide
> **Comprehensive Enterprise Deep-Dive with Architecture Diagrams, Internal Mechanics, Production Java 17+ / Spring Boot 3.x Examples, Real-World Project Incidents & Industry Best Practices**

---

# Table of Contents
1. [Module 1: Spring MVC Core Architecture & Web Layer (Q1 – Q25)](#module-1-spring-mvc-core-architecture--web-layer)
   - [Q1. What is Spring MVC?](#q1-what-is-spring-mvc)
   - [Q2. Explain the architecture of Spring MVC.](#q2-explain-the-architecture-of-spring-mvc)
   - [Q3. What is the role of DispatcherServlet?](#q3-what-is-the-role-of-dispatcherservlet)
   - [Q4. What is the front controller in Spring MVC?](#q4-what-is-the-front-controller-in-spring-mvc)
   - [Q5. What are the main components of Spring MVC?](#q5-what-are-the-main-components-of-spring-mvc)
   - [Q6. What is the difference between @Controller and @RestController?](#q6-what-is-the-difference-between-controller-and-restcontroller)
   - [Q7. What is @RequestMapping used for?](#q7-what-is-requestmapping-used-for)
   - [Q8. How do you handle GET and POST requests in Spring MVC?](#q8-how-do-you-handle-get-and-post-requests-in-spring-mvc)
   - [Q9. What is the difference between @GetMapping and @PostMapping?](#q9-what-is-the-difference-between-getmapping-and-postmapping)
   - [Q10. What is a Model in Spring MVC?](#q10-what-is-a-model-in-spring-mvc)
   - [Q11. What is the purpose of ModelAndView?](#q11-what-is-the-purpose-of-modelandview)
   - [Q12. What are @PathVariable and @RequestParam annotations?](#q12-what-are-pathvariable-and-requestparam-annotations)
   - [Q13. How can you handle form submission in Spring MVC?](#q13-how-can-you-handle-form-submission-in-spring-mvc)
   - [Q14. How do you perform data validation in Spring MVC?](#q14-how-do-you-perform-data-validation-in-spring-mvc)
   - [Q15. How can you handle exceptions globally in Spring MVC?](#q15-how-can-you-handle-exceptions-globally-in-spring-mvc)
   - [Q16. What is @ExceptionHandler annotation?](#q16-what-is-exceptionhandler-annotation)
   - [Q17. How do you return JSON data from a controller?](#q17-how-do-you-return-json-data-from-a-controller)
   - [Q18. How to upload files in Spring MVC?](#q18-how-to-upload-files-in-spring-mvc)
   - [Q19. What is the difference between redirect: and forward:?](#q19-what-is-the-difference-between-redirect-and-forward)
   - [Q20. How do you configure Spring MVC without XML (Java-based config)?](#q20-how-do-you-configure-spring-mvc-without-xml-java-based-config)
   - [Q21. How to integrate Spring MVC with Hibernate/JPA?](#q21-how-to-integrate-spring-mvc-with-hibernatejpa)
   - [Q22. How do you manage sessions in Spring MVC?](#q22-how-do-you-manage-sessions-in-spring-mvc)
   - [Q23. How do you use @SessionAttributes and @ModelAttribute?](#q23-how-do-you-use-sessionattributes-and-modelattribute)
   - [Q24. How do you internationalize (i18n) a Spring MVC application?](#q24-how-do-you-internationalize-i18n-a-spring-mvc-application)
   - [Q25. How can you secure a Spring MVC application?](#q25-how-can-you-secure-a-spring-mvc-application)
2. [Module 2: Spring Boot Fundamentals & Core Mechanics (Q26 – Q65)](#module-2-spring-boot-fundamentals--core-mechanics)
   - [Q26. What is Spring Boot?](#q26-what-is-spring-boot)
   - [Q27. What are the advantages of using Spring Boot?](#q27-what-are-the-advantages-of-using-spring-boot)
   - [Q28. What is the difference between Spring and Spring Boot?](#q28-what-is-the-difference-between-spring-and-spring-boot)
   - [Q29. What is the use of application.properties or application.yml?](#q29-what-is-the-use-of-applicationproperties-or-applicationyml)
   - [Q30. What are Starters in Spring Boot?](#q30-what-are-starters-in-spring-boot)
   - [Q31. What is the purpose of @SpringBootApplication?](#q31-what-is-the-purpose-of-springbootapplication)
   - [Q32. What are the main components included in @SpringBootApplication?](#q32-what-are-the-main-components-included-in-springbootapplication)
   - [Q33. What is auto-configuration in Spring Boot?](#q33-what-is-auto-configuration-in-spring-boot)
   - [Q34. How does Spring Boot automatically configure beans?](#q34-how-does-spring-boot-automatically-configure-beans)
   - [Q35. How do you create a Spring Boot REST API?](#q35-how-do-you-create-a-spring-boot-rest-api)
   - [Q36. What is @RestController?](#q36-what-is-restcontroller)
   - [Q37. What is the difference between @Component, @Service, @Repository, and @Controller?](#q37-what-is-the-difference-between-component-service-repository-and-controller)
   - [Q38. How do you inject dependencies in Spring Boot?](#q38-how-do-you-inject-dependencies-in-spring-boot)
   - [Q39. What are the different ways to configure a Spring Boot application?](#q39-what-are-the-different-ways-to-configure-a-spring-boot-application)
   - [Q40. What is the role of @Value and @ConfigurationProperties?](#q40-what-is-the-role-of-value-and-configurationproperties)
   - [Q41. What is CommandLineRunner?](#q41-what-is-commandlinerunner)
   - [Q42. What is the use of application.properties file?](#q42-what-is-the-use-of-applicationproperties-file)
   - [Q43. What is @EnableAutoConfiguration?](#q43-what-is-enableautoconfiguration)
   - [Q44. How do you exclude specific auto-configuration classes?](#q44-how-do-you-exclude-specific-auto-configuration-classes)
   - [Q45. How do you create a custom banner in Spring Boot?](#q45-how-do-you-create-a-custom-banner-in-spring-boot)
   - [Q46. How do you connect Spring Boot to a database?](#q46-how-do-you-connect-spring-boot-to-a-database)
   - [Q47. What is the use of spring.datasource.url property?](#q47-what-is-the-use-of-springdatasourceurl-property)
   - [Q48. What is Spring Data JPA?](#q48-what-is-spring-data-jpa)
   - [Q49. How do you configure JPA in Spring Boot?](#q49-how-do-you-configure-jpa-in-spring-boot)
   - [Q50. What is the use of spring.jpa.hibernate.ddl-auto?](#q50-what-is-the-use-of-springjpahibernateddl-auto)
   - [Q51. What is the difference between create, update, and validate in Hibernate DDL-auto?](#q51-what-is-the-difference-between-create-update-and-validate-in-hibernate-ddl-auto)
   - [Q52. How do you execute native queries in Spring Data JPA?](#q52-how-do-you-execute-native-queries-in-spring-data-jpa)
   - [Q53. How to define custom queries using @Query annotation?](#q53-how-to-define-custom-queries-using-query-annotation)
   - [Q54. How do you enable pagination and sorting in Spring Data JPA?](#q54-how-do-you-enable-pagination-and-sorting-in-spring-data-jpa)
   - [Q55. How do you handle database transactions in Spring Boot?](#q55-how-do-you-handle-database-transactions-in-spring-boot)
   - [Q56. What is Actuator in Spring Boot?](#q56-what-is-actuator-in-spring-boot)
   - [Q57. What is the difference between @RestControllerAdvice and @ControllerAdvice?](#q57-what-is-the-difference-between-restcontrolleradvice-and-controlleradvice)
   - [Q58. How can you handle exceptions globally in Spring Boot?](#q58-how-can-you-handle-exceptions-globally-in-spring-boot)
   - [Q59. How to use Swagger/OpenAPI with Spring Boot?](#q59-how-to-use-swaggeropenapi-with-spring-boot)
   - [Q60. How to implement validation using @Valid and @NotNull annotations?](#q60-how-to-implement-validation-using-valid-and-notnull-annotations)
   - [Q61. How do you manage different profiles in Spring Boot?](#q61-how-do-you-manage-different-profiles-in-spring-boot)
   - [Q62. How do you secure a REST API using Spring Security?](#q62-how-do-you-secure-a-rest-api-using-spring-security)
   - [Q63. What is the difference between synchronous and asynchronous REST endpoints?](#q63-what-is-the-difference-between-synchronous-and-asynchronous-rest-endpoints)
   - [Q64. How do you handle file uploads and downloads in Spring Boot REST?](#q64-how-do-you-handle-file-uploads-and-downloads-in-spring-boot-rest)
   - [Q65. How do you consume REST APIs using RestTemplate or WebClient?](#q65-how-do-you-consume-rest-apis-using-resttemplate-or-webclient)

---

# Module 1: Spring MVC Core Architecture & Web Layer

---

### Q1. What is Spring MVC?

#### 1. Concept & Interview Answer
- **Spring MVC** is an HTTP-oriented web framework built on the **Model-View-Controller** design pattern and centered around the **Front Controller** architectural pattern (`DispatcherServlet`).
- It cleanly isolates web applications into three key layers:
  - **Model:** Encapsulates business data, state, and domain logic (POJOs/DTOs).
  - **View:** Formats and renders the model data to the user (HTML via Thymeleaf/JSP or JSON/XML for RESTful APIs).
  - **Controller:** Acts as the traffic coordinator; intercepts user requests, binds request parameters, invokes service layer logic, and decides which model/view to return.
- Provides complete dependency injection, declarative validation, seamless exception handling, and pluggable view resolution.

#### 2. Internal Working & Request Lifecycle
```
[ HTTP Request (Browser/Postman) ]
               │
               ▼
     [ DispatcherServlet ] ◄── (Front Controller)
         │           ▲
         │ (1)       │ (2) HandlerExecutionChain
         ▼           │
     [ HandlerMapping ]
         │
         │ (3) Dispatch to Adapter
         ▼
     [ HandlerAdapter ] ──(4) Invoke Controller Method──► [ @Controller / @RestController ]
         │                                                        │
         │◄─────────────────(5) Return Model/View or DTO──────────┘
         │
         ├───► If SSR View: [ ViewResolver ] ──► Renders Template (.html)
         └───► If REST API: [ HttpMessageConverter (Jackson) ] ──► Serializes JSON to Response
               │
               ▼
[ HTTP 200 OK / JSON / HTML Response ]
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderWebViewController {

    private final OrderService orderService;

    public OrderWebViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}/summary")
    public String showOrderSummary(@PathVariable("id") Long orderId, Model model) {
        OrderSummaryDTO orderSummary = orderService.getOrderSummary(orderId);
        model.addAttribute("order", orderSummary);
        return "orders/summary"; // ViewResolver maps to /templates/orders/summary.html
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a banking portal, web dashboard views and REST services were tightly coupled in monolithic servlets. Using Spring MVC, the team extracted the UI into clean Thymeleaf templates backed by standard `@Controller` classes, while external partner integrations were routed through dedicated `@RestController` endpoints. This reduced code duplication by 45% and made unit testing controllers trivial using `MockMvc`.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** keep controllers ultra-thin. Controllers should only extract request parameters, call the domain service layer, and return response models or view names.
- ✅ **DO** use constructor-based dependency injection over `@Autowired` field injection to ensure immutability and easy unit testing.
- ❌ **DON'T** write business or transaction logic (`@Transactional`) inside `@Controller` methods; transactions should strictly live in the `@Service` layer.
- ❌ **DON'T** return domain JPA `@Entity` objects directly to views or web clients to avoid exposing internal database schemas and triggering unintended lazy loading queries.

---

### Q2. Explain the architecture of Spring MVC.

#### 1. Concept & Interview Answer
The Spring MVC architecture is built around a centralized **Front Controller** (`DispatcherServlet`) that orchestrates request processing through several modular, extensible components:
1. **`DispatcherServlet`**: The master entry point that receives all incoming HTTP requests.
2. **`HandlerMapping`**: Maps the incoming request URL and HTTP method to a specific handler method (`@RequestMapping`).
3. **`HandlerAdapter`**: Adapts and invokes the controller method via reflection, handling parameter binding and argument resolution.
4. **`HandlerInterceptor`**: Intercepts requests before, during, and after handler execution (used for logging, auth tokens, audits).
5. **`HttpMessageConverter` / `ViewResolver`**: Converts raw return values to JSON/XML or resolves logical view names into physical templates.

#### 2. Detailed Internal Flow Diagram
```
Client Request
      │
      ▼
1. DispatcherServlet.doDispatch()
      │
      ├──► 2. HandlerMapping.getHandler(request)
      │       └── Returns: HandlerExecutionChain [Interceptors + HandlerMethod]
      │
      ├──► 3. HandlerInterceptor.preHandle() (Auth / Auditing)
      │
      ├──► 4. HandlerAdapter.handle() (Invokes Controller Method via Reflection)
      │       ├── Argument Resolvers: @PathVariable, @RequestParam, @RequestBody
      │       └── Return Value Handlers: @ResponseBody, ModelAndView
      │
      ├──► 5. HandlerInterceptor.postHandle()
      │
      ├──► 6. Render View / Write to HttpServletResponse (Jackson JSON / Thymeleaf)
      │
      └──► 7. HandlerInterceptor.afterCompletion() (Resource Cleanup)
```

#### 3. Production Code Example (Custom Interceptor & Config)
```java
package com.enterprise.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdInterceptor.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        request.setAttribute("CORRELATION_ID", correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);
        log.info("Incoming Request: [{} {}] CorrelationId: [{}]", request.getMethod(), request.getRequestURI(), correlationId);
        return true; // Continue execution chain
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // Manipulate model before view rendering if needed
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("Completed Request: [{} {}] Status: [{}]", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}
```

```java
// Registration in WebMvcConfigurer
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CorrelationIdInterceptor correlationIdInterceptor;

    public WebMvcConfig(CorrelationIdInterceptor correlationIdInterceptor) {
        this.correlationIdInterceptor = correlationIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(correlationIdInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/auth/**");
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A high-throughput financial microservice experienced severe tracing issues because distributed trace IDs were missing from downstream HTTP calls. By registering a custom `HandlerInterceptor` in Spring MVC's architecture, every incoming HTTP request automatically had its `X-Trace-ID` extracted, put into the SLF4J MDC (Mapped Diagnostic Context), and populated in the response header without touching individual controller endpoints.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `HandlerInterceptor` for cross-cutting web concerns (metrics, correlation IDs, rate-limit checks) instead of duplicating logic across controllers.
- ✅ **DO** ensure `preHandle` returns `false` and explicitly writes an HTTP response status code (e.g., `401 Unauthorized`) if an authorization check fails in an interceptor.
- ❌ **DON'T** place authentication logic in custom interceptors when Spring Security is available; Spring Security filters run before `DispatcherServlet` and provide much stronger protection against unauthorized payload processing.

---

### Q3. What is the role of DispatcherServlet?

#### 1. Concept & Interview Answer
- `DispatcherServlet` is the central **Front Controller** in Spring MVC. It inherits from `HttpServlet` (via `FrameworkServlet` -> `HttpServletBean` -> `HttpServlet`).
- It acts as the single entry point for all web requests entering the application.
- **Key Responsibilities:**
  1. Captures all incoming HTTP requests configured via servlet mapping (e.g., `/` or `/api/*`).
  2. Consults `HandlerMapping` to locate the appropriate controller.
  3. Uses `HandlerAdapter` to execute controller methods with bound parameters.
  4. Resolves views via `ViewResolver` or writes raw response bodies via `HttpMessageConverter`.
  5. Catches and delegates exceptions to `@ControllerAdvice` / `HandlerExceptionResolver`.

#### 2. Class Hierarchy & Execution Path
```
       jakarta.servlet.http.HttpServlet
                      ▲
                      │
        org.springframework.web.servlet.HttpServletBean
                      ▲
                      │
        org.springframework.web.servlet.FrameworkServlet (Creates WebApplicationContext)
                      ▲
                      │
        org.springframework.web.servlet.DispatcherServlet
                      │
          ┌───────────┴───────────┐
          │ doService()           │
          │ doDispatch()          │ ──► Core orchestration logic
          └───────────────────────┘
```

#### 3. Production Code Example (Customizing DispatcherServlet in Spring Boot)
```java
package com.enterprise.web.config;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class DispatcherServletCustomConfig {

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        // Throw 404 NoHandlerFoundException instead of default static error page
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {
        DispatcherServletRegistrationBean registration = new DispatcherServletRegistrationBean(dispatcherServlet, "/api/v1/*");
        registration.setName("customApiDispatcherServlet");
        registration.setLoadOnStartup(1); // Eager initialization on server boot
        return registration;
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a low-latency payment processing service, the first user request after deployment experienced a 2.5-second latency spike (cold-start penalty) because `DispatcherServlet` initialized its handler mappings and beans lazily on the first incoming request. Setting `registration.setLoadOnStartup(1)` (or `spring.mvc.servlet.load-on-startup=1` in `application.yml`) forced `DispatcherServlet` to initialize all beans during server startup, eliminating the first-request latency spike.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** set `load-on-startup=1` in enterprise production environments to ensure all controller routes and converters are warmed up before receiving production traffic.
- ✅ **DO** enable `spring.mvc.throw-exception-if-no-handler-found=true` if you want a global `@RestControllerAdvice` to catch 404s and return a uniform JSON error payload.
- ❌ **DON'T** create multiple `DispatcherServlet` instances unless you have a strict multi-tenant or legacy split architecture where web portals and REST APIs require isolated Spring `WebApplicationContext` containers.

---

### Q4. What is the front controller in Spring MVC?

#### 1. Concept & Interview Answer
- The **Front Controller** is a core enterprise design pattern (defined in Gang of Four / Martin Fowler's Patterns of Enterprise Application Architecture) where **a single centralized controller handles all requests for a website or application**.
- In Spring MVC, **`DispatcherServlet`** is the implementation of the Front Controller pattern.
- **Why use a Front Controller?**
  - Eliminates code duplication: Common tasks (URL routing, security checks, character encoding, session management, locale resolution) are handled centrally in one place instead of repeating them across dozens of individual servlets.
  - Enforces a consistent architectural pipeline across all web requests.

#### 2. Architecture Comparison: Front Controller vs Traditional Servlets
```
Traditional Servlet Architecture (Anti-pattern for large apps):
[ Request /orders ] ────────► [ OrderServlet ] (Duplicate Auth + Parsing + Error handling)
[ Request /users  ] ────────► [ UserServlet  ] (Duplicate Auth + Parsing + Error handling)
[ Request /auth   ] ────────► [ AuthServlet  ] (Duplicate Auth + Parsing + Error handling)

Spring MVC Front Controller Pattern:
[ All Requests ] ──► [ DispatcherServlet (Front Controller) ]
                              │ (Centralized Security, Parsing, Interception)
                              ├──► [ OrderController ]
                              ├──► [ UserController ]
                              └──► [ AuthController ]
```

#### 3. Production Code Example (Centralized Filter + Front Controller Integration)
```java
package com.enterprise.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter running ahead of DispatcherServlet (Front Controller)
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Ensure character encoding is UTF-8 across all requests before DispatcherServlet processes them
        httpRequest.setCharacterEncoding("UTF-8");
        
        // Pass request to DispatcherServlet
        chain.doFilter(httpRequest, response);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A legacy application had 40 separate servlets. Whenever a new security compliance header (`Strict-Transport-Security` / `X-Content-Type-Options`) or internationalized character encoding was required, all 40 servlets had to be manually edited and redeployed. Migrating to Spring MVC's `DispatcherServlet` front controller pattern allowed security headers, correlation IDs, and encoding rules to be configured once globally.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** understand the lifecycle boundary: `Servlet Filters` run **before** `DispatcherServlet`, whereas `HandlerInterceptors` run **inside** `DispatcherServlet` (having direct access to the target Spring Controller handler method).
- ✅ **DO** use Filters for protocol/HTTP-level concerns (CORS, GZIP compression, Character Encoding) and Interceptors for application-level concerns (Permissions, Spring-managed beans).

---

### Q5. What are the main components of Spring MVC?

#### 1. Concept & Interview Answer
Spring MVC consists of 8 core building blocks:
1. **`DispatcherServlet`**: The Front Controller delegating requests.
2. **`HandlerMapping`**: Maps incoming request URLs to matching controller methods (e.g., `RequestMappingHandlerMapping`).
3. **`HandlerAdapter`**: Executes the handler method found by `HandlerMapping` (e.g., `RequestMappingHandlerAdapter`).
4. **`Controller` / `@RestController`**: Application-specific bean containing business endpoint logic.
5. **`ModelAndView` / `Model`**: Data container holding attributes to be passed to the view.
6. **`ViewResolver`**: Resolves string view names (e.g., `"index"`) into concrete view engines (Thymeleaf, FreeMarker, JSP).
7. **`HttpMessageConverter`**: Reads/writes HTTP request and response bodies directly for JSON/XML (Jackson, Gson).
8. **`HandlerExceptionResolver`**: Maps uncaught exceptions to HTTP error codes or custom error views (e.g., `ExceptionHandlerExceptionResolver`).

#### 2. Component Collaboration Lifecycle
```
[ Client ]
    │
    ▼ (1) HTTP GET /orders/10
[ DispatcherServlet ]
    │──(2) URL Lookup──► [ HandlerMapping ] (Returns HandlerMethod)
    │
    │──(3) Execute Handler──► [ HandlerAdapter ]
    │                              │
    │                              ▼ (4)
    │                       [ OrderController ] ──► [ OrderService ]
    │                              │
    │                              ▼ (5)
    │                       Returns OrderDTO / Model
    │
    ├─── If JSON Request ──► [ HttpMessageConverter (Jackson) ] ──► JSON Stream
    └─── If HTML Request ──► [ ViewResolver ] ──► Thymeleaf Template Engine
    │
    ▼ (6)
[ HTTP Response Stream ]
```

#### 3. Production Code Example (Customizing MVC Components via `WebMvcConfigurer`)
```java
package com.enterprise.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CustomMvcComponentsConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Find or create Jackson converter with strict production ISO-8601 Date formatting
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                ObjectMapper mapper = jacksonConverter.getObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
        }
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An e-commerce service was returning `java.time.LocalDateTime` as an array `[2026, 9, 26, 20, 30, 0]` instead of an ISO-8601 string `"2026-09-26T20:30:00Z"`, causing mobile clients to crash during JSON deserialization. By configuring the `MappingJackson2HttpMessageConverter` component inside `WebMvcConfigurer`, all date-time serialization was standardized globally without modifying a single controller.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** customize Spring MVC components by implementing `WebMvcConfigurer` rather than subclassing `WebMvcConfigurationSupport`, which would disable Spring Boot's auto-configuration.
- ❌ **DON'T** use `@EnableWebMvc` in a Spring Boot application unless you want to take 100% manual control and discard all of Spring Boot's web autoconfigurations (like static resource handling, default converters, and error pages).

---

### Q6. What is the difference between @Controller and @RestController?

#### 1. Concept & Interview Answer
- **`@Controller`**: Standard archetype for **Server-Side Rendering (SSR)**. A handler method returns a `String` (logical view name) or `ModelAndView`. Spring passes the model data to a `ViewResolver` to render an HTML page.
- **`@RestController`**: Introduced in Spring 4.0, it is a meta-annotation composed of **`@Controller` + `@ResponseBody`**.
- In `@RestController`, the returned Java object is written directly into the HTTP response body stream as JSON or XML using `HttpMessageConverter`, completely bypassing view resolution.

#### 2. Deep-Dive Comparison Table
| Feature | `@Controller` | `@RestController` |
| :--- | :--- | :--- |
| **Annotation Composition** | `@Component` | `@Controller` + `@ResponseBody` |
| **Default Return Value** | Logical View Name (e.g. `"dashboard"`) | Serialized HTTP Response Body (JSON/XML) |
| **ViewResolver Used?** | **Yes** (Resolves `.html`, `.jsp`) | **No** (Bypasses view resolution) |
| **Typical Target Clients** | Web Browsers requesting HTML | SPA (React/Angular), Mobile Apps, Microservices |
| **Can return JSON?** | Yes, but requires explicit `@ResponseBody` on method | Yes, automatically enabled for all methods |

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 1. Traditional MVC Controller for SSR
@Controller
@RequestMapping("/portal/users")
public class UserPortalController {

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        model.addAttribute("username", "Alex");
        return "users/profile"; // Resolves to templates/users/profile.html
    }

    // Can return JSON if explicitly annotated with @ResponseBody
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "{\"status\":\"UP\"}";
    }
}

// 2. Modern REST API Controller for JSON
@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {

    public record UserResponse(Long id, String email, String role) {}

    @GetMapping("/{id}")
    public UserResponse getUserDetails(@PathVariable Long id) {
        // Automatically serialized into JSON: {"id":1,"email":"alex@enterprise.com","role":"ADMIN"}
        return new UserResponse(id, "alex@enterprise.com", "ADMIN");
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer created an authentication API using `@Controller` and returned an `AuthTokenResponse` object without adding `@ResponseBody`. Spring treated the `AuthTokenResponse.toString()` as a view name and looked for a template named `AuthTokenResponse[token=eyJhbGci...]`, throwing a `Circular view path` error. Changing the class annotation to `@RestController` fixed the bug and returned the JSON payload as intended.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use Java 17 `record` for request and response DTOs in `@RestController` for immutability, built-in constructors, getters, and clean JSON serialization.
- ✅ **DO** explicitly return `ResponseEntity<T>` in `@RestController` methods when returning non-200 status codes (e.g., `ResponseEntity.status(HttpStatus.CREATED).body(createdEntity)`).
- ❌ **DON'T** mix HTML-generating methods and REST API methods in the same controller class. Maintain strict package separation: `com.company.controller.web` and `com.company.controller.api`.

---

### Q7. What is @RequestMapping used for?

#### 1. Concept & Interview Answer
- `@RequestMapping` is the foundational annotation used to map incoming web requests to specific controller classes and handler methods.
- It can be applied at:
  1. **Class level:** Defines a shared base URL path, media types, or header requirements for all handler methods in the class.
  2. **Method level:** Defines specific endpoint routing, HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`), request parameters, and headers.
- **Key Attributes:**
  - `value` / `path`: URL mapping (supports Ant-style paths and URI templates like `/users/{id}`).
  - `method`: HTTP request method (`RequestMethod.GET`, `RequestMethod.POST`, etc.).
  - `consumes`: Restricts matching by incoming `Content-Type` header (e.g., `MediaType.APPLICATION_JSON_VALUE`).
  - `produces`: Restricts matching by incoming `Accept` header (e.g., `MediaType.APPLICATION_JSON_VALUE`).
  - `params`: Matches only if specific query parameters are present (e.g., `params = "version=2"`).
  - `headers`: Matches only if specific HTTP headers are present (e.g., `headers = "X-API-KEY"`).

#### 2. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/api/v1/customers",
    produces = MediaType.APPLICATION_JSON_VALUE // All endpoints return JSON
)
public class CustomerController {

    public record CustomerDto(Long id, String name, String email) {}

    // Match only if Content-Type is application/json and header X-Tenant-Id is present
    @RequestMapping(
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        headers = "X-Tenant-Id"
    )
    public ResponseEntity<CustomerDto> createCustomer(
            @RequestHeader("X-Tenant-Id") String tenantId,
            @RequestBody CustomerDto payload) {
        // Business logic...
        return ResponseEntity.ok(payload);
    }
}
```

#### 3. Real-World Project Scenario
> **Incident / Scenario:** An API Gateway was forwarding legacy XML requests and modern JSON requests to the same customer endpoint. By configuring `consumes = MediaType.APPLICATION_JSON_VALUE` on one handler and `consumes = MediaType.APPLICATION_XML_VALUE` on an adjacent handler under the same URL path, Spring MVC automatically routed requests to the appropriate parser without writing complex `if-else` parsing branches.

#### 4. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer method-specific shortcut annotations (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`) over verbose `@RequestMapping(method = RequestMethod.GET)`.
- ✅ **DO** declare class-level base paths using `@RequestMapping("/api/v1/resources")` to keep individual method annotations clean and maintain API versioning consistency.
- ❌ **DON'T** omit HTTP methods on `@RequestMapping` at the method level; doing so causes the endpoint to accept **ALL** HTTP verbs (GET, POST, DELETE, PUT, OPTIONS, HEAD), posing a security risk.

---

### Q8. How do you handle GET and POST requests in Spring MVC?

#### 1. Concept & Interview Answer
- **GET Requests:** Used to retrieve resources without modifying server state (safe & idempotent). Parameters are passed in the URL path (`@PathVariable`) or query string (`@RequestParam`). Handled via `@GetMapping`.
- **POST Requests:** Used to create new resources or submit sensitive data (non-idempotent). Data is typically passed in the HTTP request body (`@RequestBody` or form data). Handled via `@PostMapping`.

#### 2. Request Data Extraction Mechanics
```
GET /api/v1/orders/1001?includeDetails=true
      │           │            │
      │           └──► @PathVariable("id") Long id
      └──────────────► @RequestParam(defaultValue="false") boolean includeDetails

POST /api/v1/orders
Headers: Content-Type: application/json
Body: { "customerId": 50, "amount": 199.99 }
      │
      └──► @RequestBody @Valid CreateOrderRequest request
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderManagementController {

    public record CreateOrderRequest(
        @NotNull Long customerId,
        @NotNull @Positive BigDecimal amount
    ) {}

    public record OrderResponse(Long orderId, Long customerId, BigDecimal amount, String status) {}

    // Handling GET Request
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable("id") Long orderId,
            @RequestParam(name = "detailed", defaultValue = "false") boolean detailed) {
        OrderResponse response = new OrderResponse(orderId, 101L, new BigDecimal("199.99"), "CONFIRMED");
        return ResponseEntity.ok(response);
    }

    // Handling POST Request
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long generatedId = 9999L;
        OrderResponse createdOrder = new OrderResponse(generatedId, request.customerId(), request.amount(), "CREATED");
        
        // Return 201 Created with Location Header
        URI location = URI.create("/api/v1/orders/" + generatedId);
        return ResponseEntity.created(location).body(createdOrder);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An internal dashboard was triggering payment submissions using GET requests with query parameters (`GET /pay?card=...&amt=...`). The sensitive card information was permanently recorded in proxy server access logs, browser history, and CDN caches. Refactoring to a secure `@PostMapping` with an encrypted `@RequestBody` and proper HTTPS payload stopped the sensitive data leakage and complied with PCI-DSS audit requirements.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** return HTTP `201 Created` along with a `Location` header containing the URL of the newly created resource on successful POST requests.
- ✅ **DO** keep GET requests strictly read-only and idempotent. Never perform database mutations inside a GET request.
- ❌ **DON'T** pass sensitive authentication credentials, passwords, or PII via GET query parameters.

---

### Q9. What is the difference between @GetMapping and @PostMapping?

#### 1. Concept & Interview Answer
- `@GetMapping` is a shortcut composed annotation for `@RequestMapping(method = RequestMethod.GET)`.
- `@PostMapping` is a shortcut composed annotation for `@RequestMapping(method = RequestMethod.POST)`.

#### 2. Key Differences Matrix
| Feature | `@GetMapping` | `@PostMapping` |
| :--- | :--- | :--- |
| **HTTP Verb** | `GET` | `POST` |
| **HTTP Semantics** | Safe & Idempotent (Read-only) | Non-Idempotent (Creates/Mutates state) |
| **Payload/Body** | No request body (uses URL/Query params) | Uses Request Body (JSON/XML/Form data) |
| **Caching** | Cacheable by browsers, CDNs, proxies | Non-cacheable by default |
| **Bookmarking** | URLs can be bookmarked and shared | Cannot be bookmarked directly |
| **Security** | Data visible in URL logs | Data encrypted inside TLS request body |

#### 3. Production Code Example
```java
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    // GET: Querying balance (Safe, Cacheable, Idempotent)
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    // POST: Transferring funds (State-changing, Non-Idempotent)
    @PostMapping("/transfers")
    public ResponseEntity<TransferReceipt> executeTransfer(@Valid @RequestBody FundTransferRequest request) {
        TransferReceipt receipt = accountService.transferFunds(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A web scraper crawler periodically crawled a web application and triggered state deletions because a developer had mapped a delete action to `@GetMapping("/users/delete/{id}")`. Replacing it with `@DeleteMapping` (or `@PostMapping`) prevented search engine bots from accidentally wiping user accounts.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** strictly adhere to RFC 7231 HTTP method semantics.
- ✅ **DO** use `@GetMapping` when fetching data, `@PostMapping` when creating resources, `@PutMapping` for complete replacements, `@PatchMapping` for partial updates, and `@DeleteMapping` for deletions.

---

### Q10. What is a Model in Spring MVC?

#### 1. Concept & Interview Answer
- In Spring MVC, `Model` (`org.springframework.ui.Model`) is an interface that acts as a data carrier/holder between the Controller and the View template.
- Internally, it is essentially a `Map<String, Object>` where keys are attribute names and values are the Java objects to be exposed to the view rendering engine (such as Thymeleaf or JSP).
- Spring automatically injects an instance of `Model` (or `ModelMap`) into controller handler methods when declared as a method parameter.

#### 2. Internal Working Diagram
```
[ HTTP Request ] ──► [ Controller Method(Model model) ]
                               │
                               ├── model.addAttribute("user", userDTO);
                               │
                               ▼
                      [ Model Map Container ]
                      { "user" : UserDTO(id=1, name="Alice") }
                               │
                               ▼
                      [ ViewResolver / Thymeleaf Engine ]
                      Replaces <span th:text="${user.name}"></span>
                               │
                               ▼
                      [ Rendered HTML: <span>Alice</span> ]
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductCatalogController {

    public record Product(Long id, String name, double price) {}

    @GetMapping("/catalog")
    public String showCatalog(@RequestParam(defaultValue = "ALL") String category, Model model) {
        List<Product> products = List.of(
            new Product(1L, "Laptop Stand", 49.99),
            new Product(2L, "Mechanical Keyboard", 129.50)
        );

        // Populate Model attributes
        model.addAttribute("selectedCategory", category);
        model.addAttribute("productList", products);
        model.addAttribute("totalCount", products.size());

        return "catalog/products"; // View resolves to /templates/catalog/products.html
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a high-traffic e-commerce portal, an un-sanitized user input string was added directly to `model.addAttribute("searchQuery", rawInput)` and rendered via an unescaped JSP tag, creating a stored Cross-Site Scripting (XSS) vulnerability. Upgrading to Thymeleaf (which escapes model attributes by default via `th:text`) and sanitizing model attributes resolved the security finding.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer `Model` interface over `ModelMap` or `ModelAndView` for cleaner, more modern controller signatures.
- ✅ **DO** use meaningful, camelCase attribute names when populating the model.
- ❌ **DON'T** pass heavyweight JPA entities with lazy associations into the Model if the view is rendered outside of an active database transaction (`LazyInitializationException`). Always convert entities to DTOs before adding to `Model`.

---

### Q11. What is the purpose of ModelAndView?

#### 1. Concept & Interview Answer
- `ModelAndView` is a composite holder class in Spring MVC that encapsulates **both** the Model data (as a Map) and the View information (as a view name `String` or concrete `View` object) in a single return object.
- Unlike declaring `Model model` in parameters and returning a `String` view name, `ModelAndView` allows a controller method to construct and return both dynamically in one programmatic statement.

#### 2. Model vs ModelMap vs ModelAndView Comparison
| Type | Nature | How it is used | Return Type of Method |
| :--- | :--- | :--- | :--- |
| **`Model`** | Interface | Injected as a method parameter | Method returns `String` (view name) |
| **`ModelMap`** | Class (implements `Map`) | Injected as a method parameter | Method returns `String` (view name) |
| **`ModelAndView`** | Class | Instantiated inside controller method | Method returns `ModelAndView` object |

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InvoiceViewController {

    private final InvoiceService invoiceService;

    public InvoiceViewController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices/{id}")
    public ModelAndView getInvoiceView(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView();

        if (invoiceService.exists(id)) {
            mav.setViewName("invoices/details"); // /templates/invoices/details.html
            mav.addObject("invoice", invoiceService.getInvoice(id));
            mav.addObject("status", "FOUND");
        } else {
            mav.setViewName("errors/not-found");
            mav.addObject("errorMessage", "Invoice #" + id + " does not exist.");
        }

        return mav;
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a dynamic reporting application, the target view engine varied at runtime based on the requested format: HTML web dashboard vs JasperReports PDF download. `ModelAndView` allowed the controller to programmatically set either `mav.setViewName("dashboard")` or `mav.setView(new PdfReportView())` while supplying the exact same underlying model dataset.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `ModelAndView` when conditional routing dynamically selects completely different `View` implementations (e.g., PDF/Excel download vs HTML).
- ❌ **DON'T** use `ModelAndView` for standard REST APIs (`@RestController`). REST endpoints should return typed DTOs wrapped in `ResponseEntity<T>`.

---

### Q12. What are @PathVariable and @RequestParam annotations?

#### 1. Concept & Interview Answer
- **`@PathVariable`**: Extracts dynamic values embedded directly into the URI path template (e.g., `/users/{id}`). Used to identify a **specific resource**.
- **`@RequestParam`**: Extracts query parameters from the URL query string (e.g., `?page=1&size=20`) or form-encoded POST fields. Used for **filtering, sorting, searching, or pagination**.

#### 2. Architectural Comparison Matrix
| Feature | `@PathVariable` | `@RequestParam` |
| :--- | :--- | :--- |
| **URL Example** | `/api/v1/products/45` | `/api/v1/products?category=tech&page=0` |
| **Usage Intent** | Identifies a specific unique resource | Filters, sorts, or paginates resources |
| **Default Required?** | `required = true` | `required = true` (can set `false` / `defaultValue`) |
| **Data Encoding** | URL Path Segment | URL Query Parameter / Form data |

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    public record EmployeeDto(Long id, String name, String role) {}

    // Combining both @PathVariable and @RequestParam in an enterprise endpoint
    @GetMapping("/{deptId}/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(
            @PathVariable(name = "deptId") Long departmentId,
            @RequestParam(name = "role", required = false) String roleFilter,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size) {
        
        List<EmployeeDto> employees = employeeService.findEmployees(departmentId, roleFilter, page, size);
        return ResponseEntity.ok(employees);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A frontend client omitted a non-critical search parameter, causing an unexpected `MissingServletRequestParameterException: Required request parameter 'filter' for method parameter type String is not present` (HTTP 400). Adding `required = false` and `defaultValue = ""` on the `@RequestParam` eliminated the production 400 errors.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always specify the `name` or `value` attribute explicitly (e.g., `@PathVariable("deptId")`) rather than relying on parameter name discovery, which can fail if compiled without the `-parameters` javac flag.
- ✅ **DO** provide sensible `defaultValue` attributes for pagination and sorting `@RequestParam` variables.
- ❌ **DON'T** use `@PathVariable` to pass complex search criteria with special characters (like slashes or colons), which can break URL decoding and web server route matching.

---

### Q13. How can you handle form submission in Spring MVC?

#### 1. Concept & Interview Answer
- Form submission in Spring MVC is handled via data binding using the **`@ModelAttribute`** annotation.
- When an HTML form is submitted with `POST` (typically `application/x-www-form-urlencoded` or `multipart/form-data`):
  1. Spring MVC instantiates the command/form backing object (POJO).
  2. The `WebDataBinder` automatically matches form input field `name` attributes to the setter methods or fields of the POJO.
  3. Declarative validation (`@Valid` / `@Validated`) validates constraints.
  4. Any validation errors are placed into a `BindingResult` object, which must immediately follow the model attribute in the method signature.

#### 2. Form Submission Flow Diagram
```
[ Browser HTML Form ] ──(POST application/x-www-form-urlencoded)──►
                                    │
                                    ▼
                         [ DispatcherServlet ]
                                    │
                                    ▼
                         [ WebDataBinder ]
             (Maps input name="email" to UserForm.setEmail())
                                    │
                                    ▼
                    [ Bean Validation (Hibernate Validator) ]
                                    │
               ┌────────────────────┴────────────────────┐
               ▼ (Has Errors)                            ▼ (Valid)
      [ BindingResult.hasErrors() ]               [ Process Order ]
      Returns "users/registration"                Redirects to "redirect:/success"
      (Displays error messages)                   (Post/Redirect/Get Pattern)
```

#### 3. Production Code Example (Form Object + Controller + PRG Pattern)
```java
package com.enterprise.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationForm {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

```java
package com.enterprise.web.controller;

import com.enterprise.web.dto.UserRegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    @GetMapping
    public String showForm(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new UserRegistrationForm());
        }
        return "users/registration";
    }

    @PostMapping
    public String handleRegistration(
            @Valid @ModelAttribute("registrationForm") UserRegistrationForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "users/registration"; // Return view with validation error messages
        }

        // Execute registration service...
        redirectAttributes.addFlashAttribute("successMessage", "Account created successfully!");
        return "redirect:/register/success"; // PRG Pattern (Post-Redirect-Get)
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "users/success";
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** Users were hitting browser "Refresh" (F5) on order confirmation pages, resulting in duplicate credit card charges because the controller returned a view directly from a POST request. Implementing the **Post/Redirect/Get (PRG)** pattern with `RedirectAttributes.addFlashAttribute()` ensured that browser refreshes performed safe idempotent GET requests.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always implement the **Post/Redirect/Get (PRG)** pattern on web form submissions to prevent duplicate form submissions upon page refresh.
- ✅ **DO** place `BindingResult` **immediately after** the `@Valid @ModelAttribute` argument in the method signature; if other arguments are placed in between, Spring throws an `IllegalStateException`.
- ❌ **DON'T** bind forms directly to JPA entities. Use separate form DTOs to avoid Mass Assignment vulnerabilities (where malicious users submit hidden fields like `isAdmin=true`).

---

### Q14. How do you perform data validation in Spring MVC?

#### 1. Concept & Interview Answer
- Data validation in Spring MVC is performed using the **Jakarta Bean Validation API** (standard specification) implemented by **Hibernate Validator**.
- By adding standard validation annotations (`@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Pattern`, `@Email`) to DTO fields and annotating controller method arguments with **`@Valid`** (Jakarta standard) or **`@Validated`** (Spring variant that supports validation groups).
- If validation fails:
  - For `@Controller` forms: Validation errors are captured in `BindingResult`.
  - For `@RestController`: Spring throws `MethodArgumentNotValidException` (HTTP 400 Bad Request), which is intercepted by a global `@RestControllerAdvice`.

#### 2. Validation Architecture Flow
```
[ Incoming JSON Request ]
          │
          ▼
[ DispatcherServlet ] ──► [ RequestMappingHandlerAdapter ]
                                   │
                                   ▼
                       [ Jackson Deserializer ] (Creates DTO)
                                   │
                                   ▼
                       [ WebDataBinder / Validator ]
                       (Validates @NotNull, @Email, etc.)
                                   │
             ┌─────────────────────┴─────────────────────┐
             ▼ (Valid)                                   ▼ (Validation Fails)
     [ Controller Method ]               [ MethodArgumentNotValidException ]
                                                         │
                                                         ▼
                                             [ @RestControllerAdvice ]
                                                         │
                                                         ▼
                                             [ HTTP 400 Bad Request JSON ]
```

#### 3. Production Code Example
```java
package com.enterprise.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotBlank(message = "Transaction reference is mandatory")
    @Pattern(regexp = "^TXN-[A-Z0-9]{8}$", message = "Reference must follow format TXN-XXXXXXXX")
    String transactionRef,

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email address format")
    String customerEmail
) {}
```

```java
// Controller endpoint
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentApiController {

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An attacker submitted negative transfer amounts (`amount: -500.00`) to a balance transfer endpoint, causing debit logic to invert into credit additions. Adding `@DecimalMin(value = "0.01")` and `@Positive` constraints coupled with `@Valid` on the controller DTO immediately blocked all fraudulent payloads at the controller boundary before business logic was touched.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** validate all untrusted input at the web layer before passing data to services.
- ✅ **DO** write custom validation annotations (implementing `ConstraintValidator<Annotation, FieldType>`) for complex business checks (e.g., IBAN validation, cross-field password matching).
- ❌ **DON'T** write manual `if (dto.getAmount() == null)` checks inside your controller methods; leverage declarative Jakarta Bean Validation annotations.

---

### Q15. How can you handle exceptions globally in Spring MVC?

#### 1. Concept & Interview Answer
- Global exception handling in Spring MVC is implemented using **`@ControllerAdvice`** (for MVC view controllers) or **`@RestControllerAdvice`** (for REST APIs).
- It uses the **AOP (Aspect-Oriented Programming)** interceptor pattern to catch exceptions thrown by any `@RequestMapping` method across the entire application.
- In Spring Boot 3.x, global handlers return standard **RFC 7807 `ProblemDetail`** objects containing standard error structures (type, title, status, detail, timestamp, path).

#### 2. Exception Dispatching Flow
```
[ Controller Method ] ──Throws Exception (e.g., EntityNotFoundException)──►
                                    │
                                    ▼
                         [ DispatcherServlet ]
                                    │
                                    ▼
                     [ HandlerExceptionResolver ]
                                    │
                                    ▼
     [ @RestControllerAdvice / @ExceptionHandler(EntityNotFoundException.class) ]
                                    │
                                    ▼
     [ ProblemDetail / Standard JSON Error Response (HTTP 404) ]
```

#### 3. Production Code Example (Spring Boot 3.x RFC 7807 Standard)
```java
package com.enterprise.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    // 1. Handle Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.enterprise.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    // 2. Handle Validation Failures (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for request payload");
        problemDetail.setTitle("Invalid Request Payload");
        problemDetail.setType(URI.create("https://api.enterprise.com/errors/validation"));
        problemDetail.setProperty("timestamp", Instant.now());

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("invalidFields", fieldErrors);

        return problemDetail;
    }

    // 3. Fallback for Unhandled Internal Server Errors
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred.");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** Uncaught `NullPointerException` and `SQLException` stack traces were leaking raw database table names, SQL queries, and internal server file paths to API consumers. Introducing a centralized `@RestControllerAdvice` suppressed internal stack traces from public responses, logged them securely into server logs with correlation IDs, and returned standardized `ProblemDetail` JSON to clients.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** adopt the standard RFC 7807 `ProblemDetail` format (native in Spring Boot 3.x) for consistent enterprise API error responses.
- ✅ **DO** include a unique correlation/trace ID in the error response payload so users can quote it to customer support for log lookup.
- ❌ **DON'T** expose raw Java stack traces, database schema details, or server directory paths in production HTTP responses.

---

### Q16. What is @ExceptionHandler annotation?

#### 1. Concept & Interview Answer
- `@ExceptionHandler` is an annotation used to define a method that handles specific exceptions thrown by controller methods.
- **Scope of `@ExceptionHandler`:**
  - **Local (Inside a specific `@Controller` class):** Handles exceptions originating only from that controller.
  - **Global (Inside a `@ControllerAdvice` or `@RestControllerAdvice` class):** Intercepts and handles specified exceptions application-wide across all controllers.
- Accepts an array of target exception classes: `@ExceptionHandler({OrderNotFoundException.class, PaymentFailedException.class})`.

#### 2. Resolution Precedence Order
When an exception occurs, Spring MVC searches for handlers in this exact order:
1. Local `@ExceptionHandler` inside the throwing Controller.
2. Base controller class `@ExceptionHandler` (if inherited).
3. Global `@ControllerAdvice` matching the specific exception type.
4. Global `@ControllerAdvice` matching the closest superclass (e.g., `RuntimeException.class` -> `Exception.class`).

#### 3. Production Code Example
```java
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    // Local exception handler: only handles SubscriptionExpiredException thrown within this controller
    @ExceptionHandler(SubscriptionExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleSubscriptionExpired(SubscriptionExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(Map.of(
            "error", "SUBSCRIPTION_EXPIRED",
            "message", ex.getMessage(),
            "renewalUrl", "https://app.com/renew"
        ));
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An inventory service needed special HTTP 409 Conflict handling for `OptimisticLockingFailureException` to prompt the client to retry. Instead of littering every service and controller method with `try-catch` blocks, a single `@ExceptionHandler(OptimisticLockingFailureException.class)` was added to the `@RestControllerAdvice`, cleanly transforming database lock contention into graceful 409 responses with retry headers.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** place general exception handlers in a centralized `@RestControllerAdvice` and reserve local `@ExceptionHandler` methods only for controller-specific recovery logic.
- ✅ **DO** inject `HttpServletRequest` or `WebRequest` into `@ExceptionHandler` method signatures if you need the request URL or headers.

---

### Q17. How do you return JSON data from a controller?

#### 1. Concept & Interview Answer
To return JSON data from a Spring MVC controller:
1. **Method 1 (Recommended):** Annotate the controller class with **`@RestController`**. All handler methods will automatically serialize returned Java objects to JSON.
2. **Method 2:** Annotate a specific method in a standard `@Controller` with **`@ResponseBody`**.
3. **Method 3:** Return a **`ResponseEntity<T>`** from a `@RestController` method to customize HTTP status codes, headers, and JSON body.
- **Under the Hood:** Spring MVC utilizes `MappingJackson2HttpMessageConverter` (backed by the Jackson `ObjectMapper` library on the classpath) to serialize POJOs/Records into JSON bytes written to the `HttpServletResponse` output stream.

#### 2. Serialization Mechanism
```
Controller Method returns: AccountDto(id=101, balance=500.00)
                 │
                 ▼
[ RequestResponseBodyMethodProcessor ]
                 │
                 ▼
[ MappingJackson2HttpMessageConverter ]
                 │
                 ▼
[ Jackson ObjectMapper.writeValue() ]
                 │
                 ▼
Writes to OutputStream: {"id":101,"balance":500.00}
Headers: Content-Type: application/json;charset=UTF-8
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountApiController {

    // Record with Jackson customization annotations
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AccountResponse(
        @JsonProperty("account_number") String accountNumber,
        @JsonProperty("account_type") String accountType,
        Double balance,
        String optionalPromoNote
    ) {}

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        AccountResponse response = new AccountResponse(accountNumber, "SAVINGS", 14500.75, null);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Service-Version", "v1.2.0");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an enterprise API returning customer entities with bidirectional JPA `@OneToMany` relationships (Customer <-> Orders), Jackson encountered a `JsonMappingException: Infinite recursion (StackOverflowError)`. The fix was using immutable DTO Records instead of entities and applying `@JsonIgnore` / `@JsonManagedReference` where necessary.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use Java 17 `record` for request/response DTOs; they provide built-in immutability, compact syntax, and seamless Jackson integration.
- ✅ **DO** configure `spring.jackson.default-property-inclusion=non_null` if you want to omit null fields globally from API JSON payloads to save network bandwidth.
- ❌ **DON'T** manually construct JSON strings using string concatenation (e.g., `"{\"id\":" + id + "}"`); always let Jackson serialize typed objects.

---

### Q18. How to upload files in Spring MVC?

#### 1. Concept & Interview Answer
- File uploads in Spring MVC are handled using the **`MultipartFile`** interface (or `List<MultipartFile>` for multiple files).
- The client submits a `POST` request with `Content-Type: multipart/form-data`.
- Spring MVC uses **`StandardServletMultipartResolver`** (built into Servlet 3.0+) to parse multi-part boundary streams into `MultipartFile` objects.
- `MultipartFile` provides methods: `.getOriginalFilename()`, `.getContentType()`, `.getSize()`, `.getBytes()`, and `.getInputStream()`.

#### 2. File Upload Architecture
```
[ Client Form / Postman ] ──(multipart/form-data with file stream)──►
                                     │
                                     ▼
                          [ DispatcherServlet ]
                                     │
                                     ▼
                       [ MultipartResolver ]
             (Splits form parts & boundary streams)
                                     │
                                     ▼
        [ Controller: uploadFile(@RequestParam("file") MultipartFile file) ]
                                     │
                                     ▼
       [ Stream file.getInputStream() ──► Cloud S3 / Local Disk Storage ]
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentUploadController {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
        MediaType.APPLICATION_PDF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType) throws IOException {

        // 1. Validation
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file cannot be empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File exceeds 10MB limit");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Invalid file type");
        }

        // 2. Safe file storage (Prevent Path Traversal)
        String safeFileName = UUID.randomUUID() + "_" + Path.of(file.getOriginalFilename()).getFileName().toString();
        Path targetLocation = Paths.get("/var/enterprise/uploads").resolve(safeFileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully: " + safeFileName);
    }
}
```

```properties
# application.properties configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=15MB
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A document portal suffered a major security vulnerability where an attacker uploaded a file named `../../../../etc/cron.d/malicious_job`. Because the code used `file.getOriginalFilename()` directly in file path concatenation, the file escaped the upload directory (Path Traversal). Sanitizing the file name via `Path.of(originalFilename).getFileName().toString()` and generating random UUID storage keys closed the vulnerability.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** stream files directly to cloud storage (e.g., AWS S3, Azure Blob) via `file.getInputStream()` rather than reading the entire file into heap memory via `file.getBytes()`, which causes OutOfMemoryErrors on large uploads.
- ✅ **DO** validate file MIME types using Apache Tika (magic number byte inspection) rather than trusting client-provided `file.getContentType()` headers alone.
- ❌ **DON'T** store uploaded files directly in the web application's root deployment directory.

---

### Q19. What is the difference between redirect: and forward:?

#### 1. Concept & Interview Answer
- **`forward:` (Server-side Forward):**
  - Executed entirely on the **server side** within the servlet container.
  - The browser is **unaware** of the forward; the URL in the browser address bar **does not change**.
  - Request and request-scoped attributes are **preserved** and available to the target resource.
  - Faster because only **1 HTTP request/response cycle** is involved.
- **`redirect:` (Client-side Redirect):**
  - Sends an **HTTP 302 Found** (or 303/307) response to the browser with a `Location` header.
  - The browser makes a **brand-new HTTP GET request** to the new URL; the address bar **changes**.
  - Original request attributes are **lost** (must use `RedirectAttributes` flash attributes to pass data across redirects).
  - Involves **2 round trips** between client and server.

#### 2. Architecture Comparison Diagram
```
FORWARD (Server-Side):
Browser ──(1) GET /old-page──► [ DispatcherServlet ] ──Internal Forward──► [ /new-page ]
Browser ◄──(2) HTML Rendered from /new-page (URL remains /old-page)──────────┘

REDIRECT (Client-Side):
Browser ──(1) POST /submit──► [ DispatcherServlet ]
Browser ◄──(2) HTTP 302 Found (Location: /thank-you)
Browser ──(3) GET /thank-you────────────────────────► [ DispatcherServlet ]
Browser ◄──(4) HTTP 200 OK HTML (URL changes to /thank-you)
```

#### 3. Production Code Example
```java
@Controller
public class NavigationController {

    // 1. Forward Example (Internal routing, URL stays "/legacy-profile")
    @GetMapping("/legacy-profile")
    public String forwardToNewProfile() {
        return "forward:/v2/profile"; // Internal server dispatch
    }

    // 2. Redirect Example (Browser URL updates to "/dashboard", PRG pattern)
    @PostMapping("/login")
    public String performLogin(@RequestParam String username, RedirectAttributes redirectAttributes) {
        // Authenticate...
        redirectAttributes.addFlashAttribute("welcomeMessage", "Welcome back, " + username);
        return "redirect:/dashboard"; // Instructs browser to issue GET /dashboard
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "portal/dashboard";
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** After completing payment processing, a checkout controller forwarded internally to `forward:/order-confirmed`. When users refreshed the confirmation page, the browser re-submitted the entire credit card payment POST request. Changing `forward:` to `redirect:/order-confirmed` implemented the Post-Redirect-Get pattern and prevented thousands of duplicate payment attempts.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `redirect:` after all state-changing POST/PUT operations (Post/Redirect/Get pattern).
- ✅ **DO** use `RedirectAttributes.addFlashAttribute()` to pass temporary messages (e.g., success alerts) across redirects stored in HTTP session and auto-cleaned after display.
- ❌ **DON'T** use `forward:` when redirecting across different domain origins or external URLs; `forward:` only works within the same ServletContext.

---

### Q20. How do you configure Spring MVC without XML (Java-based config)?

#### 1. Concept & Interview Answer
- In modern Spring MVC (since Spring 3.1+ and Servlet 3.0+), XML configuration (`web.xml` and `spring-servlet.xml`) is replaced with pure **Java Configuration**:
  1. **WebApplicationInitializer**: Replaces `web.xml` by programmatically registering `DispatcherServlet` and filters on server startup.
  2. **`@Configuration` + `WebMvcConfigurer`**: Replaces `spring-servlet.xml` to configure view resolvers, resource handlers, interceptors, formatters, and CORS.
  3. **`@EnableWebMvc`**: Imports core Spring MVC configuration beans (when running in standalone Spring without Spring Boot).

#### 2. Modern Java-Based Configuration Setup
```java
package com.enterprise.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.enterprise.web")
public class AppWebMvcConfig implements WebMvcConfigurer {

    // 1. Static Resource Mapping (CSS, JS, Images)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }

    // 2. Global CORS Configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://portal.enterprise.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 3. View Resolver Configuration (Thymeleaf)
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        return engine;
    }

    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
```

```java
// Modern web.xml replacement (Servlet 3.0+ Container Initializer)
package com.enterprise.web.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { AppRootDomainConfig.class }; // Services, Repositories, Security
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { AppWebMvcConfig.class }; // Controllers, ViewResolvers
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // DispatcherServlet handles all incoming requests
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During a migration from legacy Tomcat XML configs to cloud-native microservices, developer onboarding was hindered by 500-line XML configuration files. Converting to pure Java configuration with `WebMvcConfigurer` enabled compile-time type safety, eliminated runtime XML parsing errors, and allowed environment-specific `@Profile("dev")` / `@Profile("prod")` conditional configurations.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** implement `WebMvcConfigurer` to customize web configurations without breaking Spring Boot defaults.
- ❌ **DON'T** use `@EnableWebMvc` in Spring Boot applications unless you intentionally want to turn off Spring Boot's opinionated web auto-configurations.

---

### Q21. How to integrate Spring MVC with Hibernate/JPA?

#### 1. Concept & Interview Answer
Integrating Spring MVC with Hibernate/JPA involves configuring:
1. **`DataSource`**: Connection pool manager (e.g., HikariCP).
2. **`LocalContainerEntityManagerFactoryBean`**: Sets up JPA `EntityManagerFactory` and integrates Hibernate as the JPA provider.
3. **`PlatformTransactionManager`** (`JpaTransactionManager`): Enables declarative transaction management via `@Transactional`.
4. **Spring Data JPA Repositories**: Interfaces extending `JpaRepository<Entity, ID>` providing automated CRUD, pagination, and query generation.
5. In Spring Boot, all of this is auto-configured simply by including `spring-boot-starter-data-jpa` and database driver dependencies.

#### 2. Architecture Layer Integration
```
[ Browser / API Client ]
           │
           ▼
   [ @RestController ]  (Web Layer: DTO validation, HTTP status codes)
           │
           ▼
     [ @Service ]       (Business Layer: @Transactional boundaries)
           │
           ▼
   [ JpaRepository ]    (Persistence Layer: Spring Data JPA + Hibernate)
           │
           ▼
     [ HikariCP ]       (Connection Pool) ──► [ Relational Database (PostgreSQL/MySQL) ]
```

#### 3. Production Code Example
```java
// 1. JPA Entity
package com.enterprise.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    // Constructors, Getters, Setters...
    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
```

```java
// 2. Spring Data JPA Repository
package com.enterprise.domain.repository;

import com.enterprise.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
```

```java
// 3. Service Layer with @Transactional Boundary
package com.enterprise.domain.service;

import com.enterprise.domain.entity.Account;
import com.enterprise.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferMoney(String fromAccNum, String toAccNum, BigDecimal amount) {
        Account from = accountRepository.findByAccountNumber(fromAccNum)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account to = accountRepository.findByAccountNumber(toAccNum)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        // Hibernate dirty checking auto-persists updates on transaction commit
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A fund transfer endpoint crashed halfway through due to a network glitch. Because the developer forgot `@Transactional` on the service method, money was deducted from the sender account, but the recipient account never received it (inconsistent database state). Adding `@Transactional(rollbackFor = Exception.class)` ensured that any mid-flight exception automatically rolled back all database operations within the unit of work.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** place `@Transactional` strictly at the **Service layer**, never in the Controller or Repository layer.
- ✅ **DO** use `readOnly = true` on read operations (`@Transactional(readOnly = true)`) to enable Hibernate dirty-checking optimizations and route queries to read-replicas.
- ❌ **DON'T** perform long-running blocking external HTTP/REST calls inside `@Transactional` methods; doing so holds database connections open from the pool unnecessarily, causing connection pool exhaustion.

---

### Q22. How do you manage sessions in Spring MVC?

#### 1. Concept & Interview Answer
Session management in Spring MVC can be handled across different scopes:
1. **Direct `HttpSession` Injection:** Accessing the standard `jakarta.servlet.http.HttpSession` parameter in a controller method.
2. **`@SessionAttribute`:** Binds a specific pre-existing session attribute to a controller method parameter.
3. **`@SessionAttributes`:** Stores model attributes into the HTTP session between multiple requests (commonly used in multi-step wizard flows).
4. **Spring Session (Distributed):** Replaces container-managed in-memory sessions (Tomcat) with external distributed storage (Redis, Hazelcast, JDBC), critical for scalable microservices behind load balancers.

#### 2. Architecture Comparison: In-Memory vs Distributed Session
```
Traditional Tomcat In-Memory Session (Fails on horizontal scaling):
[ User ] ──► [ Load Balancer ] ──► [ Instance A (Session Data Here) ]
                                   [ Instance B (Session Missing! User Logged Out!) ]

Spring Session + Redis (Production-Grade Scalable):
[ User ] ──► [ Load Balancer ] ──► [ Instance A ] ──┐
                                                    ├──► [ Central Redis Session Store ]
                                  [ Instance B ] ──┘
```

#### 3. Production Code Example (Spring Session with Redis)
```java
// Dependencies: spring-session-data-redis + redis
package com.enterprise.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class ShoppingCartSessionController {

    public record CartItem(String productId, int quantity) {}

    @PostMapping("/add")
    public String addItemToCart(@RequestBody CartItem item, HttpSession session) {
        // Retrieve or initialize shopping cart in distributed session
        CartItemsList cart = (CartItemsList) session.getAttribute("USER_CART");
        if (cart == null) {
            cart = new CartItemsList();
            session.setAttribute("USER_CART", cart);
        }
        cart.add(item);
        return "Item added to session cart. Session ID: " + session.getId();
    }

    @GetMapping
    public CartItemsList getCart(HttpSession session) {
        CartItemsList cart = (CartItemsList) session.getAttribute("USER_CART");
        return cart != null ? cart : new CartItemsList();
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During a Black Friday flash sale, an e-commerce platform autoscaled its Spring Boot instances from 4 to 20 nodes behind an AWS ALB. Users reported being repeatedly logged out and losing their shopping carts mid-checkout because the load balancer routed subsequent requests to different container instances lacking sticky sessions. Implementing **Spring Session with Redis** unified session state across all nodes instantly.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use **Spring Session + Redis** for any stateful web application running in clustered or containerized (Kubernetes) environments.
- ✅ **DO** prefer **stateless JWT tokens** for pure REST APIs instead of server-side HTTP sessions whenever possible.
- ❌ **DON'T** store large objects or entity graphs inside `HttpSession`; store only minimal lightweight identifiers (like `userId` or `cartId`) to avoid excessive memory and serialization overhead.

---

### Q23. How do you use @SessionAttributes and @ModelAttribute?

#### 1. Concept & Interview Answer
- **`@ModelAttribute`** has two primary uses:
  1. **On a method argument:** Binds incoming request parameters or form fields to a model object and exposes it to the web view.
  2. **On a method:** Executes **before** any `@RequestMapping` method in the controller to populate shared attributes into the `Model` (e.g., dropdown list data).
- **`@SessionAttributes`**:
  - Declared at the **class level** (e.g., `@SessionAttributes("orderWizard")`).
  - Instructs Spring to store specified model attributes in the `HttpSession` across multiple requests until explicitly cleared via `SessionStatus.setComplete()`.

#### 2. Multi-Step Wizard Flow with @SessionAttributes
```
Step 1: GET /wizard/step1 ──► Controller creates OrderForm ──► Saved in @SessionAttributes
                                                                          │
Step 2: POST /wizard/step2 ──► Updates same OrderForm in Session ◄────────┤
                                                                          │
Step 3: POST /wizard/finish ──► Submits Order ──► SessionStatus.setComplete() (Removed from Session)
```

#### 3. Production Code Example
```java
package com.enterprise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.Serializable;
import java.util.List;

@Controller
@RequestMapping("/checkout/wizard")
@SessionAttributes("checkoutOrder") // Persist this attribute in session across steps
public class CheckoutWizardController {

    public static class CheckoutOrder implements Serializable {
        private String shippingAddress;
        private String paymentMethod;
        // Getters and Setters...
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    // Automatically executed before any handler to initialize model if absent
    @ModelAttribute("checkoutOrder")
    public CheckoutOrder initializeOrder() {
        return new CheckoutOrder();
    }

    // Populate common reference data for views
    @ModelAttribute("paymentOptions")
    public List<String> populatePaymentOptions() {
        return List.of("CREDIT_CARD", "PAYPAL", "APPLE_PAY");
    }

    @GetMapping("/step1")
    public String showStep1(@ModelAttribute("checkoutOrder") CheckoutOrder order) {
        return "checkout/step1";
    }

    @PostMapping("/step2")
    public String processStep1(@ModelAttribute("checkoutOrder") CheckoutOrder order) {
        return "checkout/step2";
    }

    @PostMapping("/complete")
    public String completeOrder(@ModelAttribute("checkoutOrder") CheckoutOrder order, SessionStatus status) {
        // Save order to database...
        status.setComplete(); // Cleans up the "checkoutOrder" attribute from the session
        return "redirect:/checkout/confirmation";
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an insurance policy quotation wizard, users who completed a quote and immediately started a second quote saw old data pre-filled because the previous session attribute was never purged. Adding `SessionStatus.setComplete()` on the final submission step cleared the session model and resolved the cross-quote data contamination bug.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always call `status.setComplete()` when the multi-step workflow finishes to prevent session memory bloat.
- ✅ **DO** ensure any class stored in `@SessionAttributes` implements `java.io.Serializable` so distributed session stores (like Redis) can serialize it without throwing `NotSerializableException`.

---

### Q24. How do you internationalize (i18n) a Spring MVC application?

#### 1. Concept & Interview Answer
Internationalization (i18n) in Spring MVC enables an application to serve content in different languages based on user locale.
It requires three core components:
1. **`MessageSource` (`ResourceBundleMessageSource`)**: Loads localized key-value message properties files (e.g., `messages.properties`, `messages_fr.properties`, `messages_es.properties`).
2. **`LocaleResolver`**: Determines the user's current locale. Implementations include:
   - `AcceptHeaderLocaleResolver` (default, reads HTTP `Accept-Language` header).
   - `CookieLocaleResolver` (stores locale in a browser cookie).
   - `SessionLocaleResolver` (stores locale in the user's `HttpSession`).
3. **`LocaleChangeInterceptor`**: Intercepts requests and switches the locale when a request parameter is present (e.g., `?lang=es`).

#### 2. i18n Resolution Architecture
```
[ HTTP Request (?lang=de) ]
            │
            ▼
[ LocaleChangeInterceptor ] ──► Detects lang=de param
            │
            ▼
[ LocaleResolver (SessionLocaleResolver) ] ──► Updates user session locale to Locale.GERMAN
            │
            ▼
[ View / Controller (MessageSource) ]
            │
            ▼
[ Reads: messages_de.properties ] ──► "welcome.message=Willkommen!"
```

#### 3. Production Code Example
```java
package com.enterprise.web.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

    // 1. Define MessageSource
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    // 2. Define LocaleResolver (Session-based)
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    // 3. Define LocaleChangeInterceptor
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // e.g. /home?lang=fr
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
```

```properties
# resources/i18n/messages.properties (Default English)
welcome.greeting=Welcome to Enterprise Portal!

# resources/i18n/messages_es.properties (Spanish)
welcome.greeting=¡Bienvenido al portal empresarial!

# resources/i18n/messages_fr.properties (French)
welcome.greeting=Bienvenue sur le portail d'entreprise!
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a global deployment, French accented characters (e.g., `é`, `à`, `ç`) were corrupted into question marks (`?`) on rendered web pages. The root cause was Java's legacy ISO-8859-1 bundle encoding default. Switching to `ReloadableResourceBundleMessageSource` and explicitly invoking `.setDefaultEncoding("UTF-8")` resolved character corruption across all European languages.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `ReloadableResourceBundleMessageSource` instead of `ResourceBundleMessageSource` in production because it supports UTF-8 natively and allows reloading messages without restarting the server.
- ✅ **DO** use `MessageSource` in global exception handlers (`@RestControllerAdvice`) to return localized error messages based on the client's `LocaleContextHolder.getLocale()`.

---

### Q25. How can you secure a Spring MVC application?

#### 1. Concept & Interview Answer
Securing a Spring MVC application is achieved by integrating **Spring Security**, which operates via a chain of servlet filters (`SecurityFilterChain`) executing before `DispatcherServlet`.
Key layers of defense:
1. **Authentication:** Verifying user identity via Form Login, HTTP Basic, OAuth2/OIDC, or Stateless JWT.
2. **Authorization (RBAC):** Restricting endpoint access based on roles/authorities (`hasRole('ADMIN')`, `hasAuthority('SCOPE_read')`).
3. **Transport Security & CSRF:** Enforcing HTTPS/TLS, activating CSRF protection for stateful forms, and enabling standard security headers (HSTS, CSP, X-Frame-Options).
4. **Method-Level Security:** Using `@PreAuthorize("hasRole('ADMIN')")` or `@Secured` for defense-in-depth on service methods.
5. **Input Sanitization:** Preventing SQL Injection (via JPA/PreparedStatements) and XSS (via HTML escaping and Content Security Policies).

#### 2. Spring Security Architecture Pipeline in Spring MVC
```
[ Incoming HTTP Request ]
           │
           ▼
[ DelegatingFilterProxy ]
           │
           ▼
[ FilterChainProxy (SecurityFilterChain) ]
   ├── 1. CorsFilter
   ├── 2. CsrfFilter
   ├── 3. JwtAuthenticationFilter / UsernamePasswordAuthenticationFilter
   ├── 4. ExceptionTranslationFilter
   └── 5. AuthorizationFilter (Enforces URL path permissions)
           │
           ▼ (If Authorized)
[ DispatcherServlet (Spring MVC Front Controller) ]
           │
           ▼
[ @RestController / @Controller ]
```

#### 3. Production Code Example (Spring Security 6.x / Spring Boot 3.x)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize
public class EnterpriseSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength 12 work factor
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF configuration (Disable for stateless REST APIs using tokens, keep for Web Forms)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            // 2. Session Management (Stateless for REST API)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. URL Authorization Rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/public/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/orders/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )

            // 4. Standard Enterprise Security Headers
            .headers(headers -> headers
                .contentTypeOptions(Customizer.withDefaults()) // X-Content-Type-Options: nosniff
                .xssProtection(Customizer.withDefaults())
                .frameOptions(frame -> frame.deny())           // X-Frame-Options: DENY (Clickjacking protection)
                .referrerPolicy(ref -> ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            )

            // 5. HTTP Basic / OAuth2 / JWT Filter integration
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
```

```java
// Method-Level Security Example on Service
@Service
public class PayrollService {

    @PreAuthorize("hasRole('HR_MANAGER') or #employeeId == authentication.principal.id")
    public PayrollDto getSalaryDetails(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A multi-tenant SaaS application experienced an authorization bypass vulnerability (IDOR - Insecure Direct Object Reference) where regular users altered URL path variables (`GET /api/v1/tenants/999/invoices`) to view competitor financial records. Implementing `@PreAuthorize("@tenantSecurityService.isOwner(authentication, #tenantId)")` combined with Spring Security's `SecurityFilterChain` stopped the unauthorized cross-tenant data access.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `BCryptPasswordEncoder` with an appropriate work factor (e.g., 12) or Argon2 for hashing passwords. Never store plain text or MD5/SHA-1 hashes.
- ✅ **DO** enable method-level security with `@EnableMethodSecurity` to ensure authorization is verified even if an internal bean method is accidentally invoked.
- ❌ **DON'T** disable CSRF protection on standard browser-based cookie-authenticated web applications. Only disable CSRF on stateless REST APIs authenticated purely via non-cookie Bearer tokens.

---

# Module 2: Spring Boot Fundamentals & Core Mechanics

---

### Q26. What is Spring Boot?

#### 1. Concept & Interview Answer
- **Spring Boot** is an opinionated, production-ready extension of the Spring Framework designed to radically simplify the bootstrapping, configuration, and deployment of Spring applications.
- It eliminates the notorious "XML configuration hell" and complex boilerplate setup through four core pillars:
  1. **Starters (`spring-boot-starter-*`):** Curated dependency descriptor bundles.
  2. **Auto-Configuration (`@EnableAutoConfiguration`):** Automatically configures Spring beans based on libraries present on the classpath.
  3. **Embedded Web Servers:** Bundles Tomcat, Jetty, or Undertow directly into executable fat JARs (no external WAR deployment required).
  4. **Production-Ready Features (Spring Boot Actuator):** Built-in health checks, JVM metrics, thread dumps, and audit tracing.

#### 2. Architecture & Bootstrapping Pipeline
```
[ SpringApplication.run(Application.class, args) ]
                        │
                        ▼
1. Initialize SpringApplicationRunListeners
2. Prepare Environment (application.yml, System Properties, CLI Args)
3. Print Banner (ASCII / Custom)
4. Create ApplicationContext (AnnotationConfigServletWebServerApplicationContext)
5. Execute AutoConfigurationImportSelector (Reads META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports)
6. Start Embedded Web Server (Tomcat listening on port 8080)
7. Execute CommandLineRunner & ApplicationRunner beans
                        │
                        ▼
            [ Application Ready & Healthy ]
```

#### 3. Production Code Example
```java
package com.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class EnterpriseApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EnterpriseApplication.class);
        // Track startup performance & bean initialization steps
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A banking engineering team spent 3–4 days configuring dependencies, servlet mappings, and transaction managers in XML files every time a new microservice was spun up. Migrating to Spring Boot reduced new microservice provisioning time from days to under 10 minutes, standardizing the runtime environment across 60+ distributed services.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** package Spring Boot applications as executable "Fat JARs" with embedded web servers for containerized Kubernetes deployments.
- ✅ **DO** use `SpringApplicationBuilder` if you need programmatic control over parent-child contexts or startup flags.
- ❌ **DON'T** package microservices as `.war` files to deploy onto shared external application servers unless forced by legacy corporate infrastructure constraints.

---

### Q27. What are the advantages of using Spring Boot?

#### 1. Concept & Interview Answer
Spring Boot provides significant enterprise advantages:
1. **Zero XML & Opinionated Defaults:** Eliminates thousands of lines of boilerplate XML and configuration classes.
2. **Starter POMs:** Pre-tested, version-aligned dependency bundles preventing dependency version conflicts (JAR Hell).
3. **Embedded Web Containers:** Runs standalone via `java -jar app.jar` without requiring separate web server installation.
4. **Actuator for Observability:** Instant out-of-the-box Prometheus/Grafana metrics, `/health`, `/info`, and `/env` endpoints.
5. **Externalized Configuration:** Seamless multi-environment switching (`dev`, `stage`, `prod`) using `application-{profile}.yml` and environment variables.
6. **Cloud-Native & Container Friendly:** Seamless integration with Docker, Kubernetes, Spring Cloud, and GraalVM Native Images.

#### 2. Feature Comparison Matrix
| Capability | Traditional Spring Framework | Spring Boot |
| :--- | :--- | :--- |
| **Configuration** | Heavy XML (`web.xml`, `context.xml`) or verbose `@Configuration` | Automatic based on classpath (`@EnableAutoConfiguration`) |
| **Dependency Management** | Manual version synchronization for each library | Managed parent BOM (`spring-boot-starter-parent`) |
| **Server Deployment** | Requires external Tomcat/WebLogic server | Embedded Tomcat/Jetty/Undertow within Fat JAR |
| **Monitoring** | Requires custom monitoring filters & MBeans | Spring Boot Actuator with Micrometer & Prometheus |
| **Bootstrap Speed** | Slow setup (hours/days) | Instant setup (minutes via Spring Initializr) |

#### 3. Production Code Example (Customizing Embedded Tomcat)
```java
package com.enterprise.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setPort(8080);
        factory.getSession().setTimeout(Duration.ofMinutes(30));
        factory.addConnectorCustomizers(connector -> {
            AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
            protocol.setMaxThreads(200);
            protocol.setMinSpareThreads(20);
            protocol.setConnectionTimeout(20000);
            protocol.setMaxKeepAliveRequests(100);
        });
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a legacy enterprise application, upgrading the Jackson JSON library required manually bumping 12 related transitive artifacts, resulting in a runtime `NoSuchMethodError` in production. Migrating to `spring-boot-starter-parent` delegated all transitive library version alignments to Spring Boot's thoroughly tested Dependency Management BOM, preventing classpath collisions.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** inherit from `spring-boot-starter-parent` or import `spring-boot-dependencies` in your Maven/Gradle build file to guarantee library compatibility.
- ✅ **DO** customize embedded server properties via `server.tomcat.*` in `application.yml` instead of writing custom Java code unless advanced low-level socket customization is required.

---

### Q28. What is the difference between Spring and Spring Boot?

#### 1. Concept & Interview Answer
- **Spring Framework** is the foundational **Inversion of Control (IoC) and Dependency Injection (DI)** ecosystem providing core abstractions (Spring Core, Spring Context, Spring AOP, Spring JDBC, Spring MVC). It is un-opinionated and requires developers to explicitly configure all infrastructure beans.
- **Spring Boot** is an **opinionated framework built on top of Spring**. It does not replace Spring; rather, it packages the Spring ecosystem with smart defaults, starters, auto-configuration, and an embedded server to enable rapid application development.

#### 2. Architecture Layer Relationship
```
┌─────────────────────────────────────────────────────────────┐
│                 Spring Boot Application                     │
│  (Starters + AutoConfiguration + Actuator + Embedded Server)│
├─────────────────────────────────────────────────────────────┤
│                    Spring Framework                         │
│  (Spring MVC, Spring Data, Spring Security, Spring Batch)   │
├─────────────────────────────────────────────────────────────┤
│                    Spring Core Engine                       │
│             (BeanFactory, IoC Container, DI, AOP)           │
└─────────────────────────────────────────────────────────────┘
```

#### 3. Comparison Breakdown
| Dimension | Spring Framework | Spring Boot |
| :--- | :--- | :--- |
| **Core Purpose** | Provides foundational IoC, DI, and enterprise modules | Rapid development framework to run Spring apps with zero hassle |
| **Configuration** | Explicit manual Java `@Bean` or XML config required | Auto-configuration configures 90% of beans automatically |
| **Execution** | Packaged as WAR and deployed to external servlet container | Packaged as standalone executable JAR with embedded Tomcat |
| **Testing Support** | `spring-test` with manual context setup | `@SpringBootTest` with full context slicing (`@WebMvcTest`, `@DataJpaTest`) |

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A legacy developer wrote 250 lines of Java configuration to configure an `EntityManagerFactory`, `HikariDataSource`, `JpaTransactionManager`, and Hibernate dialect for a standard PostgreSQL connection in Spring. In Spring Boot, the exact same capability was achieved with **0 lines of Java code** simply by adding `spring-boot-starter-data-jpa` and specifying 3 connection lines in `application.yml`.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** choose Spring Boot for all modern greenfield Java enterprise projects, microservices, and event-driven architectures.
- ❌ **DON'T** think of Spring and Spring Boot as mutually exclusive; Spring Boot is the modern way to build Spring applications.

---

### Q29. What is the use of application.properties or application.yml?

#### 1. Concept & Interview Answer
- `application.properties` (or `application.yml`) is the primary configuration file used to externalize application configuration and adjust Spring Boot auto-configuration behaviors without modifying compiled source code.
- It configures:
  - **Server settings:** Port, context path, SSL, compression (`server.port=8080`).
  - **Database & JPA:** DataSource URL, pool sizes, DDL auto mode (`spring.datasource.url=...`).
  - **Logging:** Log levels and file paths (`logging.level.root=INFO`).
  - **Actuator Endpoints:** Exposure and security (`management.endpoints.web.exposure.include=health,metrics`).
  - **Custom Business Properties:** Injected via `@Value` or `@ConfigurationProperties`.

#### 2. YAML vs Properties Format Comparison
```yaml
# application.yml (Hierarchical, readable, avoids repeated prefixes)
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/orders_db
    hikari:
      maximum-pool-size: 20
```

```properties
# application.properties (Flat key-value format)
server.port=8080
server.servlet.context-path=/api
spring.datasource.url=jdbc:postgresql://localhost:5432/orders_db
spring.datasource.hikari.maximum-pool-size=20
```

#### 3. Production Code Example (Strongly Typed Config Binding)
```java
package com.enterprise.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.payment.gateway")
public record PaymentGatewayProperties(
    @NotBlank String apiUrl,
    @NotBlank String apiKey,
    @Min(1000) @Max(30000) int timeoutMillis,
    boolean mockEnabled
) {}
```

```yaml
# application.yml
app:
  payment:
    gateway:
      api-url: "https://api.stripe.com/v1"
      api-key: "${STRIPE_SECRET_KEY:default_mock_key}"
      timeout-millis: 5000
      mock-enabled: false
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer hardcoded a production payment webhook URL inside a Java service. During staging testing, live customer orders were sent to the production webhook. Migrating the URL to `application.yml` with environment-specific overrides (`application-staging.yml` vs `application-prod.yml`) completely isolated environments and prevented cross-environment data contamination.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer `application.yml` for new enterprise projects due to its clean hierarchical nesting and readability.
- ✅ **DO** validate configuration properties at startup using `@Validated` with Jakarta Bean Validation on `@ConfigurationProperties` classes.
- ❌ **DON'T** commit sensitive secrets (database passwords, API private keys) in plain text in `application.yml`; inject them via environment variables or secret managers (AWS Secrets Manager, HashiCorp Vault).

---

### Q30. What are Starters in Spring Boot?

#### 1. Concept & Interview Answer
- **Spring Boot Starters** (`spring-boot-starter-*`) are a set of convenient dependency descriptors that aggregate all necessary libraries and transitive dependencies required for a specific technology stack into a single dependency import.
- **Why are Starters valuable?**
  1. **Eliminate Dependency Hell:** Starters ensure all included libraries (and their transitive dependencies) are 100% compatible and version-aligned.
  2. **Automated Setup:** Including a starter triggers the corresponding Auto-Configuration classes automatically.
- **Common Official Starters:**
  - `spring-boot-starter-web`: Spring MVC, REST, Jackson, and embedded Tomcat.
  - `spring-boot-starter-data-jpa`: Spring Data JPA, Hibernate, and HikariCP.
  - `spring-boot-starter-security`: Spring Security and crypto libraries.
  - `spring-boot-starter-actuator`: Health, metrics, and monitoring endpoints.
  - `spring-boot-starter-test`: JUnit 5, Mockito, AssertJ, and Spring Test.

#### 2. Starter Dependency Decomposition
```
spring-boot-starter-web
  ├── spring-boot-starter (Core auto-configuration & logging)
  │     ├── spring-boot
  │     ├── spring-boot-autoconfigure
  │     └── spring-boot-starter-logging (Logback + SLF4J)
  ├── spring-boot-starter-json (Jackson Databind + JavaTimeModule)
  ├── spring-boot-starter-tomcat (Embedded Tomcat Web Server)
  ├── spring-web (Core HTTP abstractions)
  └── spring-webmvc (DispatcherServlet & Controller framework)
```

#### 3. Production Code Example (Custom Enterprise Starter Structure)
```
my-enterprise-audit-starter/
  ├── my-enterprise-audit-autoconfigure/ (Contains @AutoConfiguration & Beans)
  │     ├── src/main/java/com/enterprise/audit/AuditAutoConfiguration.java
  │     └── src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
  └── my-enterprise-audit-starter/       (Empty POM aggregating dependency & autoconfigure)
        └── pom.xml
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an enterprise with 40 microservices, developers were manually configuring different versions of Jackson and Logback, leading to serialization inconsistencies and missing JSON logs in Datadog. Creating a shared internal starter (`company-starter-observability`) standardized logging formats, trace propagation, and metrics across all 40 services with a single line in `pom.xml`.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** follow naming conventions: Official starters are named `spring-boot-starter-*`, while custom third-party/company starters should be named `*-spring-boot-starter` (e.g., `audit-spring-boot-starter`).
- ❌ **DON'T** manually specify `<version>` tags for dependencies managed by `spring-boot-starter-parent`; let the parent POM control compatible versions.

---

### Q31. What is the purpose of @SpringBootApplication?

#### 1. Concept & Interview Answer
- **`@SpringBootApplication`** is the primary bootstrap annotation placed on the main class of a Spring Boot application.
- It is a convenience **composed (meta) annotation** that bundles three essential Spring annotations into one:
  1. **`@SpringBootConfiguration`**: Specialization of `@Configuration` denoting the class as a configuration source for Spring beans.
  2. **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration engine to register beans based on classpath libraries.
  3. **`@ComponentScan`**: Scans the current package and all sub-packages recursively for `@Component`, `@Service`, `@Repository`, and `@Controller` beans.

#### 2. Meta-Annotation Composition Diagram
```
                     @SpringBootApplication
                               │
       ┌───────────────────────┼───────────────────────┐
       ▼                       ▼                       ▼
@SpringBootConfiguration   @EnableAutoConfiguration   @ComponentScan
(Registers @Beans)         (Auto-configures beans     (Scans package &
                            based on classpath)        sub-packages)
```

#### 3. Production Code Example
```java
package com.enterprise.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
    scanBasePackages = "com.enterprise.order", // Explicit base package scanning
    proxyBeanMethods = false                  // Optimizes startup time if no inter-bean @Bean calls exist
)
@ConfigurationPropertiesScan("com.enterprise.order.config")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer created a new `@Service` class in package `com.enterprise.shared.service` while the `@SpringBootApplication` main class was in `com.enterprise.order`. Spring threw `NoSuchBeanDefinitionException` on startup because `@ComponentScan` only scans the package containing the main class and its child packages. Moving the main class to root `com.enterprise` (or adding `scanBasePackages = "com.enterprise"`) fixed the issue.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** place the `@SpringBootApplication` annotated class in the root base package (e.g., `com.company.project`) so that all sub-packages are automatically scanned without needing explicit `scanBasePackages`.
- ✅ **DO** set `proxyBeanMethods = false` on configuration classes when `@Bean` methods do not call each other directly to reduce CGLIB proxy generation overhead and boost startup speed.

---

### Q32. What are the main components included in @SpringBootApplication?

#### 1. Concept & Interview Answer
`@SpringBootApplication` is composed of three critical underlying components and their attributes:
1. **`@Configuration` (via `@SpringBootConfiguration`):** Marks the class as a source of bean definitions for the Spring IoC container.
2. **`@EnableAutoConfiguration`:** Triggers the auto-configuration mechanism (`AutoConfigurationImportSelector`), loading pre-packaged configurations from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
3. **`@ComponentScan`:** Configures component scanning directives with default filters to discover beans in the application package tree.
- Additionally, it inherits attributes like `exclude` and `excludeName` to disable unwanted auto-configurations.

#### 2. Component Role Breakdown Table
| Included Annotation | Key Responsibility | Customization Option |
| :--- | :--- | :--- |
| **`@SpringBootConfiguration`** | Declares standard Spring configuration and `@Bean` declarations | `proxyBeanMethods = true/false` |
| **`@EnableAutoConfiguration`** | Scans classpath for libraries and auto-registers corresponding beans | `exclude = { DataSourceAutoConfiguration.class }` |
| **`@ComponentScan`** | Scans class hierarchy for `@Component`, `@Service`, `@Repository` | `scanBasePackages = {"com.company.moduleA", "com.company.moduleB"}` |

#### 3. Production Code Example (Excluding Auto-Configurations)
```java
package com.enterprise.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// Exclude Database Auto-configurations when building a stateless messaging consumer
@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
public class InventoryConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryConsumerApplication.class, args);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A lightweight utility microservice included `spring-boot-starter-data-jpa` as part of a common shared dependency, but did not have a database configured. On startup, Spring Boot failed with `Failed to configure a DataSource: 'url' attribute is not specified`. Rather than removing the shared POM, the team applied `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})` to bypass database initialization cleanly.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** understand what each sub-annotation does so you can troubleshoot bean discovery and auto-configuration conflicts effectively.
- ❌ **DON'T** manually add `@ComponentScan` or `@Configuration` on the same class that already has `@SpringBootApplication`; it is redundant.

---

### Q33. What is auto-configuration in Spring Boot?

#### 1. Concept & Interview Answer
- **Auto-configuration** is Spring Boot's intelligent mechanism that automatically configures and registers Spring beans into the `ApplicationContext` based on:
  1. The **JAR dependencies** available on the classpath (e.g., if `h2.jar` or `postgresql.jar` is present, it auto-configures a `DataSource`).
  2. Existing **custom beans** defined by the developer (if developer defines a `DataSource`, Spring Boot backs off).
  3. Properties defined in **`application.yml`** (e.g., `spring.datasource.url`).
- It follows the **"Convention over Configuration"** philosophy: it provides sensible production defaults out of the box while allowing developers 100% override flexibility.

#### 2. How Auto-Configuration Evaluates Conditions
```
1. Spring Boot checks META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
2. Iterates over AutoConfiguration classes (e.g., JmxAutoConfiguration, DataSourceAutoConfiguration)
3. Evaluates @Conditional annotations:
   ├── @ConditionalOnClass(DataSource.class)       ──► Class present on classpath?
   ├── @ConditionalOnMissingBean(DataSource.class)  ──► Has user defined their own bean?
   └── @ConditionalOnProperty(name="spring.datasource.enabled", matchIfMissing=true)
4. If ALL conditions match:
   └── Creates and registers the DataSource bean in ApplicationContext!
```

#### 3. Production Code Example (Writing a Custom Auto-Configuration)
```java
package com.enterprise.autoconfigure.sms;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration // Spring Boot 3.x replacement for @Configuration in autoconfigures
@ConditionalOnClass(SmsClient.class) // Only active if SmsClient is on classpath
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(prefix = "enterprise.sms", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SmsService.class) // Only creates bean if user didn't provide custom SmsService
    public SmsService defaultSmsService(SmsProperties properties) {
        return new TwilioSmsService(properties.getApiKey(), properties.getSenderId());
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An engineering team needed a standardized internal rate-limiter for 50 microservices. By authoring a custom `RateLimiterAutoConfiguration` with `@ConditionalOnProperty(name="security.rate-limiter.enabled", havingValue="true")`, any microservice could enable the rate-limiter filter just by adding the JAR and setting the property in `application.yml`, eliminating copy-pasted boilerplate.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** run your application with `--debug` or `debug=true` in `application.yml` during development to view the **Condition Evaluation Report** (shows which auto-configurations matched and which were skipped).
- ❌ **DON'T** fight auto-configuration; if you need custom behavior, simply declare your own `@Bean`, and Spring Boot's `@ConditionalOnMissingBean` will gracefully back off.

---

### Q34. How does Spring Boot automatically configure beans?

#### 1. Concept & Interview Answer
Spring Boot configures beans automatically through the following internal sequence:
1. **Importing Auto-Configurations:** At startup, `AutoConfigurationImportSelector` reads `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (in Spring Boot 3.x) or `spring.factories` (in Spring Boot 2.x).
2. **Conditional Evaluation (`@Conditional`):** Each auto-configuration class uses condition annotations:
   - `@ConditionalOnClass`: Checks if a specific `.class` file exists on the classpath.
   - `@ConditionalOnMissingBean`: Checks if the user has NOT already declared a bean of that type.
   - `@ConditionalOnProperty`: Checks if a property in `application.yml` has a specific value.
   - `@ConditionalOnWebApplication`: Checks if the app is a servlet/reactive web application.
3. **Bean Creation:** If and only if all conditional checks evaluate to `true`, the `@Bean` factory methods execute and register instances into the Spring `ApplicationContext`.

#### 2. Key Conditional Annotations Reference
| Annotation | Trigger Condition |
| :--- | :--- |
| **`@ConditionalOnClass(X.class)`** | Matches if class `X` is present on the runtime classpath |
| **`@ConditionalOnMissingClass("X")`** | Matches if class `X` is absent from the classpath |
| **`@ConditionalOnBean(Y.class)`** | Matches if a bean of type `Y` already exists in context |
| **`@ConditionalOnMissingBean(Y.class)`** | Matches if NO bean of type `Y` has been registered by the user |
| **`@ConditionalOnProperty`** | Matches based on `application.yml` key and value |
| **`@ConditionalOnResource`** | Matches if a specific resource file exists on the classpath |

#### 3. Production Code Example (Inspect Condition Evaluation via Actuator)
```properties
# Enable debug mode to print Auto-Configuration Report on startup
debug=true

# Expose Actuator conditions endpoint
management.endpoints.web.exposure.include=conditions,beans
```

```json
// Sample response from GET /actuator/conditions
{
  "contexts": {
    "application": {
      "positiveMatches": {
        "DataSourceAutoConfiguration": [
          {
            "condition": "OnClassCondition",
            "message": "@ConditionalOnClass found required classes 'javax.sql.DataSource', 'org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType'"
          }
        ]
      },
      "negativeMatches": {
        "ActiveMQAutoConfiguration": [
          {
            "condition": "OnClassCondition",
            "message": "@ConditionalOnClass did not find required class 'jakarta.jms.ConnectionFactory'"
          }
        ]
      }
    }
  }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer defined a custom `PasswordEncoder` `@Bean`, but was worried it would conflict with Spring Security's default auto-configured encoder. Because Spring Security uses `@ConditionalOnMissingBean(PasswordEncoder.class)`, Spring Boot detected the user-defined bean and safely skipped its default encoder without throwing duplicate bean definition errors.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always use `@ConditionalOnMissingBean` on library `@Bean` declarations so application developers can override your default beans seamlessly.
- ✅ **DO** leverage `AutoConfigureAfter` or `AutoConfigureBefore` annotations when developing custom auto-configurations that depend on other auto-configurations running first.

---

### Q35. How do you create a Spring Boot REST API?

#### 1. Concept & Interview Answer
To create a modern, enterprise-grade REST API in Spring Boot:
1. **Dependencies:** Add `spring-boot-starter-web` and `spring-boot-starter-validation`.
2. **DTO / Domain Record:** Define strongly-typed request/response records with Jakarta validation annotations.
3. **Repository & Service:** Encapsulate database interactions and business logic.
4. **`@RestController`:** Expose endpoints mapped with `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`.
5. **Exception Handling:** Use `@RestControllerAdvice` to map errors to RFC 7807 `ProblemDetail`.

#### 2. REST API Request-Response Architecture
```
[ HTTP Client (Postman/Web) ]
           │
           ▼
[ @RestController ] ──► Validates DTO (@Valid)
           │
           ▼
     [ @Service ]   ──► Executes Business Rules (@Transactional)
           │
           ▼
   [ JpaRepository ]──► Executes SQL via Hibernate
           │
           ▼
 [ Relational Database ]
```

#### 3. Complete Production Code Example
```java
package com.enterprise.product.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    // Request & Response Records
    public record CreateProductRequest(
        @NotBlank(message = "Product title cannot be blank") String title,
        @NotNull(message = "Price is required") @Positive(message = "Price must be greater than 0") BigDecimal price,
        String category
    ) {}

    public record ProductResponse(Long id, String title, BigDecimal price, String category) {}

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse created = productService.create(request);
        URI location = URI.create("/api/v1/products/" + created.id());
        return ResponseEntity.created(location).body(created); // 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id)); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll()); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An API team was returning HTTP 200 OK for all requests—even when an item was created (should be 201) or when an item was not found (should be 404). Frontend clients had to parse `response.body.errorMessage` to detect failures. Refactoring to standard REST status codes (`201 Created`, `204 No Content`, `404 Not Found`) enabled standard API Gateway caching, retry policies, and error handling.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use proper HTTP verbs: `POST` (create), `GET` (read), `PUT` (replace), `PATCH` (partial update), `DELETE` (remove).
- ✅ **DO** return `201 Created` with a `Location` header on resource creation, and `204 No Content` on successful deletion.
- ❌ **DON'T** expose internal database IDs directly if they are sequential auto-incrementing integers that can be scraped; consider using UUIDs or Hashids for public endpoints.

---

### Q36. What is @RestController?

#### 1. Concept & Interview Answer
- **`@RestController`** is a specialized convenience archetype annotation introduced in Spring 4.0 that designates a class as a RESTful web controller.
- It is a **meta-annotation** composed of **`@Controller`** and **`@ResponseBody`**.
- **Key Characteristics:**
  1. Every handler method inside the class implicitly inherits `@ResponseBody`.
  2. The return value of every method is automatically serialized directly into the HTTP response body stream (as JSON, XML, or binary) using `HttpMessageConverter`.
  3. Completely eliminates the need to return view templates or use a `ViewResolver`.

#### 2. Source Code Composition
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller    // Spring stereotype bean for web controllers
@ResponseBody  // Direct serialization of return values to HTTP response body
public @interface RestController {
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
```

#### 3. Production Code Example
```java
package com.enterprise.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
public class SystemHealthRestController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", Instant.now(),
            "region", "us-east-1"
        ));
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an enterprise codebase, developers were annotating classes with `@Controller` and then redundantly adding `@ResponseBody` to every single method (30+ methods per controller). Replacing `@Controller` with `@RestController` cleaned up the codebase and removed hundreds of duplicate annotations.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always use `@RestController` for API-driven architectures (Microservices, SPAs, Mobile backends).
- ❌ **DON'T** put `@ResponseBody` on individual methods inside a `@RestController` class; it is redundant.

---

### Q37. What is the difference between @Component, @Service, @Repository, and @Controller?

#### 1. Concept & Interview Answer
All four annotations are **Spring Stereotype Annotations** registered in the IoC container, but they serve distinct architectural layers and provide specialized behaviors:

| Annotation | Architectural Layer | Specialized Behavior / Exception Translation |
| :--- | :--- | :--- |
| **`@Component`** | General Purpose / Utility | Generic Spring-managed bean. Parent annotation of all stereotypes. |
| **`@Service`** | Business / Domain Layer | Semantic marker for business logic; ideal target for `@Transactional` boundaries and AOP pointcuts. |
| **`@Repository`** | Persistence / DAO Layer | Enables **Automatic Exception Translation** (converts low-level JDBC/Hibernate SQLExceptions into Spring's unified `DataAccessException` hierarchy). |
| **`@Controller`** | Presentation / Web Layer | Handles incoming HTTP requests, model binding, and view resolution (or REST API responses). |

#### 2. Stereotype Hierarchy & Exception Translation
```
                         @Component (Generic Spring Bean)
                               │
            ┌──────────────────┼──────────────────┐
            ▼                  ▼                  ▼
       @Controller          @Service         @Repository
    (Web / REST Layer)  (Business Logic)   (Persistence / DAO)
                                                  │
                                                  ▼
                                    PersistenceExceptionTranslationPostProcessor
                                    (Translates DB exceptions to DataAccessException)
```

#### 3. Production Code Example
```java
// 1. Data Access Layer
@Repository
public class CustomAccountRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateAccountBalance(Long id, BigDecimal newBalance) {
        // Any raw PersistenceException or SQLException thrown here is automatically
        // translated into Spring's unchecked DataAccessException (e.g. CannotAcquireLockException)
        entityManager.createQuery("UPDATE Account a SET a.balance = :bal WHERE a.id = :id")
                     .setParameter("bal", newBalance)
                     .setParameter("id", id)
                     .executeUpdate();
    }
}

// 2. Service Layer
@Service
public class AccountServiceImpl {
    private final CustomAccountRepositoryImpl repo;

    public AccountServiceImpl(CustomAccountRepositoryImpl repo) {
        this.repo = repo;
    }

    @Transactional
    public void processSalaryCredit(Long id, BigDecimal amount) {
        // Business rules and transaction management
        repo.updateAccountBalance(id, amount);
    }
}

// 3. Controller Layer
@RestController
@RequestMapping("/api/accounts")
public class AccountApiController {
    private final AccountServiceImpl service;

    public AccountApiController(AccountServiceImpl service) {
        this.service = service;
    }
    // Web endpoints...
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A DAO class was annotated with generic `@Component` instead of `@Repository`. When database deadlocks occurred, Hibernate threw vendor-specific `org.hibernate.exception.LockAcquisitionException`. Because the Spring exception translator was not active, retry aspects configured to catch Spring's `CannotAcquireLockException` failed to trigger. Switching to `@Repository` activated the `PersistenceExceptionTranslationPostProcessor`, allowing global deadlock retry mechanisms to function correctly.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use the specific stereotype (`@Service`, `@Repository`, `@Controller`) matching the architectural role of your class rather than generic `@Component`.
- ✅ **DO** reserve `@Component` for cross-cutting utility classes, filters, interceptors, and background workers.

---

### Q38. How do you inject dependencies in Spring Boot?

#### 1. Concept & Interview Answer
Spring supports three primary types of Dependency Injection (DI):
1. **Constructor Injection (Industry Standard & Recommended):** Dependencies are provided via the class constructor.
2. **Setter Injection:** Dependencies are injected through public setter methods using `@Autowired`.
3. **Field Injection (Anti-pattern):** Dependencies are injected directly into private fields using `@Autowired` via reflection.

#### 2. Comparison Matrix: Constructor vs Setter vs Field Injection
| Criteria | Constructor Injection | Setter Injection | Field Injection |
| :--- | :--- | :--- | :--- |
| **Immutability (`final` fields)** | **Yes** (Fields can be `final`) | No | No |
| **Unit Testability without Spring** | **Trivial** (Pass mocks directly into constructor) | Possible (Call setters) | **Difficult** (Requires reflection or Mockito `ReflectionTestUtils`) |
| **Circular Dependency Detection** | Detected immediately at application startup (`BeanCurrentlyInCreationException`) | Allowed (Masks architectural flaws) | Allowed (Masks architectural flaws) |
| **Null Safety** | Guarantees all required dependencies are initialized | Risk of `NullPointerException` if setter not called | Risk of `NPE` outside Spring context |
| **Spring Recommendation** | **Official Best Practice** | Optional dependencies only | **Discouraged** |

#### 3. Production Code Example
```java
package com.enterprise.service;

import com.enterprise.repository.AuditLogRepository;
import com.enterprise.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    // 1. Mandatory dependencies declared as private final (Immutability guarantee)
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    // 2. Single constructor (Spring 4.3+ automatically autowires without @Autowired annotation)
    public UserManagementService(UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public void registerUser(String email) {
        userRepository.saveUser(email);
        auditLogRepository.logAction("REGISTER", email);
    }
}
```

```java
// Unit Testing without Spring Container (Lightning fast)
class UserManagementServiceTest {
    @Test
    void testRegistration() {
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        AuditLogRepository mockAuditRepo = Mockito.mock(AuditLogRepository.class);

        // Plain Java constructor call: No Spring context, no reflection!
        UserManagementService service = new UserManagementService(mockUserRepo, mockAuditRepo);
        service.registerUser("test@enterprise.com");

        Mockito.verify(mockUserRepo).saveUser("test@enterprise.com");
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a legacy codebase using field injection (`@Autowired private OrderService orderService;`), unit tests took 45 seconds to boot the entire Spring test context for a simple logic test. Furthermore, creating instances in standalone helper scripts resulted in `NullPointerException`. Converting the service to constructor injection enabled instantaneous sub-second POJO unit tests with Mockito and guaranteed compile-time safety.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use constructor injection for all mandatory dependencies.
- ✅ **DO** mark injected fields as `private final` to enforce immutability and thread safety.
- ❌ **DON'T** use `@Autowired` on private fields (Field Injection).
- ✅ **DO** use Lombok's `@RequiredArgsConstructor` if you want to avoid writing constructor boilerplate.

---

### Q39. What are the different ways to configure a Spring Boot application?

#### 1. Concept & Interview Answer
Spring Boot provides an extensive externalized configuration hierarchy allowing values to be configured and overridden seamlessly.
Key configuration mechanisms include:
1. **`application.properties` / `application.yml`**: Packaged within the JAR or placed externally.
2. **Environment-Specific Profiles**: `application-{profile}.yml` (e.g., `application-dev.yml`, `application-prod.yml`).
3. **Environment Variables (OS / Container):** Overrides properties using uppercase and underscores (e.g., `SPRING_DATASOURCE_URL` overrides `spring.datasource.url`).
4. **Command-Line Arguments:** Passed at runtime (`java -jar app.jar --server.port=9090`).
5. **Java System Properties:** Passed via JVM args (`-Dserver.port=9090`).
6. **Configuration Servers:** Spring Cloud Config Server, HashiCorp Vault, AWS Parameter Store, Kubernetes ConfigMaps.

#### 2. Configuration Precedence Order (Top overrides Bottom)
```
1. Command Line Arguments (--server.port=9090)
2. Java System Properties (-Dserver.port=9090)
3. OS Environment Variables (SERVER_PORT=9090)
4. Profile-specific application.yml outside packaged JAR (config/application-prod.yml)
5. Profile-specific application.yml inside packaged JAR (classpath:application-prod.yml)
6. Standard application.yml outside packaged JAR (config/application.yml)
7. Standard application.yml inside packaged JAR (classpath:application.yml)
8. @PropertySource annotations on @Configuration classes
9. Default properties (SpringApplication.setDefaultProperties)
```

#### 3. Production Code Example (Kubernetes & Environment Variable Overrides)
```yaml
# src/main/resources/application.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dev_db
    username: dev_user
    password: dev_password
```

```bash
# In Kubernetes Pod Specification / Docker Container runtime:
export SPRING_DATASOURCE_URL="jdbc:postgresql://prod-aurora-cluster.internal:5432/orders_prod"
export SPRING_DATASOURCE_USERNAME="prod_master"
export SPRING_DATASOURCE_PASSWORD="super_secret_vault_password"

# Executing jar - Environment variables automatically take precedence!
java -jar enterprise-order-service.jar
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During a Kubernetes migration, a container image built for staging was accidentally deployed to production with staging database URLs baked into the internal `application.yml`. Because Spring Boot's configuration precedence allows OS Environment Variables to override internal JAR properties, injecting `SPRING_DATASOURCE_URL` via Kubernetes ConfigMaps and Secrets ensured the exact same container image ran safely across Dev, QA, Staging, and Production without rebuilding the artifact.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** follow the "Build Once, Deploy Everywhere" 12-Factor App methodology by injecting environment-specific parameters via Environment Variables.
- ✅ **DO** use relaxed binding rules: `SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE` automatically maps to `spring.datasource.hikari.maximum-pool-size`.

---

### Q40. What is the role of @Value and @ConfigurationProperties?

#### 1. Concept & Interview Answer
- **`@Value`**:
  - Injects individual scalar values, SpEL (Spring Expression Language) expressions, or environment property values directly into a single field or constructor parameter (e.g., `@Value("${app.timeout:5000}")`).
  - Lacks hierarchical grouping and does not support type-safe validation.
- **`@ConfigurationProperties`**:
  - Binds entire hierarchical property structures (prefixed) into a strongly-typed Java class or Java 17 `record`.
  - Supports **relaxed binding**, **Jakarta Bean Validation (`@Validated`)**, **complex nested lists/maps**, and **meta-data auto-completion in IDEs**.

#### 2. Deep-Dive Comparison Matrix
| Feature | `@Value` | `@ConfigurationProperties` |
| :--- | :--- | :--- |
| **Binding Mechanism** | Single property per annotation | Binds an entire hierarchical tree with a common prefix |
| **Relaxed Binding** | ❌ No (Exact property name required) | ✅ Yes (`kebab-case`, `camelCase`, `SNAKE_CASE` all match) |
| **Validation Support** | ❌ No | ✅ Yes (Supports `@NotNull`, `@Min`, `@Pattern`) |
| **Java 17 Record Support**| Limited | ✅ Yes (Immutable constructor binding) |
| **SpEL Support** | ✅ Yes (`@Value("#{systemProperties['user.home']}")`) | ❌ No |
| **IDE Auto-completion** | ❌ No | ✅ Yes (via `spring-boot-configuration-processor`) |

#### 3. Production Code Example
```java
// 1. Immutable Type-Safe Configuration Record with Validation
package com.enterprise.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "enterprise.mail")
public record MailServerProperties(
    @NotBlank String host,
    @Min(1) @Max(65535) int port,
    @DefaultValue("true") boolean useTls,
    List<String> defaultRecipients
) {}
```

```java
// 2. Service consuming the configuration
@Service
public class NotificationService {

    private final MailServerProperties mailProps;

    public NotificationService(MailServerProperties mailProps) {
        this.mailProps = mailProps;
    }

    public void sendAlert(String message) {
        System.out.println("Connecting to mail host: " + mailProps.host() + ":" + mailProps.port());
    }
}
```

```yaml
# application.yml
enterprise:
  mail:
    host: "smtp.sendgrid.net"
    port: 587
    use-tls: true
    default-recipients:
      - "alerts@enterprise.com"
      - "ops@enterprise.com"
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An application failed in production with invalid timeout values because a developer typed `app.timeout=FIVE_SECONDS` instead of an integer. Because `@Value("${app.timeout}")` does not validate types until used, the app crashed during an active user checkout request. Migrating to `@ConfigurationProperties` with `@Min(1000)` failed the application immediately at startup during CI deployment, preventing the misconfiguration from reaching live traffic.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer `@ConfigurationProperties` over `@Value` for all application configurations and third-party integrations.
- ✅ **DO** add `spring-boot-configuration-processor` to your build file to generate IDE auto-completion metadata for your custom properties.
- ❌ **DON'T** scatter `@Value("${...}")` annotations across dozens of service classes; centralize related properties into dedicated properties classes.

---

### Q41. What is CommandLineRunner?

#### 1. Concept & Interview Answer
- **`CommandLineRunner`** is a functional callback interface in Spring Boot used to execute a block of code **after the `ApplicationContext` is fully initialized and the embedded web server is started, but before the application begins accepting live traffic**.
- It provides a single method: `void run(String... args) throws Exception`, which receives the raw command-line arguments passed to `main(String[] args)`.
- Multiple runners can be ordered using the `@Order` annotation.
- Similar to **`ApplicationRunner`**, which provides formatted `ApplicationArguments` (options vs non-options) instead of raw strings.

#### 2. Startup Execution Sequence
```
[ SpringApplication.run() ]
             │
             ▼
[ Initialize & Populate ApplicationContext ]
             │
             ▼
[ Start Embedded Tomcat on Port 8080 ]
             │
             ▼
[ Discover & Execute @Order CommandLineRunner / ApplicationRunner Beans ]
   ├── @Order(1) DatabaseCachePreloaderRunner.run()
   └── @Order(2) SqsQueueListenerWarmupRunner.run()
             │
             ▼
[ Ready: App Accepts External HTTP Requests ]
```

#### 3. Production Code Example
```java
package com.enterprise.runner;

import com.enterprise.repository.RoleRepository;
import com.enterprise.domain.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1) // Runs first among startup runners
public class MasterDataSeederRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MasterDataSeederRunner.class);
    private final RoleRepository roleRepository;

    public MasterDataSeederRunner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking Master Data initialization...");
        if (roleRepository.count() == 0) {
            log.info("Seeding default roles (ROLE_ADMIN, ROLE_USER)...");
            roleRepository.saveAll(List.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));
            log.info("Master data successfully seeded.");
        }
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a high-traffic pricing service, the first 100 incoming customer requests experienced 3-second latencies because the Redis currency exchange cache was cold on new container deployment. Implementing a `CommandLineRunner` pre-loaded the exchange rates cache during pod startup before the Kubernetes readiness probe turned green, ensuring 100% warm-cache sub-10ms response times from the very first request.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `CommandLineRunner` or `ApplicationRunner` for pre-flight data warming, cache preloading, and startup verification checks.
- ✅ **DO** specify `@Order` if you have multiple runners with strict execution dependencies.
- ❌ **DON'T** place heavy, infinite-looping tasks inside a runner; runners run synchronously on the main startup thread and will block the application from completing its startup sequence.

---

### Q42. What is the use of application.properties file?

#### 1. Concept & Interview Answer
- The `application.properties` (or `application.yml`) file serves as the centralized repository for externalized configuration in a Spring Boot application.
- **Key Enterprise Capabilities:**
  1. **Framework Tuning:** Configures Spring, Hibernate, HikariCP, Tomcat, Jackson, and Spring Security properties.
  2. **Profile-Specific Overlays:** Can be combined with `application-{profile}.properties` to provide environment-specific configurations.
  3. **Multi-Document Files:** In Spring Boot 2.4+, a single `.yml` or `.properties` file can define multiple profile sections using `---` (or `spring.config.activate.on-profile`).
  4. **Property Placeholders:** Supports variable substitution (e.g., `app.url=${APP_DOMAIN:localhost}:8080`).

#### 2. Multi-Profile Single YAML Document Architecture
```yaml
# application.yml (Base Defaults for All Profiles)
spring:
  application:
    name: order-service
  jpa:
    open-in-view: false

server:
  port: 8080

---
# Development Profile Configuration
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:h2:mem:orderdb
  jpa:
    show-sql: true

---
# Production Profile Configuration
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://prod-db-cluster:5432/orderdb
    hikari:
      maximum-pool-size: 50
  jpa:
    show-sql: false
```

#### 3. Production Code Example (Property Placeholders & Default Fallbacks)
```properties
# Basic property with default fallback if environment variable is missing
app.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}

# Value expansion referencing another property
app.upload.base-dir=${user.home}/enterprise/uploads
app.upload.temp-dir=${app.upload.base-dir}/temp
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A legacy application had `show-sql: true` active in production, flooding disk storage with gigabytes of raw SQL logs per hour and degrading database throughput. Organizing configurations into clear profile-activated documents (`on-profile: dev` vs `on-profile: prod`) ensured verbose debugging was strictly confined to developer workstations while production ran in optimized silent mode.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always set `spring.jpa.open-in-view=false` in `application.properties` to avoid silent connection leaks across the web layer.
- ✅ **DO** use property defaults syntax `${ENV_VAR:defaultValue}` to prevent application startup crashes when running on local development machines without cloud environment variables.

---

### Q43. What is @EnableAutoConfiguration?

#### 1. Concept & Interview Answer
- **`@EnableAutoConfiguration`** is the core annotation that activates Spring Boot's intelligent auto-configuration mechanism.
- It instructs Spring Boot to look at the classpath, determine which libraries are present, and automatically register matching configuration beans.
- It is imported automatically when you use **`@SpringBootApplication`**, so developers rarely need to declare it explicitly.
- Internally, it imports **`AutoConfigurationImportSelector`**, which reads all auto-configuration candidate classes listed in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

#### 2. Internal Mechanism Flow
```
@EnableAutoConfiguration
           │
           ▼
Imports AutoConfigurationImportSelector
           │
           ▼
Reads META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
           │
           ▼
Filters Candidates using @ConditionalOnClass, @ConditionalOnMissingBean, etc.
           │
           ▼
Registers matched Configuration classes into Spring BeanFactory
```

#### 3. Production Code Example
```java
package com.enterprise;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Standalone manual usage without @SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class StandaloneCustomApp {
    // Custom configuration...
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an isolated unit test slicing scenario (`@DataJpaTest`), standard full-context `@SpringBootApplication` was loading unnecessary web controllers and security filters. Understanding `@EnableAutoConfiguration` allowed the test framework to selectively auto-configure only JPA repositories and in-memory databases, reducing test suite execution time from 4 minutes to 12 seconds.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** rely on `@SpringBootApplication`, which encompasses `@EnableAutoConfiguration` cleanly.
- ❌ **DON'T** manually add `@EnableAutoConfiguration` if you are already using `@SpringBootApplication`.

---

### Q44. How do you exclude specific auto-configuration classes?

#### 1. Concept & Interview Answer
If Spring Boot auto-configures a bean or feature that you do not want, you can disable it using three primary techniques:
1. **Via `@SpringBootApplication` Annotation:** Using the `exclude` attribute.
2. **Via `@EnableAutoConfiguration` Annotation:** Using `exclude` or `excludeName`.
3. **Via `application.properties` / `application.yml`:** Using `spring.autoconfigure.exclude` (allows disabling auto-configuration without code changes).

#### 2. Production Code Example

##### Method 1: Programmatic Exclusion via `@SpringBootApplication`
```java
package com.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
    exclude = {
        SecurityAutoConfiguration.class,   // Disable default Spring Security login form
        DataSourceAutoConfiguration.class, // Disable automatic database setup
        RedisAutoConfiguration.class       // Disable Redis auto-connection
    }
)
public class MicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
```

##### Method 2: External Configuration Exclusion via `application.yml`
```yaml
# application.yml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
      - org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A development team added `spring-boot-starter-security` to prepare for future authentication work, but immediately got blocked because Spring Security auto-generated a random password on startup and secured all existing REST endpoints with basic auth. Adding `SecurityAutoConfiguration.class` to `exclude` temporarily disabled the default security interceptors until the team finished writing their custom JWT filter chain.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `spring.autoconfigure.exclude` in `application-test.yml` when you want to disable specific auto-configurations (like external caching or cloud messaging) purely for integration test runs.
- ❌ **DON'T** exclude auto-configurations unnecessarily; if you simply want to customize a bean, declaring your own `@Bean` is cleaner than disabling the entire auto-configuration class.

---

### Q45. How do you create a custom banner in Spring Boot?

#### 1. Concept & Interview Answer
- By default, Spring Boot prints an ASCII art "Spring" banner in the console on application startup.
- **How to customize:**
  1. **Text Banner:** Create a file named **`banner.txt`** and place it inside the `src/main/resources/` directory.
  2. **Image Banner:** Place a `banner.png`, `banner.jpg`, or `banner.gif` in `src/main/resources/` (Spring Boot converts images to ASCII art).
  3. **Custom Location:** Set `spring.banner.location=classpath:custom-banner.txt` in `application.yml`.
  4. **Programmatic Banner:** Implement the `Banner` interface and pass it to `SpringApplication.setBanner()`.
  5. **Turning Off Banner:** Set `spring.main.banner-mode=off` or call `app.setBannerMode(Banner.Mode.OFF)`.

#### 2. Variables Supported in `banner.txt`
| Variable | Description |
| :--- | :--- |
| **`${spring-boot.version}`** | Version of Spring Boot in use (e.g., `3.2.4`) |
| **`${application.version}`** | Version of the application declared in `pom.xml` |
| **`${application.title}`** | Title of the application declared in `pom.xml` |
| **`${AnsiColor.BRIGHT_GREEN}`** | ANSI Color formatting for terminal output |
| **`${AnsiBackground.BLACK}`** | ANSI Background formatting |

#### 3. Production Code Example

##### Custom `src/main/resources/banner.txt`
```text
${AnsiColor.BRIGHT_CYAN}
  ███████╗███╗   ██╗████████╗███████╗██████╗ ██████╗ ██████╗ ██╗███████╗███████╗
  ██╔════╝████╗  ██║╚══██╔══╝██╔════╝██╔══██╗██╔══██╗██╔══██╗██║██╔════╝██╔════╝
  █████╗  ██╔██╗ ██║   ██║   █████╗  ██████╔╝██████╔╝██████╔╝██║███████╗█████╗  
  ██╔══╝  ██║╚██╗██║   ██║   ██╔══╝  ██╔══██╗██╔═══╝ ██╔══██╗██║╚════██║██╔══╝  
  ███████╗██║ ╚████║   ██║   ███████╗██║  ██║██║     ██║  ██║██║███████║███████╗
  ╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝╚══════╝╚══════╝
${AnsiColor.BRIGHT_YELLOW}
  :: Enterprise Core Banking API :: (v${application.version:-1.0.0})
  :: Running on Spring Boot v${spring-boot.version} ::
${AnsiColor.DEFAULT}
```

##### Programmatic Banner Control
```java
package com.enterprise;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        // Disable banner in CI/CD pipeline or batch jobs to keep logs clean
        if ("true".equalsIgnoreCase(System.getenv("CI_MODE"))) {
            app.setBannerMode(Banner.Mode.OFF);
        }
        app.run(args);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a cloud environment with 80 microservices, developers viewing centralized Kibana/Datadog logs had difficulty immediately identifying the running service name and version during pod restart loops. Adding a standardized `banner.txt` displaying the service name, build hash, and Spring Boot version made log inspection and instant version verification effortless during production incidents.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** include `${application.version}` and `${spring-boot.version}` in custom banners for instant build verification in console logs.
- ✅ **DO** disable banners in batch processing CLI tools or short-lived AWS Lambda functions (`spring.main.banner-mode=off`) to minimize startup time and log noise.

---

### Q46. How do you connect Spring Boot to a database?

#### 1. Concept & Interview Answer
Connecting Spring Boot to a relational database requires three core steps:
1. **Add Dependencies:** Include `spring-boot-starter-data-jpa` and the JDBC driver for your database (e.g., `org.postgresql:postgresql` or `com.mysql:mysql-connector-j`).
2. **Configure Connection Properties:** Specify `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in `application.yml`.
3. **Auto-Configuration Activation:** Spring Boot's `DataSourceAutoConfiguration` automatically detects the driver, instantiates a high-performance **HikariCP connection pool**, and configures the JPA `EntityManagerFactory`.

#### 2. Connection Initialization Flow
```
[ application.yml (Credentials & Pool configs) ]
                       │
                       ▼
[ DataSourceAutoConfiguration ] ──► Initializes HikariCP Connection Pool
                       │
                       ▼
[ HibernateJpaAutoConfiguration ] ──► Builds EntityManagerFactory & SessionFactory
                       │
                       ▼
[ Spring Data JpaRepositories ] ──► Injected into Services for CRUD execution
                       │
                       ▼
[ PostgreSQL / MySQL Database ]
```

#### 3. Production Code Example
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://prod-aurora-db.internal:5432/enterprise_orders?sslmode=require
    username: ${DB_USER:order_app_user}
    password: ${DB_PASSWORD:secret_vault_pass}
    driver-class-name: org.postgresql.Driver
    hikari:
      pool-name: EnterpriseHikariPool
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000       # 5 minutes
      connection-timeout: 20000  # 20 seconds
      max-lifetime: 1200000      # 20 minutes (prevents stale DB firewall drops)

  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A high-throughput API service suffered intermittent connection dropouts and 30-second thread freezes during traffic surges. The root cause was unconfigured default pool settings where connections timed out silently across cloud firewalls. Configuring HikariCP explicitly with `max-lifetime=1200000` (less than the cloud firewall 30-minute idle drop window) and `connection-timeout=20000` stabilized database connection reliability to 99.999%.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use HikariCP (Spring Boot's default connection pool) as it is the fastest and most reliable JDBC pool available.
- ✅ **DO** ensure `max-lifetime` is set 2–3 minutes shorter than any database or cloud infrastructure connection timeout limit.
- ❌ **DON'T** hardcode database credentials in `application.yml`; inject them using environment variables or Kubernetes secrets.

---

### Q47. What is the use of spring.datasource.url property?

#### 1. Concept & Interview Answer
- `spring.datasource.url` specifies the **JDBC Uniform Resource Locator (URL)** that the application uses to establish physical TCP socket connections with the relational database.
- It defines:
  1. **Protocol & Driver Sub-protocol:** `jdbc:postgresql://`, `jdbc:mysql://`, `jdbc:oracle:thin:@`.
  2. **Host and Port:** Location of the database cluster (e.g., `localhost:5432`).
  3. **Database / Schema Name:** Target database name (`/enterprise_orders`).
  4. **Connection Parameters:** SSL mode, connection character encodings, timeouts, failover hosts, and read-replica routing rules.

#### 2. Advanced JDBC URL Configurations
```properties
# 1. PostgreSQL with SSL & High-Availability Target Server Type
spring.datasource.url=jdbc:postgresql://pg-primary:5432,pg-standby:5432/orders_db?targetServerType=primary&sslmode=verify-full&sslrootcert=/etc/ssl/root.crt

# 2. MySQL with Zero DateTime & Auto-Reconnect
spring.datasource.url=jdbc:mysql://mysql-cluster:3306/orders_db?useSSL=true&requireSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true

# 3. Oracle DB TNS Descriptor
spring.datasource.url=jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=oracle-scan.internal)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=ORCL_APP)))
```

#### 3. Production Code Example (Dynamic Multi-Tenant URL Routing)
```java
package com.enterprise.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.Map;

public class TenantAwareRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getCurrentTenantId(); // Returns "TENANT_US" or "TENANT_EU"
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an AWS Multi-AZ deployment, database failovers took 5 minutes to recover because the JDBC URL pointed to a static IP rather than the Aurora Cluster Endpoint. Updating the JDBC URL to `jdbc:postgresql://aurora-cluster.cluster-xyz.us-east-1.rds.amazonaws.com:5432/prod_db?targetServerType=primary` enabled automated sub-10-second DNS failover without restarting application pods.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always append `sslmode=require` (or `verify-full`) in production database URLs to encrypt data in transit.
- ✅ **DO** explicitly configure `serverTimezone=UTC` in JDBC URLs to avoid datetime drift across global servers.

---

### Q48. What is Spring Data JPA?

#### 1. Concept & Interview Answer
- **Spring Data JPA** is a core abstraction framework within the Spring Data family that sits on top of standard JPA (Java Persistence API) and ORM providers like Hibernate.
- **Key Capabilities:**
  1. **Automated Repository Generation:** Automatically generates implementations for interfaces extending `JpaRepository<T, ID>` or `CrudRepository<T, ID>` at runtime using dynamic proxies.
  2. **Derived Query Methods:** Generates SQL queries automatically from method naming conventions (e.g., `findByEmailAndStatus(String email, Status status)`).
  3. **Declarative Queries:** Supports JPQL and Native SQL via `@Query`.
  4. **Built-in Pagination & Sorting:** Out-of-the-box `Pageable` and `Sort` arguments returning `Page<T>` or `Slice<T>`.
  5. **Auditing:** Automated timestamping and user tracking via `@CreatedDate`, `@LastModifiedDate`, and `@CreatedBy`.

#### 2. Architecture Abstraction Layers
```
[ Your Service Layer (OrderService) ]
                 │
                 ▼
[ Spring Data JpaRepository Interface ] ──► (Dynamic Proxy created at runtime)
                 │
                 ▼
[ JPA Standard Specification (EntityManager) ]
                 │
                 ▼
[ Hibernate ORM Provider (Session & Query Engine) ]
                 │
                 ▼
[ JDBC Driver / HikariCP Pool ] ──► [ Relational Database ]
```

#### 3. Production Code Example
```java
package com.enterprise.repository;

import com.enterprise.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 1. Derived Query Method (Auto-generated JPQL by Spring Data)
    Optional<Customer> findByEmailIgnoreCase(String email);

    // 2. Pagination & Sorting Query
    Page<Customer> findByStatusAndCreatedDateAfter(String status, Instant after, Pageable pageable);

    // 3. Custom JPQL Query
    @Query("SELECT c FROM Customer c WHERE c.loyaltyTier = :tier AND c.active = true")
    Page<Customer> findActiveLoyalCustomers(@Param("tier") String tier, Pageable pageable);

    // 4. Modifying Query for Bulk Updates
    @Modifying
    @Query("UPDATE Customer c SET c.active = false WHERE c.lastLoginDate < :cutoff")
    int deactivateInactiveAccounts(@Param("cutoff") Instant cutoff);
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A team maintaining a legacy application had over 15,000 lines of repetitive DAO boilerplate code executing raw JDBC `PreparedStatement` and `ResultSet` mapping loops. Migrating to Spring Data JPA eliminated 90% of the DAO codebase while preserving full query flexibility, significantly accelerating sprint velocity and reducing SQL typo bugs.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer derived query methods for simple queries and `@Query` with JPQL for queries involving complex joins or projections.
- ✅ **DO** use Spring Data Projections (interface-based or DTO-based) to select only required columns instead of fetching full entities for read-only listings.
- ❌ **DON'T** write derived query method names with more than 4 conditions (e.g., `findByXAndYOrZAndA...`); use `@Query` or Spring Data JPA Specifications for readability.

---

### Q49. How do you configure JPA in Spring Boot?

#### 1. Concept & Interview Answer
In Spring Boot, JPA is configured primarily through properties in `application.yml` under the `spring.jpa.*` prefix:
1. **`spring.jpa.hibernate.ddl-auto`**: Controls database schema generation (`validate`, `update`, `none`).
2. **`spring.jpa.show-sql` / `properties.hibernate.format_sql`**: Controls SQL logging and formatting.
3. **`spring.jpa.open-in-view`**: Toggles the Open EntityManager in View filter (should be set to `false`).
4. **`spring.jpa.properties.hibernate.dialect`**: Specifies the database SQL dialect (optional in Spring Boot 3 as Hibernate 6 auto-detects dialects).
5. **`@EnableJpaAuditing`**: Enables automatic entity auditing (`@CreatedDate`, `@LastModifiedDate`).

#### 2. Production Configuration Example
```yaml
spring:
  jpa:
    open-in-view: false                     # CRITICAL: Prevents connection leakage in web tier
    show-sql: false                        # Keep false in prod; use logging level for debug
    hibernate:
      ddl-auto: validate                   # Enforce schema validation against migration scripts
    properties:
      hibernate:
        format_sql: false
        jdbc:
          batch_size: 50                   # Batch inserts and updates for high throughput
          order_inserts: true              # Sort inserts for optimal batching
          order_updates: true              # Sort updates for optimal batching
          fetch_size: 100                  # Number of rows fetched per network round-trip
        generate_statistics: false         # Enable only for performance profiling

logging:
  level:
    org.hibernate.SQL: INFO
    org.hibernate.orm.jdbc.bind: TRACE    # Logs bound SQL parameters in dev if needed
```

```java
// Enabling JPA Auditing
package com.enterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
    // Enables @CreatedDate and @LastModifiedDate in Entities
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An application suffered high latency under load because it performed 1,000 separate SQL INSERT network trips when processing batch invoices. Setting `hibernate.jdbc.batch_size=50`, `order_inserts=true`, and `order_updates=true` in `application.yml` reduced database network round-trips by 98%, cutting batch execution time from 42 seconds to 1.8 seconds.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** set `spring.jpa.open-in-view=false` in every production Spring Boot service.
- ✅ **DO** configure JDBC batch sizing (`hibernate.jdbc.batch_size=50`) to optimize bulk write operations.
- ❌ **DON'T** use `ddl-auto=create` or `ddl-auto=update` in production environments; manage schema changes strictly via versioned migration tools (Flyway or Liquibase).

---

### Q50. What is the use of spring.jpa.hibernate.ddl-auto?

#### 1. Concept & Interview Answer
- `spring.jpa.hibernate.ddl-auto` specifies how Hibernate should handle the database **Data Definition Language (DDL)** schema on application startup.
- It determines whether Hibernate should validate, create, update, or drop database tables, constraints, foreign keys, and indexes based on JPA entity annotations (`@Entity`, `@Table`, `@Column`).

#### 2. Core DDL-Auto Modes Reference
| Mode Value | Behavior on Startup | Behavior on Shutdown | Suitable Environment |
| :--- | :--- | :--- | :--- |
| **`none`** | Does nothing. Schema remains untouched. | Does nothing. | Production |
| **`validate`** | Validates that DB tables & columns match JPA entity annotations. Throws exception if mismatch exists. | Does nothing. | **Production (Recommended)** / Staging |
| **`update`** | Compares entity models with DB; alters existing tables and adds missing tables/columns. **Never deletes** existing columns or constraints. | Does nothing. | Local Development Only |
| **`create`** | Drops existing schema and creates all tables from scratch on startup. | Does nothing. | Automated Unit Testing |
| **`create-drop`** | Creates schema from scratch on startup. | **Drops entire schema on shutdown**. | In-Memory Testing (H2) |

#### 3. Production Code Example
```yaml
# 1. Local Development (application-dev.yml)
spring:
  jpa:
    hibernate:
      ddl-auto: update

---
# 2. Production (application-prod.yml)
spring:
  jpa:
    hibernate:
      ddl-auto: validate # Enforces safety; app fails to start if DB schema deviates from code
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer deployed a service to production with `ddl-auto: create-drop` active in configuration. When the container restarted for routine maintenance, Hibernate executed `DROP ALL OBJECTS`, wiping out 400,000 active customer records in production. The team instituted a strict policy: `ddl-auto` is locked to `validate` or `none` in all production and staging profiles, and all schema modifications are managed exclusively via Flyway scripts.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `ddl-auto=validate` in production to catch entity-database schema mismatches during startup before requests are served.
- ❌ **DON'T** ever use `create`, `create-drop`, or `update` in production.

---

### Q51. What is the difference between create, update, and validate in Hibernate DDL-auto?

#### 1. Concept & Interview Answer
- **`create`**: Drops all existing tables, sequences, and constraints and recreates them from scratch every time the application boots up. All existing data is permanently lost.
- **`update`**: Analyzes the existing database schema and executes `ALTER TABLE` / `CREATE TABLE` DDL statements to match the entity mappings. It adds new columns, tables, or constraints, but **never removes deprecated columns or modified data types** to prevent data loss.
- **`validate`**: Reads the live database metadata and compares it against JPA entity annotations. If any table, column, or data type mismatch is detected, it **aborts application startup immediately** with a `SchemaManagementException`. It executes zero DDL mutations.

#### 2. Visual Comparison Matrix
```
[ App Boots ]
     │
     ├──► ddl-auto: create   ──► DROP TABLE ... ──► CREATE TABLE ... (Data Lost!)
     │
     ├──► ddl-auto: update   ──► ALTER TABLE ADD COLUMN ... (Adds only, never drops)
     │
     └──► ddl-auto: validate ──► Checks metadata ──► Mismatch? Throws Exception & Aborts
```

#### 3. Production Code Example
```yaml
# CI / Test Pipeline (application-test.yml)
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop # Clean slate for every test execution run
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A team used `ddl-auto=update` during development and renamed an entity field from `customerPhone` to `phoneNumber`. Hibernate added a new column `phone_number` but left the old `customer_phone` column populated with historical data. Queries read `NULL` from the new column because `update` did not migrate the historical data. The team switched to versioned migrations with Flyway where data migration SQL is explicitly scripted.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `validate` alongside database migration tools (Flyway/Liquibase) in production to ensure code and migrations are in sync.
- ❌ **DON'T** rely on `update` for renaming columns or changing data types, as Hibernate cannot generate data-migration SQL.

---

### Q52. How do you execute native queries in Spring Data JPA?

#### 1. Concept & Interview Answer
- A **Native Query** in Spring Data JPA is a raw SQL query executed directly by the underlying database engine rather than being parsed as JPQL by Hibernate.
- Enabled by setting **`nativeQuery = true`** inside the **`@Query`** annotation on a repository method.
- **When to use Native Queries:**
  1. Utilizing database-specific proprietary SQL features (e.g., PostgreSQL JSONB operators `->>`, `jsonb_array_elements`, Window Functions `OVER (PARTITION BY ...)`).
  2. Executing complex recursive Common Table Expressions (CTEs) or complex analytical reporting queries.
  3. Query performance optimization when Hibernate's generated JPQL produces suboptimal join execution plans.

#### 2. Native Query vs JPQL Comparison
| Feature | JPQL (`nativeQuery = false`) | Native SQL (`nativeQuery = true`) |
| :--- | :--- | :--- |
| **Syntax** | Object-oriented (references Java Entities & Fields) | Raw SQL (references DB Tables & Column names) |
| **Database Portability** | Database-independent (Hibernate translates to target DB) | Vendor-specific (Tied to PostgreSQL/MySQL/Oracle) |
| **Advanced SQL Support** | Limited to standard JPA query language features | Full access to DB-specific functions, CTEs, Windowing |
| **Projections** | Entities or DTO constructors | Interface Projections or `Tuple` / `@SqlResultSetMapping` |

#### 3. Production Code Example (PostgreSQL JSONB & Window Functions)
```java
package com.enterprise.repository;

import com.enterprise.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderNativeRepository extends JpaRepository<Order, Long> {

    // 1. Interface-based projection for native query results
    interface CustomerSpendProjection {
        Long getCustomerId();
        String getCustomerName();
        Double getTotalSpent();
        Integer getOrderCount();
    }

    // 2. Native query using PostgreSQL aggregation and window functions
    @Query(value = """
        SELECT 
            c.id AS customerId,
            c.full_name AS customerName,
            SUM(o.total_amount) AS totalSpent,
            COUNT(o.id) AS orderCount
        FROM customers c
        JOIN orders o ON c.id = o.customer_id
        WHERE o.status = 'COMPLETED' AND o.created_at >= NOW() - INTERVAL '30 days'
        GROUP BY c.id, c.full_name
        HAVING SUM(o.total_amount) > :minSpend
        ORDER BY totalSpent DESC
        """,
        countQuery = """
        SELECT COUNT(DISTINCT c.id)
        FROM customers c
        JOIN orders o ON c.id = o.customer_id
        WHERE o.status = 'COMPLETED' AND o.created_at >= NOW() - INTERVAL '30 days'
        GROUP BY c.id
        HAVING SUM(o.total_amount) > :minSpend
        """,
        nativeQuery = true)
    Page<CustomerSpendProjection> findTopSpendersNative(
            @Param("minSpend") Double minSpend,
            Pageable pageable);
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An analytics dashboard running a complex JPQL report query was timing out after 60 seconds because Hibernate generated 14 nested subqueries. Rewriting the query into a single native PostgreSQL query leveraging Common Table Expressions (`WITH monthly_sales AS (...)`) reduced query execution time from 60 seconds to 120 milliseconds.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always provide a `countQuery` attribute when using native queries with pagination (`Pageable`), otherwise Spring Data cannot calculate total pages.
- ✅ **DO** map native query results to **Interface Projections** using SQL aliases (`AS columnName`) matching the getter method names.
- ❌ **DON'T** use native queries for simple CRUD operations; reserve them for advanced, vendor-specific reporting or bulk operations.

---

### Q53. How to define custom queries using @Query annotation?

#### 1. Concept & Interview Answer
- The **`@Query`** annotation allows developers to write custom queries directly on Spring Data JPA repository methods.
- **Two Primary Flavors:**
  1. **JPQL (Java Persistence Query Language - Default):** Operates on entity class names and Java property names. Database-agnostic.
  2. **Native SQL (`nativeQuery = true`):** Operates directly on database tables and columns.
- Parameters can be bound using **Named Parameters** (`:paramName` with `@Param`) or **Positional Parameters** (`?1`, `?2`). Named parameters are strongly recommended for maintainability.

#### 2. Parameter Binding & Projection Mechanics
```
Method Call: repo.findOrdersByStatusAndDate("SHIPPED", cutoffDate)
                               │
                               ▼
        @Query("SELECT o FROM Order o WHERE o.status = :status AND o.orderDate >= :date")
                               │
                               ├── :status ──► Bound to parameter @Param("status")
                               └── :date   ──► Bound to parameter @Param("date")
                               │
                               ▼
     [ Hibernate generates optimized SQL with PreparedStatements ]
```

#### 3. Production Code Example (JPQL with DTO Constructor Expression)
```java
package com.enterprise.repository;

import com.enterprise.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderCustomRepository extends JpaRepository<Order, Long> {

    // Record DTO used for direct constructor projection
    record OrderSummaryDto(Long orderId, String trackingNumber, Double amount, String customerEmail) {}

    // JPQL Constructor Expression: Instantiates DTO directly in the query (bypasses Entity hydration)
    @Query("""
        SELECT new com.enterprise.repository.OrderCustomRepository$OrderSummaryDto(
            o.id,
            o.trackingNumber,
            o.totalAmount,
            o.customer.email
        )
        FROM Order o
        JOIN o.customer c
        WHERE o.status = :status
          AND o.createdAt >= :since
        ORDER BY o.createdAt DESC
        """)
    List<OrderSummaryDto> findOrderSummaries(
            @Param("status") String status,
            @Param("since") Instant since);
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An API endpoint fetching order summaries for a dashboard was fetching full `Order` entities containing large text descriptions and lazy associations, causing 100MB of heap allocation per request. Refactoring the JPQL query to use a DTO Constructor Expression (`SELECT new OrderSummaryDto(...)`) fetched only the 4 required columns from the database, reducing memory consumption by 85%.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always use named parameters (`:paramName` with `@Param("paramName")`) instead of positional parameters (`?1`) for clarity and refactoring safety.
- ✅ **DO** leverage JPQL Constructor Expressions (`SELECT new com.dto.MyDto(...)`) for read-only query endpoints to bypass Hibernate entity lifecycle overhead.
- ❌ **DON'T** use String concatenation inside queries to prevent SQL Injection; let Spring Data bind parameters via PreparedStatements.

---

### Q54. How do you enable pagination and sorting in Spring Data JPA?

#### 1. Concept & Interview Answer
- Spring Data JPA provides native support for pagination and sorting by accepting a **`Pageable`** (or `Sort`) parameter in repository method signatures.
- **Return Types:**
  1. **`Page<T>`**: Contains the content list, current page index, total page count, and **total element count** (triggers an additional `SELECT COUNT(*)` query).
  2. **`Slice<T>`**: Contains the content list and a boolean flag `hasNext()`. **Does NOT execute a `COUNT(*)` query**, making it much faster for infinite scrolling (mobile apps).
  3. **`List<T>`**: Returns just the paginated list when total count is not needed.
- In Spring MVC, Spring automatically resolves incoming query parameters `?page=0&size=20&sort=createdAt,desc` into a `Pageable` instance using `PageableHandlerMethodArgumentResolver`.

#### 2. Pagination Flow Architecture
```
HTTP GET /api/v1/orders?page=2&size=10&sort=totalAmount,desc
                       │
                       ▼
[ PageableHandlerMethodArgumentResolver ] ──► Creates PageRequest.of(2, 10, Sort.by("totalAmount").descending())
                       │
                       ▼
[ Repository.findAll(Pageable) ]
   ├── Query 1: SELECT * FROM orders ORDER BY total_amount DESC LIMIT 10 OFFSET 20;
   └── Query 2: SELECT COUNT(*) FROM orders; (Executed only for Page<T>)
                       │
                       ▼
[ Returns Page<OrderResponseDto> (Content + Total Pages + HasNext) ]
```

#### 3. Production Code Example
```java
package com.enterprise.order.controller;

import com.enterprise.domain.entity.Order;
import com.enterprise.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderPaginationController {

    private final OrderRepository orderRepository;

    public OrderPaginationController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrdersPaged(
            @PageableDefault(page = 0, size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Order> orderPage = orderRepository.findAll(pageable);

        // Map entity page to DTO page preserving pagination metadata
        Page<OrderDto> dtoPage = orderPage.map(order -> new OrderDto(
            order.getId(),
            order.getOrderNumber(),
            order.getTotalAmount(),
            order.getCreatedAt()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    public record OrderDto(Long id, String orderNumber, Double amount, java.time.Instant createdAt) {}
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** On a database table with 20 million records, an infinite-scroll mobile API was timing out because `Page<T>` executed `SELECT COUNT(*)` on every scroll request. Switching the repository return type from `Page<T>` to `Slice<T>` eliminated the expensive count query, reducing endpoint response times from 4.8 seconds to 15 milliseconds.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `Slice<T>` instead of `Page<T>` for mobile feeds and infinite scroll use cases where total record counts are not displayed.
- ✅ **DO** ensure the sorted column (e.g., `createdAt`) has a database B-Tree index to prevent slow sequential table scans during `ORDER BY ... LIMIT ... OFFSET`.
- ❌ **DON'T** allow unbounded `size` parameters from clients (e.g., `size=1000000`); enforce max page sizes using `spring.data.web.pageable.max-page-size=100` in `application.yml`.

---

### Q55. How do you handle database transactions in Spring Boot?

#### 1. Concept & Interview Answer
- Database transactions in Spring Boot are managed declaratively using the **`@Transactional`** annotation (from `org.springframework.transaction.annotation.Transactional`).
- **How it works:** Spring creates an **AOP (Aspect-Oriented Programming) proxy** around the `@Transactional` bean.
  1. When a transactional method is entered, the proxy opens a database transaction via `PlatformTransactionManager` (`JpaTransactionManager`).
  2. If the method completes successfully, the proxy **commits** the transaction and flushes dirty entities to the database.
  3. If an unchecked exception (`RuntimeException` or `Error`) is thrown, the proxy **rolls back** the transaction automatically.
- **Key Attributes:** `propagation`, `isolation`, `readOnly`, `timeout`, and `rollbackFor`.

#### 2. Transaction Proxy Lifecycle
```
Caller (Controller) ──► [ Spring AOP TransactionInterceptor Proxy ]
                                   │
                                   ├──► 1. Opens DB Transaction (setAutoCommit(false))
                                   │
                                   ▼
                         [ Target Service Method ]
                                   │
         ┌─────────────────────────┴─────────────────────────┐
         ▼ (Method Success)                                  ▼ (Throws RuntimeException)
2. Commit Transaction                               2. Rollback Transaction
   Flushes Hibernate L1 Cache                          Reverts all SQL operations
         │                                                   │
         ▼                                                   ▼
[ DB Commit OK ]                                    [ DB Rollback OK ]
```

#### 3. Production Code Example
```java
package com.enterprise.banking.service;

import com.enterprise.banking.repository.AccountRepository;
import com.enterprise.domain.entity.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankTransferService {

    private final AccountRepository accountRepository;
    private final AuditService auditService;

    public BankTransferService(AccountRepository accountRepository, AuditService auditService) {
        this.accountRepository = accountRepository;
        this.auditService = auditService;
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = Exception.class, // Rollback for Checked AND Unchecked exceptions
        timeout = 10                  // Timeout after 10 seconds to prevent DB lock stalls
    )
    public void executeTransfer(Long fromId, Long toId, BigDecimal amount) throws Exception {
        Account source = accountRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account destination = accountRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Balance too low");
        }

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        // Audit log in a separate independent transaction that won't rollback if transfer fails
        auditService.logAuditRecordAsync("TRANSFER", fromId, toId, amount);
    }
}
```

```java
@Service
public class AuditService {
    @Transactional(propagation = Propagation.REQUIRES_NEW) // Creates brand-new isolated transaction
    public void logAuditRecordAsync(String action, Long fromId, Long toId, BigDecimal amount) {
        // Save audit entry...
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A developer threw a checked exception (`throw new CustomBusinessException("Validation failed")`) inside a `@Transactional` method. Because Spring's default `@Transactional` only rolls back for unchecked `RuntimeException` and `Error`, the database transaction **committed anyway**, corrupting financial data. Adding `rollbackFor = Exception.class` ensured rollback on all checked business exceptions.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** always specify `@Transactional(rollbackFor = Exception.class)` to prevent unhandled checked exceptions from committing dirty states.
- ✅ **DO** use `@Transactional(readOnly = true)` for read-only service methods (improves performance by disabling Hibernate dirty checking).
- ❌ **DON'T** call a `@Transactional` method from another method **within the same class** (Self-Invocation Problem); this bypasses the Spring AOP proxy and disables transaction management.

---

### Q56. What is Actuator in Spring Boot?

#### 1. Concept & Interview Answer
- **Spring Boot Actuator** (`spring-boot-starter-actuator`) provides production-ready operational and monitoring capabilities for Spring Boot applications.
- It exposes HTTP and JMX management endpoints that provide deep visibility into application health, performance metrics, environment properties, and thread activity.
- **Key Built-in Endpoints:**
  - `/actuator/health`: Liveness and readiness probes for Kubernetes.
  - `/actuator/metrics`: Micrometer JVM, CPU, and custom business metrics (integrates with Prometheus/Grafana).
  - `/actuator/env`: View active environment properties and configuration values.
  - `/actuator/loggers`: View and dynamically adjust log levels at runtime without restarting the application.
  - `/actuator/threaddump`: Real-time JVM thread dumps for diagnosing deadlocks and thread contention.

#### 2. Production Configuration Example
```yaml
# application.yml
management:
  server:
    port: 8081                         # Run Actuator on dedicated internal management port
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: health,info,metrics,prometheus,loggers
  endpoint:
    health:
      show-details: when_authorized    # Do not leak internal DB credentials to public
      probes:
        enabled: true                  # Enable Kubernetes liveness & readiness probes
  metrics:
    tags:
      application: ${spring.application.name}
      environment: ${ENVIRONMENT:prod}
```

#### 3. Production Code Example (Custom Health Indicator & Micrometer Metric)
```java
package com.enterprise.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayHealthIndicator implements HealthIndicator {

    private final Counter paymentFailureCounter;

    public PaymentGatewayHealthIndicator(MeterRegistry registry) {
        // Register custom Prometheus counter metric
        this.paymentFailureCounter = Counter.builder("enterprise.payment.failures")
                .description("Number of failed payment gateway attempts")
                .tag("provider", "stripe")
                .register(registry);
    }

    @Override
    public Health health() {
        boolean gatewayReachable = checkStripeConnectivity();
        if (gatewayReachable) {
            return Health.up()
                    .withDetail("gateway", "Stripe")
                    .withDetail("latencyMs", 42)
                    .build();
        } else {
            paymentFailureCounter.increment();
            return Health.down()
                    .withDetail("gateway", "Stripe")
                    .withDetail("error", "Connection timeout to Stripe API")
                    .build();
        }
    }

    private boolean checkStripeConnectivity() {
        return true; // Health check probe logic
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During a severe production memory leak, developers needed to change logging from `INFO` to `DEBUG` on a specific payment package without redeploying the pods. Using Actuator's loggers endpoint (`POST /actuator/loggers/com.enterprise.payment` with payload `{"configuredLevel":"DEBUG"}`), they enabled real-time debugging instantly without restarting the application.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** run Actuator on an isolated internal management port (`management.server.port=8081`) blocked from external public internet access via API Gateways/firewalls.
- ✅ **DO** enable Kubernetes probes (`management.endpoint.health.probes.enabled=true`) to supply `/actuator/health/liveness` and `/actuator/health/readiness` endpoints for zero-downtime rolling deployments.
- ❌ **DON'T** expose sensitive endpoints like `/actuator/heapdump`, `/actuator/env`, or `/actuator/shutdown` to the public internet.

---

### Q57. What is the difference between @RestControllerAdvice and @ControllerAdvice?

#### 1. Concept & Interview Answer
- **`@ControllerAdvice`**: Used for global exception handling across traditional Spring MVC controllers returning HTML views. Methods annotated with `@ExceptionHandler` inside `@ControllerAdvice` return view names or `ModelAndView`.
- **`@RestControllerAdvice`**: Introduced in Spring 4.3, it is a convenience meta-annotation composed of **`@ControllerAdvice` + `@ResponseBody`**.
- In `@RestControllerAdvice`, every `@ExceptionHandler` method automatically writes its returned object directly into the HTTP response body stream serialized as JSON/XML (RFC 7807 `ProblemDetail` or custom error DTO), completely bypassing view resolution.

#### 2. Architectural Comparison Matrix
| Feature | `@ControllerAdvice` | `@RestControllerAdvice` |
| :--- | :--- | :--- |
| **Annotation Composition** | `@Component` | `@ControllerAdvice` + `@ResponseBody` |
| **Default Return Type** | Logical View Name (e.g., `"error/500"`) | Serialized JSON / XML Error Payload |
| **Target Controllers** | Traditional `@Controller` (Thymeleaf/JSP) | Modern `@RestController` (APIs) |
| **ViewResolver Used?** | **Yes** | **No** |

#### 3. Production Code Example
```java
package com.enterprise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice(basePackages = "com.enterprise.api") // Scoped to REST API controllers
public class EnterpriseApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        problem.setType(URI.create("https://enterprise.com/errors/bad-request"));
        problem.setProperty("timestamp", Instant.now());
        return problem; // Serialized directly to JSON!
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A REST API team implemented global exception handling using `@ControllerAdvice` without `@ResponseBody`. When an unhandled exception occurred, Spring attempted to find a view template matching the `CustomErrorDto.toString()`, throwing an additional `NestedServletException` and returning a messy 500 HTML page to mobile clients expecting JSON. Switching to `@RestControllerAdvice` returned clean JSON payloads with appropriate HTTP status codes.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `@RestControllerAdvice` for all modern microservices and RESTful API applications.
- ✅ **DO** restrict advice scope using attributes like `@RestControllerAdvice(basePackages = "com.enterprise.api")` or `@RestControllerAdvice(annotations = RestController.class)`.

---

### Q58. How can you handle exceptions globally in Spring Boot?

#### 1. Concept & Interview Answer
In modern Spring Boot (3.x+), global exception handling is achieved through three layered strategies:
1. **`@RestControllerAdvice` + `@ExceptionHandler`:** Centralized aspect intercepting exceptions thrown from any controller and mapping them to standard RFC 7807 `ProblemDetail` responses.
2. **`ResponseEntityExceptionHandler`:** A Spring base class you can extend in your advice class to override default handling of standard Spring MVC exceptions (e.g., `MethodArgumentNotValidException`, `HttpRequestMethodNotSupportedException`, `HttpMediaTypeNotSupportedException`).
3. **`ErrorController` / `BasicErrorController`:** Fallback for low-level servlet container errors occurring **outside** `DispatcherServlet` (e.g., filter errors, 404s before routing).

#### 2. Exception Handling Hierarchy Flow
```
Controller / Filter throws Exception
                 │
                 ├──► Thrown inside DispatcherServlet?
                 │         │
                 │         ├──► Yes: Handled by @RestControllerAdvice (extends ResponseEntityExceptionHandler)
                 │         │          └── Returns structured RFC 7807 JSON Error
                 │         │
                 │         └──► No: (Filter / Container level error)
                 │                    └── Delegated to BasicErrorController (/error)
```

#### 3. Production Code Example (Extending `ResponseEntityExceptionHandler`)
```java
package com.enterprise.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalEnterpriseExceptionHandler extends ResponseEntityExceptionHandler {

    // 1. Override standard Spring MVC validation handling
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Request validation failed");
        problemDetail.setTitle("Constraint Violation");
        problemDetail.setType(URI.create("https://api.enterprise.com/errors/validation"));
        problemDetail.setProperty("timestamp", Instant.now());

        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (m1, m2) -> m1));
        problemDetail.setProperty("invalidFields", errors);

        return ResponseEntity.status(status).headers(headers).body(problemDetail);
    }

    // 2. Custom Business Exception Handler
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage());
        problem.setTitle("Business Rule Violation");
        problem.setProperty("errorCode", ex.getErrorCode());
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An enterprise had 35 microservices, each formatting error responses differently (some returned `{"msg": "..."}`, others returned `{"error_description": "..."}`). Adopting a centralized shared starter extending `ResponseEntityExceptionHandler` with RFC 7807 `ProblemDetail` standardized error contracts across all frontends and third-party consumers.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** extend `ResponseEntityExceptionHandler` in your `@RestControllerAdvice` to avoid reinventing exception handling for standard Spring framework exceptions.
- ✅ **DO** enable `spring.mvc.problemdetails.enabled=true` in `application.yml` to turn on automatic RFC 7807 formatting for all default Spring web exceptions.

---

### Q59. How to use Swagger/OpenAPI with Spring Boot?

#### 1. Concept & Interview Answer
- In Spring Boot 3.x, **Springdoc-OpenAPI** (`springdoc-openapi-starter-webmvc-ui`) is the standard library used to generate interactive API documentation following the **OpenAPI 3.0** specification.
- **Key Capabilities:**
  1. **Automated Documentation:** Automatically scans `@RestController` endpoints, parameter types, DTO schemas, and validation constraints.
  2. **Interactive UI (Swagger UI):** Accessible at `/swagger-ui/index.html` allowing developers to execute and test live API calls directly from the browser.
  3. **Schema Generation:** Exposes raw OpenAPI JSON specifications at `/v3/api-docs`.
  4. **Annotations:** Uses `@Tag`, `@Operation`, `@ApiResponse`, and `@Parameter` for rich endpoint documentation.

#### 2. Production Code Example

##### Step 1: Maven / Gradle Dependency
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

##### Step 2: OpenAPI Bean Configuration
```java
package com.enterprise.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocumentationConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("Enterprise Order Management API")
                .version("1.0.0")
                .description("Production-grade RESTful API for Order Processing")
                .contact(new Contact().name("API Support").email("api-team@enterprise.com"))
                .license(new License().name("Apache 2.0").url("https://springdoc.org")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

##### Step 3: Documenting Controllers & DTOs
```java
package com.enterprise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders API", description = "Endpoints for creating and retrieving customer orders")
public class OrderDocumentationController {

    @Operation(summary = "Get Order by ID", description = "Retrieves complete order details along with shipping status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found and returned successfully"),
        @ApiResponse(responseCode = "404", description = "Order with specified ID not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid JWT token")
    })
    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok("Order details for #" + id);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** Frontend and mobile development teams were repeatedly delayed waiting for backend developers to manually update static Postman collections. Integrating `springdoc-openapi` automated documentation generation directly from the Java code on every build, allowing client teams to test live endpoints on staging and generate client SDKs automatically via OpenAPI Generator.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** disable Swagger UI in production environments if security policies mandate internal-only API documentation (`springdoc.swagger-ui.enabled=false`).
- ✅ **DO** document authentication mechanisms (e.g., Bearer JWT) in the `OpenAPI` `@Bean` configuration so developers can authenticate directly inside Swagger UI.

---

### Q60. How to implement validation using @Valid and @NotNull annotations?

#### 1. Concept & Interview Answer
- In Spring Boot, validation is powered by **Jakarta Bean Validation** (Hibernate Validator) via `spring-boot-starter-validation`.
- Adding constraint annotations on DTO fields:
  - **`@NotNull`**: Ensures value is not `null` (allows empty strings `""`).
  - **`@NotEmpty`**: Ensures value is not `null` and size > 0 (for Strings, Collections, Arrays).
  - **`@NotBlank`**: Ensures string is not `null`, not empty, and contains at least one non-whitespace character.
  - **`@Min` / `@Max` / `@Positive`**: Numeric boundary constraints.
  - **`@Email` / `@Pattern`**: String format verification.
- **`@Valid`** (Jakarta) or **`@Validated`** (Spring): Placed on `@RequestBody` arguments to trigger validation at the controller boundary.

#### 2. Validation Constraints Comparison Matrix
| Annotation | `null` Allowed? | `""` (Empty String) Allowed? | `"   "` (Whitespace) Allowed? | Applicable Types |
| :--- | :--- | :--- | :--- | :--- |
| **`@NotNull`** | ❌ No | ✅ Yes | ✅ Yes | Any Object Type |
| **`@NotEmpty`** | ❌ No | ❌ No | ✅ Yes | `CharSequence`, `Collection`, `Map`, `Array` |
| **`@NotBlank`** | ❌ No | ❌ No | ❌ No | `CharSequence` (Strings only) |

#### 3. Production Code Example (Custom Validator & Nested Validation)
```java
package com.enterprise.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateOrderDto(
    @NotBlank(message = "Order title is mandatory")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title,

    @NotNull(message = "Total amount is mandatory")
    @Positive(message = "Amount must be greater than zero")
    BigDecimal totalAmount,

    @NotEmpty(message = "Order must contain at least one line item")
    @Valid // CRITICAL: Enables cascading validation for nested objects inside collection!
    List<OrderItemDto> items
) {}

record OrderItemDto(
    @NotBlank(message = "SKU cannot be blank") String sku,
    @Min(value = 1, message = "Quantity must be at least 1") int quantity
) {}
```

```java
// Controller endpoint triggering validation
@RestController
@RequestMapping("/api/v1/orders")
public class OrderValidationController {

    @PostMapping
    public ResponseEntity<Void> placeOrder(@Valid @RequestBody CreateOrderDto request) {
        orderService.process(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** Orders were being created with empty product items because developers omitted `@Valid` on the nested `List<OrderItemDto> items` collection. Adding `@Valid` before the nested list enabled cascading validation, instantly blocking orders containing invalid SKUs or negative quantities.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `@NotBlank` for required string fields instead of `@NotNull`.
- ✅ **DO** remember to add `@Valid` on nested objects or collections within a DTO to trigger cascading validation.
- ❌ **DON'T** perform heavy database lookups inside custom Bean Validators; keep validation fast and perform database state verification in the `@Service` layer.

---

### Q61. How do you manage different profiles in Spring Boot?

#### 1. Concept & Interview Answer
- **Spring Profiles** provide a way to segregate parts of your application configuration and make it available only in specific environments (`dev`, `test`, `stage`, `prod`).
- **How to activate profiles:**
  1. **In `application.yml`:** `spring.profiles.active: dev`.
  2. **Via Command Line:** `java -jar app.jar --spring.profiles.active=prod`.
  3. **Via Environment Variable:** `export SPRING_PROFILES_ACTIVE=prod`.
  4. **Via JVM System Property:** `-Dspring.profiles.active=prod`.
- **Conditional Beans:** Beans can be conditionally loaded using the **`@Profile("dev")`** or `@Profile("!prod")` annotation.

#### 2. Multi-Profile Loading Architecture
```
                     [ SPRING_PROFILES_ACTIVE=prod ]
                                   │
                                   ▼
        ┌──────────────────────────┴──────────────────────────┐
        ▼                                                     ▼
[ application.yml ] (Base Shared Config)          [ application-prod.yml ] (Prod Overrides)
├── server.port: 8080                             ├── datasource.url: prod-aurora
└── app.name: order-service                       └── hikari.max-pool-size: 50
```

#### 3. Production Code Example (Profile-Specific Beans)
```java
package com.enterprise.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}

// 1. Mock Implementation for Local Development & Testing
@Service
@Profile({"dev", "test"})
public class MockEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("[MOCK EMAIL] To: {} | Subject: {} | Body: {}", to, subject, body);
    }
}

// 2. Real AWS SES Implementation for Production & Staging
@Service
@Profile({"stage", "prod"})
public class AwsSesEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(AwsSesEmailService.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("Dispatching email via AWS SES API to: {}", to);
        // Actual AWS SES client call...
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During development testing, a batch notification job sent 5,000 real SMS messages to actual customers because the production SMS gateway bean was active locally. Using `@Profile("dev")` with a mock SMS bean for development and `@Profile("prod")` for live environments eliminated accidental third-party API invocations.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** maintain base shared properties in `application.yml` and isolate only environment-specific overrides into `application-{profile}.yml`.
- ✅ **DO** use profile expressions like `@Profile("prod & cloud")` or `@Profile("!dev")` for fine-grained bean activation.
- ❌ **DON'T** store sensitive production passwords in `application-prod.yml`; inject them via environment variables at container runtime.

---

### Q62. How do you secure a REST API using Spring Security?

#### 1. Concept & Interview Answer
Securing a Spring Boot REST API involves:
1. **Stateless Session Management:** Configuring `SessionCreationPolicy.STATELESS` to prevent server-side `HttpSession` creation.
2. **Disabling CSRF:** CSRF attacks rely on browser cookie authentication; stateless JWT token APIs do not require CSRF protection.
3. **JWT Authentication Filter:** A custom filter intercepting incoming requests, extracting the `Authorization: Bearer <token>` header, validating the signature, and populating `SecurityContextHolder`.
4. **URL Authorization Rules:** Restricting URL endpoints using `.requestMatchers().hasRole()`.
5. **Method Security:** Enabling `@EnableMethodSecurity` for granular `@PreAuthorize` checks.

#### 2. JWT Filter Chain Architecture
```
[ Client HTTP Request ] ──► Header: Authorization: Bearer eyJhbGci...
                                     │
                                     ▼
                      [ JwtAuthenticationFilter ]
                                     │
                                     ├──► Validates JWT Signature & Expiration
                                     ├──► Extracts Username & Roles (Claims)
                                     │
                                     ▼
                   [ SecurityContextHolder.getContext().setAuthentication(auth) ]
                                     │
                                     ▼
                      [ AuthorizationFilter ] ──► Checks URL / Role permissions
                                     │
                                     ▼
                      [ @RestController (Protected Resource) ]
```

#### 3. Production Code Example (Spring Security 6.x / Spring Boot 3.x)
```java
package com.enterprise.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class RestSecurityConfig {

    private final JwtTokenService jwtTokenService;

    public RestSecurityConfig(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disabled for stateless REST APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/actuator/health/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

```java
// Custom OncePerRequestFilter for JWT Verification
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenService.validateToken(token)) {
                String username = jwtTokenService.extractUsername(token);
                List<SimpleGrantedAuthority> authorities = jwtTokenService.extractRoles(token);

                var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A microservice cluster experienced high memory usage because Spring Security was creating an in-memory `HttpSession` for every REST API call. Setting `sessionCreationPolicy(SessionCreationPolicy.STATELESS)` stopped session creation, saving 2GB of RAM per server instance and ensuring seamless horizontal scaling.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** extend `OncePerRequestFilter` for custom authentication filters to guarantee the filter runs exactly once per request dispatch.
- ✅ **DO** ensure the `SecurityContext` is cleared automatically at the end of the request (handled by Spring Security's filter chain).

---

### Q63. What is the difference between synchronous and asynchronous REST endpoints?

#### 1. Concept & Interview Answer
- **Synchronous REST Endpoints (Standard):**
  - The HTTP request thread (from Tomcat's thread pool) is **blocked** waiting for the controller, service, and database operations to finish before sending the response.
  - Simple, but if backend operations take 5 seconds, the Tomcat worker thread cannot serve any other incoming requests, leading to **thread pool starvation**.
- **Asynchronous REST Endpoints:**
  - The worker thread initiates background processing and is immediately **released back to the servlet container thread pool** to serve other requests.
  - Implemented using **`CompletableFuture<T>`**, **`DeferredResult<T>`**, or Spring WebFlux reactive streams (`Mono<T>` / `Flux<T>`).

#### 2. Thread Execution Comparison Diagram
```
SYNCHRONOUS REQUEST:
Tomcat Thread-1: [ Accept Request ──► Wait DB ──► Wait External API ──► Send Response ] (Thread blocked for 5s!)

ASYNCHRONOUS REQUEST (CompletableFuture / DeferredResult):
Tomcat Thread-1: [ Accept Request ──► Dispatch to Worker Pool ] ──► (Thread-1 Released in 2ms!)
                                             │
WorkerPool-Thread-5:                         └──► [ Execute Long Task ] ──► [ Send HTTP 200 ]
```

#### 3. Production Code Example
```java
package com.enterprise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/reports")
public class AsyncReportController {

    private final HeavyReportService reportService;

    public AsyncReportController(HeavyReportService reportService) {
        this.reportService = reportService;
    }

    // Asynchronous Endpoint returning CompletableFuture
    @GetMapping("/generate/{userId}")
    public CompletableFuture<ResponseEntity<String>> generateReportAsync(@PathVariable Long userId) {
        return reportService.generatePdfReport(userId)
                .thenApply(pdfPath -> ResponseEntity.ok("Report generated at: " + pdfPath))
                .exceptionally(ex -> ResponseEntity.internalServerError().body("Failed: " + ex.getMessage()));
    }
}
```

```java
@Service
public class HeavyReportService {

    @Async("reportTaskExecutor") // Runs on dedicated custom thread pool
    public CompletableFuture<String> generatePdfReport(Long userId) {
        // Simulate long-running generation (3 seconds)
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
        return CompletableFuture.completedFuture("/storage/reports/report_" + userId + ".pdf");
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** During month-end tax report generation, 150 concurrent users requested PDF downloads simultaneously. Because the endpoints were synchronous and took 8 seconds each, all 200 Tomcat worker threads were exhausted within 5 seconds, causing HTTP 504 Gateway Timeouts for regular login and checkout requests. Converting report generation to asynchronous endpoints backed by a separate `reportTaskExecutor` thread pool completely protected the main web server threads from starvation.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use dedicated custom `ThreadPoolTaskExecutor` beans for `@Async` tasks instead of Spring's default `SimpleAsyncTaskExecutor` (which spawns unpooled threads).
- ✅ **DO** use asynchronous endpoints or event-driven background queues (RabbitMQ/Kafka) for any operation taking longer than 1 second.

---

### Q64. How do you handle file uploads and downloads in Spring Boot REST?

#### 1. Concept & Interview Answer
- **File Uploads:** Handled using `MultipartFile` mapped to a `@RequestParam("file")` with `consumes = MediaType.MULTIPART_FORM_DATA_VALUE`.
- **File Downloads:** Handled by returning a `ResponseEntity<Resource>` (e.g., `ByteArrayResource`, `InputStreamResource`, `FileSystemResource`) with appropriate HTTP headers:
  - `Content-Disposition: attachment; filename="document.pdf"` (Forces browser download).
  - `Content-Type: application/pdf` (MIME type).
  - `Content-Length`: Size in bytes (enables download progress bars).

#### 2. Production Code Example (Streaming File Download)
```java
package com.enterprise.file;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
public class FileApiController {

    private final Path storageDir = Paths.get("/var/enterprise/storage");

    // 1. Upload File Endpoint
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileKey = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destination = storageDir.resolve(fileKey).toFile();
        file.transferTo(destination);
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded: " + fileKey);
    }

    // 2. Download File Endpoint (Streaming without loading entire file to RAM)
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) throws FileNotFoundException {
        File file = storageDir.resolve(fileName).toFile();
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A document retrieval endpoint loaded 500MB video files into memory using `Files.readAllBytes(path)` before returning them in `ResponseEntity.ok(bytes)`. When multiple users downloaded files simultaneously, the JVM crashed with `java.lang.OutOfMemoryError: Java heap space`. Switching to `InputStreamResource` streamed the bytes directly from disk to the network socket, maintaining steady 50MB JVM heap usage regardless of file size.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** stream files using `InputStreamResource` or `UrlResource` to avoid loading massive files into heap memory.
- ✅ **DO** sanitize file names to prevent directory traversal attacks (`Path.normalize()`).

---

### Q65. How do you consume REST APIs using RestTemplate or WebClient?

#### 1. Concept & Interview Answer
Spring provides two primary HTTP client abstractions:
1. **`RestTemplate` (Legacy / Maintenance Mode):**
   - Synchronous, blocking HTTP client built on Java Servlet API.
   - Simple and familiar, but blocks the executing thread until the remote server responds.
2. **`WebClient` (Modern / Recommended):**
   - Introduced in Spring 5 (`spring-boot-starter-webflux`), it is a non-blocking, reactive HTTP client.
   - Supports both **synchronous (blocking via `.block()`)** and **asynchronous (reactive via `Mono`/`Flux`)** modes.
   - Provides significantly higher throughput and lower resource utilization under high concurrency.
3. **`RestClient` (Spring 6 / Spring Boot 3.2+):**
   - A modern synchronous, fluent client that offers the clean API of `WebClient` on top of traditional blocking HTTP libraries.

#### 2. RestTemplate vs WebClient vs RestClient Comparison
| Feature | `RestTemplate` | `WebClient` | `RestClient` (Spring 3.2+) |
| :--- | :--- | :--- | :--- |
| **Execution Model** | Blocking / Synchronous | Non-blocking / Reactive / Sync | Blocking / Fluent API |
| **Status** | Maintenance Mode | Active Standard | Active Standard |
| **Streaming Support** | No | Yes (Reactive Streams) | No |
| **Dependency** | `spring-web` | `spring-boot-starter-webflux` | `spring-web` |

#### 3. Production Code Example

##### Option 1: Modern `WebClient` (Async & Non-blocking)
```java
package com.enterprise.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class PaymentGatewayClient {

    private final WebClient webClient;

    public PaymentGatewayClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.stripe.com/v1")
                .defaultHeader("Authorization", "Bearer sk_test_secret")
                .build();
    }

    public record ChargeRequest(int amount, String currency) {}
    public record ChargeResponse(String id, String status) {}

    // Non-blocking Reactive Call
    public Mono<ChargeResponse> createChargeReactive(ChargeRequest request) {
        return webClient.post()
                .uri("/charges")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChargeResponse.class)
                .timeout(Duration.ofSeconds(3));
    }

    // Synchronous Blocking Call (When required in standard Spring MVC)
    public ChargeResponse createChargeBlocking(ChargeRequest request) {
        return createChargeReactive(request).block(); // Safe blocking if inside synchronous service
    }
}
```

##### Option 2: Spring Boot 3.2+ Modern `RestClient`
```java
package com.enterprise.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderServiceClient {

    private final RestClient restClient;

    public OrderServiceClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://orders.internal.enterprise.com/api").build();
    }

    public OrderDto getOrder(Long orderId) {
        return restClient.get()
                .uri("/orders/{id}", orderId)
                .retrieve()
                .body(OrderDto.class);
    }

    public record OrderDto(Long id, Double amount) {}
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An aggregation microservice called 5 downstream third-party REST APIs using `RestTemplate` sequentially (5 * 400ms = 2,000ms latency). Refactoring to `WebClient` and triggering the 5 HTTP requests concurrently using `Mono.zip()` reduced total endpoint latency from 2.0 seconds to 420 milliseconds.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer `WebClient` (for async/reactive needs) or `RestClient` (for synchronous fluent needs) over legacy `RestTemplate`.
- ✅ **DO** always configure explicit connection timeouts and read timeouts on your HTTP client builders to prevent downstream outages from cascading into your service.

---



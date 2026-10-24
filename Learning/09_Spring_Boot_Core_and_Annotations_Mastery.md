# Chapter 9: Spring Boot Core, Dependency Injection & Annotation Architecture

This master guide covers the real-world Spring Boot interview questions asked across **Paramatrix, C-EDGE, Zycus, Team Computers, In Solutions Global, Servosys, Rebit, and Nimap Infotech**.

---

## 📑 Table of Contents
1. [Core Java Entry Point: Overriding & Overloading the `main` Method](#1-core-java-entry-point-overriding--overloading-the-main-method)
2. [Spring IoC Container & Dependency Injection Internals](#2-spring-ioc-container--dependency-injection-internals)
3. [@Component vs @Bean vs Stereotype Annotations](#3-component-vs-bean-vs-stereotype-annotations)
4. [@Autowired Internal Mechanics & Why Constructor Injection Wins](#4-autowired-internal-mechanics--why-constructor-injection-wins)
5. [Bean Scopes, Prototype in Singleton & The Lookup Method Problem](#5-bean-scopes-prototype-in-singleton--the-lookup-method-problem)
6. [@Controller vs @RestController & View Resolvers](#6-controller-vs-restcontroller--view-resolvers)
7. [Global Exception Handling: @ControllerAdvice & @ExceptionHandler](#7-global-exception-handling-controlleradvice--exceptionhandler)
8. [@Transactional Mechanics, Propagation & Partial Rollback](#8-transactional-mechanics-propagation--partial-rollback)
9. [Configuring Multiple DataSources in One Spring Boot Application](#9-configuring-multiple-datasources-in-one-spring-boot-application)
10. [Centralized Configuration: Git-backed Spring Cloud Config vs application.properties](#10-centralized-configuration-git-backed-spring-cloud-config-vs-applicationproperties)

---

## 1. Core Java Entry Point: Overriding & Overloading the `main` Method

### 1. Previous Knowledge Required
- **Static vs Instance Methods**: Static methods belong to the class footprint (`Metaspace`), not to individual instances on the Heap.
- **Dynamic Method Dispatch (Runtime Polymorphism)**: Method overriding requires an object instance at runtime so the JVM virtual method table (`vtable`) can resolve the child method dynamically.

### 2. Logic & Theory (Deep-Dive)
- **Can we override the `main` method in Java?**
  **NO, we cannot override the `main` method.** The standard signature is `public static void main(String[] args)`. Because it is declared `static`, it belongs to the class definition. When a subclass declares an identical static method, it is **Method Hiding**, not method overriding. The method called is determined strictly at compile time based on the reference type, never through dynamic runtime dispatch.
- **Can we overload the `main` method in Java?**
  **YES, absolutely.** You can define multiple `main` methods in the same class with different parameter types (e.g., `main(int x)`, `main(String[] args, int y)`). However, the JVM launcher (`java.exe` / JVM execution engine) specifically searches for the precise signature `public static void main(String[] args)` as the process entry point. The overloaded versions must be called explicitly from your code.

### 3. How to Answer in an Interview
> *"We cannot override the `main` method because it is static. In Java, static methods are resolved at compile time using static binding (Class footprint) rather than runtime polymorphism. If a child class creates a main method with the same signature, it simply shadows or hides the parent's static method.*
>
> *However, we can easily overload the `main` method by changing the parameter list. The JVM will only invoke `public static void main(String[] args)` when launching the application, but our code can call the overloaded methods anytime."*

### 4. Practical Code Example
```java
public class ApplicationLauncher {

    // JVM Entry point
    public static void main(String[] args) {
        System.out.println("Standard JVM main method executed.");
        // Explicitly calling overloaded versions
        main(42);
        main("LoanOrigination", true);
    }

    // Overloaded main method #1
    public static void main(int exitCode) {
        System.out.println("Overloaded main(int) called with: " + exitCode);
    }

    // Overloaded main method #2
    public static void main(String moduleName, boolean isSecure) {
        System.out.println("Overloaded main(String, boolean) -> " + moduleName + ", Secure=" + isSecure);
    }
}

class ChildLauncher extends ApplicationLauncher {
    // This is METHOD HIDING, NOT overriding!
    public static void main(String[] args) {
        System.out.println("Child class method hiding parent main.");
    }
}
```

### 5. Real-World Project Application
- In enterprise frameworks like Spring Boot (`@SpringBootApplication`), the `main` method delegates to `SpringApplication.run(Application.class, args)`. 
- Overloading is useful in CLI maintenance utilities where a diagnostic script can be run with custom programmatic inputs during automated CI/CD integration tests without command-line strings.

---

## 2. Spring IoC Container & Dependency Injection Internals

### 1. Previous Knowledge Required
- **Tight Coupling**: Direct object instantiation using `new ServiceImpl()` inside callers binds the classes tightly, making unit testing and swapping implementations impossible.
- **Inversion of Control (IoC)**: The principle where the control of object creation, lifecycle, and dependency wiring is inverted and delegated to an external container/framework.

### 2. Logic & Theory (Deep-Dive)
- **Why do we need IoC if we can manually instantiate objects?**
  1. **Lifecycle Management**: The container handles singletons, destruction hooks, graceful shutdowns, and proxy creation (transactions, security, caching).
  2. **Decoupled Architecture & Testability**: With DI, you inject mock dependencies (`Mockito.mock()`) during testing without touching business logic.
  3. **Cross-Cutting Concerns (AOP Proxying)**: When Spring manages a bean, it doesn't just hold the raw object; it wraps it in a dynamic CGLIB or JDK dynamic proxy to intercept calls for `@Transactional`, `@Async`, and `@Cacheable`. You cannot manually do this with a raw `new` keyword without re-writing a proxy engine.

```
+-----------------------------------------------------------+
|                   Spring IoC Container                    |
|                                                           |
|  1. Component Scanning (@ComponentScan / ClasspathSearch) |
|  2. BeanDefinition Creation (Metadata, Scope, Lazy/Eager) |
|  3. BeanFactoryPostProcessors (e.g. Property Resolvers)   |
|  4. Instantiation (Reflection / CGLIB Constructor)       |
|  5. Dependency Injection (Population of fields/setters)   |
|  6. BeanPostProcessors (AOP Proxies, @Transactional)      |
|  7. Initialization (@PostConstruct, InitializingBean)     |
|                                                           |
|                 Ready Bean in ApplicationContext          |
+-----------------------------------------------------------+
```

### 3. How to Answer in an Interview
> *"While we can manually create objects with the `new` keyword, doing so forces tight coupling, breaks the Single Responsibility Principle, and makes mocking during unit testing painful.*
>
> *More critically, the Spring IoC container does far more than object creation: it manages the complete bean lifecycle, resolves complex circular and transitive dependencies, and dynamically weaves AOP proxies around beans to provide declarative transactions (`@Transactional`), security checks, and distributed tracing. If you instantiate an object manually using `new`, none of Spring's proxies, caching, or transactional boundaries will ever be triggered."*

### 4. Practical Code Example
```java
// Anti-Pattern: Tight Coupling
public class OrderServiceManual {
    private PaymentService paymentService = new StripePaymentService(); // Hardcoded! Can't test with Mock
}

// Production Best Practice: Inversion of Control via Constructor Injection
public interface PaymentGateway {
    void processPayment(BigDecimal amount);
}

@Service
public class OrderService {
    private final PaymentGateway paymentGateway;

    // IoC Container injects whatever implementation is registered (Stripe, Razorpay, Mock)
    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void checkout(BigDecimal amount) {
        paymentGateway.processPayment(amount);
    }
}
```

### 5. Real-World Project Application
- In a **Banking Loan Origination System (LOS)**, the `LoanDisbursementService` depends on `CreditScoreClient`, `AadhaarKycValidator`, and `NotificationService`. 
- By using IoC, during local testing we inject mock KYC validators that return dummy verified statuses, while in production Spring automatically injects the live government API clients.

---

## 3. @Component vs @Bean vs Stereotype Annotations

### 1. Previous Knowledge Required
- Classpath scanning mechanism (`@ComponentScan`).
- 3rd-party libraries where source code is compiled into JARs and cannot be annotated.

### 2. Logic & Theory (Deep-Dive)

| Feature | `@Component` | `@Bean` |
| :--- | :--- | :--- |
| **Target Level** | Class level (`@Target(ElementType.TYPE)`) | Method level (`@Target(ElementType.METHOD)`) |
| **Discovery** | Auto-detected via classpath scanning | Explicitly defined inside a `@Configuration` class |
| **Custom Code Ownership** | Used for your own custom project code | Used for 3rd-party library classes (e.g., `RestTemplate`, `ObjectMapper`, `ModelMapper`) |
| **Custom Initialization** | Standard constructors / `@PostConstruct` | Full programmatic logic before returning the instance |

#### Stereotype Annotations Hierarchy
All specialized stereotypes are meta-annotated with `@Component`:
- `@Repository`: Encapsulates database operations; automatically translates vendor-specific SQL exceptions into Spring's unified `DataAccessException` hierarchy.
- `@Service`: Marks business logic layer. Holds domain transactions.
- `@Controller`: Handles incoming HTTP web requests, returns Model & View names.
- `@RestController`: Meta-annotated with `@Controller` and `@ResponseBody`. Converts Java return objects into JSON/XML via `HttpMessageConverter`.

### 3. How to Answer in an Interview
> *"The fundamental difference between `@Component` and `@Bean` is ownership and declaration scope. We use `@Component` on our own source code classes so that Spring's auto-scanning mechanism detects and registers them automatically.*
>
> *We use `@Bean` on methods inside `@Configuration` classes when we need to instantiate and configure classes from third-party libraries where we cannot edit the class code to add `@Component`—for example, creating a custom `RestTemplate`, `ObjectMapper`, or Redis connection factory."*

### 4. Practical Code Example
```java
// 1. @Component on your own classes
@Service // Specialization of @Component for business logic
public class KycServiceImpl implements KycService {
    public boolean verifyAadhaar(String aadhaarNumber) {
        return aadhaarNumber != null && aadhaarNumber.length() == 12;
    }
}

// 2. @Bean for 3rd party classes inside a @Configuration class
@Configuration
public class ThirdPartyConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3 seconds timeout
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
```

### 5. Real-World Project Application
- In **payment gateway integrations (Razorpay / PayU)**, the SDK provides an external client class `RazorpayClient`. You cannot add `@Component` to their JAR file. 
- You define a `@Configuration` class with a `@Bean` method reading API keys from `application.yml` to return a singleton `RazorpayClient`.

---

## 4. @Autowired Internal Mechanics & Why Constructor Injection Wins

### 1. Previous Knowledge Required
- Java Reflection API (`Class.getDeclaredFields()`, `field.setAccessible(true)`).
- The three injection types: **Field Injection**, **Setter Injection**, **Constructor Injection**.

### 2. Logic & Theory (Deep-Dive)

#### How `@Autowired` Works Internally:
1. Spring uses `AutowiredAnnotationBeanPostProcessor`, a specialized `BeanPostProcessor`.
2. During the `postProcessProperties()` phase of bean instantiation:
   - Spring inspects the class metadata for fields, setters, or constructors annotated with `@Autowired` or `@Value`.
   - It searches `ApplicationContext` for candidate beans matching the target **Type** (`byType`).
   - If multiple candidates match the type, it attempts to resolve by **Name** (`byName`).
   - If ambiguity remains, it looks for `@Primary` or `@Qualifier("beanName")`.
   - For field injection, it uses reflection: `field.setAccessible(true); field.set(targetBean, injectedBean);`.

#### Why Constructor Injection is the Industry Standard:
1. **Immutability**: Dependencies can be declared `final`, preventing accidental reassignment.
2. **Prevents `NullPointerException` in Unit Tests**: Objects cannot be instantiated in a broken, half-initialized state. With field injection, `new OrderService()` compiles fine but crashes at runtime with an NPE because fields are null.
3. **Circular Dependency Detection at Startup**: Constructor injection forces Spring to fail fast on circular dependencies at boot time rather than masking them.
4. **No Reflection Overhead**: Plain Java constructor calls; Spring 4.3+ doesn't even require an explicit `@Autowired` if there is only one constructor.

### 3. How to Answer in an Interview
> *"Internally, `@Autowired` is handled by Spring's `AutowiredAnnotationBeanPostProcessor`. It uses reflection to scan fields, setters, or constructors, searches the ApplicationContext by type first, and breaks ties by bean name or `@Qualifier`.*
>
> *Constructor injection is strongly preferred over field injection for four critical production reasons: First, it allows dependencies to be `final` and immutable. Second, it prevents NullPointerExceptions in unit tests because you cannot instantiate the class without supplying its dependencies. Third, it eliminates hidden coupling and keeps dependencies explicit. Fourth, it allows the JVM to fail fast during startup if there are circular dependencies."*

### 4. Practical Code Example
```java
// ❌ Flawed Approach: Field Injection
@Service
public class BadCustomerService {
    @Autowired
    private CustomerRepository repo; // Cannot be final; NPE in unit tests without Spring runner
}

// ✅ Production Standard: Constructor Injection (Lombok or Explicit)
@Service
public class GoodCustomerService {

    private final CustomerRepository customerRepository;
    private final NotificationService notificationService;

    // Single constructor: @Autowired is implicit in Spring 4.3+
    public GoodCustomerService(CustomerRepository customerRepository, 
                               NotificationService notificationService) {
        this.customerRepository = Objects.requireNonNull(customerRepository, "Repo cannot be null");
        this.notificationService = Objects.requireNonNull(notificationService, "Notification cannot be null");
    }

    public void registerCustomer(String name, String email) {
        Customer c = customerRepository.save(new Customer(name, email));
        notificationService.sendWelcomeEmail(c.getEmail());
    }
}
```

### 5. Real-World Project Application
- In enterprise applications adhering to **SonarQube clean code rules**, field injection is flagged as a code smell (Rule: *S3749 - Members of Spring components should be injected via constructor*).

---

## 5. Bean Scopes, Prototype in Singleton & The Lookup Method Problem

### 1. Previous Knowledge Required
- **Singleton**: Exactly one instance per Spring `ApplicationContext`.
- **Prototype**: A brand-new instance created every time the bean is requested.
- **Web Scopes**: `request` (one per HTTP request), `session` (one per user session), `application` (one per ServletContext).

### 2. Logic & Theory (Deep-Dive)

#### The Classic Interview Trap: Prototype Inside Singleton
- Suppose `OrderService` is a `@Scope("singleton")`, and it injects `InvoiceTokenGenerator` which is `@Scope("prototype")`.
- **What happens?** The prototype bean is injected **only once** when the singleton bean is instantiated at application startup. Subsequent calls to `orderService.generateInvoice()` will reuse the exact same initial instance, completely defeating the prototype scope!

#### Solutions to the Problem:
1. **`@Lookup` Annotation**: Instructs Spring CGLIB to dynamically subclass the singleton and override the annotated lookup method to fetch a new prototype bean from the container on every call.
2. **`ObjectFactory<T>` / `Provider<T>`**: Injects a factory that calls `getObject()` lazily.

### 3. How to Answer in an Interview
> *"By default, Spring beans are Singletons. When you inject a Prototype bean into a Singleton bean, the injection occurs only once during the singleton's initialization. As a result, the singleton continues using that same stale prototype instance.*
>
> *To solve this cleanly, we use Spring's `@Lookup` method injection or `ObjectProvider<T>`. With `@Lookup`, Spring CGLIB dynamically generates a subclass at runtime and overrides the lookup method to retrieve a fresh prototype instance from the ApplicationContext on each invocation."*

### 4. Practical Code Example
```java
// Prototype Bean
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionToken {
    private final String tokenId = UUID.randomUUID().toString();
    private final Instant createdAt = Instant.now();

    public String getTokenId() { return tokenId; }
}

// Singleton Bean using @Lookup
@Service
public class PaymentProcessingService {

    public String processPayment() {
        TransactionToken freshToken = getTransactionToken(); // New token on EVERY call!
        return "Processing with token: " + freshToken.getTokenId();
    }

    // Spring CGLIB dynamically overrides this method at runtime
    @Lookup
    public TransactionToken getTransactionToken() {
        return null; // Implementation supplied by Spring proxy
    }
}
```

---

## 6. @Controller vs @RestController & View Resolvers

### 1. Previous Knowledge Required
- Traditional MVC (Model-View-Controller) architecture returning HTML/JSP pages vs REST APIs returning raw JSON payloads.
- `DispatcherServlet` request lifecycle.

### 2. Logic & Theory (Deep-Dive)

```
                       HTTP Request
                            |
                            v
                   [DispatcherServlet]
                      /           \
         @Controller /             \ @RestController
                    v               v
           Returns "index"       Returns CustomerDTO
             (View Name)            (Data Object)
                  |                       |
                  v                       v
          [ViewResolver]          [HttpMessageConverter]
       InternalResourceViewResolver (MappingJackson2HttpMessageConverter)
                  |                       |
                  v                       v
           Renders index.jsp        Serializes to JSON
```

- `@Controller`: Used for server-side rendering (JSP, Thymeleaf). The returned `String` is treated by the `ViewResolver` (e.g., `InternalResourceViewResolver`) as a view name to locate `/WEB-INF/views/index.jsp`.
- `@RestController`: Equivalent to `@Controller + @ResponseBody`. Bypasses the ViewResolver completely. Return values are serialized directly into HTTP response bodies via `HttpMessageConverter` (Jackson for JSON).

### 3. How to Answer in an Interview
> *"The key difference lies in response handling. A `@Controller` is meant for traditional MVC web applications where methods return a View name (String) that a ViewResolver maps to a physical template like a JSP or Thymeleaf file.*
>
> *A `@RestController` is a convenience annotation combining `@Controller` and `@ResponseBody`. It indicates that the return value of each method is not a view name, but data written directly to the HTTP response body serialized into JSON or XML by Jackson converters."*

### 4. Practical Code Example
```java
// 1. Traditional MVC Controller returning JSP
@Controller
@RequestMapping("/home")
public class WebPageController {
    @GetMapping
    public String renderHomePage(Model model) {
        model.addAttribute("username", "Saikiran");
        return "dashboard"; // ViewResolver resolves to /WEB-INF/views/dashboard.jsp
    }
}

// 2. REST API Controller returning JSON
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiController {

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        CustomerResponse response = new CustomerResponse(id, "Rahul Sharma", "ACTIVE");
        return ResponseEntity.ok(response); // Serialized as {"id":1,"name":"Rahul Sharma","status":"ACTIVE"}
    }
}
```

---

## 7. Global Exception Handling: @ControllerAdvice & @ExceptionHandler

### 1. Previous Knowledge Required
- HTTP status codes (400 Bad Request, 404 Not Found, 409 Conflict, 500 Internal Server Error).
- Checked vs Unchecked Exceptions.

### 2. Logic & Theory (Deep-Dive)
- Without global exception handling, exceptions bubble up to the servlet container and produce ugly stack traces or default Tomcat HTML error pages.
- `@ExceptionHandler`: Declared inside a controller to catch specific exceptions thrown by methods in that specific controller.
- `@ControllerAdvice` / `@RestControllerAdvice`: An application-wide interceptor using Spring AOP. It intercepts exceptions thrown across **all** `@RestController` classes, transforms them into a structured JSON error response (`ErrorResponseDTO`), and sets the proper HTTP status code.

### 3. How to Answer in an Interview
> *"In Spring Boot, we handle exceptions globally using `@RestControllerAdvice` combined with `@ExceptionHandler` methods. `@RestControllerAdvice` acts as an AOP interceptor around all controllers.*
>
> *When an exception is thrown anywhere in the controller or service layer, the advice catches it, maps it to a standard enterprise JSON schema with a timestamp, error code, and message, and returns a clean `ResponseEntity` with the corresponding HTTP status code (e.g., 404 for ResourceNotFound, 409 for duplicate conflicts)."*

### 4. Practical Code Example
```java
// 1. Custom Business Exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// 2. Standardized Enterprise Error Response DTO
public record ApiErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {}

// 3. Centralized Global Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please contact support.",
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### 5. Real-World Project Application
- In **Banking APIs**, PCI-DSS compliance strictly forbids returning internal stack traces, SQL error strings, or table names to the frontend. A `@RestControllerAdvice` sanitizes all errors into clean, secure error responses.

---

## 8. @Transactional Mechanics, Propagation & Partial Rollback

### 1. Previous Knowledge Required
- ACID properties (Atomicity, Consistency, Isolation, Durability).
- Spring AOP Dynamic Proxies (JDK dynamic proxy vs CGLIB).

### 2. Logic & Theory (Deep-Dive)

#### Internal Working of `@Transactional`:
1. When Spring initializes, `TransactionInterceptor` creates an AOP proxy around the `@Transactional` class.
2. When an external call invokes the method:
   - The proxy obtains a database connection from the `DataSourceTransactionManager`.
   - It sets `connection.setAutoCommit(false)`.
   - It invokes the target business method.
   - If the method completes normally, the proxy calls `connection.commit()`.
   - If an unhandled `RuntimeException` or `Error` occurs, the proxy calls `connection.rollback()`.
   - **Crucial Rule**: By default, Spring **does NOT roll back on checked exceptions** (`Exception.class`) unless explicitly configured with `@Transactional(rollbackFor = Exception.class)`.

#### Common Interview Traps:
- **Self-Invocation Trap**: If method `A()` calls method `B()` in the same class, and `B()` has `@Transactional(propagation = Propagation.REQUIRES_NEW)`, the new transaction is **IGNORED**. Why? Because the call is made internally via `this.B()`, bypassing the Spring CGLIB proxy!
- **Handling Partial Failures (Question from Team Computers)**:
  *"I want to perform multiple operations where one step might fail, but I want previous database operations to remain committed. How do I do this?"*
  - **Solution**: Use `Propagation.REQUIRES_NEW` on the secondary operation inside a **separate service class**, and wrap the call in a `try-catch` block in the parent service so the outer transaction can commit even if the nested one rolls back.

```
Outer Service (REQUIRED) ---> starts Tx 1
   |
   |---> SubService.auditLog() (REQUIRES_NEW) ---> suspends Tx 1, starts Tx 2
   |        |
   |        +---> commits or rolls back Tx 2, resumes Tx 1
   |
   +---> commits Tx 1
```

### 3. How to Answer in an Interview
> *"Under the hood, `@Transactional` uses Spring AOP proxies. The proxy intercepts the method call, borrows a database connection, turns off auto-commit, runs the target method, and commits or rolls back based on whether an unhandled RuntimeException was thrown.*
>
> *For partial transactions where step 1 must succeed even if step 2 fails, we isolate step 2 into a separate Spring bean with `Propagation.REQUIRES_NEW`. This suspends the outer transaction and starts an independent transaction. In the caller method, we wrap that call in a try-catch to absorb the exception so the primary transaction can safely commit."*

### 4. Practical Code Example
```java
@Service
public class AuditLogService {

    // Must be in a SEPARATE bean so Spring proxy intercepts it!
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeAuditLog(String action, Long userId) {
        // This transaction will commit/rollback independently of the caller
        auditRepository.save(new AuditRecord(action, userId, Instant.now()));
    }
}

@Service
public class LoanProcessingService {

    private final LoanRepository loanRepository;
    private final AuditLogService auditLogService;

    public LoanProcessingService(LoanRepository loanRepository, AuditLogService auditLogService) {
        this.loanRepository = loanRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void approveLoan(Long loanId, Long officerId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        loan.setStatus("APPROVED");
        loanRepository.save(loan); // Primary Transaction

        try {
            // REQUIRES_NEW: Runs in its own separate transaction
            auditLogService.writeAuditLog("LOAN_APPROVED", officerId);
        } catch (Exception ex) {
            // Catching exception prevents the outer loan approval from rolling back!
            log.error("Audit log failed, but loan approval remains committed: {}", ex.getMessage());
        }
    }
}
```

---

## 9. Configuring Multiple DataSources in One Spring Boot Application

### 1. Previous Knowledge Required
- `DataSource`, `EntityManagerFactory`, and `PlatformTransactionManager`.
- Primary vs Secondary beans (`@Primary`).

### 2. Logic & Theory (Deep-Dive)
In a modern enterprise system, a single Spring Boot application often reads from an operational OLTP database (e.g., MySQL) and writes audit or reporting logs to a secondary database (e.g., PostgreSQL).
By default, Spring Boot auto-configures a single DataSource. To connect to two databases:
1. Define distinct configuration properties in `application.yml` (e.g., `spring.datasource.primary.*` and `spring.datasource.secondary.*`).
2. Create two independent `@Configuration` classes.
3. Configure separate `DataSource`, `LocalContainerEntityManagerFactoryBean`, and `PlatformTransactionManager` beans for each.
4. Mark one configuration as `@Primary` so Spring components without qualifiers know which default to choose.
5. Use `@EnableJpaRepositories` to map specific repository package paths to their respective EntityManagerFactory.

### 3. How to Answer in an Interview
> *"To configure multiple databases in a single Spring Boot application, we disable default single-datasource autoconfiguration and define two distinct configuration classes.*
>
> *Each class configures its own `DataSource`, `EntityManagerFactory`, and `PlatformTransactionManager`. We designate one as `@Primary` and point each configuration's `@EnableJpaRepositories` to distinct repository package paths (for example, `com.app.primary.repo` vs `com.app.secondary.repo`)."*

### 4. Practical Code Example

#### `application.yml`:
```yaml
spring:
  datasource:
    primary:
      jdbc-url: jdbc:mysql://localhost:3306/loan_db
      username: root
      password: secretpassword
      driver-class-name: com.mysql.cj.jdbc.Driver
    secondary:
      jdbc-url: jdbc:postgresql://localhost:5432/audit_db
      username: postgres
      password: secretpassword
      driver-class-name: org.postgresql.Driver
```

#### Primary Database Configuration:
```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.bank.loan.repository.primary",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDbConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(primaryDataSource())
                .packages("com.bank.loan.entity.primary")
                .persistenceUnit("primaryPU")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEmf) {
        return new JpaTransactionManager(primaryEmf);
    }
}
```

---

## 10. Centralized Configuration: Git-backed Spring Cloud Config vs application.properties

### 1. Previous Knowledge Required
- Externalized configuration (`application.properties` / `application.yml`).
- Cloud 12-Factor App principle: *Store configuration in the environment / external config repository*.

### 2. Logic & Theory (Deep-Dive)

#### The Limitation of Local `application.properties`:
In a microservices architecture with 20 microservices running 5 instances each (100 containers):
- If you need to change a database timeout or feature flag in `application.properties`, you must rebuild or restart all 100 containers.
- There is no central audit trail of who changed what configuration setting and when.

#### Spring Cloud Config Server Solution:
- Config files are stored in a centralized Git repository (`application.yml`, `loan-service-prod.yml`, `payment-service-dev.yml`).
- A dedicated **Spring Cloud Config Server** serves these properties over HTTP.
- Microservices fetch their configuration at boot time.
- **Dynamic Refresh without Restart**: By adding Spring Boot Actuator and the `@RefreshScope` annotation on beans, triggering a `POST /actuator/refresh` endpoint re-reads modified configurations from Git and dynamically reinjects them into beans without restarting the application!
- **Vault Integration**: Secrets (passwords, tokens) can be encrypted with HashiCorp Vault or asymmetric RSA keys inside Git.

### 3. How to Answer in an Interview
> *"In monolithic apps, local `application.properties` works fine. But in microservices architectures with dozens of distributed instances, local properties introduce severe maintenance overhead—any configuration change requires rebuilding or redeploying services.*
>
> *Spring Cloud Config externalizes configuration into a centralized Git repository. It provides version control, audit trails, and environment-specific profiles. Combined with Spring Cloud Bus and `@RefreshScope`, we can update configurations across 50 running instances on the fly without restarting a single JVM instance."*

### 4. Practical Code Example

#### Microservice `bootstrap.yml`:
```yaml
spring:
  application:
    name: loan-origination-service
  cloud:
    config:
      uri: http://config-server:8888
      fail-fast: true
      retry:
        max-attempts: 5
```

#### Dynamic Configuration Bean:
```java
@RestController
@RefreshScope // Allows dynamic update when POST /actuator/refresh is called
@RequestMapping("/api/v1/config")
public class FeatureFlagController {

    @Value("${feature.instant-approval.enabled:false}")
    private boolean instantApprovalEnabled;

    @GetMapping("/instant-approval")
    public ResponseEntity<Boolean> isInstantApprovalActive() {
        return ResponseEntity.ok(instantApprovalEnabled);
    }
}
```

---

*Continue to [Chapter 10: Spring Security, JWT & Microservices Security Architecture](file:///Users/apple/Workspace/Home/Javascript/Learning/10_Spring_Security_JWT_and_Auth_Architecture.md)*

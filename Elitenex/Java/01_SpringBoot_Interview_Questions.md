# 🚀 Top 20 Spring Boot Interview Questions & Answers

---

### Q1: What is Spring Boot, and how does it differ from traditional Spring Framework?
**Answer:**
Spring Boot is an opinionated, production-ready extension of the Spring Framework designed to simplify the bootstrapping, configuration, and deployment of Spring applications.

**Key Differences:**
| Feature | Traditional Spring Framework | Spring Boot |
| :--- | :--- | :--- |
| **Configuration** | Heavy XML or extensive `@Configuration` boilerplate | Opinionated convention-over-configuration; auto-configuration |
| **Server** | Requires manual deployment to external server (Tomcat, WildFly) | Embedded servlet container (Tomcat, Jetty, Undertow) included in JAR |
| **Dependency Management** | Manually manage individual library versions & transitive conflicts | `spring-boot-starter-*` BOM curates compatible dependency versions |
| **Production Readiness** | Requires manual metrics, health checks, and monitoring setup | Spring Boot Actuator provides built-in metrics, health, and audits |
| **Deployment** | Typically packaged as `.war` files | Packaged as self-contained executable `.jar` (with embedded runtime) |

**Key Value Proposition:** Rapid Application Development (RAD) with zero XML configuration required.

---

### Q2: How does Spring Boot Auto-Configuration work internally?
**Answer:**
Auto-configuration dynamically configures beans based on classpath contents, existing bean definitions, and environment properties.

**Mechanism:**
1. The `@SpringBootApplication` meta-annotation includes `@EnableAutoConfiguration`.
2. `@EnableAutoConfiguration` imports `AutoConfigurationImportSelector`.
3. In **Spring Boot 2.x**, it read class names from `META-INF/spring.factories` under `EnableAutoConfiguration`.
4. In **Spring Boot 3.x**, it reads candidate classes from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
5. Each auto-configuration class uses **conditional annotations** to determine if beans should be registered:
   - `@ConditionalOnClass(DataSource.class)`: Applies only if the class is on the classpath.
   - `@ConditionalOnMissingBean(DataSource.class)`: Applies only if user hasn't defined their own bean.
   - `@ConditionalOnProperty(name = "feature.enabled", havingValue = "true")`: Based on property values.

```java
@AutoConfiguration
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceAutoConfiguration {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```

---

### Q3: What happens behind the scenes during `SpringApplication.run(Application.class, args)`?
**Answer:**
The `SpringApplication.run()` method executes the following key lifecycle phases:
1. **Bootstrap Initialization:** Creates `SpringApplication` instance, detects application type (`SERVLET`, `REACTIVE`, or `NONE`), finds initializers and listeners from `META-INF/spring.factories` or service loaders.
2. **Environment Preparation:** Creates `ConfigurableEnvironment`, binds command-line args, loads active profiles, and parses `application.properties`/`application.yml`.
3. **Print Banner:** Renders the ASCII banner (customizable or suppressible).
4. **Create ApplicationContext:** Instantiates `AnnotationConfigServletWebServerApplicationContext` (for standard MVC apps) or `AnnotationConfigReactiveWebServerApplicationContext` (for WebFlux).
5. **Context Pre-Processing:** Applies `ApplicationContextInitializer` beans, broadcasts `ApplicationEnvironmentPreparedEvent` and `ApplicationPreparedEvent`.
6. **Bean Loading & Refresh:** Loads source beans into the context, runs `@ComponentScan`, evaluates auto-configurations, initializes singletons, and boots the embedded web server (Tomcat/Jetty).
7. **Post-Initialization Execution:** Triggers `ApplicationRunner` and `CommandLineRunner` beans in order (`@Order`).
8. **Ready State:** Fires `ApplicationReadyEvent`. The application is ready to accept traffic.

---

### Q4: What are Spring Boot Starters, and how would you create a Custom Starter?
**Answer:**
A **Starter** is a descriptor module that aggregates convenient dependency descriptors, transitively bringing in all necessary libraries and default configurations for a specific technology (e.g., `spring-boot-starter-web`, `spring-boot-starter-data-jpa`).

**To create a custom starter:**
1. **Autoconfigure Module (`my-custom-spring-boot-autoconfigure`):**
   - Contains your service implementation: `MyCustomService`.
   - Contains configuration properties: `@ConfigurationProperties(prefix = "my.starter")`.
   - Contains `@AutoConfiguration` class registering beans conditionally.
   - Register the class in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
2. **Starter POM Module (`my-custom-spring-boot-starter`):**
   - Empty code module that only contains `pom.xml`.
   - Depends on the autoconfigure module and third-party dependencies required.
3. Consumers only add `<dependency><artifactId>my-custom-spring-boot-starter</artifactId></dependency>`.

---

### Q5: Explain Externalized Configuration precedence and `@Value` vs `@ConfigurationProperties`.
**Answer:**
Spring Boot evaluates configuration properties in a strictly defined order (highest priority wins):
1. Devtools global settings (`~/.spring-boot-devtools.properties`)
2. `@TestPropertySource` annotations on tests
3. **Command line arguments** (e.g., `--server.port=8081`)
4. `SPRING_APPLICATION_JSON` properties
5. ServletConfig / ServletContext parameters
6. JNDI attributes
7. Java System properties (`System.getProperties()`)
8. OS environment variables (`SERVER_PORT=8081`)
9. Profile-specific files outside packaged jar (`application-{profile}.properties`)
10. Profile-specific files inside packaged jar
11. Application properties outside packaged jar (`application.properties`)
12. Application properties inside packaged jar
13. Default properties set via `SpringApplication.setDefaultProperties`

**`@Value` vs `@ConfigurationProperties`:**
| Feature | `@Value` | `@ConfigurationProperties` |
| :--- | :--- | :--- |
| **Binding Type** | Injects single property value | Binds entire hierarchal structure to a POJO |
| **Relaxed Binding** | No (`camelCase` only) | Yes (`kebab-case`, `snake_case`, etc.) |
| **Validation** | No direct validation | Supports `@Validated` (Jakarta Bean Validation) |
| **SpEL Support** | Yes (`#{...}`) | No |
| **Type Safety** | Weak | Strong (IDE auto-completion with metadata) |

```java
@Configuration
@ConfigurationProperties(prefix = "app.payment")
@Validated
public record PaymentConfig(
    @NotBlank String gatewayUrl,
    @Min(1) int timeoutMs
) {}
```

---

### Q6: What is Spring Boot Actuator, and how do you secure and customize health endpoints?
**Answer:**
Spring Boot Actuator brings production-ready features: health checking, application metrics, auditing, environment inspection, and thread/heap dumps.

**Key Endpoints:**
- `/actuator/health`: Application health (`UP`, `DOWN`, `OUT_OF_SERVICE`).
- `/actuator/metrics`: System and application metrics (Micrometer, JVM, CPU).
- `/actuator/info`: Arbitrary application information (git commit, build info).
- `/actuator/prometheus`: Metrics formatted for Prometheus scraping.

**Custom Health Indicator:**
```java
@Component
public class PaymentGatewayHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean serviceUp = checkPaymentGateway();
        if (serviceUp) {
            return Health.up().withDetail("gateway", "Available").build();
        }
        return Health.down().withDetail("gateway", "Latency spike / unreachable").build();
    }
}
```

**Security Best Practice:**
Expose only `health` and `info` publicly. Protect sensitive management endpoints behind authentication and role check via Spring Security:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,info,prometheus"
  endpoint:
    health:
      show-details: when-authorized
```

---

### Q7: How do Embedded Servlet Containers work, and how do you switch from Tomcat to Undertow or Jetty?
**Answer:**
Spring Boot creates an `ApplicationContext` that starts an embedded web server during startup.
- `ServletWebServerFactoryAutoConfiguration` detects which factory is present on the classpath (`TomcatServletWebServerFactory`, `JettyServletWebServerFactory`, or `UndertowServletWebServerFactory`).
- The factory builds and starts the container as a regular Java process without needing a separate servlet container installation.

**Switching to Undertow (in Maven `pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

---

### Q8: What are Spring Bean Scopes, and how do you solve the "Prototype bean inside Singleton bean" problem?
**Answer:**
**Bean Scopes:**
- `singleton` (default): Exactly one instance per Spring IoC container.
- `prototype`: A new instance is created every time requested.
- Web scopes: `request` (per HTTP request), `session` (per HTTP session), `application` (per ServletContext), `websocket`.

**The Problem:**
If a `prototype` bean is injected into a `singleton` bean via `@Autowired`, the singleton is created only once, so the prototype dependency is resolved **only once**. The singleton continues reusing that same instance!

**Solutions:**
1. **Method Injection (`@Lookup`):**
```java
@Component
public class OrderService {
    @Lookup
    public OrderProcessor getOrderProcessor() {
        return null; // Spring CGLIB overrides this dynamically
    }
}
```
2. **`ObjectProvider<T>` (Recommended modern approach):**
```java
@Service
public class OrderService {
    private final ObjectProvider<OrderProcessor> processorProvider;

    public OrderService(ObjectProvider<OrderProcessor> processorProvider) {
        this.processorProvider = processorProvider;
    }

    public void process() {
        OrderProcessor processor = processorProvider.getObject(); // Fresh prototype instance every time
        processor.execute();
    }
}
```
3. **Scoped Proxy (`@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)`)**.

---

### Q9: How do you implement Global Exception Handling using `@RestControllerAdvice` and RFC 9457 `ProblemDetail` in Spring Boot 3?
**Answer:**
Spring 6 / Spring Boot 3 natively adopts **RFC 9457 Problem Details for HTTP APIs** via the `ProblemDetail` class.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setType(URI.create("https://api.myapp.com/errors/not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        problem.setProperty("fieldErrors", errors);
        return problem;
    }
}
```

---

### Q10: How does Request Validation work using Jakarta Bean Validation?
**Answer:**
Spring Boot includes `spring-boot-starter-validation` (built on Hibernate Validator).
- Annotate DTO fields with validation constraints (`@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Min`, `@Pattern`).
- In controller methods, use `@Valid` or `@Validated` on `@RequestBody`.
- If validation fails, `MethodArgumentNotValidException` is thrown with an `Errors` / `BindingResult` list.

**Custom Validator Example:**
```java
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^\\+?[1-9]\\d{1,14}$");
    }
}
```

---

### Q11: How do you configure Spring Security 6 in Spring Boot 3 without `WebSecurityConfigurerAdapter`?
**Answer:**
In Spring Security 6, `WebSecurityConfigurerAdapter` is completely removed. Configuration is done component-based using the `SecurityFilterChain` bean.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/actuator/health").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### Q12: How does Spring Boot configure Logging, and how do you implement Structured JSON Logging & MDC?
**Answer:**
Spring Boot uses **SLF4J** as the facade with **Logback** as the default runtime implementation.

**Key Configurations (`logback-spring.xml`):**
- Configure log levels via properties: `logging.level.org.springframework.web=INFO`.
- Dynamic log level changes at runtime via Actuator endpoint: `POST /actuator/loggers/com.example.service {"configuredLevel": "DEBUG"}`.

**MDC (Mapped Diagnostic Context) for Distributed Correlation:**
MDC attaches contextual key-value pairs (e.g., `traceId`, `userId`) to thread-local logs.
```java
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader("X-Correlation-Id");
        if (correlationId == null) correlationId = UUID.randomUUID().toString();
        
        MDC.put("correlationId", correlationId);
        response.setHeader("X-Correlation-Id", correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId"); // Prevent memory leak
        }
    }
}
```

---

### Q13: How do `@Async` and `@Scheduled` work, and how do you configure custom Thread Pools?
**Answer:**
- `@EnableAsync` and `@Async`: Executes methods in a separate thread. Returns `void` or `CompletableFuture<T>`.
- `@EnableScheduling` and `@Scheduled`: Executes periodic jobs (cron expression, fixedRate, or fixedDelay).

**Pitfall:** By default, Spring uses a `SimpleAsyncTaskExecutor` for `@Async`, which does **not reuse threads** and spawns a new thread per invocation—risking OutOfMemoryError in production!

**Production ThreadPool Configuration:**
```java
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncWorker-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
```

---

### Q14: How does Spring Cache Abstraction work (`@Cacheable`, `@CachePut`, `@CacheEvict`), and how do you integrate Redis?
**Answer:**
Spring provides an abstraction layer over caching engines (Caffeine, Ehcache, Redis).
- `@Cacheable`: Checks cache first. If found, returns it; if not, invokes method and stores result in cache.
- `@CachePut`: Always executes the method and updates the cache with the returned value.
- `@CacheEvict`: Removes entries from the cache (e.g., on entity deletion or modification).
- `@Caching`: Combines multiple cache operations.

```java
@Service
public class UserService {
    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```
**Redis Integration:** Add `spring-boot-starter-data-redis` and configure `spring.data.redis.host=localhost` and a custom `RedisCacheConfiguration` with TTL and JSON serialization.

---

### Q15: Explain Spring Boot Testing Slices (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`) and Testcontainers.
**Answer:**
Testing slices load only the relevant layer of the Spring `ApplicationContext`, speeding up test execution.

| Annotation | Layer Tested | What it Loads | What it Mocks |
| :--- | :--- | :--- | :--- |
| `@SpringBootTest` | Full Integration | Complete context, all beans, database, web server | Nothing by default |
| `@WebMvcTest` | Controller / Web | `@Controller`, `@RestController`, filters, Security | Services, Repositories (`@MockBean`) |
| `@DataJpaTest` | Persistence | `@Entity`, Spring Data Repositories, EntityManager | Controllers, Services (uses embedded/test DB) |

**Testcontainers:** Replaces H2 in-memory databases with real containerized databases (PostgreSQL, MySQL, Kafka) for faithful integration testing:
```java
@SpringBootTest
@Testcontainers
class OrderServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

---

### Q16: Compare JAR vs WAR, Layered Jars, and GraalVM Native Images in Spring Boot 3.
**Answer:**
1. **JAR vs WAR:**
   - **Fat JAR:** Bundles embedded server + application code + dependencies into one executable file using `org.springframework.boot.loader.JarLauncher`. Preferred in cloud-native/microservice apps.
   - **WAR:** Built to deploy into an existing external application server (e.g., WildFly/WebLogic). Extends `SpringBootServletInitializer`.
2. **Layered Jars:**
   - Deconstructs the fat jar into layers: `dependencies`, `spring-boot-loader`, `snapshot-dependencies`, `application`.
   - **Benefit:** Docker image layer caching. If only business code changes, Docker rebuilds only the thin application layer (~few MBs) instead of pushing all heavy dependencies (~80MB).
3. **GraalVM Native Image (AOT):**
   - Introduced in Spring Boot 3 via Spring AOT (Ahead-of-Time compilation).
   - Generates platform-native binary executable.
   - **Advantages:** Instant startup (< 50ms), drastically reduced memory footprint (RSS ~30MB).
   - **Trade-offs:** Longer build times, requires explicit reflection hints for dynamic code.

---

### Q17: What is the difference between a Servlet Filter and a Spring HandlerInterceptor?
**Answer:**
Both intercept HTTP requests, but at different layers in the execution lifecycle.

| Dimension | Servlet Filter | Spring HandlerInterceptor |
| :--- | :--- | :--- |
| **Origin** | Standard Java EE / Jakarta Servlet API | Spring MVC Framework |
| **Execution Point** | Before request reaches `DispatcherServlet` | Between `DispatcherServlet` and `@Controller` |
| **Context Awareness**| Lower-level servlet constructs | Direct access to Spring `@HandlerMethod` metadata |
| **Exception Handling**| Outside Spring MVC exception resolution | Can access `ModelAndView` and exceptions |
| **Best Used For** | Authentication token parsing, CORS, compression, logging raw body | Fine-grained authorization, controller auditing, modifying model |

---

### Q18: How do you implement Content Negotiation and API Versioning in Spring Boot?
**Answer:**
**Content Negotiation:**
Spring decides response format (JSON, XML) based on HTTP headers (`Accept`) or query params.
```java
@GetMapping(value = "/users/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public User getUser(@PathVariable Long id) { ... }
```

**API Versioning Strategies:**
1. **URI Versioning (Most Popular):** `/api/v1/orders` vs `/api/v2/orders`. Easy to route and cache.
2. **Header Versioning:** `X-API-VERSION: 2`. Keeps URLs clean.
3. **Accept Header (MIME Type / Content Negotiation):** `Accept: application/vnd.company.v2+json`. RESTful, but harder to test via browser.
4. **Query Parameter:** `/api/orders?version=2`. Simple, but can conflict with query filtering.

---

### Q19: What are Virtual Threads (Project Loom) in Spring Boot 3.2+, and what are their benefits?
**Answer:**
Virtual Threads (Java 21+) are lightweight threads managed by the JVM rather than the OS kernel.
- **Traditional Platform Threads:** 1:1 mapping with OS kernel threads. Memory overhead ~1MB stack. Limit ~few thousand threads before thread exhaustion.
- **Virtual Threads (M:N):** JVM schedules millions of virtual threads on a small pool of carrier OS threads. When a virtual thread blocks on I/O (database, REST call), the JVM unmounts it and mounts another runnable thread.

**Enabling in Spring Boot 3.2+:**
```properties
spring.threads.virtual.enabled=true
```
**Benefits:** High throughput for I/O-bound synchronous web applications without rewriting code to reactive (WebFlux) programming.
**Caveat:** Avoid `synchronized` blocks that pin carrier threads (use `ReentrantLock` instead).

---

### Q20: What are the key Production Performance Tuning practices for Spring Boot?
**Answer:**
1. **Connection Pool Tuning (HikariCP):**
   - Tune `maximum-pool-size` (Formula: $Connections = ((CoreCount * 2) + EffectiveSpindleCount)$).
   - Set sensible `connection-timeout`, `idle-timeout`, and `max-lifetime`.
2. **JVM Memory & Garbage Collection:**
   - Use G1GC or ZGC for low-latency: `-XX:+UseG1GC -XX:MaxGCPauseMillis=200`.
   - Set min and max heap equally: `-Xms4g -Xmx4g` to avoid heap resizing pauses.
3. **Disable Unused Auto-Configurations:**
   - `@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, JmxAutoConfiguration.class })`.
4. **Spring Boot 3 Startup Optimization:**
   - Enable lazy bean initialization: `spring.main.lazy-initialization=true` (great for dev/local testing).
   - Use CDS (Class Data Sharing) or GraalVM AOT.
5. **Tomcat Thread Pool Tuning:**
   - Configure `server.tomcat.threads.max` and `server.tomcat.accept-count` according to workload.


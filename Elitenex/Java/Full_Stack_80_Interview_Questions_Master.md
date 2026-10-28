# 🎯 Full-Stack Master Guide: 80 Critical Interview Questions & Answers
### Spring Boot • Microservices • JPA & Hibernate • Angular

This comprehensive interview preparation handbook covers **80 essential questions and in-depth answers** across the modern enterprise full-stack development ecosystem.

---

## 📑 Quick Navigation & Table of Contents

- [Part 1: Spring Boot (Questions 1 – 20)](#part-1-spring-boot-20-questions)
  - [Q1: Spring Boot vs Traditional Spring](#q1-what-is-spring-boot-and-how-does-it-differ-from-traditional-spring-framework)
  - [Q2: Auto-Configuration Internals](#q2-how-does-spring-boot-auto-configuration-work-internally)
  - [Q3: SpringApplication.run() Execution Sequence](#q3-what-happens-behind-the-scenes-during-springapplicationrunapplicationclass-args)
  - [Q4: Spring Boot Starters & Custom Starter Creation](#q4-what-are-spring-boot-starters-and-how-would-you-create-a-custom-starter)
  - [Q5: Externalized Configuration & Precedence](#q5-explain-externalized-configuration-precedence-and-value-vs-configurationproperties)
  - [Q6: Spring Boot Actuator & Custom Health Indicators](#q6-what-is-spring-boot-actuator-and-how-do-you-secure-and-customize-health-endpoints)
  - [Q7: Embedded Servlet Containers & Tuning](#q7-how-do-embedded-servlet-containers-work-and-how-do-you-switch-from-tomcat-to-undertow-or-jetty)
  - [Q8: Bean Scopes & Prototype Injection in Singleton](#q8-what-are-spring-bean-scopes-and-how-do-you-solve-the-prototype-bean-inside-singleton-bean-problem)
  - [Q9: Global Exception Handling & RFC 9457 ProblemDetail](#q9-how-do-you-implement-global-exception-handling-using-restcontrolleradvice-and-rfc-9457-problemdetail-in-spring-boot-3)
  - [Q10: Jakarta Bean Validation & Custom Constraints](#q10-how-does-request-validation-work-using-jakarta-bean-validation)
  - [Q11: Spring Security 6 SecurityFilterChain](#q11-how-do-you-configure-spring-security-6-in-spring-boot-3-without-websecurityconfigureradapter)
  - [Q12: Logging, MDC & Correlation IDs](#q12-how-does-spring-boot-configure-logging-and-how-do-you-implement-structured-json-logging--mdc)
  - [Q13: Async, Scheduling & Custom ThreadPools](#q13-how-do-async-and-scheduled-work-and-how-do-you-configure-custom-thread-pools)
  - [Q14: Cache Abstraction & Redis Integration](#q14-how-does-spring-cache-abstraction-work-cacheable-cacheput-cacheevict-and-how-do-you-integrate-redis)
  - [Q15: Testing Slices & Testcontainers](#q15-explain-spring-boot-testing-slices-springboottest-webmvctest-datajpatest-and-testcontainers)
  - [Q16: JAR vs WAR, Layered Jars & GraalVM AOT](#q16-compare-jar-vs-war-layered-jars-and-graalvm-native-images-in-spring-boot-3)
  - [Q17: Servlet Filter vs Spring HandlerInterceptor](#q17-what-is-the-difference-between-a-servlet-filter-and-a-spring-handlerinterceptor)
  - [Q18: Content Negotiation & API Versioning](#q18-how-do-you-implement-content-negotiation-and-api-versioning-in-spring-boot)
  - [Q19: Virtual Threads (Project Loom) in Boot 3.2+](#q19-what-are-virtual-threads-project-loom-in-spring-boot-32-and-what-are-their-benefits)
  - [Q20: Production Performance Tuning Practices](#q20-what-are-the-key-production-performance-tuning-practices-for-spring-boot)

- [Part 2: Microservices Architecture (Questions 21 – 40)](#part-2-microservices-architecture-20-questions)
  - [Q21: Monolith vs Microservices](#q21-what-is-microservices-architecture-and-how-does-it-compare-to-monolithic-architecture)
  - [Q22: Service Discovery (Client vs Server-Side)](#q22-what-is-service-discovery-and-how-does-client-side-vs-server-side-discovery-work)
  - [Q23: API Gateway Pattern & Spring Cloud Gateway](#q23-what-is-the-api-gateway-pattern-and-what-functions-does-spring-cloud-gateway-perform)
  - [Q24: Synchronous vs Asynchronous Communication](#q24-compare-synchronous-vs-asynchronous-communication-in-microservices)
  - [Q25: Circuit Breaker Pattern & Resilience4j](#q25-explain-the-circuit-breaker-pattern-and-resilience4j-states)
  - [Q26: Distributed Tracing (Trace ID, Span ID, Micrometer)](#q26-what-is-distributed-tracing-and-how-do-trace-id-span-id-and-micrometer-tracing-work)
  - [Q27: Saga Pattern (Orchestration vs Choreography)](#q27-what-is-the-saga-pattern-and-how-do-orchestration-and-choreography-sagas-differ)
  - [Q28: CQRS & Event Sourcing](#q28-what-is-cqrs-command-query-responsibility-segregation-and-event-sourcing)
  - [Q29: Spring Cloud Config & Dynamic Refresh](#q29-how-does-centralized-configuration-work-with-spring-cloud-config-server-and-dynamic-refresh)
  - [Q30: Database-per-Service & Cross-Service Queries](#q30-what-is-the-database-per-service-pattern-and-how-do-you-handle-cross-service-queries)
  - [Q31: Idempotency in Distributed APIs](#q31-what-is-idempotency-and-how-do-you-guarantee-it-in-distributed-paymentorder-apis)
  - [Q32: Microservices Security, OAuth2, JWT & mTLS](#q32-how-do-you-implement-end-to-end-security-across-microservices)
  - [Q33: Strangler Fig Migration Pattern](#q33-what-is-the-strangler-fig-pattern-for-monolithic-migration)
  - [Q34: Transactional Outbox Pattern & CDC](#q34-what-is-the-transactional-outbox-pattern-and-change-data-capture-cdc)
  - [Q35: Service Mesh (Istio/Envoy) vs Spring Cloud](#q35-what-is-a-service-mesh-and-how-does-it-compare-to-spring-cloud)
  - [Q36: Kubernetes Primitives for Microservices](#q36-how-do-kubernetes-primitives-map-to-microservices-architecture)
  - [Q37: Dead Letter Queue (DLQ) & Poison Pills](#q37-how-do-you-handle-dead-letter-queues-dlq-and-poison-pill-messages-in-event-streams)
  - [Q38: Kubernetes Probes & Actuator Health Groups](#q38-explain-kubernetes-liveness-readiness-and-startup-probes-with-spring-boot-actuator)
  - [Q39: Contract Testing (Pact / Spring Cloud Contract)](#q39-what-is-consumer-driven-contract-testing-pact--spring-cloud-contract)
  - [Q40: Distributed Caching Strategies (Cache-Aside, Write-Behind)](#q40-what-are-distributed-caching-strategies-in-microservices)

- [Part 3: JPA, Hibernate & Spring Data JPA (Questions 41 – 60)](#part-3-jpa-hibernate--spring-data-jpa-20-questions)
  - [Q41: JPA vs Hibernate vs Spring Data JPA](#q41-what-is-the-relationship-and-difference-between-jpa-hibernate-and-spring-data-jpa)
  - [Q42: Entity Lifecycle States (Transient, Managed, Detached, Removed)](#q42-explain-the-4-entity-lifecycle-states-in-jpa)
  - [Q43: Hibernate First-Level (L1) vs Second-Level (L2) Cache](#q43-explain-hibernate-first-level-l1-vs-second-level-l2-cache)
  - [Q44: N+1 Query Problem & Solutions](#q44-what-is-the-n1-query-problem-in-jpa-and-how-do-you-resolve-it)
  - [Q45: Lazy vs Eager Loading & LazyInitializationException](#q45-compare-lazy-vs-eager-loading-and-how-to-prevent-lazyinitializationexception)
  - [Q46: Modeling Relationships & `mappedBy`](#q46-how-do-you-model-onetomany-and-manytoone-relationships-properly)
  - [Q47: CascadeType.REMOVE vs orphanRemoval](#q47-difference-between-cascadetyperemove-and-orphanremoval--true)
  - [Q48: Spring Data JPA Querying Techniques](#q48-compare-spring-data-jpa-querying-mechanisms)
  - [Q49: Projections (DTO vs Interface)](#q49-what-are-spring-data-jpa-projections-dto-vs-interface)
  - [Q50: `@Transactional` & Propagation Modes](#q50-how-does-spring-transactional-work-and-what-are-its-propagation-behaviors)
  - [Q51: Isolation Levels & Concurrency Anomalies](#q51-explain-transaction-isolation-levels-and-concurrency-anomalies)
  - [Q52: Self-Invocation Issue with `@Transactional`](#q52-why-does-self-invocation-break-transactional-and-how-do-you-solve-it)
  - [Q53: Optimistic vs Pessimistic Locking](#q53-compare-optimistic-vs-pessimistic-locking-in-jpa)
  - [Q54: Spring Data JPA Auditing](#q54-how-does-spring-data-jpa-auditing-work)
  - [Q55: Pagination & Sorting (Page vs Slice vs Keyset)](#q55-compare-paget-vs-slicet-vs-listt-in-spring-data-jpa-pagination)
  - [Q56: Inheritance Mapping Strategies](#q56-explain-jpa-inheritance-mapping-strategies)
  - [Q57: Composite Primary Keys (`@EmbeddedId` vs `@IdClass`)](#q57-how-do-you-map-composite-primary-keys-in-jpa)
  - [Q58: Entity Lifecycle Callbacks & EntityListeners](#q58-what-are-jpa-entity-lifecycle-callbacks-and-entitylisteners)
  - [Q59: Hibernate Dirty Checking & `@DynamicUpdate`](#q59-how-does-hibernate-dirty-checking-work-and-what-is-dynamicupdate)
  - [Q60: High-Performance Batch Processing](#q60-how-do-you-perform-high-performance-batch-inserts-and-updates-in-hibernate)

- [Part 4: Angular Framework (Questions 61 – 80)](#part-4-angular-framework-20-questions)
  - [Q61: Angular Architecture & Core Building Blocks](#q61-what-is-angular-and-what-are-its-core-architectural-building-blocks)
  - [Q62: Standalone Components](#q62-what-are-standalone-components-and-how-do-they-replace-ngmodule)
  - [Q63: Component Lifecycle Hooks](#q63-walk-through-the-angular-component-lifecycle-hooks)
  - [Q64: Change Detection (Default vs OnPush, Zone.js)](#q64-how-does-change-detection-work-in-angular-compare-default-vs-onpush)
  - [Q65: Angular Signals & Zoneless Angular](#q65-what-are-angular-signals-angular-16-and-how-do-they-enable-zoneless-angular)
  - [Q66: Signal-Based Inputs, Outputs & Models](#q66-what-are-signal-based-component-inputs-outputs-and-models-angular-172)
  - [Q67: Built-in Control Flow Syntax (`@if`, `@for`, `@switch`)](#q67-explain-the-built-in-control-flow-syntax-in-angular-17-vs-legacy-directives)
  - [Q68: Hierarchical DI & the `inject()` Function](#q68-how-does-angulars-hierarchical-dependency-injection-work-and-what-is-the-inject-function)
  - [Q69: Observables vs Promises vs Subjects](#q69-compare-observables-promises-and-subjects-in-rxjs)
  - [Q70: Async Pipe & Memory Leak Prevention](#q70-why-should-you-prefer-the-async-pipe-and-how-do-you-prevent-rxjs-memory-leaks)
  - [Q71: RxJS Flattening Operators (`switchMap`, `mergeMap`, etc.)](#q71-explain-key-rxjs-flattening-operators-switchmap-mergemap-concatmap-exhaustmap)
  - [Q72: Routing & Component Lazy Loading](#q72-how-does-angular-routing-work-and-how-do-you-implement-component-lazy-loading)
  - [Q73: Functional Route Guards (`CanActivateFn`)](#q73-what-are-route-guards-and-how-do-you-write-functional-guards-canactivatefn)
  - [Q74: Template-Driven vs Reactive Forms](#q74-compare-template-driven-forms-vs-reactive-forms)
  - [Q75: Functional HTTP Interceptors (`HttpInterceptorFn`)](#q75-how-do-you-configure-modern-functional-http-interceptors-httpinterceptorfn)
  - [Q76: Pure vs Impure Pipes & Template Method Pitfall](#q76-explain-pure-vs-impure-pipes-and-why-you-should-avoid-calling-methods-in-templates)
  - [Q77: Content Projection & Signal Queries (`viewChild`)](#q77-what-is-content-projection-ng-content-and-viewchild--contentchild)
  - [Q78: State Management (Services vs Signals vs NgRx)](#q78-what-are-state-management-strategies-in-angular-when-should-you-use-ngrx)
  - [Q79: Performance Optimization & `@defer`](#q79-what-are-the-key-performance-optimization-techniques-in-angular)
  - [Q80: Angular Unit Testing (`TestBed`, `HttpTestingController`)](#q80-how-do-you-write-unit-tests-in-angular-using-testbed-and-httptestingcontroller)

---

## 📂 Modular Guide Files
You can also open individual topical guides in this directory:
- [01_SpringBoot_Interview_Questions.md](file:///Users/apple/Documents/Notes/Java/01_SpringBoot_Interview_Questions.md)
- [02_Microservices_Interview_Questions.md](file:///Users/apple/Documents/Notes/Java/02_Microservices_Interview_Questions.md)
- [03_JPA_Hibernate_Interview_Questions.md](file:///Users/apple/Documents/Notes/Java/03_JPA_Hibernate_Interview_Questions.md)
- [04_Angular_Interview_Questions.md](file:///Users/apple/Documents/Notes/Java/04_Angular_Interview_Questions.md)
- [05_SQL_Interview_Questions_30.md](file:///Users/apple/Documents/Notes/Java/05_SQL_Interview_Questions_30.md) (30 Advanced SQL Questions)

---

# Part 1: Spring Boot (20 Questions)

### Q1: What is Spring Boot, and how does it differ from traditional Spring Framework?
Spring Boot is an opinionated, production-ready extension of the Spring Framework designed to simplify the bootstrapping, configuration, and deployment of Spring applications.
- **Traditional Spring:** Heavy XML or extensive `@Configuration` boilerplate, requires manual deployment to an external servlet container (Tomcat, WildFly), manual dependency resolution.
- **Spring Boot:** Auto-configuration based on classpath, embedded web server (Tomcat/Jetty/Undertow), curated starter BOMs (`spring-boot-starter-*`), out-of-the-box production readiness via Actuator.

### Q2: How does Spring Boot Auto-Configuration work internally?
1. The `@SpringBootApplication` meta-annotation includes `@EnableAutoConfiguration`.
2. It invokes `AutoConfigurationImportSelector` which reads candidate configuration classes from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (Spring Boot 3) or `spring.factories` (Boot 2).
3. Conditional annotations (`@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty`) evaluate the environment and conditionally register beans only if prerequisites are satisfied.

### Q3: What happens behind the scenes during `SpringApplication.run(Application.class, args)`?
1. Initializes `SpringApplication`, determining application type (`SERVLET`, `REACTIVE`, `NONE`).
2. Creates and configures `ConfigurableEnvironment` (loads profiles, system properties, and `application.yml`).
3. Instantiates the `ApplicationContext` (`AnnotationConfigServletWebServerApplicationContext`).
4. Executes `@ComponentScan`, applies auto-configurations, builds the bean graph, and starts the embedded web container (e.g. Tomcat on port 8080).
5. Fires lifecycle events (`ApplicationStartedEvent`, `ApplicationReadyEvent`) and runs `CommandLineRunner` / `ApplicationRunner` beans.

### Q4: What are Spring Boot Starters, and how would you create a Custom Starter?
Starters are dependency descriptor templates aggregating compatible library dependencies.
- **To build a custom starter:**
  1. Create an autoconfigure module containing the `@ConfigurationProperties` and `@AutoConfiguration` class registering service beans conditionally (`@ConditionalOnMissingBean`).
  2. Declare the configuration class in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
  3. Create an empty starter POM project that exports the autoconfigure module and third-party dependencies.

### Q5: Explain Externalized Configuration precedence and `@Value` vs `@ConfigurationProperties`.
**Precedence (highest to lowest):** Devtools $\rightarrow$ Command line args (`--server.port=8081`) $\rightarrow$ `SPRING_APPLICATION_JSON` $\rightarrow$ OS environment variables $\rightarrow$ `application-{profile}.properties` $\rightarrow$ `application.properties`.
- **`@Value`:** Injects a single property; supports SpEL; no relaxed binding or Bean Validation.
- **`@ConfigurationProperties`:** Binds a hierarchical property subtree into a type-safe POJO/record; supports relaxed binding (`kebab-case` to `camelCase`) and Jakarta Bean Validation.

### Q6: What is Spring Boot Actuator, and how do you secure and customize health endpoints?
Actuator provides production monitoring endpoints: `/actuator/health`, `/actuator/metrics`, `/actuator/info`, `/actuator/prometheus`.
- **Custom Health Indicator:** Implement `HealthIndicator` and return `Health.up()` or `Health.down().withDetail("reason", ...)`.
- **Security:** Expose only `health` and `info` publicly via `management.endpoints.web.exposure.include="health,info"`. Protect other management routes behind Spring Security role checks.

### Q7: How do Embedded Servlet Containers work, and how do you switch from Tomcat to Undertow or Jetty?
Spring Boot creates a `ServletWebServerFactory` bean (`TomcatServletWebServerFactory`, `UndertowServletWebServerFactory`) at startup and launches the server programmatically inside the JVM process.
- **To switch:** Exclude `spring-boot-starter-tomcat` from `spring-boot-starter-web` in `pom.xml` and include `spring-boot-starter-undertow`.

### Q8: What are Spring Bean Scopes, and how do you solve the "Prototype bean inside Singleton bean" problem?
- Scopes: `singleton` (default), `prototype`, `request`, `session`, `application`.
- **The Problem:** Injected prototype dependencies in a singleton are created once at initialization and never re-created.
- **Solution:** Inject `ObjectProvider<T>` and invoke `provider.getObject()` to retrieve fresh instances on-demand, or use `@Lookup` method injection or `@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)`.

### Q9: How do you implement Global Exception Handling using `@RestControllerAdvice` and RFC 9457 `ProblemDetail` in Spring Boot 3?
Annotate a class with `@RestControllerAdvice` and define methods with `@ExceptionHandler(MyException.class)` returning `ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage())`. Add custom properties via `problemDetail.setProperty("timestamp", Instant.now())`.

### Q10: How does Request Validation work using Jakarta Bean Validation?
Add `spring-boot-starter-validation`. Annotate DTO fields (`@NotBlank`, `@Size`, `@Email`, `@Min`). Annotate controller parameters with `@Valid` or `@Validated`. Spring automatically triggers `MethodArgumentNotValidException` when validation fails. Implement `ConstraintValidator<Annotation, FieldType>` for custom domain validation rules.

### Q11: How do you configure Spring Security 6 in Spring Boot 3 without `WebSecurityConfigurerAdapter`?
Define a `@Bean` returning `SecurityFilterChain`. Configure authorization using lambda DSL:
`http.csrf(AbstractHttpConfigurer::disable).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build()`.

### Q12: How does Spring Boot configure Logging, and how do you implement Structured JSON Logging & MDC?
Uses SLF4J backed by Logback. Use `MDC.put("correlationId", id)` in a `OncePerRequestFilter` to attach tracing identifiers to thread-local logs; always clear MDC in a `finally` block. In production, configure Logback with `LogstashEncoder` to emit structured JSON logs ingested by Elasticsearch / Splunk.

### Q13: How do `@Async` and `@Scheduled` work, and how do you configure custom Thread Pools?
Enable with `@EnableAsync` and `@EnableScheduling`. Never rely on the default `SimpleAsyncTaskExecutor` (which creates an unbounded new thread per call). Configure a dedicated `ThreadPoolTaskExecutor` bean setting `corePoolSize`, `maxPoolSize`, `queueCapacity`, and `CallerRunsPolicy`.

### Q14: How does Spring Cache Abstraction work (`@Cacheable`, `@CachePut`, `@CacheEvict`), and how do you integrate Redis?
- `@Cacheable`: Checks cache; executes method on miss and caches result.
- `@CachePut`: Always executes method and updates cache entry.
- `@CacheEvict`: Removes specified key or all keys (`allEntries = true`) from cache.
- **Redis:** Include `spring-boot-starter-data-redis` and configure `RedisCacheConfiguration` with custom serialization (JSON) and TTL.

### Q15: Explain Spring Boot Testing Slices (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`) and Testcontainers.
- `@SpringBootTest`: Boots entire Spring ApplicationContext (integration tests).
- `@WebMvcTest`: Boots only web layer (controllers, filters, security) and mocks services via `@MockBean`.
- `@DataJpaTest`: Boots only JPA layer, entities, and repositories using an embedded DB or Testcontainers.
- **Testcontainers:** Uses Docker to spin up lightweight real PostgreSQL/MySQL/Kafka instances during test execution.

### Q16: Compare JAR vs WAR, Layered Jars, and GraalVM Native Images in Spring Boot 3.
- **Fat JAR:** Embeds runtime and dependencies; run via `java -jar app.jar`.
- **WAR:** Deployed to external servlet containers.
- **Layered Jars:** Splits fat JAR into layers (dependencies, application) for optimized Docker caching.
- **GraalVM Native Image (AOT):** Ahead-of-Time compiles Spring Boot into a platform-native executable with sub-50ms startup and ~30MB memory footprint.

### Q17: What is the difference between a Servlet Filter and a Spring HandlerInterceptor?
- **Servlet Filter:** Intercepts raw HTTP requests before reaching `DispatcherServlet`; used for authentication tokens, CORS, request body logging, gzip compression.
- **HandlerInterceptor:** Sits between `DispatcherServlet` and `@Controller`; has access to the target Spring `HandlerMethod` metadata and `ModelAndView`.

### Q18: How do you implement Content Negotiation and API Versioning in Spring Boot?
- **Content Negotiation:** Use `@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })` matching client `Accept` headers.
- **Versioning:** URI Path Versioning (`/api/v1/orders`), Header Versioning (`X-API-VERSION: 1`), or Media Type Versioning (`Accept: application/vnd.company.v1+json`).

### Q19: What are Virtual Threads (Project Loom) in Spring Boot 3.2+, and what are their benefits?
Enable via `spring.threads.virtual.enabled=true` (Java 21+). JVM manages lightweight virtual threads mapped to carrier OS threads. When blocking on I/O, virtual threads unmount without blocking OS threads, allowing high throughput for standard synchronous code.

### Q20: What are the key Production Performance Tuning practices for Spring Boot?
1. Tune HikariCP connection pool (`maximum-pool-size`, connection timeouts).
2. JVM tuning: Use G1GC or ZGC, set identical min and max heap (`-Xms4g -Xmx4g`).
3. Startup: Use Class Data Sharing (CDS) and GraalVM AOT.
4. Tune embedded Tomcat max threads and keep-alive timeouts.

---

# Part 2: Microservices Architecture (20 Questions)

### Q21: What is Microservices Architecture, and how does it compare to Monolithic Architecture?
Microservices decouple applications into independent, fine-grained, business-oriented services communicating via network protocols.
- **Monolith:** Single deployable artifact, shared database, easy local debugging, but hard to scale bottleneck modules and prone to entire-system outages.
- **Microservices:** Independent deployments, localized horizontal scaling, polyglot technology stack, high resilience with circuit breakers, but brings distributed data complexity.

### Q22: What is Service Discovery, and how does Client-Side vs Server-Side Discovery work?
Dynamic lookup mechanism for ephemeral service instances.
- **Client-Side (Eureka / OpenFeign):** Client queries Eureka registry, gets list of healthy instances, and load-balances requests locally.
- **Server-Side (Kubernetes Service / AWS ALB):** Client calls a stable DNS/virtual IP; the proxy/router resolves backend pod addresses.

### Q23: What is the API Gateway pattern, and what functions does Spring Cloud Gateway perform?
The unified front door for external traffic. Handles reverse proxy routing, SSL termination, authentication/authorization validation, distributed rate limiting (Redis Token Bucket), and request transformation.

### Q24: Compare Synchronous vs Asynchronous Communication in Microservices.
- **Synchronous (REST / gRPC / Feign):** Caller blocks waiting for response; tightly coupled in time; risk of cascading failures.
- **Asynchronous (Kafka / RabbitMQ):** Event-driven, non-blocking, decoupled; messages persist in broker queues even if consumer is temporarily down.

### Q25: Explain the Circuit Breaker Pattern and Resilience4j states.
Prevents system-wide failure by cutting off calls to unhealthy downstream dependencies.
- **CLOSED:** Normal operations; requests pass through.
- **OPEN:** Failure rate threshold exceeded (e.g. 50%); calls fail immediately and execute fallback methods.
- **HALF-OPEN:** Allows limited test traffic after wait duration. If successful, resets to CLOSED; otherwise re-opens.

### Q26: What is Distributed Tracing, and how do Trace ID, Span ID, and Micrometer Tracing work?
- **Trace ID:** Unique ID assigned to an inbound request at the API Gateway and passed across all downstream microservices via HTTP headers (`traceparent`).
- **Span ID:** Identifies a single unit of work within a specific microservice.
- **Micrometer Tracing:** Propagates context and exports metrics/spans to Zipkin or Jaeger for distributed latency visualization.

### Q27: What is the Saga Pattern, and how do Orchestration and Choreography Sagas differ?
Manages distributed transactions across multiple microservices without 2-phase commit (2PC).
- **Choreography:** Decentralized; services emit and listen to domain events across Kafka topics.
- **Orchestration:** Central orchestrator sends explicit commands to participants and triggers compensating transactions if any step fails.

### Q28: What is CQRS (Command Query Responsibility Segregation) and Event Sourcing?
- **CQRS:** Separates write operations (commands) from read operations (queries) into distinct data models and databases.
- **Event Sourcing:** Persists every state change as an immutable append-only event log; current state is reconstructed by replaying events.

### Q29: How does Centralized Configuration work with Spring Cloud Config Server and Dynamic Refresh?
Stores environment configurations in a centralized Git or Vault repository. Services fetch configurations at bootstrap. Updating properties without restarting instances is achieved using `@RefreshScope` and triggering `/actuator/busrefresh` over Spring Cloud Bus.

### Q30: What is the Database-per-Service pattern, and how do you handle cross-service queries?
Each microservice maintains its own private database. Direct database cross-access is forbidden.
- Cross-service queries are handled via **API Composition** (gateway/aggregator fetches and merges payloads) or **CQRS Read Models** (replicated denormalized projections synced via Kafka events).

### Q31: What is Idempotency, and how do you guarantee it in distributed payment/order APIs?
An operation that yields identical state when executed multiple times.
- Implement via an `Idempotency-Key` header: Store the key in Redis with a `PROCESSING` lock, execute the database transaction, update key status to `SUCCESS` with the cached response, and return cached responses on retried duplicate keys.

### Q32: How do you implement End-to-End Security across Microservices?
1. Authenticate users at API Gateway via OAuth 2.0 / OpenID Connect (OIDC).
2. Validate JWT at Gateway and propagate downstream in `Authorization: Bearer <token>` header.
3. Encrypt service-to-service internal traffic via mutual TLS (mTLS) managed by a Service Mesh (Istio).

### Q33: What is the Strangler Fig Pattern for monolithic migration?
Incrementally replaces monolith modules with microservices by intercepting incoming traffic at the API Gateway / Reverse Proxy, directing new or migrated routes to microservices while routing residual traffic to the legacy monolith until it can be retired.

### Q34: What is the Transactional Outbox Pattern and Change Data Capture (CDC)?
Solves the dual-write problem (writing to DB and publishing to message broker atomically).
- The business record and outbox message are written to the database in a **single local transaction**.
- A CDC tool like **Debezium** reads the database transaction log (WAL / binlog) and reliably streams outbox records to Kafka.

### Q35: What is a Service Mesh, and how does it compare to Spring Cloud?
A dedicated infrastructure layer using sidecar proxies (Envoy) next to each container (Istio). Handles mTLS, traffic splitting (canary releases), and observability at the network layer in a language-agnostic manner, whereas Spring Cloud relies on Java libraries compiled into the application JAR.

### Q36: How do Kubernetes Primitives map to Microservices Architecture?
- **Pod:** Deploys containerized service instances.
- **Deployment:** Manages declarative rolling updates and replica scaling.
- **Service (ClusterIP):** Internal stable load-balanced virtual IP for pods.
- **Ingress:** Routes external traffic into internal services.
- **ConfigMap / Secret:** Injects environment configuration and credentials.
- **HPA:** Horizontally autoscales pods based on CPU/memory/metrics.

### Q37: How do you handle Dead Letter Queues (DLQ) and Poison Pill messages in Event Streams?
Configure consumer retries with exponential backoff. If max retries are exhausted, route the failed message with stack trace headers to a DLQ topic (e.g. `orders.DLQ`), commit offset on the primary partition to prevent blocking, and alert engineers for remediation.

### Q38: Explain Kubernetes Liveness, Readiness, and Startup Probes with Spring Boot Actuator.
- **Liveness (`/actuator/health/liveness`):** Detects if application is deadlocked or broken; failure triggers container restart.
- **Readiness (`/actuator/health/readiness`):** Detects if service can accept traffic; failure removes pod from load balancer endpoints.
- **Startup:** Delays liveness checking until slow initialization finishes.

### Q39: What is Consumer-Driven Contract Testing (Pact / Spring Cloud Contract)?
Consumers define contract files detailing expected HTTP/event requests and responses. Providers run automated tests against these contracts during their build pipelines. Prevents broken API changes from ever deploying to production.

### Q40: What are Distributed Caching strategies in Microservices?
- **Cache-Aside (Lazy Loading):** Service queries cache; on miss, reads DB, populates cache, and returns.
- **Write-Through:** Synchronously updates cache and DB together.
- **Write-Behind:** Updates cache immediately; cache asynchronously batches writes to DB.
- **Invalidation:** Services emit entity update events over Kafka so peer services invalidate local caches; always configure TTLs.

---

# Part 3: JPA, Hibernate & Spring Data JPA (20 Questions)

### Q41: What is the relationship and difference between JPA, Hibernate, and Spring Data JPA?
- **JPA:** Official Jakarta specification for ORM (interfaces and annotations like `@Entity`, `@Id`, `EntityManager`).
- **Hibernate:** The ORM engine implementing the JPA specification with additional proprietary features.
- **Spring Data JPA:** High-level repository abstraction layer built over JPA/Hibernate that generates queries automatically from interface methods (`JpaRepository`).

### Q42: Explain the 4 Entity Lifecycle States in JPA.
1. **Transient:** Newly instantiated object; not in DB, not managed by `EntityManager`.
2. **Persistent (Managed):** Has DB identifier; tracked by `EntityManager`; changes automatically saved on transaction commit via dirty checking.
3. **Detached:** Has DB identifier, but session is closed or cleared; mutations are not tracked automatically. Re-attach via `merge()`.
4. **Removed:** Scheduled for deletion on commit via `remove()`.

### Q43: Explain Hibernate First-Level (L1) vs Second-Level (L2) Cache.
- **L1 Cache:** Bound to current `Session`/`EntityManager`; always active; not thread-safe; destroyed when transaction/session closes.
- **L2 Cache:** Bound to `SessionFactory`; optional (Ehcache, Redis, Hazelcast); shared across all sessions and transactions; caches entities across requests.
- **Query Cache:** Caches query parameters and entity primary keys; requires L2 cache to be enabled.

### Q44: What is the N+1 Query Problem in JPA, and how do you resolve it?
Fetching $N$ parent entities generates 1 query, and accessing child relationships triggers $N$ additional queries ($1 + N$ queries).
- **Solutions:** Use `JOIN FETCH` in JPQL, `@EntityGraph(attributePaths = {"children"})`, `@BatchSize(size = 25)`, or DTO constructor projections.

### Q45: Compare Lazy vs Eager Loading and how to prevent `LazyInitializationException`.
- **EAGER:** Relationship fetched immediately with parent.
- **LAZY:** Association replaced with proxy, loaded on first access.
- `@ManyToOne` and `@OneToOne` default to `EAGER` (should be changed to `LAZY`). `@OneToMany` defaults to `LAZY`.
- **`LazyInitializationException`:** Occurs when accessing lazy associations outside an active transaction. Resolve using `JOIN FETCH`, `@EntityGraph`, or DTO projections. Avoid `spring.jpa.open-in-view=true` in production.

### Q46: How do you model `@OneToMany` and `@ManyToOne` relationships properly?
The `@ManyToOne` side owns the foreign key (`@JoinColumn`). The `@OneToMany` side is inverse and must specify `mappedBy = "<fieldName>"`. Always provide bidirectional helper synchronization methods (`addChild`, `removeChild`) to update both sides in memory.

### Q47: Difference between `CascadeType.REMOVE` and `orphanRemoval = true`.
- `CascadeType.REMOVE`: Deletes child rows only when the parent entity itself is deleted via `entityManager.remove()`.
- `orphanRemoval = true`: Deletes child rows when the parent is deleted **AND** when a child is removed from the parent's collection (`parent.getChildren().remove(child)`).

### Q48: Compare Spring Data JPA Querying Mechanisms.
- **Derived Methods:** `findByEmailAndActive(String email, boolean active)` (great for simple queries).
- **JPQL (`@Query`):** Object-oriented query against entity classes and relationships.
- **Native SQL (`@Query(nativeQuery = true)`):** Raw SQL for database-specific optimizations and functions.
- **JPA Specifications / Criteria API:** Programmatic, type-safe queries ideal for dynamic e-commerce multi-filter search forms.

### Q49: What are Spring Data JPA Projections (DTO vs Interface)?
Fetch only required columns instead of entire entity graphs.
- **Interface Projections:** Spring creates dynamic proxies for getters (`getUsername()`).
- **Class/Record DTO Projections:** Constructor expression (`SELECT new com.example.UserDto(u.id, u.name) FROM User u`) instantiates DTOs directly with no proxy overhead.

### Q50: How does Spring `@Transactional` work, and what are its Propagation Behaviors?
Spring creates an AOP proxy around the method that manages transaction boundary and commit/rollback.
- **`REQUIRED` (default):** Joins current transaction or creates new if none exists.
- **`REQUIRES_NEW`:** Suspends current transaction and creates a new independent transaction.
- **`SUPPORTS` / `NOT_SUPPORTED` / `MANDATORY` / `NEVER` / `NESTED`.

### Q51: Explain Transaction Isolation Levels and Concurrency Anomalies.
- **Anomalies:** Dirty Read (uncommitted data read), Non-Repeatable Read (re-read data altered), Phantom Read (re-read range contains new rows).
- **Levels:** `READ_UNCOMMITTED` (all anomalies allowed) $\rightarrow$ `READ_COMMITTED` (prevents dirty reads) $\rightarrow$ `REPEATABLE_READ` (prevents dirty & non-repeatable reads) $\rightarrow$ `SERIALIZABLE` (prevents all anomalies).

### Q52: Why does Self-Invocation break `@Transactional`, and how do you solve it?
Calling a transactional method from within the same class (`this.methodB()`) bypasses the Spring AOP proxy and calls the raw target instance directly.
- **Fix:** Move the method to a separate service bean, inject the service into itself via `@Lazy`, or use programmatic `TransactionTemplate`.

### Q53: Compare Optimistic vs Pessimistic Locking in JPA.
- **Optimistic Locking:** Uses `@Version` column. Updates verify version hasn't changed; throws `OptimisticLockException` on concurrent collision. Best for read-heavy systems.
- **Pessimistic Locking:** Uses database-level locks (`SELECT ... FOR UPDATE` via `@Lock(LockModeType.PESSIMISTIC_WRITE)`). Other transactions wait. Best for high-conflict financial/inventory transactions.

### Q54: How does Spring Data JPA Auditing work?
Annotate configuration with `@EnableJpaAuditing`. Add `@EntityListeners(AuditingEntityListener.class)` to entities or a `@MappedSuperclass`. Annotate fields with `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, and `@LastModifiedBy`, and provide an `AuditorAware<String>` bean.

### Q55: Compare `Page<T>` vs `Slice<T>` vs Keyset Pagination in Spring Data JPA.
- `Page<T>`: Runs data query + expensive `COUNT(*)` query to calculate total pages.
- `Slice<T>`: Fetches `size + 1` elements to check `hasNext()` without executing a count query; ideal for infinite scroll feeds.
- Keyset (Cursor): Uses `WHERE id > :lastId ORDER BY id ASC LIMIT :size` for constant $O(1)$ query time on massive datasets.

### Q56: Explain JPA Inheritance Mapping Strategies.
- `SINGLE_TABLE`: All subclasses stored in one table with a discriminator column; fastest queries; forces subclass columns to be nullable.
- `JOINED`: Subclasses map to dedicated tables joined to parent by foreign key; normalized, but requires joins.
- `TABLE_PER_CLASS`: Each concrete class maps to independent table; polymorphic queries use slow `UNION`.
- `@MappedSuperclass`: Shares common properties across entity tables without inheritance queries.

### Q57: How do you map Composite Primary Keys in JPA?
Use `@EmbeddedId` with an `@Embeddable` class that implements `Serializable` and overrides `equals()` and `hashCode()`. (Alternatively, use `@IdClass`).

### Q58: What are JPA Entity Lifecycle Callbacks and `EntityListeners`?
Lifecycle hooks on entities: `@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`, `@PreRemove`, `@PostRemove`, `@PostLoad`. Used for field hashing, encryption, or external change logging.

### Q59: How does Hibernate Dirty Checking work, and what is `@DynamicUpdate`?
During flush, Hibernate compares current entity field values against the initial snapshot taken when the entity entered the persistence context and automatically generates an `UPDATE`.
- `@DynamicUpdate`: Generates dynamic SQL including only modified columns rather than all columns.

### Q60: How do you perform high-performance Batch Inserts and Updates in Hibernate?
Set `hibernate.jdbc.batch_size=50`, `order_inserts=true`, and `order_updates=true`. Use `GenerationType.SEQUENCE` (not `IDENTITY`). In code, call `entityManager.flush()` and `entityManager.clear()` every 50 records to clear the first-level cache memory.

---

# Part 4: Angular Framework (20 Questions)

### Q61: What is Angular, and what are its core architectural building blocks?
A TypeScript-based framework for scalable web applications. Core blocks: Components (UI & logic), Templates (HTML views), Directives (behavior modifiers), Services (injected singletons/scoped logic), Pipes (display transformers), and Router (SPA navigation).

### Q62: What are Standalone Components, and how do they replace `NgModule`?
Introduced in Angular 14 and default in Angular 17+. Standalone components directly declare their own dependencies via `imports: [...]` in the `@Component` decorator, removing `NgModule` boilerplate and simplifying lazy loading.

### Q63: Walk through the Angular Component Lifecycle Hooks.
`constructor` $\rightarrow$ `ngOnChanges` $\rightarrow$ `ngOnInit` $\rightarrow$ `ngDoCheck` $\rightarrow$ `ngAfterContentInit` $\rightarrow$ `ngAfterContentChecked` $\rightarrow$ `ngAfterViewInit` $\rightarrow$ `ngAfterViewChecked` $\rightarrow$ `ngOnDestroy`. In Angular 16+, `afterRender` and `afterNextRender` handle post-render DOM operations.

### Q64: How does Change Detection work in Angular? Compare `Default` vs `OnPush`.
Synchronizes model changes to the DOM.
- **Default:** Zone.js intercepts browser events and checks every component in the entire tree top-to-bottom.
- **OnPush:** Skips the component unless an `@Input()` reference changes, an event originated inside the component, an `async` pipe emits, or signals update.

### Q65: What are Angular Signals (Angular 16+), and how do they enable Zoneless Angular?
Signals are reactive value wrappers with fine-grained dependency tracking.
- `signal(val)`: Writable signal.
- `computed(() => fn)`: Memoized derived signal.
- `effect(() => fn)`: Side-effect runner.
- **Zoneless:** Signals tell Angular exactly which DOM node needs updating, enabling Zone.js-free rendering.

### Q66: What are Signal-Based Component Inputs, Outputs, and Models (Angular 17.2+)?
Modern signal-based component API replacing decorators:
- `title = input.required<string>()` (Signal input)
- `count = model<number>(0)` (Two-way signal binding)
- `clicked = output<void>()` (Event emitter)

### Q67: Explain the Built-in Control Flow Syntax in Angular 17+ vs Legacy Directives.
Replaces `*ngIf`, `*ngFor`, `*ngSwitch` with native template syntax:
- `@if (condition) { ... } @else { ... }`
- `@for (item of items; track item.id) { ... } @empty { ... }`
- `@switch (status) { @case ('A') { ... } @default { ... } }`

### Q68: How does Angular's Hierarchical Dependency Injection work, and what is the `inject()` function?
Injectors form a tree: Platform $\rightarrow$ Root (`providedIn: 'root'`) $\rightarrow$ Environment $\rightarrow$ Component/Element Injector.
The `inject(Service)` function provides functional dependency injection without constructor parameters.

### Q69: Compare Observables, Promises, and Subjects in RxJS.
- **Promise:** Eager, single value, non-cancellable.
- **Observable:** Lazy, multiple values over time, cancellable via `unsubscribe()`.
- **Subject:** Multicast observable and observer. `BehaviorSubject` stores current value; `ReplaySubject` stores $N$ past values.

### Q70: Why should you prefer the `async` pipe, and how do you prevent RxJS memory leaks?
The `async` pipe automatically subscribes, unsubscribes upon component destruction, and triggers `OnPush` change detection. Prevent memory leaks in manual subscriptions using `takeUntilDestroyed(this.destroyRef)` or manual unsubscriptions in `ngOnDestroy`.

### Q71: Explain key RxJS Flattening Operators (`switchMap`, `mergeMap`, `concatMap`, `exhaustMap`).
- `switchMap`: Cancels previous inner observable on new outer emission (auto-complete search).
- `mergeMap`: Runs all inner observables concurrently in parallel.
- `concatMap`: Queues and runs inner observables sequentially.
- `exhaustMap`: Ignores new outer emissions while an inner observable is still active (login/submit buttons).

### Q72: How does Angular Routing work, and how do you implement Component Lazy Loading?
Use `loadComponent: () => import('./path').then(m => m.MyComponent)` inside route definitions to split bundles. Enable `withComponentInputBinding()` to bind route parameters (`:id`) directly to component inputs.

### Q73: What are Route Guards, and how do you write Functional Guards (`CanActivateFn`)?
Guards protect navigation. Use modern functional guards:
`export const authGuard: CanActivateFn = (route, state) => inject(AuthService).isLoggedIn() || inject(Router).createUrlTree(['/login']);`

### Q74: Compare Template-Driven Forms vs Reactive Forms.
- **Template-Driven:** Bound in HTML via `[(ngModel)]`; asynchronous; validation in template; suitable for simple forms.
- **Reactive Forms:** Structured in TypeScript via `FormGroup`/`FormControl`; synchronous reactive streams; robust programmatic custom validation; ideal for complex dynamic enterprise forms.

### Q75: How do you configure Modern Functional HTTP Interceptors (`HttpInterceptorFn`)?
Define `export const authInterceptor: HttpInterceptorFn = (req, next) => next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));` and register via `provideHttpClient(withInterceptors([authInterceptor]))` in `appConfig.ts`.

### Q76: Explain Pure vs Impure Pipes, and why you should avoid calling methods in templates.
- **Pure Pipes:** Cached; executed only when input primitive or reference changes.
- **Impure Pipes / Methods in Templates:** Evaluated on every change detection cycle, creating severe performance bottlenecks. Replace method calls with pure pipes or `computed()` signals.

### Q77: What is Content Projection (`<ng-content>`) and `viewChild()` / `contentChild()`?
- `<ng-content>` projects markup from parent to child.
- `viewChild('ref')` and `contentChild('ref')` (Angular 17.2+) query template and projected DOM elements as reactive signals.

### Q78: What are State Management strategies in Angular? When should you use NgRx?
- Simple apps: Use Injectable Services with Signals or `BehaviorSubject`.
- Complex enterprise apps: Use **NgRx Store** (Global Redux pattern: Actions, Reducers, Effects, Selectors) for strict unidirectional data flow, time-travel debugging, and multi-team consistency.

### Q79: What are the key Performance Optimization techniques in Angular?
Use `OnPush` change detection; defer non-critical UI via `@defer (on viewport)`; use `NgOptimizedImage` for responsive images; specify `track` in `@for`; route-level lazy loading; SSR and non-destructive hydration.

### Q80: How do you write Unit Tests in Angular using `TestBed` and `HttpTestingController`?
Configure test modules via `TestBed.configureTestingModule()`, inject dependencies with `TestBed.inject()`, mock backend HTTP requests with `HttpTestingController.expectOne()`, flush mock data with `req.flush()`, and verify with `httpTesting.verify()`.


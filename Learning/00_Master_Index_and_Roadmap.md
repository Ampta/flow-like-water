# Core Java, Multithreading & Modern JVM Complete Interview Mastery Series

Welcome to your complete, production-grade Core Java, Collections, Multithreading, JVM, and Modern Architecture reference library. Every chapter is packed with deep-dive technical explanations, internal JVM mechanics, ASCII diagrams, scenarios, Allowed vs Forbidden matrices, and production code snippets.

---

## 📚 Master Index of Chapters

### 1. [Chapter 1: OOP Hierarchy, Inheritance & Sealed Classes](file:///Users/apple/Workspace/Home/Javascript/Learning/01_OOP_Hierarchy_and_Sealed_Classes.md)
- **Abstract Classes vs Concrete Methods:** Creation rules, mandatory vs optional overrides, `final`, `abstract`, `private`, `static` method constraints.
- **Interfaces & Abstract Classes:** Why interfaces cannot extend classes, how abstract classes implement partial interfaces.
- **Sealed Classes & Sealed Interfaces (Java 17+):** `permits`, `final`, `sealed`, `non-sealed` modifiers, same-package constraints, and exhaustive pattern matching in `switch`.
- **Dynamic Method Dispatching:** Reference vs Object types (`Parent p = new Child()`), variable shadowing vs method overriding, static method hiding, covariant return types, and access modifier rules.

---

### 2. [Chapter 2: Inner Classes, Static Nested Types & Records](file:///Users/apple/Workspace/Home/Javascript/Learning/02_Inner_Classes_and_Modern_Java.md)
- **Nested & Inner Classes:** Static Nested vs Member Inner vs Local Inner vs Anonymous Inner classes.
- **Instantiation & Access Rules:** Outer `this` reference, accessing outer private members, static members in inner classes.
- **The Effectively Final Rule:** Why local variables in lambdas and inner classes must be final (Stack vs Heap lifecycle mechanics).
- **Records vs DTOs vs POJOs vs JPA Entities:** Immutability, compact constructors, validation, built-in value-based `equals()`/`hashCode()`, and architectural use-cases.

---

### 3. [Chapter 3: Exception Handling Architecture, Propagation & Patterns](file:///Users/apple/Workspace/Home/Javascript/Learning/03_Exception_Handling_Deep_Dive.md)
- **Throwable Hierarchy:** `Error` vs `Checked Exception` vs `Unchecked RuntimeException`.
- **Exception Propagation:** Call stack unrolling and bubbling up across methods.
- **`ClassNotFoundException` vs `NoClassDefFoundError`:** Compile-time vs runtime static initializer failures.
- **`NullPointerException` Prevention:** `Objects.requireNonNull`, `Optional`, and Elvis pattern.
- **`ConcurrentModificationException` Mechanics:** `modCount` vs `expectedModCount` fail-fast validation.
- **`throw` vs `throws` & Custom Exceptions:** Exception chaining (`super(msg, cause)`) and error codes.
- **`try-catch-finally` & `try-with-resources`:** Return statement puzzles, reverse closing order, and suppressed exceptions.

---

### 4. [Chapter 4: Collections Framework Master Guide](file:///Users/apple/Workspace/Home/Javascript/Learning/04_Collections_Framework_Deep_Dive.md)
- **Collection Framework Hierarchy:** `List`, `Set`, `Queue`, and `Map`.
- **List vs Set:** Duplicate rules, indexing, and null element constraints.
- **ArrayList vs LinkedList:** Contiguous memory blocks vs doubly linked nodes, CPU cache spatial locality.
- **HashMap vs Hashtable:** Concurrency, null keys/values, and modern replacements.
- **HashMap Internal Working:** `table` node array, hash spreading `h ^ (h >>> 16)`, bitwise index `(n - 1) & hash`, collision resolution, and Red-Black treeification (threshold 8, min capacity 64, untreeify 6).
- **HashSet Internal Working:** Backed by `HashMap` with dummy `PRESENT` object; why Sets disallow duplicates.
- **LinkedHashSet vs HashSet vs TreeSet:** Insertion order vs sorted order (Red-Black tree).
- **ConcurrentHashMap vs HashMap:** CAS empty bucket insertion + head node synchronization; why null is strictly forbidden.
- **Fail-Fast vs Fail-Safe (Weakly Consistent) Iterators:** `modCount` checks vs snapshots.
- **Comparable vs Comparator:** Natural single ordering vs multi-field chaining, and the integer subtraction overflow bug.
- **Iterator vs ListIterator:** Forward vs bi-directional traversal.
- **Thread-Safe Collections:** `ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`.

---

### 5. [Chapter 5: Multithreading, Concurrency, Thread Pools & Modern Async](file:///Users/apple/Workspace/Home/Javascript/Learning/05_Multithreading_and_Concurrency.md)
- **Thread Lifecycle & States:** `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED`.
- **Runnable vs Callable:** Return types, checked exceptions, and thread pool integration.
- **`volatile` vs `synchronized`:** CPU L1/L2 cache coherency, visibility vs mutual exclusion, why `volatile count++` has race conditions.
- **`wait()` vs `sleep()` & `notify()` vs `notifyAll()`:** Lock release rules, wait set waking, and avoiding lost signals.
- **`ExecutorService` & `ThreadPoolExecutor` Internals:** Core pool size, Max pool size, Work queue saturation, and 4 rejection policies (`AbortPolicy`, `CallerRunsPolicy`, `DiscardPolicy`, `DiscardOldestPolicy`).
- **`FutureTask` vs `CompletableFuture`:** Building non-blocking asynchronous event pipelines (`supplyAsync`, `thenApply`, `thenCompose`).
- **Deadlock & Detection:** 4 Coffman conditions, lock ordering, and detection via `ThreadMXBean` / `jstack`.
- **Java 21 Virtual Threads:** User-mode fibers on top of carrier OS threads.

---

### 6. [Chapter 6: Java 8 Functional Programming, Lambdas & Streams](file:///Users/apple/Workspace/Home/Javascript/Learning/06_Java8_Functional_and_Streams_Deep_Dive.md)
- **Java 8 Core Highlights:** Lambdas, functional interfaces, date/time API, default methods.
- **Functional Interfaces:** `@FunctionalInterface` SAM rule, `Predicate`, `Function`, `Consumer`, `Supplier`, and chaining.
- **Method References (`::`):** Static, instance, arbitrary object, and constructor references.
- **`Optional<T>`:** `of`, `ofNullable`, `orElse`, `orElseGet`, `orElseThrow`, `ifPresent`, and anti-patterns.
- **Stream API Architecture:** Intermediate (stateless/stateful) vs Terminal (short-circuiting) operations.
- **`map()` vs `flatMap()`:** 1-to-1 transformation vs 1-to-many flattening.
- **Lazy vs Eager Operations:** Pull-based pipeline execution triggered exclusively by terminal operations.
- **Parallel Streams & Custom Collectors:** `ForkJoinPool.commonPool` and `Collector.of()`.

---

### 7. [Chapter 7: Java 17 LTS & Modern Java Evolution](file:///Users/apple/Workspace/Home/Javascript/Learning/07_Java17_and_Modern_Java_Evolution.md)
- **Java 17 LTS Features:** Records, Sealed classes, Pattern matching for `instanceof`, Text blocks, Switch expressions, Helpful NullPointerExceptions.
- **Java 8 vs Java 17:** PermGen vs Metaspace, POJOs vs Records, Parallel GC vs G1/ZGC, language ergonomics.
- **Pattern Matching for `instanceof` & Switch:** Type casting elimination and exhaustive switches.
- **Text Blocks (`"""`):** Multi-line formatted strings without escape characters.
- **Virtual Threads Evolution:** From Java 17 foundational fibers to Java 21 LTS standard.

---

### 8. [Chapter 8: JVM Architecture, Memory Model, Garbage Collection & ClassLoaders](file:///Users/apple/Workspace/Home/Javascript/Learning/08_JVM_Architecture_Memory_and_GC.md)
- **JVM Runtime Memory Layout:** Heap, Metaspace, JVM Stack, PC Registers, Native Method Stack.
- **Heap vs Stack:** Object allocations vs stack frames, thread safety, `OutOfMemoryError` vs `StackOverflowError`.
- **Instance vs Static Variables Memory Allocation:** Heap object fields vs Metaspace class metadata.
- **String Constant Pool (SCP) & `intern()`:** Memory deduplication, `s1 == s2` vs `s1 == s3`, and heap allocation differences.
- **Garbage Collection & Generational Hypothesis:** Eden, Survivor S0/S1, Tenured Old Gen, Minor GC vs Major/Full GC.
- **Memory Leaks in Java:** Static collection retention, unclosed streams, `ThreadLocal` leaks, inner class leaks.
- **`OutOfMemoryError` Types:** `Java heap space`, `Metaspace`, `GC overhead limit exceeded`, `Unable to create new native thread`, `Direct buffer memory`.
- **ClassLoader Architecture & Delegation Model:** Bootstrap, Platform/Extension, Application/System, Parent-First delegation.

---

### 9. [Chapter 9: Spring Boot Core, Dependency Injection & Annotation Architecture](file:///Users/apple/Workspace/Home/Javascript/Learning/09_Spring_Boot_Core_and_Annotations_Mastery.md)
- **`main()` Overriding & Overloading:** Static binding vs dynamic dispatch.
- **IoC Container & Dependency Injection Internals:** Why manual instantiation fails AOP proxying and lifecycle hooks.
- **`@Component` vs `@Bean` & Stereotype Annotations:** `@Repository`, `@Service`, `@Controller`, `@RestController`.
- **`@Autowired` Internal Mechanics:** `AutowiredAnnotationBeanPostProcessor`, byType/byName resolution, why Constructor Injection is the industry standard.
- **Bean Scopes & Lookup Method Injection:** Resolving the prototype-in-singleton stale dependency issue.
- **`@Transactional` Architecture:** Dynamic CGLIB proxies, transaction propagation (`REQUIRED` vs `REQUIRES_NEW`), rollback rules, and handling partial failures.
- **Multi-DataSource Configuration:** Wiring primary and secondary databases in a single Spring Boot application.
- **Centralized Configuration:** Git-backed Spring Cloud Config Server vs local `application.properties` and `@RefreshScope`.

---

### 10. [Chapter 10: Spring Security, JWT & Enterprise Auth Architecture](file:///Users/apple/Workspace/Home/Javascript/Learning/10_Spring_Security_JWT_and_Auth_Architecture.md)
- **Spring Security Filter Chain:** `DelegatingFilterProxy`, `FilterChainProxy`, and custom `OncePerRequestFilter`.
- **JWT Architecture & Internals:** Header, Payload, and Signature mechanics; why JWT is signed, not encrypted.
- **Hashing vs Encryption:** BCrypt salt mechanics, constant-time verification; why password "unhashing" is mathematically impossible.
- **Compromised JWT Mitigation:** Short-lived access tokens, Refresh Token Rotation (RTR), and Redis session blacklists.
- **CSRF Defense:** Why stateless REST APIs with custom `Authorization: Bearer` headers disable CSRF.
- **Role-Based Access Control (RBAC):** `GrantedAuthority`, `@PreAuthorize`, and SpEL expressions.
- **Cloud Document Security:** AES-256 KMS encryption, UUID key paths, and SHA-256 tamper-detection for Aadhaar and PAN cards.

---

### 11. [Chapter 11: Microservices, Distributed Systems & System Design](file:///Users/apple/Workspace/Home/Javascript/Learning/11_Microservices_Cloud_and_Distributed_Systems.md)
- **Monolith vs Microservices & 12-Factor App:** Architecture tradeoffs, bounded contexts, and independence.
- **Inter-Service Communication:** OpenFeign declarative client vs RestTemplate vs asynchronous message brokers (Kafka/RabbitMQ).
- **Service Discovery & Registry:** Eureka client-side registry caching vs Kubernetes native DNS.
- **API Gateway & Rate Limiting:** Spring Cloud Gateway and Token Bucket rate limiting via Redis.
- **Cascading Failures & Resilience:** Resilience4j Circuit Breaker state machine (Closed, Open, Half-Open), Timeouts, and Fallbacks.
- **Distributed Transactions:** Two-Phase Commit (2PC) limitations vs Saga Pattern (Orchestration vs Choreography).
- **Production Incident Management:** Troubleshooting API latency spikes (2ms to 2s), high CPU utilization (100% CPU thread dump diagnosis), and thread pool saturation.

---

### 12. [Chapter 12: Database Performance, SQL Queries, Hibernate & JPA Internals](file:///Users/apple/Workspace/Home/Javascript/Learning/12_Database_SQL_Hibernate_JPA_Performance.md)
- **Nth Highest & 2nd Highest Salary Queries:** Window functions (`DENSE_RANK()`) vs correlated subqueries.
- **Deleting Duplicate Records:** CTE with `ROW_NUMBER() OVER (PARTITION BY ...)` vs self-joins.
- **SQL Joins & Subqueries:** INNER, LEFT, RIGHT, FULL, and CROSS joins; filtering without `WHERE` clauses.
- **Constraints & Differences:** Primary vs Unique keys, `TRUNCATE` vs `DELETE` vs `DROP`.
- **Hibernate vs JPA & 4 Entity States:** Transient, Managed/Persistent, Detached, Removed.
- **Hibernate `get()` vs `load()`:** Eager DB hit vs CGLIB lazy proxy.
- **The N+1 Query Problem:** Diagnosis, JPQL `JOIN FETCH`, Spring Data `@EntityGraph`, and batch fetching.
- **Hibernate Caching Architecture:** First-Level (L1 Session) vs Second-Level (L2 SessionFactory with Redis/EhCache).
- **Pagination for 10 Million Records:** Why `OFFSET` degrades performance and how Keyset Pagination (Seek method) achieves constant $O(\log N)$ time.
- **Sharding vs Partitioning:** Range/Hash table partitioning vs horizontal sharding across database nodes.

---

### 13. [Chapter 13: All 57-Page Coding Challenges & Stream API Solutions](file:///Users/apple/Workspace/Home/Javascript/Learning/13_Coding_Challenges_and_Stream_API_Solutions.md)
- **Even/Odd In-Place Sorting without Built-in Methods** (Inferyx).
- **In-Place Array Reversal using Two Pointers** (Inferyx).
- **Nth Highest Price Product from Map via Streams** (Applied Cloud Computing).
- **File Extension Frequency Map via Streams** (Applied Cloud Computing).
- **Integer to Binary Conversion without Library Methods** (NeoSoft).
- **Sum of Digits in Alphanumeric String "ABCXYZ123"** (Rebit).
- **Character with Second Highest Frequency** (Rebit).
- **Multi-Level Grouping by Designation and Gender** (Cbt Infotech / Rebit).
- **City Name Character Length Frequency** (Crisil).
- **Reverse Words "hello world" -> "olleh dlrow"** (In Solutions Global).
- **Shift All Zeros to Right Side in $O(N)$** (NuSummit).
- **Longest Consecutive Increasing Subarray** (Servosys).
- **Rightmost Digits Transformation** (V2STech Solutions).
- **StudentDTO Filtering, Sorting & Email Extraction** (Team Computers).
- **Stream Lazy Evaluation Trap:** Why `list.stream().map(i -> i * i)` does not mutate the list.

---

### 14. [Chapter 14: Angular Frontend, RxJS & Fullstack Integration](file:///Users/apple/Workspace/Home/Javascript/Learning/14_Angular_Frontend_and_FullStack_Integration.md)
- **Angular Architecture & Lifecycle Hooks:** `ngOnInit`, `ngOnChanges`, `ngAfterViewInit`, `ngOnDestroy`.
- **Component Communication:** `@Input()`, `@Output()` + `EventEmitter`, `@ViewChild`, and Shared Services with `BehaviorSubject`.
- **RxJS Observables vs Promises:** Lazy evaluation, cancellation, and preventing memory leaks with `takeUntilDestroyed`.
- **Debouncing API Calls:** Search autocomplete with `debounceTime(300)` and `switchMap()`.
- **Angular HTTP Interceptors:** Appending JWT Bearer tokens and global 401 error handling.
- **AOT vs JIT Compilation:** Bundle size optimization and compile-time template checks.
- **Route Guards & Navigation State:** `CanActivateFn`, passing data via router extras, and CORS setup with Spring Boot.

---

### 15. [Chapter 15: Real-World Project (LOS) Architecture & Production Scenario Interviews](file:///Users/apple/Workspace/Home/Javascript/Learning/15_Real_World_Project_LOS_and_Scenario_Interviews.md)
- **Loan Origination System (LOS) End-to-End Architecture:** Microservices breakdown, data flows, and Kafka event streaming.
- **CIBIL / Credit Bureau Underwriting Integration:** Score evaluation rules and external mTLS API calls.
- **Aadhaar & PAN Verification Workflow:** NSDL format checks, string similarity matching, and UIDAI OTP eKYC compliance.
- **Hardest Production Bug Solved:** Resolving concurrent database deadlocks via strict lock ordering and atomic decrements.
- **Production Incident Management:** P1 outage triage, thread dump analysis with `jcmd`, and root cause analysis (RCA).
- **Daily Agile Roles & Behavioral Answers:** Daily standups, BRD reviews, sprint cycles, and why you are switching jobs.

---

### 16. [Chapter 16: Angular Core Fundamentals, CLI & Template Bindings](file:///Users/apple/Workspace/Home/Javascript/Learning/16_Angular_Core_Fundamentals_and_Architecture.md)
- **Features in Angular 7+:** Ivy compiler, Standalone Components, Typed Forms, Angular Signals, and modern control flow (`@if`, `@for`).
- **Angular vs AngularJS:** MVC/Scope vs Component Tree, JS vs TypeScript, and change detection performance.
- **TypeScript in Angular:** Static typing, decorator reflection, generics, and compile-time guarantees.
- **Angular CLI Mastery:** Scaffolding, production builds (`ng build`), and automated migrations (`ng update`).
- **Template Directives:** `*ngIf`, `*ngFor` with `trackBy`, and `[ngSwitch]` multi-branch workflows.
- **Project Creation:** Monorepos, standalone application architecture, and SCSS setup.
- **Components vs Directives vs Services:** Presentation vs DOM behavior vs domain logic.
- **Interpolation & Property Binding:** Text nodes `{{ }}` vs DOM properties `[ ]` (preserving booleans and objects).
- **Two-Way Data Binding:** `[(ngModel)]` de-sugaring into property and event bindings; custom banana-in-a-box components.
- **Event & Attribute Binding:** User interactions `(click)` vs HTML attribute overrides (`[attr.colspan]`, ARIA tags).

---

### 17. [Chapter 17: Component Communication, Directives & Pipes](file:///Users/apple/Workspace/Home/Javascript/Learning/17_Angular_Component_Communication_Directives_and_Pipes.md)
- **`@Input()` & `@Output()` Decorators:** Unidirectional data flow ("Data down, events up"), setter/getter interceptors, and modern Signal inputs (`input.required()`).
- **Parent-Child Communication:** Template references (`#ref`), `@ViewChild` programmatic control, and shared singleton event buses.
- **Structural vs Attribute Directives:** DOM layout destruction/instantiation (`*ngIf`) vs host behavior enhancement; why only one structural directive per element is allowed.
- **`ngClass` vs `ngStyle`:** Class-based centralized CSS styling vs dynamic calculated runtime properties (progress bars).
- **Pipes Architecture:** Built-in formatters, `AsyncPipe` memory leak prevention, and custom pure masking pipes (`maskSensitive`).

---

### 18. [Chapter 18: Services, Dependency Injection & Routing Architecture](file:///Users/apple/Workspace/Home/Javascript/Learning/18_Angular_Services_DI_and_Routing_Architecture.md)
- **Services Architecture:** Single Responsibility Principle, separating business logic and API requests from UI components.
- **Dependency Injection (DI) Internals:** Hierarchical injector resolution (Element, Environment, Root), tree-shaking with `providedIn: 'root'`, and multi-providers.
- **Creating & Consuming Services:** Constructor injection vs modern `inject()` functional injection.
- **Routing Configuration:** Route arrays, `<router-outlet>`, child routes, and modern `provideRouter()`.
- **`RouterLink` vs `href`:** Instant client-side SPA state transitions vs full browser reloads and token erasure.
- **Route Parameters:** Path params (`/loans/:id`), query params (`?status=val`), `history.state` data passing, and `withComponentInputBinding()`.
- **Route Guards:** Functional `CanActivateFn`, `CanDeactivateFn` unsaved change warnings, `CanMatchFn`, and `ResolveFn` pre-fetching.

---

### 19. [Chapter 19: Forms, Validation & HTTP Client](file:///Users/apple/Workspace/Home/Javascript/Learning/19_Angular_Forms_Validation_and_HTTP_Client.md)
- **Template-Driven vs Reactive Forms:** Asynchronous template parsing vs programmatic, strictly-typed model control.
- **Form Validation Engine:** Built-in validators, custom synchronous validators, cross-field matching, and asynchronous debounce validators.
- **Dynamic Form Fields with `FormArray`:** Adding and removing nested controls dynamically for co-borrowers and multi-bank accounts.
- **HTTP Client Mechanics:** Typed GET & POST requests, query parameter encoding, custom headers, and file upload progress events.
- **`HttpClientModule` vs `HttpModule`:** Automatic JSON deserialization, native interceptor pipelines, and `provideHttpClient(withFetch())`.
- **HTTP Error Handling Architecture:** Centralized `HttpInterceptorFn`, retry strategies, and mapping backend 400 validation errors to form controls.

---

### 20. [Chapter 20: RxJS, Observables, State & Performance](file:///Users/apple/Workspace/Home/Javascript/Learning/20_Angular_RxJS_Observables_State_and_Performance.md)
- **RxJS in Angular:** Reactive event streams, composable pipelines, and preventing race conditions via `switchMap`.
- **Observables vs Promises:** Lazy vs eager execution, cancellation via unsubscribe, and stream processing.
- **`BehaviorSubject` & State Management:** Storing current application state, multi-casting, and comparison with `Subject` and `ReplaySubject`.
- **Angular Performance Optimization:** `ChangeDetectionStrategy.OnPush`, `NgZone.runOutsideAngular()`, deferrable views (`@defer`), and `NgOptimizedImage`.
- **Lazy Loading Architecture:** Splitting bundles into asynchronous chunks via `loadComponent` and `PreloadAllModules`.
- **Change Detection Internals:** Zone.js monkey-patching, event loop notifications, and component tree dirty checking.
- **`OnPush` vs `Default` Strategy:** Pruning component subtrees, reference equality (`===`), and immutability rules.

---

### 21. [Chapter 21: Lifecycle Hooks, SSR Universal, Web Workers & Security](file:///Users/apple/Workspace/Home/Javascript/Learning/21_Angular_Lifecycle_Universal_Workers_and_Security.md)
- **Component Lifecycle Hooks:** Full sequence from `constructor`, `ngOnChanges`, `ngOnInit`, `ngAfterViewInit` to `ngOnDestroy`.
- **Angular Universal (SSR) & Hydration:** Server-side pre-rendering, SEO optimization, FCP acceleration, and non-destructive client hydration.
- **Web Workers:** Offloading CPU-intensive calculations (amortization tables, 20,000-row statement parsing) to background threads.
- **Enterprise Authentication & Authorization:** Complete JWT architecture, functional `authInterceptor`, role guards, and silent refresh token rotation.

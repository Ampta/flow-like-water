# 🎯 Group Discussion & Technical Interview Master Blueprint
## Comprehensive Guide: Java, Spring Boot, Microservices, System Design & Concurrency

> **Designed for Beginners to Advanced Learners**. Every topic is explained from first principles with **💡 Simple Analogies**, **📌 GD Speaking Pitches**, **🔬 Technical Mechanics**, **❌ BEFORE vs ✅ AFTER Code Comparisons**, and **🌐 Real-World Production Use Cases**.

---

## 📑 Table of Contents
1. [Java Core & OOP Deep Dive](#1-java-core--oop-deep-dive)
   - 1.1 Can we override the main method?
   - 1.2 Default methods vs Static methods in Interfaces
   - 1.3 Functional Interfaces & Why use Streams?
   - 1.4 Shallow Copy vs Deep Copy
   - 1.5 Java 17 (LTS) and Java 18 Key Features
2. [Java Collections & Data Structures Internals](#2-java-collections--data-structures-internals)
   - 2.1 HashMap Internal Working & Treeification Mechanics
   - 2.2 How HashMap checks duplicate keys (`hashCode()` and `equals()`)
   - 2.3 HashMap vs Hashtable vs ConcurrentHashMap
   - 2.4 ConcurrentModificationException (Fail-Fast vs Fail-Safe)
3. [Multithreading & Concurrency Control](#3-multithreading--concurrency-control)
   - 3.1 What is the `volatile` keyword?
   - 3.2 `volatile` vs `synchronized`
   - 3.3 `Runnable` vs `Callable` & Which to Choose
   - 3.4 `Future` and `FutureTask`
   - 3.5 Thread Locker & `ThreadLocal` Memory Safety
   - 3.6 Java ExecutorService & Thread Pools
   - 3.7 Stream vs Parallel Stream (Performance & Tuning)
4. [Spring Boot & Spring Framework Architecture](#4-spring-boot--spring-framework-architecture)
   - 4.1 `@Component` vs `@Bean` & `@Configuration` vs `@Bean` (CGLIB Proxying)
   - 4.2 `@Controller` vs `@RestController`
   - 4.3 `@ControllerAdvice` & Global Exception Handling
   - 4.4 Stereotype Annotation Hierarchy
   - 4.5 The 3 Core Annotations inside `@SpringBootApplication`
   - 4.6 View Resolvers in Spring MVC
   - 4.7 How to Configure Two Databases in One Spring Boot App
   - 4.8 Spring Boot Transaction Management (`@Transactional`)
   - 4.9 Spring Security Architecture & Filter Chain
   - 4.10 HikariCP Connection Pooling Tuning
5. [Database, JPA & Hibernate Persistence](#5-database-jpa--hibernate-persistence)
   - 5.1 What is N+1 Select Problem & 4 Ways to Rectify It? (BEFORE vs AFTER)
   - 5.2 Fetch Types: `LAZY` vs `EAGER` Loading (BEFORE vs AFTER)
   - 5.3 YAML File Structure vs `.properties` (BEFORE vs AFTER)
6. [System Design, Microservices & High-Availability Architecture](#6-system-design-microservices--high-availability-architecture)
   - 6.1 What is System Design?
   - 6.2 How to Design High-Level Architecture (5-Step Framework)
   - 6.3 Exception Handling in Distributed Systems
   - 6.4 Event-Driven Architecture (EDA)
   - 6.5 Circuit Breaker Pattern (Broken Circuit) & Resilience4j
   - 6.6 What is Apache Kafka and How it Works?
7. [Production Troubleshooting & Design Patterns](#7-production-troubleshooting--design-patterns)
   - 7.1 Memory Leak Reasons & Troubleshooting Guide
   - 7.2 How to Handle High CPU Alert in Production
   - 7.3 Design Patterns Quick Reference & Real-World Code Implementations

---

## 1. Java Core & OOP Deep Dive

### 1.1 Can we override the `main` method in Java?

#### 💡 Simple Analogy
Think of a static method like a **building's main entrance sign**. It belongs to the physical building (the class), not to an individual apartment (an object instance inside). You can't "override" a sign attached to the building structure; you can only attach a new sign in front of a child building, which **hides** the parent sign.

#### 📌 GD Speaking Pitch
> *"No, we cannot override the `main` method in Java because it is declared `static`. In Java, static methods belong to the class itself, not to object instances. Overriding relies on dynamic runtime polymorphism (virtual method invocation). When you define a main method in a subclass with the same signature, it is called **Method Hiding**, not method overriding."*

#### 🔬 Detailed Technical Explanation
1. **Method Overriding Requirement**: Overriding requires runtime method dispatch (late binding) based on the actual runtime type of the object instance.
2. **Static Binding**: Static methods are resolved at **compile-time** (early binding) based on the class reference type.
3. **Overloading vs Overriding**:
   - You **can overload** `main` (e.g., `main(int a)`, `main(String[] args, int b)`).
   - However, the JVM launcher will *only* execute `public static void main(String[] args)` as the application entry point.

#### 💻 Code Example
```java
class Parent {
    public static void main(String[] args) {
        System.out.println("Parent Main executed");
    }
}

class Child extends Parent {
    // This HIDDEN parent's main method; it does NOT override it!
    public static void main(String[] args) {
        System.out.println("Child Main executed");
    }
}

public class MainTest {
    public static void main(String[] args) {
        Parent p = new Child();
        p.main(args); // Prints "Parent Main executed" because static methods resolve by reference type (Parent)!
    }
}
```

#### 🌐 Real-World Application
Frameworks like Spring Boot rely on static entry points (`SpringApplication.run(Application.class, args)`) to bootstrap the Spring IoC Container before any application objects exist in memory.

---

### 1.2 Why were `default` methods introduced in Java 8 when `static` methods were already available?

#### 💡 Simple Analogy
Imagine an electrical wall plug standard across a country (Interface). If the power company upgrades the grid to require an extra grounding pin, legacy appliances won't plug in unless the outlet adapter (Default Method) provides backward compatibility built right into the interface!

#### 📌 GD Speaking Pitch
> *"Default methods were introduced in Java 8 primarily for **backward compatibility**. They allow developers to add new methods to existing interfaces without breaking legacy code that implements those interfaces. Static methods couldn't solve this because static methods belong strictly to the interface and cannot be overridden by implementing classes."*

#### 🔬 Detailed Technical Explanation
- **The Java 7 Problem**: Oracle wanted to introduce the Streams API to Java 8. To do this, they needed to add `stream()` to the `Collection` interface. If they added a standard abstract method, **every custom library in the world implementing `Collection` would fail to compile!**
- **Why Static Methods Weren't Enough**:
  - `static` methods inside interfaces **cannot be overridden** by implementing classes (e.g., `ArrayList`).
  - `default` methods allow an interface to provide a default implementation while allowing implementing classes like `ArrayList` or `LinkedList` to **override** it with a faster, specialized implementation.

#### 💻 Code Example
```java
public interface Vehicle {
    void start(); // Standard abstract method

    // Default method added in Java 8 with default implementation
    default void alarmOn() {
        System.out.println("Generic Vehicle Alarm Activated!");
    }
}

public class SportsCar implements Vehicle {
    @Override
    public void start() {
        System.out.println("V8 Engine Roaring!");
    }

    // Optional override for custom behavior
    @Override
    public void alarmOn() {
        System.out.println("High-decibel Security Alarm System Activated!");
    }
}
```

---

### 1.3 Functional Interfaces & Why use Streams?

#### 💡 Simple Analogy
- **Functional Interface**: A single-button remote control. It has exactly one action to press.
- **Streams**: An automated factory assembly line. Instead of manually inspecting items one by one in a loop (imperative), you set up conveyor belts, filters, and sorters (declarative stream pipeline), and press start.

#### 📌 GD Speaking Pitch
> *"A Functional Interface is an interface containing exactly one abstract method (Single Abstract Method or SAM), serving as the foundation for Lambda Expressions. We use Streams because they provide a declarative, functional approach to bulk data processing with built-in lazy evaluation, optimization, and simple parallel processing capability."*

#### 🔬 Detailed Technical Explanation
1. **Core Functional Interfaces in `java.util.function`**:
   - `Function<T, R>`: Takes input `T`, returns result `R` (`R apply(T t)`).
   - `Predicate<T>`: Takes input `T`, returns `boolean` (`boolean test(T t)`).
   - `Supplier<T>`: Takes no input, produces `T` (`T get()`).
   - `Consumer<T>`: Takes input `T`, returns nothing (`void accept(T t)`).

2. **Why Streams over Traditional `for` Loops?**:
   - **Declarative Code**: Focuses on *what* to do, not *how* to iterate.
   - **Lazy Evaluation**: Intermediate operations (`filter`, `map`) do NOT process items until a terminal operation (`collect`, `count`, `findFirst`) is called.
   - **Pipelining**: Combines multiple transformations into a single iteration pass.

#### 💻 Code Example
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Filter names starting with 'C', convert to uppercase, collect into list
List<String> result = names.stream()
    .filter(name -> name.startsWith("C")) // Predicate
    .map(String::toUpperCase)              // Function
    .collect(Collectors.toList());        // Terminal operation

System.out.println(result); // [CHARLIE]
```

---

### 1.4 Shallow Copy vs Deep Copy

#### 💡 Simple Analogy
- **Shallow Copy**: Copying a house key folder. You have a new folder, but the keys inside still open the exact same house locks. Changing the lock affects both folder holders.
- **Deep Copy**: Building an exact 1-to-1 clone of the entire house and all contents in a new town. Changing furniture in the new house does not affect the original house!

#### 📌 GD Speaking Pitch
> *"A shallow copy creates a new object instance but copies references to nested objects, meaning modifications to inner objects affect both copies. A deep copy recursively clones the parent object and all nested child objects, producing a 100% independent memory clone."*

#### 🔬 Detailed Comparison Table

| Feature | Shallow Copy | Deep Copy |
| :--- | :--- | :--- |
| **Nested Objects** | Shared references | Completely cloned instances |
| **Independence** | Outer object independent, inner fields shared | Fully independent |
| **Performance** | Fast & low memory | Slower, higher GC overhead |
| **Creation Method** | `Object.clone()`, `new ArrayList<>(list)` | Copy constructors, JSON serialization, manual recursion |

---

### 1.5 Java 17 (LTS) and Java 18 Key Features

#### 📌 GD Speaking Pitch
> *"Java 17 is a Long-Term Support (LTS) release featuring Sealed Classes, Records, Text Blocks, and Pattern Matching. Java 18 introduced UTF-8 by default and the Simple Web Server for rapid testing."*

#### 🔬 Features Breakdown
1. **Sealed Classes (`sealed`, `permits`)** (Java 17): Restricts which classes can extend a parent class, providing strict domain model boundary control.
```java
public sealed class Shape permits Circle, Square {}
public final class Circle extends Shape {}
public final class Square extends Shape {}
```
2. **Records (`record`)** (Java 17): Immutable data carrier objects that auto-generate constructors, getters, `equals()`, `hashCode()`, and `toString()`.
```java
public record UserDto(String username, String email) {}
```
3. **Text Blocks (`"""`)** (Java 17): Multi-line string literals without hideous manual `\n` escaping.
4. **UTF-8 by Default** (Java 18): Standard Java APIs default to UTF-8 across all operating systems.

---

## 2. Java Collections & Data Structures Internals

### 2.1 HashMap Internal Working & Treeification Mechanics

#### 💡 Simple Analogy
Imagine an apartment building with mailboxes (buckets). When mail arrives, the mail carrier computes a mailbox number from the recipient's name. If multiple people share a mailbox, their letters are stacked in a pile (LinkedList). If a mailbox pile grows higher than 8 letters, the post office replaces the pile with an indexed filing cabinet (Red-Black Tree) for faster searching!

#### 📌 GD Speaking Pitch
> *"HashMap operates on an array of buckets (`Node<K,V>[]`). It calculates key indices using `hash(key) & (n - 1)`. Collisions are handled via linked lists, which convert into Red-Black Trees (Treeification) when a single bucket exceeds 8 elements and overall map capacity is at least 64."*

---

### 2.2 How HashMap Checks Duplicate Keys (`hashCode()` and `equals()`)

#### 📌 GD Speaking Pitch
> *"HashMap checks duplicate keys in a two-step process: First, it compares the integer `hashCode()` of the keys. If the hashes match, it performs a second check using `==` identity OR `.equals()` equality. If both pass, the key is considered a duplicate and its value is overwritten."*

```java
// How HashMap checks matching keys inside a bucket:
if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
    // Duplicate Key Found! Overwrite value.
}
```

---

### 2.3 HashMap vs Hashtable vs ConcurrentHashMap

#### 💡 Simple Analogy
- **HashMap**: Unlocked room. Multiple people can walk in, but if two people edit the whiteboard at the exact same time, text gets corrupted.
- **Hashtable**: Single padlock on the front door. Only one person can enter at a time. Everyone else waits outside in a long slow line.
- **ConcurrentHashMap**: A room with 16 separate small whiteboards (buckets). 16 people can work simultaneously on different whiteboards without blocking each other!

#### 📌 GD Speaking Pitch
> *"HashMap is non-thread-safe and fast. Hashtable is a legacy synchronized class that locks the entire table for every read/write operation. ConcurrentHashMap provides high-concurrency performance using Bucket-level Lock Striping (CAS operations and synchronized bucket nodes) without locking the whole map."*

---

### 2.4 ConcurrentModificationException (Fail-Fast vs Fail-Safe)

#### 📌 GD Speaking Pitch
> *"ConcurrentModificationException occurs when a thread modifies a collection structurally (adding or removing items) while iterating over it without using the iterator's own remove method. Fail-Fast iterators detect this via an internal `modCount` counter mismatch, whereas Fail-Safe iterators operate on a snapshot copy of the data."*

#### 💻 Code Example & Fix
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// ❌ WRONG (Throws ConcurrentModificationException):
for (String item : list) {
    if (item.equals("B")) list.remove(item);
}

// ✅ CORRECT Fix 1 (Using Iterator.remove()):
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) it.remove();
}

// ✅ CORRECT Fix 2 (Using removeIf):
list.removeIf(item -> item.equals("B"));
```

---

## 3. Multithreading & Concurrency Control

### 3.1 What is the `volatile` keyword?

#### 💡 Simple Analogy
In a multi-core CPU, each CPU core has its own ultra-fast local cache (L1/L2 cache), like a personal notepad. Without `volatile`, Core 1 writes a variable update to its notepad, but Core 2 reads from main RAM and never sees Core 1's change! `volatile` forces all reads and writes to go directly to **Main RAM**.

#### 📌 GD Speaking Pitch
> *"The `volatile` keyword in Java guarantees **memory visibility** and prevents **instruction reordering** by compiler and CPU hardware. It ensures every read/write to a variable is flushed directly to Main RAM, making updates immediately visible across all CPU threads."*

---

### 3.2 `volatile` vs `synchronized`

#### 📌 GD Speaking Pitch
> *"`volatile` guarantees visibility of single variable reads/writes without blocking threads. `synchronized` guarantees both visibility AND atomicity by acquiring a mutual exclusion lock on a monitor object, causing competing threads to enter a blocked state."*

---

### 3.3 `Runnable` vs `Callable` & Which to Choose

#### 📌 GD Speaking Pitch
> *"Use `Runnable` for fire-and-forget background tasks that do not return a result or throw checked exceptions. Choose `Callable<V>` when your asynchronous task needs to return a computed result (`Future<V>`) or handle checked exceptions."*

#### 💻 Code Example
```java
// 1. Runnable: void return, no checked exception allowed
Runnable r = () -> System.out.println("Logging async metric...");

// 2. Callable: Returns String, allows checked exception
Callable<String> c = () -> {
    if (databaseDown) throw new SQLException("Database unreachable!");
    return "User Record Data";
};
```

---

### 3.4 `Future` and `FutureTask`

#### 💻 Code Example
```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});

// Do other work here...

Integer result = future.get(2, TimeUnit.SECONDS); // Returns 42
System.out.println("Result: " + result);
executor.shutdown();
```

---

### 3.5 Thread Locker & `ThreadLocal` Memory Safety

#### 💻 Correct Code Pattern
```java
public class UserContextFilter implements Filter {
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        try {
            userHolder.set(extractUserFromToken(req));
            chain.doFilter(req, res); // Process request
        } finally {
            userHolder.remove(); // 🚨 CRITICAL: Always clean up in finally block!
        }
    }
}
```

---

### 3.6 Java ExecutorService & Thread Pools

#### 💻 Code Example
```java
// Recommended Custom ThreadPoolExecutor for Production:
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    10,                         // Core pool size
    20,                         // Maximum pool size
    60L, TimeUnit.SECONDS,      // Keep-alive time
    new ArrayBlockingQueue<>(100), // Bounded queue capacity
    new ThreadPoolExecutor.CallerRunsPolicy() // Backpressure rejection policy
);
```

---

### 3.7 Stream vs Parallel Stream (Performance & Tuning)

#### 📌 GD Speaking Pitch
> *"Parallel streams split workloads across CPU cores using the shared `ForkJoinPool.commonPool()`. They excel at large, CPU-bound data operations, but cause severe performance degradation if used for blocking database or HTTP network I/O operations!"*

---

## 4. Spring Boot & Spring Framework Architecture

### 4.1 `@Component` vs `@Bean` & `@Configuration` vs `@Bean`

#### 📌 GD Speaking Pitch
> *"`@Component` is used on custom source classes for automatic package component scanning. `@Bean` is used on factory methods inside `@Configuration` classes to register 3rd-party library objects. `@Configuration` enables CGLIB proxying so calling a `@Bean` method internally returns the cached singleton bean instance."*

#### 💻 Code Example
```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // 3rd party class registered as Spring Bean
    }
}
```

---

### 4.2 `@Controller` vs `@RestController`

#### 📌 GD Speaking Pitch
> *"`@RestController` is a convenience meta-annotation combining `@Controller` and `@ResponseBody`. It automatically serializes method return objects directly into JSON/XML HTTP responses instead of resolving HTML template views."*

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id); // Automatically converted to JSON!
    }
}
```

---

### 4.3 `@ControllerAdvice` & Global Exception Handling

#### 📌 GD Speaking Pitch
> *"`@ControllerAdvice` provides global, centralized exception handling across all Spring REST controllers using `@ExceptionHandler` methods, ensuring consistent standard error responses across microservices."*

#### 💻 Code Example
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

### 4.4 Stereotype Annotation Hierarchy

All Spring stereotypes (`@Service`, `@Repository`, `@Controller`) are specialized meta-annotations of `@Component`:
- `@Component`: Generic Spring-managed bean.
- `@Service`: Marks business logic service layer.
- `@Repository`: Marks Data Access Object (DAO) persistence layer; **automatically translates SQL/ORM vendor exceptions into Spring's unified `DataAccessException` hierarchy!**
- `@Controller` / `@RestController`: Presentation HTTP REST layer.

---

### 4.5 The 3 Core Annotations inside `@SpringBootApplication`

#### 📌 GD Speaking Pitch
> *"`@SpringBootApplication` is a composite meta-annotation combining `@SpringBootConfiguration`, `@EnableAutoConfiguration`, and `@ComponentScan`."*

1. **`@SpringBootConfiguration`**: Marks the class as a primary Spring configuration class (specialized `@Configuration`).
2. **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration engine, inspecting classpath dependencies (e.g. `spring-boot-starter-web` auto-configures embedded Tomcat and DispatcherServlet).
3. **`@ComponentScan`**: Instructs Spring to scan the current package and sub-packages for `@Component`, `@Service`, `@Repository`, and `@RestController` beans.

---

### 4.6 View Resolvers in Spring MVC

#### 📌 GD Speaking Pitch
> *"ViewResolvers map logical view names returned by controllers to physical template rendering files. Key types include `InternalResourceViewResolver` (for JSP), `ThymeleafViewResolver` (HTML5 templates), and `ContentNegotiatingViewResolver` (delegates based on `Accept` header)."*

---

### 4.7 How to Configure Two Databases in One Spring Boot App

#### 💡 Step-by-Step Architecture
1. **Define properties in `application.yml`**:
```yaml
spring:
  datasource:
    primary:
      url: jdbc:postgresql://localhost:5432/user_db
      username: postgres
    secondary:
      url: jdbc:mysql://localhost:3306/order_db
      username: root
```
2. **Create Primary Config**:
```java
@Configuration
@EnableJpaRepositories(
    basePackages = "com.app.repo.primary",
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
}
```

---

### 4.8 Spring Boot Transaction Management (`@Transactional`)

#### 📌 GD Speaking Pitch
> *"`@Transactional` uses Spring AOP to wrap methods in database transactions. Key settings include propagation levels (e.g. `REQUIRED`, `REQUIRES_NEW`) and isolation levels. Spring automatically rolls back transactions for Unchecked (`RuntimeException`), but requires `rollbackFor = Exception.class` for Checked exceptions."*

#### 🔬 Propagation Types
- `REQUIRED` (Default): Joins existing transaction; creates a new one if none exists.
- `REQUIRES_NEW`: Suspends current transaction and opens a brand-new, isolated transaction.

---

### 4.9 Spring Security Architecture & Filter Chain

#### 📌 GD Speaking Pitch
> *"Spring Security operates via a chain of Servlet Filters (`DelegatedFilterProxy` -> `SecurityFilterChain`). Incoming HTTP requests pass through `JwtAuthenticationFilter`, extract JWT tokens, validate credentials, and populate `SecurityContextHolder.getContext().setAuthentication(auth)`."*

---

### 4.10 HikariCP Connection Pooling Tuning

#### 📌 GD Speaking Pitch
> *"HikariCP is Spring Boot's high-performance default JDBC connection pool. Key parameters include `maximumPoolSize`, `minimumIdle`, `connectionTimeout`, and `leakDetectionThreshold`."*

- **Max Connections Formula**: $\text{Pool Size} = (\text{CPU Cores} \times 2) + \text{Disk Spindles}$.

---

## 5. Database, JPA & Hibernate Persistence

### 5.1 What is the N+1 Select Problem & 4 Ways to Rectify It?

#### 💡 Simple Analogy
Imagine a teacher fetching 100 student report cards.
- **Without Fix (N+1 Problem)**: The teacher walks to the filing cabinet **1 time** to get the list of 100 student names, then makes **100 individual trips** back and forth to fetch each individual student's report card from a separate cabinet! (Total = **101 trips**!).
- **With Fix (JOIN FETCH)**: The teacher makes **1 single trip**, picks up the student list AND all 100 report cards attached together in one single folder! (Total = **1 trip**!).

#### 📌 GD Speaking Pitch
> *"The N+1 problem occurs when fetching 1 parent entity results in Hibernate executing N additional SQL queries to fetch child entities. We fix it using JPQL `JOIN FETCH`, `@EntityGraph`, `@BatchSize`, or DTO Projections."*

---

#### ❌ BEFORE (The Bad Code & The N+1 Query Explosion)

```java
// Entity definitions: One Author has Many Books
@Entity
public class Author {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
```

```java
// BAD CODE: Fetching 100 authors and trying to access their books
@Transactional
public void printAuthorBookCounts() {
    // Query 1: Fetches 100 Authors
    List<Author> authors = authorRepository.findAll(); 

    // Loop through 100 authors:
    for (Author author : authors) {
        // ❌ FOR EVERY AUTHOR, HIBERNATE FIRES A NEW SQL SELECT QUERY FOR BOOKS!
        System.out.println(author.getName() + " has books: " + author.getBooks().size());
    }
}
```

#### 🚨 Raw SQL Fired in Database Logs (101 Queries!):
```sql
-- Query 1 (The "1" Query):
SELECT * FROM author;

-- Query 2 to 101 (The "N" Queries fired 100 times in loop!):
SELECT * FROM book WHERE author_id = 1;
SELECT * FROM book WHERE author_id = 2;
SELECT * FROM book WHERE author_id = 3;
...
SELECT * FROM book WHERE author_id = 100;
-- 🔥 Result: Database CPU hits 100%, server response time drops from 10ms to 5,000ms!
```

---

#### ✅ AFTER (The 4 Solutions & Fixed Code)

##### Solution 1: Using JPQL `JOIN FETCH` (Reduces 101 Queries -> 1 Query)
```java
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // ✅ FIXED: JOIN FETCH pulls Author and Books in 1 single SQL JOIN query!
    @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books")
    List<Author> findAllAuthorsWithBooks();
}
```
#### ⚡ SQL Fired in Database Log (Just 1 Query!):
```sql
SELECT a.id, a.name, b.id, b.title, b.author_id 
FROM author a 
INNER JOIN book b ON a.id = b.author_id;
-- 🚀 Result: Executed in 2ms! 100 queries eliminated!
```

##### Solution 2: Using JPA `@EntityGraph` Annotation
```java
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // ✅ FIXED: Spring Data JPA automatically adds LEFT OUTER JOIN for books!
    @EntityGraph(attributePaths = {"books"})
    List<Author> findAll();
}
```

##### Solution 3: Using Hibernate `@BatchSize(size = 20)`
```java
@Entity
public class Author {
    @Id @GeneratedValue
    private Long id;

    // ✅ FIXED: Batches lazy loading in groups of 20 using SQL "IN (?, ?, ...)"
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();
}
```
#### ⚡ SQL Fired in Database Log (Reduces 101 Queries -> 6 Queries!):
```sql
SELECT * FROM author;
-- Fetches books for 20 authors at a time using IN clause:
SELECT * FROM book WHERE author_id IN (1, 2, 3, ... 20);
SELECT * FROM book WHERE author_id IN (21, 22, ... 40);
...
```

##### Solution 4: Using DTO Constructor Projection
```java
// DTO class holding just the exact data needed
public record AuthorBookCountDto(String authorName, long bookCount) {}

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // ✅ FIXED: Selects only necessary aggregated columns directly
    @Query("SELECT new com.app.dto.AuthorBookCountDto(a.name, COUNT(b.id)) " +
           "FROM Author a JOIN a.books b GROUP BY a.name")
    List<AuthorBookCountDto> fetchAuthorBookCounts();
}
```

---

### 5.2 Fetch Types: `LAZY` vs `EAGER` Loading

#### 💡 Simple Analogy
- **`EAGER` (Over-packing)**: Every time you step out of your house to buy milk, you carry your 50kg luggage suitcase, winter coats, and tent along with you!
- **`LAZY` (On-Demand)**: You walk out carrying only your wallet. If you decide later you need a tent, you go back and get the tent.

#### 📌 GD Speaking Pitch
> *"EAGER loading fetches child associations immediately along with the parent entity, leading to massive memory bloat and slow queries. LAZY loading creates a proxy object and defers database queries until the child collection is explicitly accessed. Always default to LAZY loading."*

---

#### ❌ BEFORE (The Bad EAGER Loading Code & Problem)

```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;

    // ❌ BAD: EAGER fetching automatically loads thousands of historical orders & logs!
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orderHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<AuditLog> auditLogs = new ArrayList<>();
}
```

```java
// Triggering simple query:
User user = userRepository.findById(1L).get();
```
#### 🚨 What Happens Behind The Scenes (Memory & Performance Crash):
1. Even if you only wanted to check `user.getUsername()`, Hibernate executes massive multi-table SQL `OUTER JOIN`s fetching 50,000 `Order` records and 10,000 `AuditLog` records!
2. JVM Heap Memory spikes by 500MB for a single simple user lookup!
3. Multiple `EAGER` collections trigger a `MultipleBagFetchException` in Hibernate!

---

#### ✅ AFTER (The Fixed LAZY Loading Code)

```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;

    // ✅ FIXED: Default to LAZY! Hibernate injects a lightweight Bytecode Proxy
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orderHistory = new ArrayList<>();
}
```

```java
// Step 1: Fetches ONLY the user row (Fast 1ms query!)
User user = userRepository.findById(1L).get(); 

// Step 2: orderHistory is NOT loaded yet! It's an empty Proxy wrapper.

// Step 3: Database query for orders is triggered ONLY when explicitly accessed!
int totalOrders = user.getOrderHistory().size(); // SQL query for orders fires HERE!
```

#### ⚠️ The `LazyInitializationException` Trap & Solution:
If you try to call `user.getOrderHistory().size()` **after** the `@Transactional` boundary / DB Session has closed, Hibernate throws `LazyInitializationException: could not initialize proxy - no Session`.
- **Fix**: Either fetch orders within the `@Transactional` service layer or use `JOIN FETCH` / `@EntityGraph`!

---

### 5.3 YAML File Structure vs `.properties`

#### 💡 Simple Analogy
- **`.properties`**: Writing your address on 50 separate envelopes repetitively: `USA.NY.NYC.Street1`, `USA.NY.NYC.Street2`, `USA.NY.NYC.Street3`.
- **`application.yml`**: An organized file tree folder structure: `USA -> NY -> NYC -> [Street1, Street2, Street3]`.

#### 📌 GD Speaking Pitch
> *"YAML (`application.yml`) provides hierarchical nested indentation and native list support, eliminating repetitive key prefix boilerplate present in `.properties` files while supporting multi-profile configurations in a single file."*

---

#### ❌ BEFORE (Unorganized `.properties` File)

```properties
# Repetitive prefix boilerplate! Hard to read & maintain!
spring.datasource.primary.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.primary.username=postgres
spring.datasource.primary.password=secret123
spring.datasource.primary.hikari.maximum-pool-size=20
spring.datasource.primary.hikari.minimum-idle=5

spring.datasource.secondary.url=jdbc:mysql://localhost:3306/order_db
spring.datasource.secondary.username=root
spring.datasource.secondary.password=mysql123
spring.datasource.secondary.hikari.maximum-pool-size=10

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# List representation in properties is ugly:
my.app.allowed-origins[0]=https://example.com
my.app.allowed-origins[1]=https://admin.example.com
```

---

#### ✅ AFTER (Clean Hierarchical `application.yml` File)

```yaml
# ✅ Clean, nested, human-readable hierarchy!
spring:
  datasource:
    primary:
      url: jdbc:postgresql://localhost:5432/user_db
      username: postgres
      password: secret123
      hikari:
        maximum-pool-size: 20
        minimum-idle: 5
    secondary:
      url: jdbc:mysql://localhost:3306/order_db
      username: root
      password: mysql123
      hikari:
        maximum-pool-size: 10
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

# Clean list structure:
my:
  app:
    allowed-origins:
      - https://example.com
      - https://admin.example.com

---
# Multi-profile configuration in ONE single file using '---' separator!
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    primary:
      url: jdbc:postgresql://prod-db-cluster:5432/user_db
```

---

## 6. System Design, Microservices & High-Availability Architecture

### 6.1 What is System Design?

#### 💡 Simple Analogy
Designing a city blueprint. You don't just build house walls (code logic); you design power grids, traffic roads, water pipes, sewage overflow systems, and fire stations so the city survives traffic rush hour (high load) and power outages (failures).

#### 📌 GD Speaking Pitch
> *"System Design is the discipline of architecting end-to-end software components, modules, databases, caching layers, and communication APIs to satisfy functional business features while adhering to non-functional requirements such as high availability, horizontal scalability, low latency, and fault tolerance."*

#### 💻 Code & Architecture Comparison: Monolith vs Scalable Microservice

```java
// Monolithic Service (Coupled DB & Business Logic):
@Service
public class LegacyOrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private EmailService emailService;

    @Transactional
    public void createOrder(Order order) {
        orderRepo.save(order); // DB write
        emailService.sendEmail(order.getUserEmail()); // Synchronous blocking call!
    }
}
```

```java
// Scalable Decoupled Architecture (Async Event Emitting):
@Service
public class ModernOrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Transactional
    public void createOrder(Order order) {
        Order savedOrder = orderRepo.save(order);
        // Emits event to message broker asynchronously!
        kafkaTemplate.send("orders-topic", new OrderEvent(savedOrder.getId(), "CREATED"));
    }
}
```

---

### 6.2 How to Design High-Level Architecture (5-Step Framework)

#### 💡 Simple Analogy
An architect building a skyscraper:
1. Measure earthquake risk & population (System constraints).
2. Draw floor plans (API & Data models).
3. Build elevator shafts & structural pillars (API Gateway & Microservices).
4. Install water tanks & storage (Databases & Caching).
5. Install backup generators (Kafka & Circuit Breakers).

#### 📌 GD Speaking Pitch
> *"I follow a structured 5-step High-Level Architecture framework: 1. Requirement & Scale Estimation, 2. API Contract & Data Schema, 3. Core Component Blueprint (Load Balancer, API Gateway, Services), 4. Caching & Data Storage Strategy, and 5. Resilience & Asynchronous Decoupling via Kafka."*

#### 💻 Practical Implementation Blueprint: URL Shortener / E-Commerce System

```java
@RestController
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private UrlRepository urlRepository;

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortKey) {
        // Step 1: Check ultra-fast distributed Redis Cache (Sub-millisecond latency!)
        String cachedLongUrl = redisTemplate.opsForValue().get(shortKey);
        if (cachedLongUrl != null) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .location(URI.create(cachedLongUrl)).build();
        }

        // Step 2: Fallback to Database Read-Replica if Cache Miss
        UrlMapping mapping = urlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Step 3: Populate Cache asynchronously for future requests
        redisTemplate.opsForValue().set(shortKey, mapping.getLongUrl(), Duration.ofHours(24));

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(mapping.getLongUrl())).build();
    }
}
```

---

### 6.3 Exception Handling in Distributed Systems

#### 💡 Simple Analogy
A package shipped across 5 international courier hubs. Every package gets a unique barcode tracking sticker (**Correlation ID**). If a hub loses a box, workers scan the barcode to trace the exact leg where it went missing.

#### 📌 GD Speaking Pitch
> *"In distributed microservices, exceptions are handled using Correlation IDs for distributed tracing (Zipkin/Sleuth/MDC), standardized RFC 7807 error responses, Idempotent retries with exponential backoff, and Dead Letter Queues (DLQ) for unprocessable messages."*

#### 💻 Complete Code Example: Correlation ID Filter & RestTemplate Interceptor

```java
// 1. Servlet Filter to extract or generate Correlation ID
@Component
public class CorrelationIdFilter implements Filter {
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        String correlationId = httpReq.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        
        // Put in Logback MDC context for unified logging
        MDC.put("correlationId", correlationId);
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove("correlationId");
        }
    }
}

// 2. Interceptor to propagate Correlation ID to downstream Microservices
@Component
public class OutgoingCorrelationInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution exec)
            throws IOException {
        String correlationId = MDC.get("correlationId");
        if (correlationId != null) {
            req.getHeaders().add("X-Correlation-ID", correlationId);
        }
        return exec.execute(req, body);
    }
}
```

---

### 6.4 Event-Driven Architecture (EDA)

#### 💡 Simple Analogy
A newspaper publisher. The publisher prints news (Emits Event) and drops it in paper boxes. Thousands of readers (Consumers) pick up the paper whenever they wake up. The publisher doesn't phone each reader individually!

#### 📌 GD Speaking Pitch
> *"Event-Driven Architecture decouples services into Event Producers and Event Consumers via a broker like Apache Kafka. It enables asynchronous non-blocking processing, high throughput write scaling, and eventual consistency across microservices."*

#### 💻 Complete Code Example: Spring Kafka Producer & Listener

```java
// 1. Event Payload DTO
public record OrderCreatedEvent(String orderId, String userId, double amount) {}

// 2. Kafka Producer Service
@Service
public class OrderEventProducer {
    @Autowired private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event) {
        System.out.println("Publishing event for order: " + event.orderId());
        kafkaTemplate.send("order-events-topic", event.orderId(), event);
    }
}

// 3. Kafka Consumer Service (Fully Decoupled Microservice)
@Service
public class InventoryEventListener {

    @KafkaListener(topics = "order-events-topic", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Reserving inventory items for order: " + event.orderId());
        // Business logic to reserve stock...
    }
}
```

---

### 6.5 Circuit Breaker Pattern (Broken Circuit) & Resilience4j

#### 💡 Simple Analogy
An electrical safety circuit breaker in your house. If an appliance shorts out, the circuit breaker **trips open** instantly to prevent a house fire (system crash). After a cooling period, you flip the switch to **half-open** to test if the appliance is safe before closing the circuit again.

#### 📌 GD Speaking Pitch
> *"Circuit Breakers prevent cascade failures in microservices. When downstream service failure rate exceeds a threshold, the circuit transitions from CLOSED to OPEN, immediately returning fallbacks without waiting for network timeouts."*

#### 💻 Complete Code Example: Resilience4j Circuit Breaker in Spring Boot

```java
// Spring Boot Service with Resilience4j Circuit Breaker
@Service
public class PaymentGatewayService {

    @Autowired private RestTemplate restTemplate;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public String processPayment(String accountId, double amount) {
        // Calling 3rd party payment gateway REST API
        return restTemplate.postForObject("https://api.stripe.com/v1/charges", amount, String.class);
    }

    // Fallback method triggered automatically when Circuit is OPEN or Exception occurs
    public String paymentFallback(String accountId, double amount, Throwable t) {
        System.err.println("Payment Gateway unreachable. Triggering Fallback logic! Error: " + t.getMessage());
        return "PAYMENT_PENDING_QUEUE";
    }
}
```

```yaml
# application.yml Configuration for Resilience4j
resilience4j.circuitbreaker:
  instances:
    paymentService:
      slidingWindowSize: 10              # Evaluate last 10 requests
      failureRateThreshold: 50           # Trip if 50% fail
      waitDurationInOpenState: 10000ms    # Stay OPEN for 10s before HALF_OPEN
      permittedNumberOfCallsInHalfOpenState: 3
```

---

### 6.6 What is Apache Kafka and How it Works?

#### 💡 Simple Analogy
A high-speed continuous cassette tape recorder. Producers record audio tracks onto numbered tape channels (Partitions inside Topics). Multiple listeners (Consumer Groups) listen to the tape with headphones at their own pace, holding a bookmark (Offset) pointing to the last second of tape they listened to.

#### 📌 GD Speaking Pitch
> *"Apache Kafka is a distributed event streaming platform offering high-throughput log append storage. Topics are divided into Partitions distributed across Brokers, where Consumer Groups consume messages sequentially via Offsets."*

#### 💻 Complete Code & Config: Spring Boot Kafka Consumer/Producer Setup

```yaml
# application.yml Kafka Configuration
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: notification-service-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
```

---

## 7. Production Troubleshooting & Design Patterns

### 7.1 Memory Leak Reasons & Troubleshooting Guide

#### 💡 Simple Analogy
A boat taking on water through tiny cracks. The passengers keep shoveling out buckets (Garbage Collection), but because the leak remains unsealed (objects still referenced by GC Roots), the boat eventually sinks (`OutOfMemoryError`).

#### 📌 GD Speaking Pitch
> *"Memory leaks occur when unused objects remain reachable from Garbage Collection roots. Common causes include unremoved ThreadLocals, static collection references, and unclosed resources. Troubleshoot by capturing a Heap Dump (`jmap`/`jcmd`) and analyzing the Dominator Tree in Eclipse MAT."*

#### 💻 3 Classic Leaky Code Examples vs Fixed Code

```java
// ❌ LEAK 1: Static collection growing infinitely
public class LeakyCache {
    public static final List<Object> cache = new ArrayList<>(); // Never cleared!
    public void addToCache(Object data) { cache.add(data); }
}

// ❌ LEAK 2: Unclosed Resource (DB Connection / File Stream)
public void readFile(String path) throws IOException {
    FileInputStream fis = new FileInputStream(path); // Never closed!
    fis.read();
}
// ✅ FIX 2: Use Try-With-Resources:
public void readFileFixed(String path) throws IOException {
    try (FileInputStream fis = new FileInputStream(path)) {
        fis.read();
    } // Automatically closed!
}

// ❌ LEAK 3: Missing hashCode() & equals() in HashMap Keys
public class BadKey {
    private String id;
    public BadKey(String id) { this.id = id; }
    // Missing equals() & hashCode()! HashMap creates duplicate entries forever!
}
```

#### 🛠️ Production Incident Troubleshooting Commands
```bash
# Step 1: Find Process ID (PID)
jps -l

# Step 2: Generate Heap Dump File
jcmd <PID> GC.heap_dump /tmp/heap_dump.hprof

# Step 3: Open /tmp/heap_dump.hprof in Eclipse Memory Analyzer Tool (MAT)
# Run "Leak Suspects Report" & analyze "Dominator Tree"!
```

---

### 7.2 How to Handle High CPU Alert in Production

#### 💡 Simple Analogy
A car engine revving at 9,000 RPM in neutral. You look under the hood with a strobe light (Thread Dump) to find which specific piston (Thread ID) is spinning out of control.

#### 📌 GD Speaking Pitch
> *"When High CPU alert fires, immediately take 3 Thread Dumps using `jstack` separated by 10s. Identify high-CPU OS thread IDs via `top -H -p <pid>`, convert thread ID to hex, and locate the exact executing Java stack line."*

#### 💻 Step-by-Step Incident Shell Script & Command Playbook

```bash
#!/bin/bash
# High CPU Production Troubleshooting Playbook

# 1. Identify Java Process ID (PID) consuming CPU
PID=$(top -b -n 1 | grep java | awk '{print $1}')
echo "Target Java PID: $PID"

# 2. Identify top CPU-consuming OS Light-Weight Process Thread ID (LWP TID)
LWP_TID=$(top -H -p $PID -b -n 1 | head -n 8 | tail -n 1 | awk '{print $1}')
echo "High CPU Thread LWP TID (Decimal): $LWP_TID"

# 3. Convert Decimal Thread ID to Hexadecimal (format: 0x...)
HEX_TID=$(printf "0x%x\n" $LWP_TID)
echo "High CPU Thread ID in Hex: $HEX_TID"

# 4. Take Thread Dump using jstack
jstack $PID > /tmp/thread_dump_$PID.txt

# 5. Search Thread Dump for the matching Hex Thread ID
grep -A 20 "$HEX_TID" /tmp/thread_dump_$PID.txt
```

#### 💻 Runaway Code Example Triggering High CPU
```java
// Infinite loop / Regex Catastrophic Backtracking triggering 100% CPU:
public class RunawayCpuTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            // Infinite loop doing heavy regex matching or math computation
            Math.sin(Math.random());
        }
    }
}
```

---

### 7.3 Design Patterns Quick Reference & Real-World Code Implementations

#### 1. Singleton Pattern (Double-Checked Locking)
```java
public class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;

    private DatabaseConnectionPool() {} // Private constructor

    public static DatabaseConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }
}
```

#### 2. Factory Pattern
```java
public interface PaymentProcessor { void process(double amount); }

public class StripeProcessor implements PaymentProcessor {
    public void process(double amount) { System.out.println("Stripe Charged: $" + amount); }
}

public class PayPalProcessor implements PaymentProcessor {
    public void process(double amount) { System.out.println("PayPal Charged: $" + amount); }
}

public class PaymentFactory {
    public static PaymentProcessor getProcessor(String type) {
        if ("STRIPE".equalsIgnoreCase(type)) return new StripeProcessor();
        if ("PAYPAL".equalsIgnoreCase(type)) return new PayPalProcessor();
        throw new IllegalArgumentException("Unknown payment type");
    }
}
```

#### 3. Builder Pattern
```java
public class UserDto {
    private final String name;
    private final String email;

    private UserDto(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
    }

    public static class Builder {
        private String name;
        private String email;

        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public UserDto build() { return new UserDto(this); }
    }
}

// Usage: UserDto user = new UserDto.Builder().name("Alice").email("alice@dev.com").build();
```

#### 4. Strategy Pattern
```java
public interface DiscountStrategy { double calculateDiscount(double price); }

public class RegularDiscount implements DiscountStrategy {
    public double calculateDiscount(double price) { return price * 0.05; }
}

public class BlackFridayDiscount implements DiscountStrategy {
    public double calculateDiscount(double price) { return price * 0.50; }
}

public class CheckoutContext {
    private DiscountStrategy strategy;
    public CheckoutContext(DiscountStrategy strategy) { this.strategy = strategy; }
    public double executeCheckout(double price) { return price - strategy.calculateDiscount(price); }
}
```

#### 5. Observer Pattern
```java
public interface EventObserver { void onEvent(String message); }

public class EventPublisher {
    private List<EventObserver> observers = new ArrayList<>();
    public void subscribe(EventObserver observer) { observers.add(observer); }
    public void notifyAll(String msg) { observers.forEach(o -> o.onEvent(msg)); }
}
```

#### 6. Decorator Pattern
```java
public interface Coffee { double getCost(); }

public class SimpleCoffee implements Coffee { public double getCost() { return 2.0; } }

public class MilkDecorator implements Coffee {
    private Coffee coffee;
    public MilkDecorator(Coffee coffee) { this.coffee = coffee; }
    public double getCost() { return coffee.getCost() + 0.5; }
}
```

#### 7. Adapter Pattern
```java
// Legacy XML API
class LegacyXmlService { String getXmlData() { return "<user><name>John</name></user>"; } }

// Target Interface expecting JSON
interface JsonDataTarget { String getJsonData(); }

// Adapter Class
class XmlToJsonAdapter implements JsonDataTarget {
    private LegacyXmlService xmlService;
    public XmlToJsonAdapter(LegacyXmlService xmlService) { this.xmlService = xmlService; }
    public String getJsonData() {
        String xml = xmlService.getXmlData();
        return "{\"name\": \"John\"}"; // Converts XML to JSON
    }
}
```

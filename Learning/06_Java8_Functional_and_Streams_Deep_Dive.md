# Chapter 6: Java 8 Functional Programming, Lambdas & Streams Master Guide

---

## 1. Java 8 Core Features Overview

Java 8 (released in 2014) is the most transformative release in Java history, introducing functional programming to the language:
1. **Lambda Expressions** & **Functional Interfaces** (`@FunctionalInterface`).
2. **Stream API** (Declarative data processing pipelines).
3. **`Optional<T>`** (Eliminating `NullPointerException`).
4. **Default and Static Methods** in Interfaces.
5. **Method References** (`::`).
6. **New Date and Time API** (`java.time` - `LocalDate`, `Instant`, `Duration`).
7. **CompletableFuture** & `StampedLock`.

---

## 2. Functional Interfaces & Core 4 Functional Types

A **Functional Interface** has **exactly ONE abstract method** (SAM - Single Abstract Method). It can have multiple `default` or `static` methods.

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input); // Single Abstract Method

    default void log(T input) { System.out.println("Processing: " + input); }
}
```

### The 4 Core Built-In Functional Interfaces:

```
+---------------------------------------------------------------------------------------------------+
| Interface          | Abstract Method         | Input Args | Return Type | Typical Use Case        |
+---------------------------------------------------------------------------------------------------+
| `Predicate<T>`     | `boolean test(T t)`     | 1 (`T`)    | `boolean`   | Filtering in `filter()` |
| `Function<T, R>`   | `R apply(T t)`          | 1 (`T`)    | `R`         | Transformation in `map()`|
| `Consumer<T>`      | `void accept(T t)`      | 1 (`T`)    | `void`      | Printing, saving to DB  |
| `Supplier<T>`      | `T get()`               | 0          | `T`         | Factory, generating IDs |
+---------------------------------------------------------------------------------------------------+
```

### Code Example: Composing Functional Interfaces

```java
import java.util.function.*;

public class FunctionalTypesDemo {
    public static void main(String[] args) {
        // 1. Predicate (Condition testing & chaining)
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isPositiveEven = isEven.and(isPositive);
        System.out.println("Is 8 positive even? " + isPositiveEven.test(8)); // true

        // 2. Function (Transformation & chaining)
        Function<String, Integer> stringLength = String::length;
        Function<Integer, Integer> square = n -> n * n;
        Function<String, Integer> lengthSquared = stringLength.andThen(square);
        System.out.println("Length squared of 'Java': " + lengthSquared.apply("Java")); // 16

        // 3. Consumer (Side-effects)
        Consumer<String> printer = s -> System.out.println("[LOG]: " + s);
        printer.accept("Transaction complete");

        // 4. Supplier (Lazy Value Provider)
        Supplier<Double> randomSupplier = Math::random;
        System.out.println("Random: " + randomSupplier.get());
    }
}
```

---

## 3. Method References (`::`)

Method references provide a compact syntax for lambdas that simply call an existing method.

```
+---------------------------------------------------------------------------------------------------+
| Type                            | Syntax                     | Equivalent Lambda                  |
+---------------------------------------------------------------------------------------------------+
| Reference to Static Method      | `Math::abs`                | `x -> Math.abs(x)`                 |
| Reference to Instance Method    | `String::toLowerCase`      | `s -> s.toLowerCase()`             |
| Reference to Arbitrary Object   | `System.out::println`      | `x -> System.out.println(x)`       |
| Constructor Reference           | `ArrayList::new`           | `() -> new ArrayList<>()`          |
+---------------------------------------------------------------------------------------------------+
```

---

## 4. `Optional<T>`: Eliminating `NullPointerException`

### 🟢 Allowed & Recommended Practices:
```java
public class OptionalBestPractices {
    public Optional<String> findUsernameById(Long id) {
        if (id == 100L) return Optional.of("Alice");
        if (id == 200L) return Optional.ofNullable(null); // Returns Optional.empty()
        return Optional.empty();
    }

    public static void main(String[] args) {
        OptionalBestPractices service = new OptionalBestPractices();

        // 1. orElse (Eager fallback)
        String user1 = service.findUsernameById(200L).orElse("Guest");

        // 2. orElseGet (Lazy Supplier fallback - only computed if empty!)
        String user2 = service.findUsernameById(200L).orElseGet(() -> computeDefaultUser());

        // 3. orElseThrow (Throw custom exception if missing)
        String user3 = service.findUsernameById(100L)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 4. ifPresent (Execute action if present)
        service.findUsernameById(100L).ifPresent(name -> System.out.println("Hello " + name));
    }

    private static String computeDefaultUser() { return "Guest_" + System.currentTimeMillis(); }
}
```

### 🔴 What NOT to do with `Optional` (Anti-patterns):
- ❌ **Never call `optional.get()` without checking `optional.isPresent()`** (Throws `NoSuchElementException`).
- ❌ **Never use `Optional` as class instance fields** (`Optional` is not `Serializable`).
- ❌ **Never pass `Optional` as method arguments** (Use method overloading or nullable parameters instead).
- ❌ **Never wrap collections in Optional** (Return `Collections.emptyList()` instead of `Optional<List<T>>`).

---

## 5. Stream API: Pipeline Architecture, `map()` vs `flatMap()` & Laziness

### Stream Pipeline Lifecycle
```
[ Data Source ] ---> [ Intermediate Operations ] ---> [ Terminal Operation ]
(List, Set, Array)   (filter, map, sorted, distinct)   (collect, count, forEach)
                     * Pull-based & LAZY               * Triggers execution!
```

---

### `map()` vs `flatMap()`

```
map()     : Transforms [ T ] ----> [ R ]                (1-to-1 relationship)
flatMap() : Transforms [ T ] ----> [ Stream<R> ] ----> [ R, R, R ] (1-to-Many Flattening)
```

```java
import java.util.*;
import java.util.stream.Collectors;

public class MapVsFlatMapDemo {
    public static void main(String[] args) {
        List<List<String>> nestedSkills = List.of(
            List.of("Java", "Spring"),
            List.of("Docker", "Kubernetes"),
            List.of("AWS", "Kafka")
        );

        // 1. map() keeps the nested structure: Stream<List<String>>
        List<Integer> listSizes = nestedSkills.stream()
            .map(List::size) // 1-to-1 transformation
            .collect(Collectors.toList());
        System.out.println("Sizes: " + listSizes); // [2, 2, 2]

        // 2. flatMap() flattens inner streams into a single merged stream: Stream<String>
        List<String> flattenedSkills = nestedSkills.stream()
            .flatMap(Collection::stream) // Flattens List<List<String>> -> Stream<String>
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("All Skills: " + flattenedSkills); 
        // [JAVA, SPRING, DOCKER, KUBERNETES, AWS, KAFKA]
    }
}
```

---

### Lazy vs Eager Evaluation in Streams

Intermediate operations are **lazy**; they do not process any elements until the **terminal operation** is invoked.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Nothing prints here because there is NO terminal operation!
names.stream().filter(name -> {
    System.out.println("Filtering: " + name);
    return name.length() > 3;
});

// Adding .count() triggers the entire pipeline:
long count = names.stream().filter(name -> {
    System.out.println("Filtering: " + name);
    return name.length() > 3;
}).count(); // Output: Filtering Alice, Filtering Bob, Filtering Charlie
```

---

## 6. Parallel Streams & Custom Collectors

### Parallel Streams & Thread Pools:
- `collection.parallelStream()` partitions data using the `Spliterator` and processes chunks concurrently across cores using **`ForkJoinPool.commonPool()`**.
- **Warning:** Parallel streams must **NEVER** modify shared mutable state (causes severe race conditions).

### Creating a Custom Collector:
```java
import java.util.stream.Collector;
import java.util.StringJoiner;

public class CustomCollectorDemo {
    public static void main(String[] args) {
        List<String> words = List.of("Java", "Streams", "Are", "Powerful");

        // Custom collector joining strings with custom prefix, delimiter, suffix
        Collector<CharSequence, StringJoiner, String> customJoiner = Collector.of(
            () -> new StringJoiner(", ", "[", "]"), // Supplier (Accumulator container)
            StringJoiner::add,                      // Accumulator (How to add item)
            StringJoiner::merge,                    // Combiner (For parallel streams)
            StringJoiner::toString                  // Finisher (Final transformation)
        );

        String result = words.stream().collect(customJoiner);
        System.out.println(result); // [Java, Streams, Are, Powerful]
    }
}
```

---

## 7. Java 8 Allowed vs Forbidden Matrix

| Feature / Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| Adding multiple abstract methods to `@FunctionalInterface` | 🔴 **FORBIDDEN** | **Compile Error:** Invalid functional interface annotation. |
| Adding `default` and `static` methods to functional interfaces | 🟢 **ALLOWED** | Only the abstract method count is strictly capped at 1. |
| Reusing a Stream instance after a terminal operation | 🔴 **FORBIDDEN** | **Runtime Exception:** `IllegalStateException: stream has already been operated upon or closed`. |
| Modifying the source collection while streaming | 🔴 **FORBIDDEN** | **Runtime Exception:** Throws `ConcurrentModificationException`. |
| Accessing non-final local variables inside lambdas | 🔴 **FORBIDDEN** | Must be `final` or `effectively final`. |

# Chapter 7: Java 17 LTS & Modern Java Evolution

---

## 1. Java 17 LTS Features Overview

Java 17 (released September 2021) is the premier **Long-Term Support (LTS)** release following Java 11 and Java 8. It brings massive syntax enhancements, JVM performance improvements, and memory optimizations.

### Top Java 17 LTS Features:
1. **Records (Java 16 Standardized, Java 17 LTS):** Immutable data carrier classes.
2. **Sealed Classes & Interfaces (Java 17):** Strict inheritance control.
3. **Pattern Matching for `instanceof` (Java 16/17):** Eliminates explicit casting.
4. **Switch Expressions & Pattern Matching:** Returns values and eliminates fall-through bugs.
5. **Text Blocks (`"""`):** Multi-line string literals without escaping.
6. **Helpful NullPointerExceptions:** Pinpoints the exact method/variable that was `null`.
7. **Strong Encapsulation of JDK Internals:** Disables illegal reflection into JVM internal APIs.
8. **New macOS / AArch64 (Apple Silicon) Port.**

---

## 2. Java 8 vs Java 17: Architectural Comparison Matrix

| Dimension | Java 8 (2014) | Java 17 LTS (2021) |
| :--- | :--- | :--- |
| **Java Memory Model** | PermGen space (Prone to `OutOfMemoryError: PermGen`) | **Metaspace** (Native memory allocation, auto-resizing). |
| **Data Carrier Syntax** | Verbose POJOs / DTOs (Requires getters, setters, equals, hashCode) | **Records** (`record User(Long id, String name) {}`). |
| **Inheritance Control** | All-or-nothing (`public` vs `final`). | **Sealed Classes** (`sealed ... permits ChildA, ChildB`). |
| **Casting Syntax** | Boilerplate: `if (o instanceof String) { String s = (String) o; }` | Pattern Matching: `if (o instanceof String s) { s.length(); }` |
| **Multi-line Strings** | Concatenation with `\n` and `+`. | **Text Blocks** (`""" ... """`). |
| **Default Garbage Collector** | Parallel GC. | **G1 GC** (Low latency, high throughput) + **ZGC** (Sub-millisecond pause). |
| **Switch Statements** | Statement only, requires `break` statements (fall-through bugs). | **Switch Expressions** (`case X -> yield value;` or arrow syntax). |
| **JVM Internal Access** | Unrestricted reflection (`sun.misc.Unsafe`). | **Strong encapsulation** (Enforced modular boundary). |

---

## 3. Pattern Matching for `instanceof` & Switch Expressions

### Pattern Matching for `instanceof`
```java
// Java 8:
public void processOld(Object obj) {
    if (obj instanceof String) {
        String s = (String) obj; // Redundant manual cast!
        System.out.println("Length: " + s.length());
    }
}

// Java 17 Pattern Matching:
public void processModern(Object obj) {
    // Variable 's' is automatically cast and scoped!
    if (obj instanceof String s && s.length() > 5) {
        System.out.println("Upper: " + s.toUpperCase());
    }
}
```

---

### Switch Expressions with Pattern Matching

```java
public class ModernSwitchDemo {
    public static String formatObject(Object obj) {
        return switch (obj) {
            case Integer i -> String.format("Integer: %d", i);
            case Long l    -> String.format("Long: %d", l);
            case Double d  -> String.format("Double: %.2f", d);
            case String s  -> String.format("String of length %d", s.length());
            case null      -> "Null object";
            default        -> obj.toString();
        };
    }
}
```

---

## 4. Text Blocks (`"""`)

```java
// Java 8:
String jsonOld = "{\n" +
                 "  \"name\": \"Alice\",\n" +
                 "  \"age\": 30\n" +
                 "}";

// Java 17 Text Block:
String jsonModern = """
    {
      "name": "Alice",
      "age": 30
    }
    """;
```

---

## 5. Virtual Threads (Project Loom) Evolution

- **Java 17 LTS:** Laid the foundational JVM fiber architecture.
- **Java 19/20:** Released as Preview Feature (JEP 425 / 436).
- **Java 21 LTS:** **Standardized & Production Ready** (JEP 444).
- **Impact:** Replaces asynchronous callback hell and reactive complexity with standard, synchronous, readable blocking code that scales to millions of concurrent tasks.

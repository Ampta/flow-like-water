# Chapter 3: Java Exception Handling Architecture, Propagation & Patterns

---

## 1. The Java Exception Hierarchy

Every throwable object in Java inherits from `java.lang.Throwable`.

```
                              [java.lang.Throwable]
                                 /              \
                   [java.lang.Error]       [java.lang.Exception]
                   (Unchecked/Fatal)             /          \
                   - OutOfMemoryError           /      [RuntimeException]
                   - StackOverflowError        /       (Unchecked/Bugs)
                   - NoClassDefFoundError     /        - NullPointerException
                   - VirtualMachineError     /         - IllegalArgumentException
                                            /          - IndexOutOfBoundsException
                                [Checked Exceptions]   - ConcurrentModificationException
                                - IOException          - ArithmeticException
                                - SQLException
                                - ClassNotFoundException
```

---

## 2. Checked vs Unchecked Exceptions

| Dimension | Checked Exceptions | Unchecked Exceptions (`RuntimeException`) |
| :--- | :--- | :--- |
| **Inheritance** | Directly extends `Exception` (not `RuntimeException`). | Extends `RuntimeException`. |
| **Compiler Enforced?** | 🟢 **YES** (Must use `try-catch` or declare `throws`). | 🔴 **NO** (Optional to catch or declare). |
| **Philosophy** | Recoverable scenarios (File missing, DB connection timeout). | Programming bugs, logical errors (Null pointer, bad index). |
| **Examples** | `IOException`, `SQLException`, `ClassNotFoundException` | `NullPointerException`, `IllegalArgumentException`, `ArithmeticException` |

---

## 3. Exception Propagation: The Call Stack Unrolling

When an exception is thrown in a method, Java searches the call stack backwards for the nearest enclosing `try-catch` block capable of handling it.

```
+-----------------------------------------------------------------------------------+
| Call Stack:                                                                       |
|  [Method C()] ---> Throws NullPointerException                                    |
|       | (Not handled here -> pops stack frame C)                                  |
|  [Method B()] ---> (Not handled here -> pops stack frame B)                       |
|       |                                                                           |
|  [Method A()] ---> Has try-catch block! Handles exception & resumes execution     |
|       |                                                                           |
|  [main()]     ---> Normal program flow continues                                  |
+-----------------------------------------------------------------------------------+
```

### Exception Propagation Rules:
- **Unchecked Exceptions (`RuntimeException`):** Automatically propagate up the call stack without needing `throws` declarations.
- **Checked Exceptions:** Will **NOT** compile if they propagate through methods that do not explicitly declare `throws ExceptionType`.

```java
public class PropagationDemo {
    void methodC() {
        // Unchecked exception propagates automatically
        throw new RuntimeException("Failure in Method C");
    }

    void methodB() {
        methodC(); // Unhandled, unrolls methodB's stack frame
    }

    void methodA() {
        try {
            methodB();
        } catch (RuntimeException e) {
            System.out.println("Handled in methodA: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new PropagationDemo().methodA();
        System.out.println("Main completes successfully.");
    }
}
```

---

## 4. `ClassNotFoundException` vs `NoClassDefFoundError`

This is one of the most frequently asked Java interview questions:

```
+-----------------------------------------------------------------------------------------------+
| Dimension        | `ClassNotFoundException`              | `NoClassDefFoundError`             |
+-----------------------------------------------------------------------------------------------+
| Type             | **Checked Exception**                 | **Fatal Error** (Unchecked)        |
| Origin           | Dynamic class loading at runtime      | Compile-time present, runtime lost |
| Trigger Scenario | `Class.forName("com.mysql.Driver")`   | Class was present at compile-time, |
|                  | or `ClassLoader.loadClass()` fails to | but at runtime the `.class` file   |
|                  | find the `.class` file on classpath.  | is missing OR static initializer   |
|                  |                                       | (`static { ... }`) threw exception!|
| Recovery         | Recoverable via `try-catch`.          | Irrecoverable runtime crash.       |
+-----------------------------------------------------------------------------------------------+
```

### Code Demonstration: `NoClassDefFoundError` caused by Static Initializer Failure
```java
class ProblematicClass {
    // If a static block fails during class loading...
    static {
        int x = 10 / 0; // Throws ExceptionInInitializerError
    }
}

public class ErrorDemo {
    public static void main(String[] args) {
        try {
            new ProblematicClass();
        } catch (Throwable t) {
            System.out.println("First attempt failed: " + t);
        }

        // Second attempt to use ProblematicClass will throw NoClassDefFoundError!
        new ProblematicClass(); // RUNTIME CRASH: java.lang.NoClassDefFoundError
    }
}
```

---

## 5. `NullPointerException` (NPE) & Modern Prevention

### What Causes NPE?
Invoking a method, accessing a field, unboxing a boxed primitive, or reading length on an object reference that points to `null`.

```java
String str = null;
str.length(); // RUNTIME ERROR: NullPointerException
```

### 3 Production Patterns to Eliminate NPE:
```java
import java.util.Objects;
import java.util.Optional;

public class NPEPrevention {
    // 1. Objects.requireNonNull with descriptive message
    public void processUser(String username) {
        Objects.requireNonNull(username, "Username must not be null for authentication");
    }

    // 2. Return Optional instead of null for absent values
    public Optional<String> findUserEmail(Long id) {
        if (id == 1L) return Optional.of("alice@example.com");
        return Optional.empty(); // NEVER return null!
    }

    // 3. Safe invocation via Optional
    public static void main(String[] args) {
        NPEPrevention demo = new NPEPrevention();
        String email = demo.findUserEmail(2L).orElse("default@example.com");
        System.out.println("Email: " + email);
    }
}
```

---

## 6. `ConcurrentModificationException` Mechanics

Occurs when a collection is modified structurally while iterating over it via fail-fast iterators (mismatch between `modCount` and `expectedModCount`).

```java
// 🔴 RUNTIME CRASH:
List<String> items = new ArrayList<>(List.of("1", "2", "3"));
for (String s : items) {
    if (s.equals("2")) {
        items.remove(s); // Modifies modCount, expectedModCount is unchanged!
    }
}

// 🟢 FIX 1: Iterator.remove()
Iterator<String> it = items.iterator();
while (it.hasNext()) {
    if (it.next().equals("2")) {
        it.remove(); // Safely updates both counters
    }
}

// 🟢 FIX 2: Java 8 Collection.removeIf()
items.removeIf(s -> s.equals("2"));
```

---

## 7. `throw` vs `throws`, `finally` Rules & `try-with-resources`

### `throw` vs `throws`
| Feature | `throw` | `throws` |
| :--- | :--- | :--- |
| **Usage** | Inside method body to trigger exception. | In method signature to declare checked exceptions. |
| **Syntax** | `throw new CustomException("msg");` | `void save() throws IOException` |

### Custom Exceptions with Error Codes & Root Cause Chaining
```java
public class OrderProcessingException extends Exception {
    private final String orderId;
    private final int errorCode;

    public OrderProcessingException(String msg, Throwable cause, String orderId, int errorCode) {
        super(msg, cause); // Retains underlying root cause exception
        this.orderId = orderId;
        this.errorCode = errorCode;
    }

    public String getOrderId() { return orderId; }
    public int getErrorCode() { return errorCode; }
}
```

### `try-catch-finally` Allowed vs Forbidden Matrix:
| Code Structure / Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| `try { } catch (Exception e) { }` | 🟢 **ALLOWED** | Standard handling. |
| `try { } finally { }` | 🟢 **ALLOWED** | Allowed without `catch`. |
| `try { }` (Alone) | 🔴 **FORBIDDEN** | Must have `catch`, `finally`, or resource declaration. |
| Catching parent exception before child | 🔴 **FORBIDDEN** | **Compile Error:** Unreachable catch block. |
| Catching checked exception never thrown in `try` | 🔴 **FORBIDDEN** | **Compile Error:** Exception is never thrown in body of try statement. |
| Resource closing in `try-with-resources` | 🟢 **ALLOWED** | Closed in **REVERSE ORDER** of declaration. |

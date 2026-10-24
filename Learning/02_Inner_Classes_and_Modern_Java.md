# Chapter 2: Inner Classes, Static Nested Classes & Modern Java Records vs DTOs

---

## 1. Nested & Inner Classes: The Complete Guide

Java provides 4 types of nested classes:
1. **Static Nested Class**
2. **Member Inner Class (Non-static)**
3. **Local Inner Class (Defined inside a method/block)**
4. **Anonymous Inner Class (Inline nameless implementation)**

```
                                  [Nested Classes]
                                    /          \
                       [Static Nested Class]   [Inner Classes (Non-Static)]
                                                /           |          \
                                  [Member Inner]   [Local Inner]   [Anonymous Inner]
```

---

### Comparison: Static Nested Class vs Member Inner Class

| Feature | Static Nested Class | Member Inner Class (Non-Static) |
| :--- | :--- | :--- |
| **Instance Relationship** | Independent of any Outer class instance. | Bound to a specific enclosing instance of Outer class. |
| **Outer Instance Reference** | **No** implicit reference to outer instance (`Outer.this` does not exist). | **Yes**, holds an implicit reference to `Outer.this`. |
| **Access to Outer Members** | Can access **static** members directly (even `private`). Cannot access instance members without an explicit object. | Can access **all** members (both static and instance, including private) directly. |
| **Instantiation Syntax** | `Outer.Nested n = new Outer.Nested();` | `Outer.Inner in = outerObj.new Inner();` |
| **Memory Leak Risk** | None. | **High risk** in long-lived scenarios (e.g., Android handlers, callbacks) because it keeps outer object alive in GC. |
| **Primary Real-world Use** | Builder Pattern, Helper Data Structures (e.g. `Map.Entry`, `LinkedList.Node`). | Event listeners, internal state machines tightly coupled to the outer instance. |

---

### Comprehensive Code Example: Static Nested vs Member Inner Class

```java
public class OuterAccount {
    private String accountNumber = "ACC-9988";
    private static String bankName = "Global Federal Bank";

    // ==========================================
    // 1. STATIC NESTED CLASS (e.g., Builder or Helper)
    // ==========================================
    public static class AccountConfig {
        private boolean isInternationalEnabled;

        public AccountConfig(boolean isInternationalEnabled) {
            this.isInternationalEnabled = isInternationalEnabled;
        }

        public void printInfo() {
            // 🟢 CAN DO: Access outer static members directly
            System.out.println("Bank: " + bankName);

            // 🔴 CANNOT DO: Access outer instance variable directly!
            // System.out.println(accountNumber); // COMPILE ERROR!

            // 🟢 CAN DO: Access instance variable ONLY via an explicit object
            OuterAccount acc = new OuterAccount();
            System.out.println("Outer private field via explicit instance: " + acc.accountNumber);
        }
    }

    // ==========================================
    // 2. MEMBER INNER CLASS (Non-Static)
    // ==========================================
    public class TransactionHistory {
        private String transactionId;

        public TransactionHistory(String transactionId) {
            this.transactionId = transactionId;
        }

        public void printReceipt() {
            // 🟢 CAN DO: Directly access BOTH static and private instance members of Outer
            System.out.println("Bank: " + bankName);
            System.out.println("Account: " + accountNumber); // Accesses Outer.this.accountNumber
            System.out.println("Transaction ID: " + this.transactionId);
        }
    }

    // Outer method calling Inner class methods
    public void executeTransaction() {
        TransactionHistory th = new TransactionHistory("TXN-101");
        th.printReceipt();
    }
}

class Main {
    public static void main(String[] args) {
        // --- 1. Instantiating Static Nested Class ---
        // Notice: NO outer object instance required!
        OuterAccount.AccountConfig config = new OuterAccount.AccountConfig(true);
        config.printInfo();

        // --- 2. Instantiating Member Inner Class ---
        // Notice: REQUIRES an existing outer object instance!
        OuterAccount account = new OuterAccount();
        OuterAccount.TransactionHistory history = account.new TransactionHistory("TXN-555");
        history.printReceipt();
    }
}
```

### Static Nested & Inner Class: Allowed vs Forbidden Matrix

| Action | Static Nested Class | Member Inner Class (Non-static) |
| :--- | :--- | :--- |
| Instantiation without outer instance | 🟢 **ALLOWED** (`new Outer.Nested()`) | 🔴 **FORBIDDEN** (`outer.new Inner()`) |
| Accessing outer `private static` fields | 🟢 **ALLOWED** | 🟢 **ALLOWED** |
| Accessing outer `private instance` fields | 🔴 **FORBIDDEN (Directly)** | 🟢 **ALLOWED (Directly via `Outer.this`)** |
| Declaring `static` fields & methods | 🟢 **ALLOWED** | 🟢 **ALLOWED (Java 16+)** |
| Holding reference to outer object | 🔴 **No reference (No memory leak)** | 🟢 **Yes (Implicit pointer; leak risk)** |

---

## 2. Local Inner Classes & The "Effectively Final" Rule

### Q: Why must local variables accessed from an inner class or lambda be `final` or `effectively final`?

**Deep Architectural Reason (Stack vs Heap):**
- **Stack Lifetime:** Method local variables live on the **thread stack frame** and are destroyed immediately when the method returns.
- **Heap Lifetime:** Inner class and lambda instances live on the **Heap** and often outlive the method execution (e.g., passed to another thread or background callback).
- To make this possible, the Java compiler creates a **hidden copy** of the local variable inside the synthetic inner class instance.
- If the original variable could be modified, the stack variable and heap copy would fall out of sync. Java enforces `final` (or effectively final) to guarantee consistency.

```java
public class ClosureDemo {
    public Runnable createPrinter(String prefix) {
        int count = 10; // Effectively final (never reassigned)
        int modifiedVar = 50;
        modifiedVar = 60; // Not effectively final!
        
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 🟢 CAN DO: Access effectively final local variable
                System.out.println(prefix + " -> Count: " + count);

                // 🔴 CANNOT DO: Access reassigned local variable!
                // System.out.println(modifiedVar); // COMPILE ERROR!
            }
        };

        // 🔴 CANNOT DO: Modify count after referencing in inner class
        // count = 20; // COMPILE ERROR!

        return task;
    }
}
```

### Anonymous Classes: Allowed vs Forbidden
| Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| Implementing multiple interfaces | 🔴 **FORBIDDEN** | **Compile Error:** Can implement exactly ONE interface or extend ONE class. |
| Having a named constructor | 🔴 **FORBIDDEN** | Anonymous classes have no name; use instance initializers `{ }` instead. |
| Accessing non-final local variables | 🔴 **FORBIDDEN** | **Compile Error:** Must be `final` or effectively final. |
| Defining new helper methods | 🟢 **ALLOWED** | Can define new methods, but they can only be called from inside the anonymous class. |

---

## 3. Java Records vs DTOs vs POJOs vs JPA Entities

Introduced in modern Java (Java 14/17), `record` eliminates boilerplate when modeling immutable data carriers.

### Structural Comparison Matrix

| Aspect | `record` (Java 16+) | Traditional `DTO` (POJO) | JPA `Entity` |
| :--- | :--- | :--- | :--- |
| **Primary Purpose** | Pure, immutable data carrier. | Data transport between layers/APIs. | Database table mapping & persistence. |
| **Mutability** | **Strictly Immutable** (fields are `private final`). | Usually mutable (or mutable with setters/getters). | Highly mutable (requires setters/reflection for ORM). |
| **Inheritance** | **Cannot extend any class** (implicitly extends `java.lang.Record`). Can implement interfaces. | Can extend classes & implement interfaces. | Can extend classes & implement interfaces. |
| **Boilerplate** | Zero. Auto-generates constructor, accessors (`id()`), `equals()`, `hashCode()`, `toString()`. | High boilerplate (or requires Lombok `@Data`). | High boilerplate (requires getters, setters, no-arg ctor). |
| **No-arg Constructor** | No (unless custom created). | Yes (required by many serializers). | **Strictly Required** by JPA / Hibernate. |
| **Custom Fields** | Cannot declare instance fields; only `static` fields. | Can declare any instance or static fields. | Maps fields to database columns (`@Column`). |

---

### Code Example: Record with Compact Constructor, Validation & Methods

```java
import java.io.Serializable;

// Defining a Record with compact constructor, validation, and interface implementation
public record UserDTO(
    Long id,
    String username,
    String email,
    int age
) implements Serializable {

    // 🟢 CAN DO: Static fields and methods
    public static final String DEFAULT_DOMAIN = "@example.com";
    public static void printInfo() { System.out.println("UserDTO Record"); }

    // 🟢 CAN DO: Compact Constructor (Validation & Normalization without this.x = x boilerplate)
    public UserDTO {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        // Normalize email automatically
        email = (email == null) ? username.toLowerCase() + DEFAULT_DOMAIN : email.toLowerCase();
    }

    // 🟢 CAN DO: Custom instance helper methods
    public boolean isAdult() {
        return age >= 18;
    }

    // 🔴 CANNOT DO: Declare instance variables!
    // private int score; // COMPILE ERROR: Instance field not permitted in record

    // 🔴 CANNOT DO: Extend another class!
    // public record UserDTO(...) extends BaseDTO // COMPILE ERROR!
}
```

### Accessing Record Data vs POJO
```java
public class RecordDemo {
    public static void main(String[] args) {
        UserDTO user1 = new UserDTO(1L, "john_doe", null, 25);
        UserDTO user2 = new UserDTO(1L, "john_doe", "john_doe@example.com", 25);

        // 1. Accessors (Note: method name is fieldName(), NOT getFieldName())
        System.out.println(user1.username()); // "john_doe"
        System.out.println(user1.email());    // "john_doe@example.com" (Normalized in compact ctor)

        // 2. Built-in toString()
        System.out.println(user1); 
        // Prints: UserDTO[id=1, username=john_doe, email=john_doe@example.com, age=25]

        // 3. Built-in equals() & hashCode() by value
        System.out.println(user1.equals(user2)); // true! (Value-based equality)
    }
}
```

### Java `record` Allowed vs Forbidden Comprehensive Matrix:
| Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| Implementing one or more interfaces | 🟢 **ALLOWED** | e.g. `record User(...) implements Serializable, Comparable<User>` |
| Declaring `static` fields and methods | 🟢 **ALLOWED** | Allowed for constants and static utility methods. |
| Customizing constructors (Compact / Canonical) | 🟢 **ALLOWED** | Used for field validation, defensive copying, normalization. |
| Modifying component values after creation | 🔴 **FORBIDDEN** | **Records are strictly immutable.** Fields are `private final`. |
| Declaring instance variables (`private int age;`) | 🔴 **FORBIDDEN** | **Compile Error:** All instance state must be in the record header component list. |
| Extending a class (`record R extends Base`) | 🔴 **FORBIDDEN** | **Compile Error:** Records already implicitly extend `java.lang.Record`. |
| Marking a record as `abstract` or `sealed` | 🔴 **FORBIDDEN** | **Compile Error:** Records are implicitly `final` and cannot be extended. |
| Providing a custom `equals()`, `hashCode()`, or `toString()` | 🟢 **ALLOWED** | Can override default generated implementations if needed. |

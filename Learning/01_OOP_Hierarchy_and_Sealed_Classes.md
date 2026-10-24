# Chapter 1: OOP Hierarchy, Inheritance, Method Dispatching & Sealed Classes

---

## 1. Abstract Classes & Concrete Methods

### Q: If an abstract class has a normal (concrete) method, must a subclass override it?
**Short Answer:** **NO.** A subclass inherits the concrete method as-is unless:
1. The subclass explicitly wants to change/extend the behavior (optional override).
2. The method in the abstract class is marked `abstract` (mandatory override).
3. The method in the abstract class is marked `final` (override is **forbidden** / compile error).

---

### 🟢 WHAT YOU CAN DO with Abstract Classes (Allowed):
- **Can have a constructor:** `abstract class A { public A() { ... } }` (Invoked via `super()` during subclass instantiation).
- **Can have instance variables & static variables:** Both mutable and immutable fields are allowed.
- **Can have 0 abstract methods:** An abstract class doesn't strictly need abstract methods (used to prevent direct instantiation).
- **Can have 100% concrete methods:** All methods can have bodies.
- **Can have `static`, `final`, and `private` concrete methods:** (e.g., utility methods, template methods that shouldn't be altered).
- **Can have a `main` method and be executed directly:** `public static void main(String[] args)` runs perfectly inside an abstract class.
- **Can implement interfaces without providing implementations:** It can delegate interface method implementations to subclasses.

### 🔴 WHAT YOU CANNOT DO with Abstract Classes (Forbidden / Compile Error):
- ❌ **CANNOT instantiate directly:** `new MyAbstractClass()` $\rightarrow$ **Compile Error**.
- ❌ **CANNOT combine `abstract` and `final` on a class:** `final abstract class A` $\rightarrow$ **Compile Error** (contradiction: `abstract` demands inheritance, `final` forbids it).
- ❌ **CANNOT combine `abstract` and `private` on a method:** `private abstract void run();` $\rightarrow$ **Compile Error** (private methods are invisible to subclasses and cannot be overridden).
- ❌ **CANNOT combine `abstract` and `final` on a method:** `public final abstract void run();` $\rightarrow$ **Compile Error**.
- ❌ **CANNOT combine `abstract` and `static` on a method:** `public static abstract void run();` $\rightarrow$ **Compile Error** (static methods are resolved at compile-time and cannot be overridden).
- ❌ **CANNOT combine `abstract` and `synchronized` in method declaration:** `public synchronized abstract void run();` $\rightarrow$ **Compile Error** (synchronization is an implementation detail, not a contract).

---

### Comprehensive Code Example: Concrete Methods, Abstract Methods & Final Methods

```java
// File: PaymentProcessor.java
abstract class PaymentProcessor {
    protected String gatewayName;

    // Abstract class constructor
    public PaymentProcessor(String gatewayName) {
        this.gatewayName = gatewayName;
    }
    
    // 1. Mandatory to implement by non-abstract subclasses
    abstract void processTransaction(double amount);

    // 2. Concrete method - OPTIONAL to override
    void logAudit(String txId) {
        System.out.println("[" + gatewayName + " AUDIT - Default]: Transaction " + txId + " recorded.");
    }

    // 3. Final method - FORBIDDEN to override
    final void validateSystemHealth() {
        System.out.println("[" + gatewayName + " SYSTEM]: Core security checks passed.");
    }

    // 4. Private method - Subclass cannot see or override
    private void encryptPayload() {
        System.out.println("[SECURITY]: Internal encryption.");
    }

    // 5. Static method - Belongs to class
    public static void printPaymentSystemVersion() {
        System.out.println("Payment System v3.2.0");
    }
}

class CreditCardProcessor extends PaymentProcessor {

    public CreditCardProcessor() {
        super("CreditCardGateway"); // Calls abstract class constructor
    }

    // MUST override abstract method
    @Override
    void processTransaction(double amount) {
        System.out.println("Processing Credit Card payment of $" + amount);
    }

    // OPTIONAL: Overriding the default audit log
    @Override
    void logAudit(String txId) {
        System.out.println("[AUDIT LOG - CreditCard]: Custom high-security log for " + txId);
    }

    // ERROR if uncommented: cannot override final method from PaymentProcessor!
    // void validateSystemHealth() { }
}

class UPIProcessor extends PaymentProcessor {
    public UPIProcessor() {
        super("UPIGateway");
    }

    // MUST override abstract method
    @Override
    void processTransaction(double amount) {
        System.out.println("Processing UPI payment of $" + amount);
    }
    // Leaves logAudit() as default inherited implementation
}

public class Main {
    public static void main(String[] args) {
        PaymentProcessor p1 = new CreditCardProcessor();
        p1.processTransaction(500.0);
        p1.logAudit("TXN_101");          // Calls CreditCardProcessor's overridden version
        p1.validateSystemHealth();        // Calls PaymentProcessor's final version

        PaymentProcessor p2 = new UPIProcessor();
        p2.processTransaction(250.0);
        p2.logAudit("TXN_102");          // Calls default PaymentProcessor version

        PaymentProcessor.printPaymentSystemVersion(); // Static method call
    }
}
```

---

## 2. Extending Scenarios: Normal Class vs Abstract Class

### Scenario A: Normal (Concrete) Class Extending Abstract Class
| Action | Is it Allowed? | Rule / Consequence |
| :--- | :--- | :--- |
| Implementing `abstract` methods | **MANDATORY** | Compilation fails if any abstract method is unfulfilled. |
| Overriding normal concrete methods | **ALLOWED (Optional)** | Subclass can provide a customized implementation. |
| Calling `super.normalMethod()` | **ALLOWED** | Can invoke the parent's base logic. |
| Overriding `final` methods | ❌ **FORBIDDEN** | **Compile Error:** Cannot override final method from parent. |
| Overriding `private` methods | ❌ **FORBIDDEN** | Not visible. Defining the same signature in child is treated as a new, unrelated method. |
| Omitting `super()` when parent has no default constructor | ❌ **FORBIDDEN** | **Compile Error:** Implicit `super()` is undefined; must explicitly call `super(args)`. |

---

### Scenario B: Abstract Class Extending Another Abstract Class

```java
abstract class Animal {
    abstract void eat();
    void sleep() { System.out.println("Animal sleeping..."); }
}

abstract class Carnivore extends Animal {
    // 🟢 CAN DO: Does NOT need to implement eat()! Leaves it to concrete children.
    
    // 🟢 CAN DO: Can add new abstract methods.
    abstract void hunt();

    // 🟢 CAN DO: Can override parent's concrete method and make it abstract again!
    @Override
    abstract void sleep(); 

    // 🟢 CAN DO: Can provide implementation for parent's abstract method if it wants.
    // @Override
    // void eat() { System.out.println("Eating meat."); }
}

class Lion extends Carnivore {
    // MANDATORY: Lion must implement ALL accumulated abstract methods (eat, hunt, sleep)
    @Override void eat() { System.out.println("Lion eats meat"); }
    @Override void hunt() { System.out.println("Lion hunts zebra"); }
    @Override void sleep() { System.out.println("Lion sleeps 18 hours"); }
}
```

| Action in Abstract Subclass | Is it Allowed? | Rule / Consequence |
| :--- | :--- | :--- |
| Implementing parent abstract methods | **OPTIONAL** | Can implement some, all, or none. |
| Adding new abstract methods | **ALLOWED** | Further restricts future concrete subclasses. |
| Turning a parent concrete method into `abstract` | **ALLOWED** | Forces concrete subclasses downstream to re-implement it. |
| Being instantiated directly | ❌ **FORBIDDEN** | Still abstract; cannot do `new Carnivore()`. |

---

## 3. Interfaces vs Abstract Classes: Can Interface Extend Abstract Class?

### Q: Can an interface extend or override an abstract class?
**Short Answer:** **NO.** 
- An `interface` **CANNOT** `extend` or `implement` a `class` (whether abstract or concrete).
- An interface can **ONLY** extend other interfaces (`interface B extends A, C`).
- **Why?** Classes contain state (instance fields) and constructors (`Object` initialization hierarchy). Interfaces define stateless behavioral contracts and do not participate in class constructor chains.

```java
// ❌ COMPILE ERROR: Interface cannot extend a class!
// interface MyInterface extends PaymentProcessor { }

// 🟢 VALID: Interface extending multiple interfaces
interface Reader { void read(); }
interface Writer { void write(); }
interface ReadWriter extends Reader, Writer { } // Valid
```

### Can an Abstract Class Implement an Interface?
**YES.** An abstract class can implement an interface without implementing all (or any) of its methods:

```java
interface NotificationService {
    void sendNotification(String message);
    void setupChannels();
}

// Valid: Abstract class does not need to implement all interface methods
abstract class BaseNotificationService implements NotificationService {
    
    // Implements one method
    @Override
    public void setupChannels() {
        System.out.println("Default channel configuration initialized.");
    }

    // Leaves sendNotification(String message) for concrete subclasses to implement
}

class EmailNotificationService extends BaseNotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending Email: " + message);
    }
}
```

### Complete Interface "Can Do vs Cannot Do" Matrix:
| Feature / Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| Interface extending multiple interfaces | 🟢 **ALLOWED** | `interface C extends A, B` |
| Class implementing multiple interfaces | 🟢 **ALLOWED** | `class App implements A, B, C` |
| Interface extending a Class | 🔴 **FORBIDDEN** | **Compile Error:** Interfaces cannot extend classes. |
| Interface having `default` methods (Java 8+) | 🟢 **ALLOWED** | Concrete methods with `default` keyword. |
| Interface having `static` methods (Java 8+) | 🟢 **ALLOWED** | Utility methods called via `InterfaceName.method()`. |
| Interface having `private` methods (Java 9+) | 🟢 **ALLOWED** | Helper methods shared across default methods. |
| Interface having constructors | 🔴 **FORBIDDEN** | **Compile Error:** Interfaces hold no instance lifecycle. |
| Interface having instance variables | 🔴 **FORBIDDEN** | All fields are implicitly `public static final` constants. |
| Interface methods marked `protected` / `final` | 🔴 **FORBIDDEN** | **Compile Error:** Methods are implicitly `public`. |

---

## 4. Sealed Classes & Interfaces (Java 17+)

### Why Sealed Classes?
Prior to Java 17, inheritance was all-or-nothing:
1. `final`: No class can inherit.
2. `public / default`: Any class can inherit.

**Sealed Classes** allow a class or interface to explicitly specify **which exact subclasses are permitted to extend or implement it**.

```
                   [sealed class Shape] (permits Circle, Rectangle, Polygon)
                          /          \                      \
                         /            \                      \
            [final class Circle]   [sealed class Rectangle]   [non-sealed class Polygon]
                                          |                         |
                                 [final class Square]          (Anyone can extend Polygon)
```

### Core Rules for Sealed Hierarchies:
1. Every permitted subclass MUST directly extend/implement the sealed type.
2. Every permitted subclass must declare exactly ONE of the three modifiers:
   - `final`: Cannot be extended any further.
   - `sealed`: Extends the sealed parent and specifies its own permitted children.
   - `non-sealed`: Opens inheritance up to anyone.
3. Permitted classes must be in the **same package** or **same named module**.

---

### Code Example: All Permitted Subclass Types

```java
// File: Shape.java
public sealed class Shape permits Circle, Rectangle, Polygon {
    public abstract double calculateArea();
}

// 1. Permitted subclass marked 'final' (Terminal node)
public final class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

// 2. Permitted subclass marked 'sealed' (Further restricts to Square)
public sealed class Rectangle extends Shape permits Square {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }
}

public final class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// 3. Permitted subclass marked 'non-sealed' (Unrestricted inheritance downstream)
public non-sealed class Polygon extends Shape {
    @Override
    public double calculateArea() {
        return 0.0;
    }
}

// Anyone can now extend Polygon without being listed in Shape's permits!
class Triangle extends Polygon {
    // Valid because Polygon is non-sealed
}
```

### Sealed Interfaces & Exhaustive Pattern Matching in `switch` (Java 21)

Because the compiler knows **all possible subclasses** of a sealed hierarchy, you **do not need a `default` case** in `switch` expressions. If a new permitted subclass is added in the future, the compiler will fail and point out every unhandled switch expression across the codebase!

```java
public sealed interface OrderEvent permits OrderCreated, OrderShipped, OrderCancelled {}

public record OrderCreated(String orderId, double amount) implements OrderEvent {}
public record OrderShipped(String orderId, String trackingCode) implements OrderEvent {}
public record OrderCancelled(String orderId, String reason) implements OrderEvent {}

public class OrderService {
    // Exhaustive switch: No default branch needed!
    public static String handleEvent(OrderEvent event) {
        return switch (event) {
            case OrderCreated created   -> "Created order " + created.orderId() + " for $" + created.amount();
            case OrderShipped shipped   -> "Shipped with tracking " + shipped.trackingCode();
            case OrderCancelled cancel -> "Order cancelled due to: " + cancel.reason();
        };
    }
}
```

---

## 5. Dynamic Method Dispatching: "Parent p = new Child()" Deep Dive

### The Golden Rule:
> **"Reference type determines WHAT you can call at compile time. Object type determines WHICH implementation runs at runtime."**

```
      Parent p = new Child();
      ^^^^^^^^       ^^^^^^^
   Compile-time      Runtime
 (Determines WHAT   (Determines WHICH
  can be called)     version executes)
```

### Variable Shadowing vs Method Overriding
- **Methods** are resolved **dynamically at runtime** based on the actual object on the heap.
- **Variables (fields)** and **static methods** are resolved **statically at compile time** based on the reference type.

```java
class SuperClass {
    public int value = 100;

    public void instanceMethod() {
        System.out.println("SuperClass instanceMethod");
    }

    public static void staticMethod() {
        System.out.println("SuperClass staticMethod [Hidden]");
    }

    private void privateMethod() {
        System.out.println("SuperClass privateMethod");
    }
}

class SubClass extends SuperClass {
    public int value = 200; // Variable Shadowing (NOT Overriding)

    @Override
    public void instanceMethod() { // Overriding
        System.out.println("SubClass instanceMethod [Overridden]");
    }

    public static void staticMethod() { // Method Hiding (NOT Overriding)
        System.out.println("SubClass staticMethod [Hiding]");
    }

    public void subOnlyMethod() {
        System.out.println("SubClass unique method");
    }
}
```

### Execution Scenario Table: `SuperClass obj = new SubClass();`

| Invocation | What Executes? | Why / Explanation |
| :--- | :--- | :--- |
| `System.out.println(obj.value);` | **100** (SuperClass value) | **Variables DO NOT override.** Field access is resolved at compile time based on the reference type (`SuperClass`). |
| `obj.instanceMethod();` | **"SubClass instanceMethod [Overridden]"** | **Overridden instance methods resolve dynamically at runtime** based on the actual object on heap (`SubClass`). |
| `obj.staticMethod();` | **"SuperClass staticMethod [Hidden]"** | **Static methods DO NOT override (Method Hiding).** Resolved at compile time based on reference type (`SuperClass`). |
| `obj.subOnlyMethod();` | ❌ **Compile-Time Error** | The reference type `SuperClass` has no knowledge of `subOnlyMethod()`. |
| `((SubClass) obj).subOnlyMethod();` | **"SubClass unique method"** | **Allowed via Explicit Downcasting.** |

---

## 6. Overriding Rules: Allowed vs Forbidden

```
+---------------------------------------------------------------------------------------------------+
| Rule Dimension           | 🟢 ALLOWED (Valid Override)            | 🔴 FORBIDDEN (Compile Error)   |
+---------------------------------------------------------------------------------------------------+
| Method Signature         | Exact same name & parameter types      | Changing parameter type        |
|                          |                                        | (That becomes Overloading!)    |
+--------------------------+----------------------------------------+--------------------------------+
| Return Type              | Same type OR Subtype                   | Unrelated type or Supertype    |
|                          | (**Covariant Return Type**)            | (e.g. Object in child,         |
|                          | (e.g. Number in parent -> Double)      | Number in parent)              |
+--------------------------+----------------------------------------+--------------------------------+
| Access Modifiers         | **Equal or WIDER visibility**          | **Narrower visibility**        |
|                          | (e.g. `protected` -> `public`)         | (e.g. `public` -> `protected`) |
+--------------------------+----------------------------------------+--------------------------------+
| Checked Exceptions       | Declare **SAME, FEWER, or NARROWER**   | Declare **BROADER / NEW**      |
|                          | checked exceptions                     | checked exceptions             |
|                          | (e.g. Parent: `throws IOException`     | (e.g. Parent: `throws IOException`
|                          |  Child: `throws FileNotFoundException` |  Child: `throws Exception`)    |
+--------------------------+----------------------------------------+--------------------------------+
| Unchecked Exceptions     | Can declare any `RuntimeException`     | No restrictions                |
| (`NullPointerException`) | without restrictions                   |                                |
+--------------------------+----------------------------------------+--------------------------------+
| `final` methods          | None                                   | Cannot override `final` method |
| `static` methods         | None                                   | Cannot override (Only Hiding)  |
| `private` methods        | None                                   | Invisible (Cannot override)    |
+---------------------------------------------------------------------------------------------------+
```

---

## 7. Real-World Architecture: When to Use What?

1. **Use Abstract Classes** when:
   - You are implementing the **Template Method Pattern** (e.g., an algorithm structure where base steps are shared, but specific steps are customized).
   - You need to share common mutable state, protected fields, or constructors across closely related classes.
2. **Use Interfaces** when:
   - You want to define a common capability across unrelated classes (e.g., `Comparable`, `Serializable`, `AutoCloseable`).
   - You need multiple inheritance of type/behavior.
3. **Use Sealed Hierarchies** when:
   - You have a known, fixed domain model (e.g., Payment Types: `Card | UPI | NetBanking`, AST syntax nodes, Domain Events) and need strict compile-time safety.

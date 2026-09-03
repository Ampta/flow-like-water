# 10. Classes & Constructors - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Object-oriented templates using class keyword, constructor, methods, and inheritance.


---

## Practice #1: Basic Class Definition with Constructor & Method

### 💡 Concept & Explanation
`class` defines a template. `constructor()` initializes object properties when instantiated with `new Car(...)`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Car {
    constructor(brand, model) {
        this.brand = brand;
        this.model = model;
    }

    getDetails() {
        return `${this.brand} ${this.model}`;
    }
}

const myCar = new Car("Toyota", "Corolla");
console.log(myCar.getDetails());
```

### 🖥️ Expected Output
```text
Toyota Corolla
```


---

## Practice #2: Class with Default Parameter Constructor Values

### 💡 Concept & Explanation
Constructors support default parameter values just like standard functions.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class User {
    constructor(username, role = "Member") {
        this.username = username;
        this.role = role;
        this.createdAt = new Date().getFullYear();
    }
}

const u1 = new User("code_hero");
console.log(u1);
```

### 🖥️ Expected Output
```text
{ username: 'code_hero', role: 'Member', createdAt: 2026 }
```


---

## Practice #3: Class Inheritance with 'extends' and 'super()'

### 💡 Concept & Explanation
`extends` inherits properties and methods from parent class. `super(name)` calls parent constructor.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Animal {
    constructor(name) {
        this.name = name;
    }
    makeSound() {
        return "Generic sound";
    }
}

class Dog extends Animal {
    constructor(name, breed) {
        super(name);
        this.breed = breed;
    }
    makeSound() {
        return "Woof! Woof!";
    }
}

const dog = new Dog("Buddy", "Golden Retriever");
console.log(dog.name, "-", dog.makeSound());
```

### 🖥️ Expected Output
```text
Buddy - Woof! Woof!
```


---

## Practice #4: Static Methods in Classes

### 💡 Concept & Explanation
`static` methods belong to the class itself, not instance objects (`MathUtils.add()` vs `new MathUtils().add()`).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class MathUtils {
    static add(a, b) {
        return a + b;
    }
    static multiply(a, b) {
        return a * b;
    }
}

console.log("Static Add:", MathUtils.add(10, 20));
```

### 🖥️ Expected Output
```text
Static Add: 30
```


---

## Practice #5: Getters and Setters in Classes

### 💡 Concept & Explanation
`get area()` allows accessing computed property without parentheses (`rect.area`). `set dimensions()` mutates values.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Rectangle {
    constructor(width, height) {
        this.width = width;
        this.height = height;
    }

    get area() {
        return this.width * this.height;
    }

    set dimensions({ width, height }) {
        this.width = width;
        this.height = height;
    }
}

const rect = new Rectangle(5, 4);
console.log("Area:", rect.area);

rect.dimensions = { width: 10, height: 2 };
console.log("New Area:", rect.area);
```

### 🖥️ Expected Output
```text
Area: 20
New Area: 20
```


---

## Practice #6: Private Class Fields (#field)

### 💡 Concept & Explanation
Fields starting with `#` are truly private and cannot be accessed or modified from outside the class instance.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class BankAccount {
    #balance = 0;

    constructor(initialDeposit) {
        this.#balance = initialDeposit;
    }

    deposit(amount) {
        this.#balance += amount;
    }

    getBalance() {
        return `$${this.#balance}`;
    }
}

const account = new BankAccount(100);
account.deposit(50);
console.log(account.getBalance());
```

### 🖥️ Expected Output
```text
$150
```


---

## Practice #7: Method Chaining (Fluent Interface)

### 💡 Concept & Explanation
Returning `this` from methods allows chaining method calls (`.add(10).multiply(2)`).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Calculator {
    constructor(value = 0) {
        this.value = value;
    }
    add(n) {
        this.value += n;
        return this;
    }
    multiply(n) {
        this.value *= n;
        return this;
    }
}

const calc = new Calculator(5).add(10).multiply(2);
console.log("Final value:", calc.value);
```

### 🖥️ Expected Output
```text
Final value: 30
```


---

## Practice #8: Polymorphism (Overriding Parent Methods)

### 💡 Concept & Explanation
Child class `Circle` overrides parent `Shape`'s `draw()` method implementation.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Shape {
    draw() { return "Drawing a generic shape"; }
}

class Circle extends Shape {
    draw() { return "Drawing a circle ◯"; }
}

const shapes = [new Shape(), new Circle()];
shapes.forEach(s => console.log(s.draw()));
```

### 🖥️ Expected Output
```text
Drawing a generic shape
Drawing a circle ◯
```


---

## Practice #9: Static Properties for Tracking Instances

### 💡 Concept & Explanation
Static property `Player.count` is shared across all class instances to track total object creations.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class Player {
    static count = 0;
    constructor(name) {
        this.name = name;
        Player.count++;
    }
}

new Player("Player 1");
new Player("Player 2");
console.log("Total players created:", Player.count);
```

### 🖥️ Expected Output
```text
Total players created: 2
```


---

## Practice #10: Class Expression Assigned to Variable

### 💡 Concept & Explanation
Classes can be defined as class expressions and assigned to variables.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const Square = class {
    constructor(side) {
        this.side = side;
    }
    get perimeter() {
        return this.side * 4;
    }
};

const sq = new Square(5);
console.log("Perimeter:", sq.perimeter);
```

### 🖥️ Expected Output
```text
Perimeter: 20
```


---

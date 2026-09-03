# 3. Arrow Functions - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Shorthand function syntax, implicit return, and lexical 'this' binding.


---

## Practice #1: Single-Line Implicit Return Arrow Function

### 💡 Concept & Explanation
When an arrow function has a single expression, curly braces `{}` and `return` keyword can be omitted. It implicitly returns the evaluated result.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const add = (a, b) => a + b;

console.log(add(15, 25));
```

### 🖥️ Expected Output
```text
40
```


---

## Practice #2: Single Parameter Arrow Function (No Parentheses)

### 💡 Concept & Explanation
If an arrow function takes exactly one parameter, parentheses around the parameter are optional (`x => x * x`).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const square = x => x * x;

console.log(square(9));
```

### 🖥️ Expected Output
```text
81
```


---

## Practice #3: Multi-Line Arrow Function with Explicit Return

### 💡 Concept & Explanation
When using curly braces `{}` in an arrow function, an explicit `return` statement is required to send back a value.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const calculateTax = (price, taxRate) => {
    const taxAmount = price * taxRate;
    const totalPrice = price + taxAmount;
    return totalPrice;
};

console.log(calculateTax(100, 0.18));
```

### 🖥️ Expected Output
```text
118
```


---

## Practice #4: Returning an Object Literal Implicitly

### 💡 Concept & Explanation
To implicitly return an object literal, wrap the object in parentheses `({ ... })` so JavaScript does not confuse `{}` with function body braces.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const createUser = (id, name) => ({ id: id, name: name, active: true });

console.log(createUser(101, "Alice"));
```

### 🖥️ Expected Output
```text
{ id: 101, name: 'Alice', active: true }
```


---

## Practice #5: Lexical 'this' Binding in Arrow Functions

### 💡 Concept & Explanation
Arrow functions do not have their own `this`. They inherit `this` lexically from the surrounding scope (here, the `timer` object).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const timer = {
    seconds: 0,
    start() {
        setTimeout(() => {
            this.seconds++;
            console.log("Seconds after 100ms:", this.seconds);
        }, 100);
    }
};

timer.start();
```

### 🖥️ Expected Output
```text
Seconds after 100ms: 1
```


---

## Practice #6: Arrow Function with Array.prototype.map

### 💡 Concept & Explanation
Arrow functions provide clean, concise syntax when passing callback functions to array transformation methods like `.map()`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const prices = [10, 20, 30];
const formatted = prices.map(price => `$${price.toFixed(2)}`);

console.log(formatted);
```

### 🖥️ Expected Output
```text
[ '$10.00', '$20.00', '$30.00' ]
```


---

## Practice #7: No Arguments Object in Arrow Functions

### 💡 Concept & Explanation
Arrow functions do NOT possess an `arguments` object. To accept a dynamic number of arguments, use the rest parameter `(...args)`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const showArgs = (...args) => {
    console.log(args);
};

showArgs(1, 2, 3);
```

### 🖥️ Expected Output
```text
[ 1, 2, 3 ]
```


---

## Practice #8: Arrow Functions Cannot Be Used as Constructors

### 💡 Concept & Explanation
Arrow functions cannot be invoked with `new` because they lack a `[[Construct]]` method and prototype.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const Person = (name) => {
    this.name = name;
};

try {
    const p = new Person("John");
} catch (err) {
    console.log("Error caught:", err.message);
}
```

### 🖥️ Expected Output
```text
Error caught: Person is not a constructor
```


---

## Practice #9: Arrow Function in Event Listener / Async Callback

### 💡 Concept & Explanation
The inner arrow function inside `forEach` preserves `this.status` from `statusChecker`, avoiding `undefined` bugs.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const statusChecker = {
    status: "ONLINE",
    check() {
        const statuses = ["OK"];
        statuses.forEach(s => {
            console.log(`System status is ${this.status} (${s})`);
        });
    }
};

statusChecker.check();
```

### 🖥️ Expected Output
```text
System status is ONLINE (OK)
```


---

## Practice #10: Currying with Chained Arrow Functions

### 💡 Concept & Explanation
Chained arrow functions create curried functions where each function takes one argument and returns another function.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const add = x => y => z => x + y + z;

console.log(add(1)(2)(3));
```

### 🖥️ Expected Output
```text
6
```


---

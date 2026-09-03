# 1. Standard Functions - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Function declarations, expressions, parameters, return values, and scopes.


---

## Practice #1: Basic Function Declaration for Calculating Area

### 💡 Concept & Explanation
A standard function declaration uses the `function` keyword, takes parameters (`width`, `height`), performs a calculation, and explicitly `return`s the result. It is hoisted to the top of its scope.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function calculateRectangleArea(width, height) {
    return width * height;
}

const area = calculateRectangleArea(5, 10);
console.log("Area:", area);
```

### 🖥️ Expected Output
```text
Area: 50
```


---

## Practice #2: Function with Default Parameter Values

### 💡 Concept & Explanation
Default parameters (`name = 'Guest'`) ensure that if arguments are omitted or passed as `undefined`, default values are automatically used instead of `undefined`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function greetUser(name = "Guest", role = "Viewer") {
    return `Hello ${name}, your role is ${role}.`;
}

console.log(greetUser("Alice", "Admin"));
console.log(greetUser());
```

### 🖥️ Expected Output
```text
Hello Alice, your role is Admin.
Hello Guest, your role is Viewer.
```


---

## Practice #3: Function Expression Assigned to Variable

### 💡 Concept & Explanation
A function expression creates a function and assigns it to a variable (`const multiply`). Unlike declarations, function expressions are not hoisted before initialization.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const multiply = function(a, b) {
    return a * b;
};

console.log(multiply(6, 7));
```

### 🖥️ Expected Output
```text
42
```


---

## Practice #4: Function Returning Another Function (Closure)

### 💡 Concept & Explanation
The outer function returns an inner function that retains access to the outer `factor` variable. This pattern is called a Closure.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function createMultiplier(factor) {
    return function(number) {
        return number * factor;
    };
}

const double = createMultiplier(2);
const triple = createMultiplier(3);

console.log(double(5));
console.log(triple(5));
```

### 🖥️ Expected Output
```text
10
15
```


---

## Practice #5: Function Hoisting Demonstration

### 💡 Concept & Explanation
Function declarations are hoisted during the compilation phase, meaning they can be called before their definition appears in the code.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
console.log(sayHello("Bob"));

function sayHello(name) {
    return `Welcome back, ${name}!`;
}
```

### 🖥️ Expected Output
```text
Welcome back, Bob!
```


---

## Practice #6: Early Return Pattern in Validation

### 💡 Concept & Explanation
Using early return statements (`return`) exits the function immediately if invalid input is detected, avoiding deeply nested `if-else` blocks.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function processAge(age) {
    if (typeof age !== "number" || age < 0) {
        return "Invalid age provided!";
    }
    if (age >= 18) {
        return "Adult";
    }
    return "Minor";
}

console.log(processAge(-5));
console.log(processAge(21));
```

### 🖥️ Expected Output
```text
Invalid age provided!
Adult
```


---

## Practice #7: Checking Arguments Object in Traditional Functions

### 💡 Concept & Explanation
Traditional functions automatically have an array-like `arguments` object containing all arguments passed to the function call.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function sumAll() {
    let total = 0;
    for (let i = 0; i < arguments.length; i++) {
        total += arguments[i];
    }
    return total;
}

console.log(sumAll(10, 20, 30, 40));
```

### 🖥️ Expected Output
```text
100
```


---

## Practice #8: Function Scope vs Block Scope

### 💡 Concept & Explanation
`var` is scoped to the nearest function block, whereas `let` and `const` are scoped to the exact block `{}` where they are defined.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function checkScope() {
    var functionScoped = "I am function scoped";
    if (true) {
        let blockScoped = "I am block scoped";
        console.log(blockScoped);
    }
    console.log(functionScoped);
}

checkScope();
```

### 🖥️ Expected Output
```text
I am block scoped
I am function scoped
```


---

## Practice #9: Recursive Function (Factorial)

### 💡 Concept & Explanation
A recursive function calls itself until it reaches a base condition (`n <= 1`). 5! = 5 * 4 * 3 * 2 * 1 = 120.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function factorial(n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}

console.log(factorial(5));
```

### 🖥️ Expected Output
```text
120
```


---

## Practice #10: Function as Object Property (Method)

### 💡 Concept & Explanation
When a function is defined as a property of an object, it is called a method. Inside standard methods, `this` refers to the owning object.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const user = {
    name: "Sarah",
    greet: function() {
        return `Hi, I am ${this.name}`;
    }
};

console.log(user.greet());
```

### 🖥️ Expected Output
```text
Hi, I am Sarah
```


---

# 2. Anonymous Functions - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Functions without a name, used as immediate values, expressions, or IIFEs.


---

## Practice #1: IIFE (Immediately Invoked Function Expression)

### 💡 Concept & Explanation
An IIFE is an anonymous function wrapped in parentheses `()` and invoked immediately with `()`. It prevents polluting the global scope.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
(function() {
    const secret = "SuperSecretKey123";
    console.log("App initialized secretly:", secret);
})();
```

### 🖥️ Expected Output
```text
App initialized secretly: SuperSecretKey123
```


---

## Practice #2: IIFE with Parameters

### 💡 Concept & Explanation
Arguments can be passed directly into the invoking parentheses `("DashboardApp", "2.1.0")` at the end of an anonymous IIFE.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
(function(appName, version) {
    console.log(`Loading ${appName} v${version}...`);
})("DashboardApp", "2.1.0");
```

### 🖥️ Expected Output
```text
Loading DashboardApp v2.1.0...
```


---

## Practice #3: Anonymous Callback in setTimeout

### 💡 Concept & Explanation
An anonymous function is passed inline to `setTimeout`. It has no name because it is only invoked asynchronously by the browser/Node event loop.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
console.log("Start");

setTimeout(function() {
    console.log("Executed after 1000ms delay");
}, 1000);

console.log("End");
```

### 🖥️ Expected Output
```text
Start
End
Executed after 1000ms delay
```


---

## Practice #4: Anonymous Callback in Array Method (forEach)

### 💡 Concept & Explanation
An anonymous function expression is passed as an inline argument to `.forEach()`, receiving each element and index during iteration.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const colors = ["red", "green", "blue"];

colors.forEach(function(color, index) {
    console.log(`${index}: ${color}`);
});
```

### 🖥️ Expected Output
```text
0: red
1: green
2: blue
```


---

## Practice #5: Anonymous Function Assigned to Event Handler (Conceptual)

### 💡 Concept & Explanation
Anonymous functions are often assigned directly to event handlers or object methods when a reusable function name is unnecessary.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const mockButton = {
    onclick: null
};

mockButton.onclick = function() {
    console.log("Button clicked!");
};

mockButton.onclick();
```

### 🖥️ Expected Output
```text
Button clicked!
```


---

## Practice #6: Anonymous Function Returning Object via IIFE

### 💡 Concept & Explanation
This module pattern uses an anonymous IIFE to encapsulate private state (`count`), exposing public methods via a returned object.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const CounterModule = (function() {
    let count = 0;
    return {
        increment: function() { count++; return count; },
        get: function() { return count; }
    };
})();

console.log(CounterModule.increment());
console.log(CounterModule.increment());
console.log(CounterModule.get());
```

### 🖥️ Expected Output
```text
1
2
2
```


---

## Practice #7: Anonymous Function in Array.prototype.sort

### 💡 Concept & Explanation
The comparator function passed to `.sort()` is anonymous and defines custom numeric ascending sort order (`a - b`).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const numbers = [40, 100, 1, 5, 25, 10];

numbers.sort(function(a, b) {
    return a - b;
});

console.log(numbers);
```

### 🖥️ Expected Output
```text
[ 1, 5, 10, 25, 40, 100 ]
```


---

## Practice #8: Anonymous Function inside Object Method

### 💡 Concept & Explanation
An anonymous function is passed dynamically as a callback argument to compute the modulus of 10 and 5.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const calculator = {
    operation: function(a, b, fn) {
        return fn(a, b);
    }
};

const result = calculator.operation(10, 5, function(x, y) {
    return x % y;
});

console.log("Remainder:", result);
```

### 🖥️ Expected Output
```text
Remainder: 0
```


---

## Practice #9: Anonymous Function in setInterval with Termination

### 💡 Concept & Explanation
An anonymous function is passed to `setInterval` to run repeatedly until `clearInterval(intervalId)` halts execution.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
let counter = 0;
const intervalId = setInterval(function() {
    counter++;
    console.log("Tick:", counter);
    if (counter === 3) {
        clearInterval(intervalId);
        console.log("Timer stopped");
    }
}, 100);
```

### 🖥️ Expected Output
```text
Tick: 1
Tick: 2
Tick: 3
Timer stopped
```


---

## Practice #10: Anonymous Function for Custom Filtering

### 💡 Concept & Explanation
An anonymous function tests each word string length; if length <= 4 returns `true`, the word is retained in the filtered result.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const words = ["apple", "banana", "kiwi", "fig", "dragonfruit"];

const shortWords = words.filter(function(word) {
    return word.length <= 4;
});

console.log(shortWords);
```

### 🖥️ Expected Output
```text
[ 'kiwi', 'fig' ]
```


---

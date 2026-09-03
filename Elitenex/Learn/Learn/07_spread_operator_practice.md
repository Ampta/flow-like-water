# 7. Spread Operator (...) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Unpacks elements of an array or properties of an object into another element/object.


---

## Practice #1: Combining Two Arrays into a New Array

### 💡 Concept & Explanation
The spread operator `...` unpacks elements of `arr1` and `arr2` into a new combined array literal.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];

const combined = [...arr1, ...arr2];
console.log(combined);
```

### 🖥️ Expected Output
```text
[ 1, 2, 3, 4, 5, 6 ]
```


---

## Practice #2: Creating a Shallow Copy of an Array

### 💡 Concept & Explanation
`[...original]` creates a new shallow array copy, so mutating `copy` does not affect `original`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const original = ["a", "b", "c"];
const copy = [...original];

copy.push("d");

console.log("Original:", original);
console.log("Copy:", copy);
```

### 🖥️ Expected Output
```text
Original: [ 'a', 'b', 'c' ]
Copy: [ 'a', 'b', 'c', 'd' ]
```


---

## Practice #3: Merging Two Objects (Overriding Properties)

### 💡 Concept & Explanation
When merging objects with `{ ...obj1, ...obj2 }`, properties from rightmost objects (`userSettings`) overwrite matching properties from earlier objects.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const defaultSettings = { theme: "light", showNotifications: true };
const userSettings = { theme: "dark" };

const finalConfig = { ...defaultSettings, ...userSettings };
console.log(finalConfig);
```

### 🖥️ Expected Output
```text
{ theme: 'dark', showNotifications: true }
```


---

## Practice #4: Adding Elements to Start or End of Array

### 💡 Concept & Explanation
Spread operator allows inserting elements easily at any position during array creation.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const existing = [2, 3, 4];
const updated = [1, ...existing, 5];

console.log(updated);
```

### 🖥️ Expected Output
```text
[ 1, 2, 3, 4, 5 ]
```


---

## Practice #5: Passing Array Elements as Function Arguments

### 💡 Concept & Explanation
`Math.max()` expects individual comma-separated number arguments. `Math.max(...numbers)` spreads the array into arguments.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const numbers = [15, 88, 42, 99, 4];
const maxVal = Math.max(...numbers);

console.log("Max Value:", maxVal);
```

### 🖥️ Expected Output
```text
Max Value: 99
```


---

## Practice #6: Converting a String into an Array of Characters

### 💡 Concept & Explanation
Strings are iterable objects, so `[...string]` spreads each unicode character into an array element.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const greeting = "Hello";
const chars = [...greeting];

console.log(chars);
```

### 🖥️ Expected Output
```text
[ 'H', 'e', 'l', 'l', 'o' ]
```


---

## Practice #7: Cloning an Object and Updating State Immutably

### 💡 Concept & Explanation
Standard React / Redux immutable update pattern: clone `state` and override `score` without mutating `state`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const state = { user: "Alice", score: 50 };
const newState = { ...state, score: state.score + 10 };

console.log("Original state:", state);
console.log("New state:", newState);
```

### 🖥️ Expected Output
```text
Original state: { user: 'Alice', score: 50 }
New state: { user: 'Alice', score: 60 }
```


---

## Practice #8: Converting NodeList or Set to Array

### 💡 Concept & Explanation
Spreading a `Set` object converts duplicate-free iterable items into a standard JavaScript array.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const mySet = new Set(["apple", "banana", "apple"]);
const uniqueArray = [...mySet];

console.log(uniqueArray);
```

### 🖥️ Expected Output
```text
[ 'apple', 'banana' ]
```


---

## Practice #9: Conditional Property Spreading

### 💡 Concept & Explanation
Short-circuit `(condition && { key: val })` combined with object spread conditionally adds properties.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const includeEmail = true;
const profile = {
    username: "coder123",
    ...(includeEmail && { email: "coder@example.com" })
};

console.log(profile);
```

### 🖥️ Expected Output
```text
{ username: 'coder123', email: 'coder@example.com' }
```


---

## Practice #10: Combining Multiple Objects with Nested Spread

### 💡 Concept & Explanation
Spreads multiple objects together into a unified flat product model.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const baseInfo = { name: "Widget", price: 10 };
const specs = { weight: "2kg", color: "blue" };

const product = { id: "P-100", ...baseInfo, ...specs };
console.log(product);
```

### 🖥️ Expected Output
```text
{ id: 'P-100', name: 'Widget', price: 10, weight: '2kg', color: 'blue' }
```


---

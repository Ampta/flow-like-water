# 8. Rest Parameter (...) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Gathers multiple function arguments or remaining object/array items into a single array.


---

## Practice #1: Function with Variadic Rest Arguments

### 💡 Concept & Explanation
The rest parameter `...nums` gathers any number of passed arguments into a real JS array named `nums`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function sumNumbers(...nums) {
    return nums.reduce((total, n) => total + n, 0);
}

console.log(sumNumbers(10, 20, 30));
console.log(sumNumbers(5, 5, 5, 5, 5));
```

### 🖥️ Expected Output
```text
60
25
```


---

## Practice #2: Combining Fixed Parameters with Rest Parameter

### 💡 Concept & Explanation
First argument binds to `level`, while all remaining positional arguments are collected into `messages` array.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function formatLog(level, ...messages) {
    console.log(`[${level.toUpperCase()}]: ${messages.join(" ")}`);
}

formatLog("info", "User", "logged", "in", "successfully");
formatLog("error", "Database", "connection", "failed");
```

### 🖥️ Expected Output
```text
[INFO]: User logged in successfully
[ERROR]: Database connection failed
```


---

## Practice #3: Rest Parameter in Object Destructuring

### 💡 Concept & Explanation
Extracts `id` property separately, and collects all remaining object properties into `details` object.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const user = { id: 1, name: "Alice", email: "alice@example.com", role: "Admin" };

const { id, ...details } = user;

console.log("ID:", id);
console.log("Details:", details);
```

### 🖥️ Expected Output
```text
ID: 1
Details: { name: 'Alice', email: 'alice@example.com', role: 'Admin' }
```


---

## Practice #4: Rest Parameter in Array Destructuring

### 💡 Concept & Explanation
`first` gets 95, `second` gets 88, and `...remainingScores` gathers all remaining items `[72, 64, 50]`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const scores = [95, 88, 72, 64, 50];
const [first, second, ...remainingScores] = scores;

console.log("1st:", first);
console.log("2nd:", second);
console.log("Others:", remainingScores);
```

### 🖥️ Expected Output
```text
1st: 95
2nd: 88
Others: [ 72, 64, 50 ]
```


---

## Practice #5: Rest Parameter Must Be the Last Parameter

### 💡 Concept & Explanation
The rest parameter MUST be placed as the final parameter in the function declaration signature.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function calculateScore(bonus, ...scores) {
    const sum = scores.reduce((a, b) => a + b, 0);
    return sum + bonus;
}

console.log(calculateScore(10, 50, 40));
```

### 🖥️ Expected Output
```text
100
```


---

## Practice #6: Filtering Variadic Arguments using Rest

### 💡 Concept & Explanation
Gathers variadic items into `items` array and filters items matching `typeof item === type`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function filterByType(type, ...items) {
    return items.filter(item => typeof item === type);
}

console.log(filterByType("number", 10, "hello", true, 42, "world"));
```

### 🖥️ Expected Output
```text
[ 10, 42 ]
```


---

## Practice #7: Rest in Arrow Functions

### 💡 Concept & Explanation
Arrow functions do not have `arguments`, so `...numbers` rest parameter is the standard way to handle dynamic inputs.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const multiplyAll = (multiplier, ...numbers) => {
    return numbers.map(n => n * multiplier);
};

console.log(multiplyAll(3, 1, 2, 3, 4));
```

### 🖥️ Expected Output
```text
[ 3, 6, 9, 12 ]
```


---

## Practice #8: Ignoring Unwanted Object Properties with Rest

### 💡 Concept & Explanation
Common utility pattern: destructure and ignore internal keys (`_internalId`), saving sanitized clean data in `publicData`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const rawData = { _internalId: "999", title: "Article 1", content: "Body text" };
const { _internalId, ...publicData } = rawData;

console.log("Public Data:", publicData);
```

### 🖥️ Expected Output
```text
Public Data: { title: 'Article 1', content: 'Body text' }
```


---

## Practice #9: Forwarding Function Arguments (Decorator Pattern)

### 💡 Concept & Explanation
Uses rest parameter `...args` to capture arguments and spread `...args` to pass them through to wrapped function `fn`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function logExecution(fn) {
    return function(...args) {
        console.log("Calling function with args:", args);
        return fn(...args);
    };
}

const add = (a, b) => a + b;
const loggedAdd = logExecution(add);

console.log("Result:", loggedAdd(5, 7));
```

### 🖥️ Expected Output
```text
Calling function with args: [ 5, 7 ]
Result: 12
```


---

## Practice #10: Rest Parameter with Default Arguments

### 💡 Concept & Explanation
Default parameter `category = 'General'` combines smoothly with rest parameter `...tags`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function appendTags(category = "General", ...tags) {
    return { category, tags };
}

console.log(appendTags("Tech", "js", "web", "coding"));
```

### 🖥️ Expected Output
```text
{ category: 'Tech', tags: [ 'js', 'web', 'coding' ] }
```


---

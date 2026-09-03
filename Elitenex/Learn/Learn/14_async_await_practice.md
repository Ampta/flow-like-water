# 14. Async / Await - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Syntactic sugar over Promises for writing asynchronous code synchronously.


---

## Practice #1: Basic Async Function with Await

### 💡 Concept & Explanation
`async` keyword turns function into Promise-returning function. `await` pauses execution until Promise resolves.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function fetchNumber() {
    return new Promise(resolve => setTimeout(() => resolve(42), 50));
}

async function main() {
    console.log("Fetching...");
    const num = await fetchNumber();
    console.log("Result:", num);
}

main();
```

### 🖥️ Expected Output
```text
Fetching...
Result: 42
```


---

## Practice #2: Error Handling with try...catch Block

### 💡 Concept & Explanation
Async/await enables using standard synchronous `try...catch...finally` blocks for async error handling!

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function failTask() {
    return new Promise((_, reject) => reject(new Error("Network Error")));
}

async function loadData() {
    try {
        await failTask();
    } catch (error) {
        console.log("Caught Error:", error.message);
    } finally {
        console.log("Operation ended");
    }
}

loadData();
```

### 🖥️ Expected Output
```text
Caught Error: Network Error
Operation ended
```


---

## Practice #3: Sequential Async Operations with Await

### 💡 Concept & Explanation
Sequential `await` statements execute step-by-step in clear readable order without nesting.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const delay = ms => new Promise(res => setTimeout(res, ms));

async function runSequence() {
    console.log("Step 1 starting...");
    await delay(50);
    console.log("Step 2 starting...");
    await delay(50);
    console.log("All steps complete!");
}

runSequence();
```

### 🖥️ Expected Output
```text
Step 1 starting...
Step 2 starting...
All steps complete!
```


---

## Practice #4: Parallel Async Execution with Promise.all and Await

### 💡 Concept & Explanation
Combining `await Promise.all([...])` executes multiple async requests concurrently rather than serially.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const getUser = () => new Promise(res => setTimeout(() => res("Alice"), 50));
const getPosts = () => new Promise(res => setTimeout(() => res(["Post 1", "Post 2"]), 50));

async function fetchDashboard() {
    // Fetches both concurrently!
    const [user, posts] = await Promise.all([getUser(), getPosts()]);
    console.log(`User: ${user}, Posts count: ${posts.length}`);
}

fetchDashboard();
```

### 🖥️ Expected Output
```text
User: Alice, Posts count: 2
```


---

## Practice #5: Async Arrow Function Syntax

### 💡 Concept & Explanation
Arrow functions can be marked async with `const fn = async () => { ... }`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const getPrice = async (itemId) => {
    return itemId * 10;
};

async function check() {
    const price = await getPrice(5);
    console.log("Price:", price);
}

check();
```

### 🖥️ Expected Output
```text
Price: 50
```


---

## Practice #6: Async Function Automatically Returns a Promise

### 💡 Concept & Explanation
An `async` function automatically wraps any returned value inside a resolved Promise (`Promise.resolve("Hello World")`).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
async function getGreeting() {
    return "Hello World";
}

getGreeting().then(msg => console.log(msg));
```

### 🖥️ Expected Output
```text
Hello World
```


---

## Practice #7: Async Iteration Loop (for...of with await)

### 💡 Concept & Explanation
Using `await` inside a standard `for...of` loop processes asynchronous items sequentially in order.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const fetchItem = id => new Promise(res => setTimeout(() => res(`Item ${id}`), 30));

async function processList(ids) {
    for (const id of ids) {
        const item = await fetchItem(id);
        console.log("Processed:", item);
    }
}

processList([1, 2, 3]);
```

### 🖥️ Expected Output
```text
Processed: Item 1
Processed: Item 2
Processed: Item 3
```


---

## Practice #8: Handling Rejection in Async Arrow Function

### 💡 Concept & Explanation
Throwing an error (`throw new Error(...)`) inside an `async` function causes the returned Promise to reject.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const fetchUserRole = async (userId) => {
    if (userId < 0) throw new Error("Invalid User ID");
    return "Admin";
};

async function test() {
    try {
        await fetchUserRole(-1);
    } catch(err) {
        console.log("Error:", err.message);
    }
}

test();
```

### 🖥️ Expected Output
```text
Error: Invalid User ID
```


---

## Practice #9: Top-Level Async IIFE Pattern

### 💡 Concept & Explanation
Wrapping code in `(async () => { ... })()` allows using `await` at top-level scope in environments without top-level await.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
(async () => {
    const result = await Promise.resolve("IIFE Async Execution");
    console.log(result);
})();
```

### 🖥️ Expected Output
```text
IIFE Async Execution
```


---

## Practice #10: Async Method inside a Class

### 💡 Concept & Explanation
Class methods can be declared `async` (`async getData()`) and awaited seamlessly.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
class ApiClient {
    async getData(endpoint) {
        return new Promise(res => setTimeout(() => res(`Response from ${endpoint}`), 50));
    }
}

async function run() {
    const client = new ApiClient();
    const res = await client.getData("/users");
    console.log(res);
}

run();
```

### 🖥️ Expected Output
```text
Response from /users
```


---

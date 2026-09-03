# 13. Promises - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Representing future asynchronous completion or failure with .then() and .catch().


---

## Practice #1: Basic Promise Construction and Resolution

### 💡 Concept & Explanation
A Promise represents an async operation that will complete (`resolve`) or fail (`reject`). `.then()` receives resolved value.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const myPromise = new Promise((resolve, reject) => {
    let success = true;
    if (success) {
        resolve("Operation successful!");
    } else {
        reject("Operation failed!");
    }
});

myPromise
    .then(result => console.log(result))
    .catch(error => console.log(error));
```

### 🖥️ Expected Output
```text
Operation successful!
```


---

## Practice #2: Promise Rejection and .catch() Handling

### 💡 Concept & Explanation
When `reject()` is called inside a promise constructor, execution jumps directly to `.catch()` handler.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const checkServer = new Promise((resolve, reject) => {
    let serverUp = false;
    if (serverUp) resolve("Server online");
    else reject("Server offline (503)");
});

checkServer
    .then(status => console.log(status))
    .catch(err => console.log("Caught:", err));
```

### 🖥️ Expected Output
```text
Caught: Server offline (503)
```


---

## Practice #3: Promise Chaining (Solving Callback Hell)

### 💡 Concept & Explanation
Returning values or promises from `.then()` allows chaining async steps sequentially in a flat structure: `5 + 10 + 10 + 10 = 35`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function addTen(val) {
    return Promise.resolve(val + 10);
}

addTen(5)
    .then(res => addTen(res))
    .then(res => addTen(res))
    .then(final => console.log("Chained Result:", final));
```

### 🖥️ Expected Output
```text
Chained Result: 35
```


---

## Practice #4: Promise.finally() Execution

### 💡 Concept & Explanation
`.finally()` runs unconditionally after the Promise is settled (either resolved or rejected).

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const loader = new Promise((resolve) => {
    setTimeout(() => resolve("Data loaded"), 50);
});

loader
    .then(data => console.log(data))
    .finally(() => console.log("Cleanup: Hide loading spinner"));
```

### 🖥️ Expected Output
```text
Data loaded
Cleanup: Hide loading spinner
```


---

## Practice #5: Promise.all() for Parallel Execution

### 💡 Concept & Explanation
`Promise.all()` waits for ALL input promises to resolve in parallel, returning an array of results. Fails if ANY reject.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const p1 = Promise.resolve(10);
const p2 = new Promise(res => setTimeout(() => res(20), 50));
const p3 = Promise.resolve(30);

Promise.all([p1, p2, p3]).then(results => {
    console.log("All Results:", results);
});
```

### 🖥️ Expected Output
```text
All Results: [ 10, 20, 30 ]
```


---

## Practice #6: Promise.race() (Fastest Settled Wins)

### 💡 Concept & Explanation
`Promise.race()` returns a promise that resolves/rejects as soon as the FIRST promise settles.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const fast = new Promise(res => setTimeout(() => res("Fast response"), 50));
const slow = new Promise(res => setTimeout(() => res("Slow response"), 200));

Promise.race([fast, slow]).then(winner => {
    console.log("Winner:", winner);
});
```

### 🖥️ Expected Output
```text
Winner: Fast response
```


---

## Practice #7: Promise.allSettled() (Handling Mixed Outcomes)

### 💡 Concept & Explanation
`Promise.allSettled()` waits for all promises to finish and returns status objects, never rejecting.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const p1 = Promise.resolve("Success 1");
const p2 = Promise.reject("Error in 2");
const p3 = Promise.resolve("Success 3");

Promise.allSettled([p1, p2, p3]).then(results => {
    console.log(results);
});
```

### 🖥️ Expected Output
```text
[
  { status: 'fulfilled', value: 'Success 1' },
  { status: 'rejected', reason: 'Error in 2' },
  { status: 'fulfilled', value: 'Success 3' }
]
```


---

## Practice #8: Creating Pre-resolved or Pre-rejected Promises

### 💡 Concept & Explanation
`Promise.resolve(val)` and `Promise.reject(err)` create settled Promise objects immediately.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const instantSuccess = Promise.resolve("Quick Value");
const instantError = Promise.reject("Quick Error");

instantSuccess.then(v => console.log("Success:", v));
instantError.catch(e => console.log("Error:", e));
```

### 🖥️ Expected Output
```text
Success: Quick Value
Error: Quick Error
```


---

## Practice #9: Promise Error Recovery in Chain

### 💡 Concept & Explanation
Catching an error inside a chain and returning a fallback value allows subsequent `.then()` blocks to continue.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
Promise.resolve("Step 1")
    .then(res => {
        throw new Error("Step 2 Failed!");
    })
    .catch(err => {
        console.log("Recovered from:", err.message);
        return "Fallback Data";
    })
    .then(data => console.log("Continued with:", data));
```

### 🖥️ Expected Output
```text
Recovered from: Step 2 Failed!
Continued with: Fallback Data
```


---

## Practice #10: Simulating API Delay with Promise Timeout

### 💡 Concept & Explanation
A clean utility helper `delay(ms)` wraps `setTimeout` inside a promise for elegant timing logic.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

console.log("Waiting...");
delay(100).then(() => console.log("100ms passed!"));
```

### 🖥️ Expected Output
```text
Waiting...
100ms passed!
```


---

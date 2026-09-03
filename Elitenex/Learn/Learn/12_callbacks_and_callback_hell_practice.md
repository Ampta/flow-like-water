# 12. Callbacks & Callback Hell - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Functions passed as arguments, async control flow, and deeply nested pyramid structure.


---

## Practice #1: Basic Synchronous Callback Function

### 💡 Concept & Explanation
A callback function is a function passed as an argument into another function to be executed later.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function processUser(name, callback) {
    console.log(`Processing user: ${name}`);
    callback(name);
}

function sendWelcomeEmail(userName) {
    console.log(`Welcome email sent to ${userName}!`);
}

processUser("Alice", sendWelcomeEmail);
```

### 🖥️ Expected Output
```text
Processing user: Alice
Welcome email sent to Alice!
```


---

## Practice #2: Asynchronous Callback with setTimeout

### 💡 Concept & Explanation
Simulates async IO operation. The callback function executes after the timer completes.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function fetchUserData(userId, callback) {
    console.log(`Fetching data for user ${userId}...`);
    setTimeout(() => {
        const data = { id: userId, username: "dev_bob" };
        callback(data);
    }, 100);
}

fetchUserData(42, (user) => {
    console.log("Fetched User:", user.username);
});
```

### 🖥️ Expected Output
```text
Fetching data for user 42...
Fetched User: dev_bob
```


---

## Practice #3: Error-First Callback Pattern (Node.js convention)

### 💡 Concept & Explanation
Standard Node convention: callback accepts `(err, result)`. If error exists, `err` is first argument; otherwise `null`.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function readFile(filename, callback) {
    if (!filename) {
        callback(new Error("Filename is required!"), null);
    } else {
        callback(null, "File content data...");
    }
}

readFile("", (err, data) => {
    if (err) {
        console.log("Error:", err.message);
        return;
    }
    console.log("Data:", data);
});
```

### 🖥️ Expected Output
```text
Error: Filename is required!
```


---

## Practice #4: Callback Hell Demonstration (Pyramid of Doom)

### 💡 Concept & Explanation
Callback Hell (Pyramid of Doom) happens when asynchronous operations are nested deeply within callbacks.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
// Classic Callback Hell: Deep nesting makes code hard to read and debug!
function step1(cb) {
    setTimeout(() => { console.log("Step 1 done"); cb(); }, 50);
}
function step2(cb) {
    setTimeout(() => { console.log("Step 2 done"); cb(); }, 50);
}
function step3(cb) {
    setTimeout(() => { console.log("Step 3 done"); cb(); }, 50);
}

step1(() => {
    step2(() => {
        step3(() => {
            console.log("All steps completed!");
        });
    });
});
```

### 🖥️ Expected Output
```text
Step 1 done
Step 2 done
Step 3 done
All steps completed!
```


---

## Practice #5: Passing Callbacks into Custom Array Filter

### 💡 Concept & Explanation
Building custom higher-order functions by accepting predicate callbacks.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function customFilter(arr, predicateCallback) {
    const result = [];
    for (let i = 0; i < arr.length; i++) {
        if (predicateCallback(arr[i])) {
            result.push(arr[i]);
        }
    }
    return result;
}

const nums = [1, 2, 3, 4, 5];
console.log(customFilter(nums, n => n > 3));
```

### 🖥️ Expected Output
```text
[ 4, 5 ]
```


---

## Practice #6: Event Handling Callback Pattern

### 💡 Concept & Explanation
Observer / Event Emitter pattern relies on storing and triggering registered callback functions.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
const emitter = {
    listeners: [],
    on(event, callback) {
        this.listeners.push(callback);
    },
    emit(data) {
        this.listeners.forEach(cb => cb(data));
    }
};

emitter.on("login", user => console.log(`User logged in: ${user}`));
emitter.emit("Sarah");
```

### 🖥️ Expected Output
```text
User logged in: Sarah
```


---

## Practice #7: Callback with Optional Parameters

### 💡 Concept & Explanation
Safely checks `typeof callback === 'function'` before attempting invocation.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function calculate(a, b, callback) {
    const result = a + b;
    if (typeof callback === "function") {
        callback(result);
    }
    return result;
}

calculate(10, 20, res => console.log("Callback result:", res));
```

### 🖥️ Expected Output
```text
Callback result: 30
```


---

## Practice #8: Sequential Async Execution with Callbacks

### 💡 Concept & Explanation
Chaining sequential async tasks requires nesting subsequent calls inside preceding callbacks.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function downloadFile(url, cb) {
    console.log(`Downloading ${url}...`);
    setTimeout(() => cb(`Data from ${url}`), 50);
}

downloadFile("file1.pdf", (res1) => {
    console.log(res1);
    downloadFile("file2.pdf", (res2) => {
        console.log(res2);
    });
});
```

### 🖥️ Expected Output
```text
Downloading file1.pdf...
Data from file1.pdf
Downloading file2.pdf...
Data from file2.pdf
```


---

## Practice #9: Handling Errors in Nested Callbacks

### 💡 Concept & Explanation
Error handling in callbacks requires manually checking `err` in every single nesting level.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function asyncTask(shouldFail, cb) {
    setTimeout(() => {
        if (shouldFail) cb("Failed!");
        else cb(null, "Success!");
    }, 50);
}

asyncTask(true, (err, res) => {
    if (err) console.log("Task Error:", err);
    else console.log(res);
});
```

### 🖥️ Expected Output
```text
Task Error: Failed!
```


---

## Practice #10: Converting Callback to Promise (Promisification Concept)

### 💡 Concept & Explanation
Promisification wraps legacy callback-based APIs inside clean, modern Promise objects.

### 🎯 Code Solution (Type this manually into your JS script file)
```javascript
function legacyApi(val, cb) {
    setTimeout(() => cb(null, val * 2), 50);
}

// Wrapping callback in a Promise
const promisifiedApi = (val) => new Promise((resolve, reject) => {
    legacyApi(val, (err, res) => {
        if (err) reject(err);
        else resolve(res);
    });
});

promisifiedApi(21).then(res => console.log("Promisified Result:", res));
```

### 🖥️ Expected Output
```text
Promisified Result: 42
```


---

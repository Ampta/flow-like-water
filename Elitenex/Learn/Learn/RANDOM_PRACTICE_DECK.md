# 🎲 Random Practice Deck (140 Mixed JavaScript Challenges)

> **Instructions**: All 140 problems across all 14 topics are shuffled randomly below. Use this deck to practice without knowing the topic in advance!


---


## Problem #1: Promise Error Recovery in Chain [13. Promises]

### 💡 Concept & Explanation
Catching an error inside a chain and returning a fallback value allows subsequent `.then()` blocks to continue.

### 🎯 Code Solution
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

## Problem #2: Destructuring Directly in Function Parameters [9. Object Destructuring]

### 💡 Concept & Explanation
Destructuring parameters inside function signature `function({ title, price })` directly extracts arguments.

### 🎯 Code Solution
```javascript
function printProductInfo({ title, price, inStock = false }) {
    console.log(`Product: ${title} | $${price} | Available: ${inStock}`);
}

const item = { title: "Wireless Mouse", price: 29.99, inStock: true };
printProductInfo(item);
```

### 🖥️ Expected Output
```text
Product: Wireless Mouse | $29.99 | Available: true
```


---

## Problem #3: Top-Level Async IIFE Pattern [14. Async / Await]

### 💡 Concept & Explanation
Wrapping code in `(async () => { ... })()` allows using `await` at top-level scope in environments without top-level await.

### 🎯 Code Solution
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

## Problem #4: Function as Object Property (Method) [1. Standard Functions]

### 💡 Concept & Explanation
When a function is defined as a property of an object, it is called a method. Inside standard methods, `this` refers to the owning object.

### 🎯 Code Solution
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

## Problem #5: Adding Elements to Start or End of Array [7. Spread Operator (...)]

### 💡 Concept & Explanation
Spread operator allows inserting elements easily at any position during array creation.

### 🎯 Code Solution
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

## Problem #6: Anonymous Function in Array.prototype.sort [2. Anonymous Functions]

### 💡 Concept & Explanation
The comparator function passed to `.sort()` is anonymous and defines custom numeric ascending sort order (`a - b`).

### 🎯 Code Solution
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

## Problem #7: Rest in Arrow Functions [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Arrow functions do not have `arguments`, so `...numbers` rest parameter is the standard way to handle dynamic inputs.

### 🎯 Code Solution
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

## Problem #8: Default Export Syntax [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
`export default` exports a single primary value/class. When importing, curly braces are NOT used.

### 🎯 Code Solution
```javascript
// Logger.js
export default class Logger {
    log(msg) {
        console.log(`[LOG]: ${msg}`);
    }
}

// main.js
import Logger from "./Logger.js";
const logger = new Logger();
logger.log("System operational");
```

### 🖥️ Expected Output
```text
[LOG]: System operational
```


---

## Problem #9: Map Array of Objects to Key-Value Pairs Tuple [5. Array Map (map)]

### 💡 Concept & Explanation
Transforms each object into a 2-element key-value array tuple `[id, role]`.

### 🎯 Code Solution
```javascript
const users = [
    { id: 101, role: "admin" },
    { id: 102, role: "user" }
];

const tuples = users.map(u => [u.id, u.role]);
console.log(tuples);
```

### 🖥️ Expected Output
```text
[ [ 101, 'admin' ], [ 102, 'user' ] ]
```


---

## Problem #10: Basic Synchronous Callback Function [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
A callback function is a function passed as an argument into another function to be executed later.

### 🎯 Code Solution
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

## Problem #11: Function with Default Parameter Values [1. Standard Functions]

### 💡 Concept & Explanation
Default parameters (`name = 'Guest'`) ensure that if arguments are omitted or passed as `undefined`, default values are automatically used instead of `undefined`.

### 🎯 Code Solution
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

## Problem #12: Sequential Async Operations with Await [14. Async / Await]

### 💡 Concept & Explanation
Sequential `await` statements execute step-by-step in clear readable order without nesting.

### 🎯 Code Solution
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

## Problem #13: Importing Named Exports with Aliases (as) [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
The `as` keyword renames imported symbols to avoid identifier naming conflicts.

### 🎯 Code Solution
```javascript
// api.js
export const fetchData = () => "Data fetched";

// app.js
import { fetchData as getApiData } from "./api.js";
console.log(getApiData());
```

### 🖥️ Expected Output
```text
Data fetched
```


---

## Problem #14: Creating Pre-resolved or Pre-rejected Promises [13. Promises]

### 💡 Concept & Explanation
`Promise.resolve(val)` and `Promise.reject(err)` create settled Promise objects immediately.

### 🎯 Code Solution
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

## Problem #15: Promise.race() (Fastest Settled Wins) [13. Promises]

### 💡 Concept & Explanation
`Promise.race()` returns a promise that resolves/rejects as soon as the FIRST promise settles.

### 🎯 Code Solution
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

## Problem #16: Named Exports Syntax [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
Named exports allow exporting multiple functions/variables from a single module file using `{ add, subtract }` syntax.

### 🎯 Code Solution
```javascript
// mathUtils.js
export const add = (a, b) => a + b;
export const subtract = (a, b) => a - b;

// main.js
import { add, subtract } from "./mathUtils.js";
console.log(add(5, 3));
```

### 🖥️ Expected Output
```text
8
```


---

## Problem #17: Event Handling Callback Pattern [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Observer / Event Emitter pattern relies on storing and triggering registered callback functions.

### 🎯 Code Solution
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

## Problem #18: Filter Active Users from User Objects [4. Array Filter (filter)]

### 💡 Concept & Explanation
Filtering an array of objects tests properties on each object to produce a new array containing matching objects.

### 🎯 Code Solution
```javascript
const users = [
    { id: 1, name: "Alice", isActive: true },
    { id: 2, name: "Bob", isActive: false },
    { id: 3, name: "Charlie", isActive: true }
];

const activeUsers = users.filter(user => user.isActive);
console.log(activeUsers);
```

### 🖥️ Expected Output
```text
[ { id: 1, name: 'Alice', isActive: true }, { id: 3, name: 'Charlie', isActive: true } ]
```


---

## Problem #19: Anonymous Function in setInterval with Termination [2. Anonymous Functions]

### 💡 Concept & Explanation
An anonymous function is passed to `setInterval` to run repeatedly until `clearInterval(intervalId)` halts execution.

### 🎯 Code Solution
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

## Problem #20: Converting a String into an Array of Characters [7. Spread Operator (...)]

### 💡 Concept & Explanation
Strings are iterable objects, so `[...string]` spreads each unicode character into an array element.

### 🎯 Code Solution
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

## Problem #21: Single Parameter Arrow Function (No Parentheses) [3. Arrow Functions]

### 💡 Concept & Explanation
If an arrow function takes exactly one parameter, parentheses around the parameter are optional (`x => x * x`).

### 🎯 Code Solution
```javascript
const square = x => x * x;

console.log(square(9));
```

### 🖥️ Expected Output
```text
81
```


---

## Problem #22: Destructuring with Assignment to Existing Variables [9. Object Destructuring]

### 💡 Concept & Explanation
Parentheses `({ a, b } = ...)` are mandatory when destructuring without `const`, `let`, or `var` keywords.

### 🎯 Code Solution
```javascript
let a = 1;
let b = 2;

({ a, b } = { a: 10, b: 20 });

console.log(`a: ${a}, b: ${b}`);
```

### 🖥️ Expected Output
```text
a: 10, b: 20
```


---

## Problem #23: Simulating API Delay with Promise Timeout [13. Promises]

### 💡 Concept & Explanation
A clean utility helper `delay(ms)` wraps `setTimeout` inside a promise for elegant timing logic.

### 🎯 Code Solution
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

## Problem #24: Filter Tasks completed in Specific Date Range [4. Array Filter (filter)]

### 💡 Concept & Explanation
Filtering by string property equality allows targeting exact categories or priority levels.

### 🎯 Code Solution
```javascript
const tasks = [
    { title: "Task 1", priority: "High" },
    { title: "Task 2", priority: "Low" },
    { title: "Task 3", priority: "High" }
];

const highPriority = tasks.filter(t => t.priority === "High");
console.log(highPriority);
```

### 🖥️ Expected Output
```text
[ { title: 'Task 1', priority: 'High' }, { title: 'Task 3', priority: 'High' } ]
```


---

## Problem #25: Class Inheritance with 'extends' and 'super()' [10. Classes & Constructors]

### 💡 Concept & Explanation
`extends` inherits properties and methods from parent class. `super(name)` calls parent constructor.

### 🎯 Code Solution
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

## Problem #26: Count Occurrences of Items (Frequency Tally) [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Initial accumulator is an empty object `{}`. It builds a frequency dictionary counting occurrences of each string.

### 🎯 Code Solution
```javascript
const fruits = ["apple", "banana", "apple", "orange", "banana", "apple"];

const tally = fruits.reduce((acc, fruit) => {
    acc[fruit] = (acc[fruit] || 0) + 1;
    return acc;
}, {});

console.log(tally);
```

### 🖥️ Expected Output
```text
{ apple: 3, banana: 2, orange: 1 }
```


---

## Problem #27: Filter Sparse Array (Removing Empty Slots) [4. Array Filter (filter)]

### 💡 Concept & Explanation
`.filter()` skips unassigned sparse array holes and strips out `undefined` entries.

### 🎯 Code Solution
```javascript
const sparseArray = [1, 2, , 4, 5, , 7];
const denseArray = sparseArray.filter(val => val !== undefined);

console.log(denseArray);
```

### 🖥️ Expected Output
```text
[ 1, 2, 4, 5, 7 ]
```


---

## Problem #28: Filter Even Numbers from Array [4. Array Filter (filter)]

### 💡 Concept & Explanation
`.filter()` iterates through each element and keeps only those for which the callback returns `true` (here `num % 2 === 0`).

### 🎯 Code Solution
```javascript
const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
const evens = numbers.filter(num => num % 2 === 0);

console.log(evens);
```

### 🖥️ Expected Output
```text
[ 2, 4, 6, 8, 10 ]
```


---

## Problem #29: Promise Chaining (Solving Callback Hell) [13. Promises]

### 💡 Concept & Explanation
Returning values or promises from `.then()` allows chaining async steps sequentially in a flat structure: `5 + 10 + 10 + 10 = 35`.

### 🎯 Code Solution
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

## Problem #30: Sum All Numbers in an Array [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
`reduce` uses an accumulator (`acc`) initialized to `0`. On each iteration, `curr` is added to `acc`.

### 🎯 Code Solution
```javascript
const numbers = [10, 20, 30, 40];
const totalSum = numbers.reduce((acc, curr) => acc + curr, 0);

console.log("Sum:", totalSum);
```

### 🖥️ Expected Output
```text
Sum: 100
```


---

## Problem #31: Filter Out Falsy Values (Boolean Shorthand) [4. Array Filter (filter)]

### 💡 Concept & Explanation
Passing the global `Boolean` constructor as callback checks truthiness for each item, filtering out `0`, `""`, `false`, `null`, and `undefined`.

### 🎯 Code Solution
```javascript
const mixedArray = [0, "hello", "", false, 42, null, undefined, "world"];
const truthyValues = mixedArray.filter(Boolean);

console.log(truthyValues);
```

### 🖥️ Expected Output
```text
[ 'hello', 42, 'world' ]
```


---

## Problem #32: Basic Promise Construction and Resolution [13. Promises]

### 💡 Concept & Explanation
A Promise represents an async operation that will complete (`resolve`) or fail (`reject`). `.then()` receives resolved value.

### 🎯 Code Solution
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

## Problem #33: Rest Parameter Must Be the Last Parameter [8. Rest Parameter (...)]

### 💡 Concept & Explanation
The rest parameter MUST be placed as the final parameter in the function declaration signature.

### 🎯 Code Solution
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

## Problem #34: Static Properties for Tracking Instances [10. Classes & Constructors]

### 💡 Concept & Explanation
Static property `Player.count` is shared across all class instances to track total object creations.

### 🎯 Code Solution
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

## Problem #35: Convert Array of Key-Value Objects to Single Object [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Transforms array of metadata key-value objects into a clean key-value lookup object.

### 🎯 Code Solution
```javascript
const keyValues = [
    { key: "env", value: "production" },
    { key: "port", value: 8080 },
    { key: "debug", value: false }
];

const config = keyValues.reduce((acc, item) => {
    acc[item.key] = item.value;
    return acc;
}, {});

console.log(config);
```

### 🖥️ Expected Output
```text
{ env: 'production', port: 8080, debug: false }
```


---

## Problem #36: Calculate Total Price of Shopping Cart Items [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Accumulates `item.price * item.qty` across all cart items starting from initial value `0`.

### 🎯 Code Solution
```javascript
const cart = [
    { product: "Phone", price: 600, qty: 1 },
    { product: "Case", price: 20, qty: 2 },
    { product: "Charger", price: 30, qty: 1 }
];

const totalPrice = cart.reduce((total, item) => total + (item.price * item.qty), 0);
console.log("Cart Total:", totalPrice);
```

### 🖥️ Expected Output
```text
Cart Total: 670
```


---

## Problem #37: Promise Rejection and .catch() Handling [13. Promises]

### 💡 Concept & Explanation
When `reject()` is called inside a promise constructor, execution jumps directly to `.catch()` handler.

### 🎯 Code Solution
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

## Problem #38: Passing Callbacks into Custom Array Filter [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Building custom higher-order functions by accepting predicate callbacks.

### 🎯 Code Solution
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

## Problem #39: Function Expression Assigned to Variable [1. Standard Functions]

### 💡 Concept & Explanation
A function expression creates a function and assigns it to a variable (`const multiply`). Unlike declarations, function expressions are not hoisted before initialization.

### 🎯 Code Solution
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

## Problem #40: Async Iteration Loop (for...of with await) [14. Async / Await]

### 💡 Concept & Explanation
Using `await` inside a standard `for...of` loop processes asynchronous items sequentially in order.

### 🎯 Code Solution
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

## Problem #41: Promise.finally() Execution [13. Promises]

### 💡 Concept & Explanation
`.finally()` runs unconditionally after the Promise is settled (either resolved or rejected).

### 🎯 Code Solution
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

## Problem #42: Cloning an Object and Updating State Immutably [7. Spread Operator (...)]

### 💡 Concept & Explanation
Standard React / Redux immutable update pattern: clone `state` and override `score` without mutating `state`.

### 🎯 Code Solution
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

## Problem #43: Anonymous Function Assigned to Event Handler (Conceptual) [2. Anonymous Functions]

### 💡 Concept & Explanation
Anonymous functions are often assigned directly to event handlers or object methods when a reusable function name is unnecessary.

### 🎯 Code Solution
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

## Problem #44: Sequential Async Execution with Callbacks [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Chaining sequential async tasks requires nesting subsequent calls inside preceding callbacks.

### 🎯 Code Solution
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

## Problem #45: Combining Two Arrays into a New Array [7. Spread Operator (...)]

### 💡 Concept & Explanation
The spread operator `...` unpacks elements of `arr1` and `arr2` into a new combined array literal.

### 🎯 Code Solution
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

## Problem #46: Re-exporting / Export Aggregation (index.js) [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
Aggregating exports via `export { ... } from './file.js'` allows creating a single clean entrypoint for package utilities.

### 🎯 Code Solution
```javascript
// index.js (Aggregator module)
export { add, subtract } from "./math.js";
export { capitalize } from "./string.js";

// app.js
import { add, capitalize } from "./index.js";
console.log(add(2, 2), capitalize("test"));
```

### 🖥️ Expected Output
```text
4 TEST
```


---

## Problem #47: Creating a Shallow Copy of an Array [7. Spread Operator (...)]

### 💡 Concept & Explanation
`[...original]` creates a new shallow array copy, so mutating `copy` does not affect `original`.

### 🎯 Code Solution
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

## Problem #48: Forwarding Function Arguments (Decorator Pattern) [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Uses rest parameter `...args` to capture arguments and spread `...args` to pass them through to wrapped function `fn`.

### 🎯 Code Solution
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

## Problem #49: Rest Parameter in Object Destructuring [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Extracts `id` property separately, and collects all remaining object properties into `details` object.

### 🎯 Code Solution
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

## Problem #50: Callback with Optional Parameters [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Safely checks `typeof callback === 'function'` before attempting invocation.

### 🎯 Code Solution
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

## Problem #51: Destructuring Return Value of a Function [9. Object Destructuring]

### 💡 Concept & Explanation
Directly destructure object returned by function calls without declaring temporary object variable.

### 🎯 Code Solution
```javascript
function getCoordinates() {
    return { x: 15, y: 42, z: 9 };
}

const { x, y } = getCoordinates();
console.log(`X: ${x}, Y: ${y}`);
```

### 🖥️ Expected Output
```text
X: 15, Y: 42
```


---

## Problem #52: Converting NodeList or Set to Array [7. Spread Operator (...)]

### 💡 Concept & Explanation
Spreading a `Set` object converts duplicate-free iterable items into a standard JavaScript array.

### 🎯 Code Solution
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

## Problem #53: Filter Strings by Minimum Length [4. Array Filter (filter)]

### 💡 Concept & Explanation
Evaluates `name.length > 5` for each string, keeping only names with strictly more than 5 characters.

### 🎯 Code Solution
```javascript
const names = ["Anna", "Christopher", "Eva", "Jonathan", "Li"];
const longNames = names.filter(name => name.length > 5);

console.log(longNames);
```

### 🖥️ Expected Output
```text
[ 'Christopher', 'Jonathan' ]
```


---

## Problem #54: Renaming Variables During Destructuring (Aliasing) [9. Object Destructuring]

### 💡 Concept & Explanation
`property: newVariableName` syntax renames properties to clean camelCase variables.

### 🎯 Code Solution
```javascript
const user = { first_name: "Alex", user_role: "Editor" };

const { first_name: firstName, user_role: role } = user;

console.log(`User ${firstName} is an ${role}.`);
```

### 🖥️ Expected Output
```text
User Alex is an Editor.
```


---

## Problem #55: Double All Numbers in an Array [5. Array Map (map)]

### 💡 Concept & Explanation
`.map()` transforms every element of `numbers` by multiplying it by 2 and returns a brand-new array of identical length.

### 🎯 Code Solution
```javascript
const numbers = [1, 2, 3, 4, 5];
const doubled = numbers.map(num => num * 2);

console.log(doubled);
```

### 🖥️ Expected Output
```text
[ 2, 4, 6, 8, 10 ]
```


---

## Problem #56: Filter Out Items from Excluded List [4. Array Filter (filter)]

### 💡 Concept & Explanation
`!outOfStock.includes(item)` filters out items present in a blacklisted / out-of-stock array.

### 🎯 Code Solution
```javascript
const inventory = ["apple", "banana", "cherry", "date", "fig"];
const outOfStock = ["banana", "date"];

const available = inventory.filter(item => !outOfStock.includes(item));
console.log(available);
```

### 🖥️ Expected Output
```text
[ 'apple', 'cherry', 'fig' ]
```


---

## Problem #57: Function Hoisting Demonstration [1. Standard Functions]

### 💡 Concept & Explanation
Function declarations are hoisted during the compilation phase, meaning they can be called before their definition appears in the code.

### 🎯 Code Solution
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

## Problem #58: Extract Specific Key from Objects [5. Array Map (map)]

### 💡 Concept & Explanation
Mapping over objects allows plucking out specific property values (`user.name`) into a simple string array.

### 🎯 Code Solution
```javascript
const users = [
    { id: 1, name: "Alice" },
    { id: 2, name: "Bob" },
    { id: 3, name: "Charlie" }
];

const userNames = users.map(user => user.name);
console.log(userNames);
```

### 🖥️ Expected Output
```text
[ 'Alice', 'Bob', 'Charlie' ]
```


---

## Problem #59: Class Expression Assigned to Variable [10. Classes & Constructors]

### 💡 Concept & Explanation
Classes can be defined as class expressions and assigned to variables.

### 🎯 Code Solution
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

## Problem #60: Function Composition Pipeline with Reduce [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Pipes initial input `10` through a chain of functions: `(10 + 5) = 15 => (15 * 2) = 30 => (30 - 3) = 27`.

### 🎯 Code Solution
```javascript
const add5 = x => x + 5;
const double = x => x * 2;
const subtract3 = x => x - 3;

const pipeline = [add5, double, subtract3];

const result = pipeline.reduce((val, fn) => fn(val), 10);
console.log("Pipeline Result:", result);
```

### 🖥️ Expected Output
```text
Pipeline Result: 27
```


---

## Problem #61: Static Methods in Classes [10. Classes & Constructors]

### 💡 Concept & Explanation
`static` methods belong to the class itself, not instance objects (`MathUtils.add()` vs `new MathUtils().add()`).

### 🎯 Code Solution
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

## Problem #62: Anonymous Function inside Object Method [2. Anonymous Functions]

### 💡 Concept & Explanation
An anonymous function is passed dynamically as a callback argument to compute the modulus of 10 and 5.

### 🎯 Code Solution
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

## Problem #63: Format Prices with Currency Symbol [5. Array Map (map)]

### 💡 Concept & Explanation
Converts numerical values into formatted string strings using template literals and `.toFixed(2)`.

### 🎯 Code Solution
```javascript
const rawPrices = [19.99, 5.5, 100, 49.9];
const formattedPrices = rawPrices.map(price => `$${price.toFixed(2)}`);

console.log(formattedPrices);
```

### 🖥️ Expected Output
```text
[ '$19.99', '$5.50', '$100.00', '$49.90' ]
```


---

## Problem #64: Parallel Async Execution with Promise.all and Await [14. Async / Await]

### 💡 Concept & Explanation
Combining `await Promise.all([...])` executes multiple async requests concurrently rather than serially.

### 🎯 Code Solution
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

## Problem #65: Getters and Setters in Classes [10. Classes & Constructors]

### 💡 Concept & Explanation
`get area()` allows accessing computed property without parentheses (`rect.area`). `set dimensions()` mutates values.

### 🎯 Code Solution
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

## Problem #66: Convert Array of String Numbers to Actual Integers [5. Array Map (map)]

### 💡 Concept & Explanation
Converts string array `["10", "20"]` into integer array `[10, 20]` by passing `Number` transformation.

### 🎯 Code Solution
```javascript
const stringNumbers = ["10", "20", "30", "40"];
const numbers = stringNumbers.map(str => Number(str));

console.log(numbers);
```

### 🖥️ Expected Output
```text
[ 10, 20, 30, 40 ]
```


---

## Problem #67: Dynamic Imports with import() Promise [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
`import('module.js')` returns a Promise for dynamic code splitting / lazy loading modules.

### 🎯 Code Solution
```javascript
async function loadFeature() {
    // Dynamically imports module on demand
    const { heavyCalculation } = await import("./heavy.js");
    console.log(heavyCalculation());
}
```

### 🖥️ Expected Output
```text
Module loaded dynamically on demand
```


---

## Problem #68: Setting Default Values for Missing Properties [9. Object Destructuring]

### 💡 Concept & Explanation
If a property is `undefined` on the source object, default values (`theme = 'dark'`) are assigned.

### 🎯 Code Solution
```javascript
const options = { title: "Settings" };

const { title, theme = "dark", showSidebar = true } = options;

console.log(title, theme, showSidebar);
```

### 🖥️ Expected Output
```text
Settings dark true
```


---

## Problem #69: Exporting List of Variables at End of File [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
Instead of writing `export` before each variable, you can export a grouped object list at the bottom of the file.

### 🎯 Code Solution
```javascript
// constants.js
const API_URL = "https://api.example.com";
const TIMEOUT = 5000;

export { API_URL, TIMEOUT };
```

### 🖥️ Expected Output
```text
Module exports API_URL and TIMEOUT
```


---

## Problem #70: Error Handling with try...catch Block [14. Async / Await]

### 💡 Concept & Explanation
Async/await enables using standard synchronous `try...catch...finally` blocks for async error handling!

### 🎯 Code Solution
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

## Problem #71: Destructuring Dynamic Computed Property Names [9. Object Destructuring]

### 💡 Concept & Explanation
Using bracket notation `[key]: variableName` allows destructuring properties computed dynamically from a variable.

### 🎯 Code Solution
```javascript
const key = "email";
const user = { username: "dev_john", email: "john@dev.com" };

const { [key]: userEmail } = user;

console.log("User Email:", userEmail);
```

### 🖥️ Expected Output
```text
User Email: john@dev.com
```


---

## Problem #72: Private Class Fields (#field) [10. Classes & Constructors]

### 💡 Concept & Explanation
Fields starting with `#` are truly private and cannot be accessed or modified from outside the class instance.

### 🎯 Code Solution
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

## Problem #73: Callback Hell Demonstration (Pyramid of Doom) [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Callback Hell (Pyramid of Doom) happens when asynchronous operations are nested deeply within callbacks.

### 🎯 Code Solution
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

## Problem #74: Combining Aliasing and Default Values [9. Object Destructuring]

### 💡 Concept & Explanation
`status_message: msg = "OK"` renames `status_message` to `msg` and falls back to `"OK"` if missing.

### 🎯 Code Solution
```javascript
const response = { status_code: 200 };

const { status_code: code, status_message: msg = "OK" } = response;

console.log(`Status: ${code} - ${msg}`);
```

### 🖥️ Expected Output
```text
Status: 200 - OK
```


---

## Problem #75: Converting Callback to Promise (Promisification Concept) [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Promisification wraps legacy callback-based APIs inside clean, modern Promise objects.

### 🎯 Code Solution
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

## Problem #76: Promise.allSettled() (Handling Mixed Outcomes) [13. Promises]

### 💡 Concept & Explanation
`Promise.allSettled()` waits for all promises to finish and returns status objects, never rejecting.

### 🎯 Code Solution
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

## Problem #77: Basic Class Definition with Constructor & Method [10. Classes & Constructors]

### 💡 Concept & Explanation
`class` defines a template. `constructor()` initializes object properties when instantiated with `new Car(...)`.

### 🎯 Code Solution
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

## Problem #78: Basic Async Function with Await [14. Async / Await]

### 💡 Concept & Explanation
`async` keyword turns function into Promise-returning function. `await` pauses execution until Promise resolves.

### 🎯 Code Solution
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

## Problem #79: Lexical 'this' Binding in Arrow Functions [3. Arrow Functions]

### 💡 Concept & Explanation
Arrow functions do not have their own `this`. They inherit `this` lexically from the surrounding scope (here, the `timer` object).

### 🎯 Code Solution
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

## Problem #80: Rest Parameter in Array Destructuring [8. Rest Parameter (...)]

### 💡 Concept & Explanation
`first` gets 95, `second` gets 88, and `...remainingScores` gathers all remaining items `[72, 64, 50]`.

### 🎯 Code Solution
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

## Problem #81: Map String Characters to Uppercase [5. Array Map (map)]

### 💡 Concept & Explanation
Calls `.toUpperCase()` on each string item in the array.

### 🎯 Code Solution
```javascript
const words = ["javascript", "react", "node"];
const upperWords = words.map(w => w.toUpperCase());

console.log(upperWords);
```

### 🖥️ Expected Output
```text
[ 'JAVASCRIPT', 'REACT', 'NODE' ]
```


---

## Problem #82: Rest Parameter with Default Arguments [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Default parameter `category = 'General'` combines smoothly with rest parameter `...tags`.

### 🎯 Code Solution
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

## Problem #83: Basic Object Destructuring [9. Object Destructuring]

### 💡 Concept & Explanation
`const { name, city } = person` extracts properties into standalone variables matching the property names.

### 🎯 Code Solution
```javascript
const person = { name: "Sarah", age: 28, city: "Seattle" };

const { name, city } = person;

console.log(`${name} lives in ${city}.`);
```

### 🖥️ Expected Output
```text
Sarah lives in Seattle.
```


---

## Problem #84: Filter Search Query Match in Array of Strings [4. Array Filter (filter)]

### 💡 Concept & Explanation
Case-insensitive search filtering using `.toLowerCase()` and `.includes()` inside the `.filter()` callback.

### 🎯 Code Solution
```javascript
const searchTerm = "script";
const skills = ["JavaScript", "Python", "TypeScript", "HTML", "PostScript"];

const matches = skills.filter(skill => 
    skill.toLowerCase().includes(searchTerm.toLowerCase())
);

console.log(matches);
```

### 🖥️ Expected Output
```text
[ 'JavaScript', 'TypeScript', 'PostScript' ]
```


---

## Problem #85: Handling Rejection in Async Arrow Function [14. Async / Await]

### 💡 Concept & Explanation
Throwing an error (`throw new Error(...)`) inside an `async` function causes the returned Promise to reject.

### 🎯 Code Solution
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

## Problem #86: IIFE (Immediately Invoked Function Expression) [2. Anonymous Functions]

### 💡 Concept & Explanation
An IIFE is an anonymous function wrapped in parentheses `()` and invoked immediately with `()`. It prevents polluting the global scope.

### 🎯 Code Solution
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

## Problem #87: Method Chaining (Fluent Interface) [10. Classes & Constructors]

### 💡 Concept & Explanation
Returning `this` from methods allows chaining method calls (`.add(10).multiply(2)`).

### 🎯 Code Solution
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

## Problem #88: Anonymous Function Returning Object via IIFE [2. Anonymous Functions]

### 💡 Concept & Explanation
This module pattern uses an anonymous IIFE to encapsulate private state (`count`), exposing public methods via a returned object.

### 🎯 Code Solution
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

## Problem #89: Conditional Property Spreading [7. Spread Operator (...)]

### 💡 Concept & Explanation
Short-circuit `(condition && { key: val })` combined with object spread conditionally adds properties.

### 🎯 Code Solution
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

## Problem #90: Calculate Average Value [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Sums values during iterations, and on the final index (`index === array.length - 1`), divides by total length.

### 🎯 Code Solution
```javascript
const marks = [80, 90, 70, 100];

const average = marks.reduce((acc, mark, index, array) => {
    acc += mark;
    if (index === array.length - 1) {
        return acc / array.length;
    }
    return acc;
}, 0);

console.log("Average:", average);
```

### 🖥️ Expected Output
```text
Average: 85
```


---

## Problem #91: Early Return Pattern in Validation [1. Standard Functions]

### 💡 Concept & Explanation
Using early return statements (`return`) exits the function immediately if invalid input is detected, avoiding deeply nested `if-else` blocks.

### 🎯 Code Solution
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

## Problem #92: Filter Products by Price Range [4. Array Filter (filter)]

### 💡 Concept & Explanation
Filters products whose `price` is less than or equal to 100, returning a subset without mutating the original array.

### 🎯 Code Solution
```javascript
const products = [
    { name: "Laptop", price: 1200 },
    { name: "Mouse", price: 25 },
    { name: "Keyboard", price: 75 },
    { name: "Monitor", price: 300 }
];

const budgetProducts = products.filter(p => p.price <= 100);
console.log(budgetProducts);
```

### 🖥️ Expected Output
```text
[ { name: 'Mouse', price: 25 }, { name: 'Keyboard', price: 75 } ]
```


---

## Problem #93: Promise.all() for Parallel Execution [13. Promises]

### 💡 Concept & Explanation
`Promise.all()` waits for ALL input promises to resolve in parallel, returning an array of results. Fails if ANY reject.

### 🎯 Code Solution
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

## Problem #94: Convert Celsius Temperatures to Fahrenheit [5. Array Map (map)]

### 💡 Concept & Explanation
Applies temperature conversion formula `(C * 9/5) + 32` to each item in the array.

### 🎯 Code Solution
```javascript
const celsiusTemps = [0, 15, 25, 30];
const fahrenheitTemps = celsiusTemps.map(c => (c * 9/5) + 32);

console.log(fahrenheitTemps);
```

### 🖥️ Expected Output
```text
[ 32, 59, 77, 86 ]
```


---

## Problem #95: Using Element Index inside Map Callback [5. Array Map (map)]

### 💡 Concept & Explanation
The second parameter of the `.map()` callback is the current `index` of the element.

### 🎯 Code Solution
```javascript
const items = ["Home", "About", "Services", "Contact"];
const menuLinks = items.map((item, index) => `${index + 1}. ${item}`);

console.log(menuLinks);
```

### 🖥️ Expected Output
```text
[ '1. Home', '2. About', '3. Services', '4. Contact' ]
```


---

## Problem #96: Anonymous Callback in setTimeout [2. Anonymous Functions]

### 💡 Concept & Explanation
An anonymous function is passed inline to `setTimeout`. It has no name because it is only invoked asynchronously by the browser/Node event loop.

### 🎯 Code Solution
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

## Problem #97: Generate HTML List Item Strings [5. Array Map (map)]

### 💡 Concept & Explanation
Maps each category into an `<li>...</li>` HTML string snippet, suitable for rendering into DOM.

### 🎯 Code Solution
```javascript
const categories = ["Tech", "Health", "Finance"];
const listItems = categories.map(cat => `<li>${cat}</li>`);

console.log(listItems.join("\n"));
```

### 🖥️ Expected Output
```text
<li>Tech</li>
<li>Health</li>
<li>Finance</li>
```


---

## Problem #98: IIFE with Parameters [2. Anonymous Functions]

### 💡 Concept & Explanation
Arguments can be passed directly into the invoking parentheses `("DashboardApp", "2.1.0")` at the end of an anonymous IIFE.

### 🎯 Code Solution
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

## Problem #99: Anonymous Callback in Array Method (forEach) [2. Anonymous Functions]

### 💡 Concept & Explanation
An anonymous function expression is passed as an inline argument to `.forEach()`, receiving each element and index during iteration.

### 🎯 Code Solution
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

## Problem #100: Combining Default and Named Exports [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
Modules can have ONE default export along with multiple named exports, imported together as `import Default, { Named }`.

### 🎯 Code Solution
```javascript
// auth.js
export const checkAuth = () => true;
export default function login() { return "Logged in"; }

// app.js
import login, { checkAuth } from "./auth.js";
console.log(login(), "IsAuth:", checkAuth());
```

### 🖥️ Expected Output
```text
Logged in IsAuth: true
```


---

## Problem #101: Module Scope Strictness (Modules are strict by default) [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
ES Modules automatically enforce Strict Mode (`'use strict'`), preventing undeclared global variable assignments.

### 🎯 Code Solution
```javascript
// ES Modules automatically run in 'use strict' mode!
// Assigning to undeclared variables throws ReferenceError.
export const runTest = () => {
    try {
        undeclaredVar = 100;
    } catch(e) {
        console.log("Strict mode error caught:", e.message);
    }
};
```

### 🖥️ Expected Output
```text
Strict mode error caught: undeclaredVar is not defined
```


---

## Problem #102: Arrow Functions Cannot Be Used as Constructors [3. Arrow Functions]

### 💡 Concept & Explanation
Arrow functions cannot be invoked with `new` because they lack a `[[Construct]]` method and prototype.

### 🎯 Code Solution
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

## Problem #103: Anonymous Function for Custom Filtering [2. Anonymous Functions]

### 💡 Concept & Explanation
An anonymous function tests each word string length; if length <= 4 returns `true`, the word is retained in the filtered result.

### 🎯 Code Solution
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

## Problem #104: Error-First Callback Pattern (Node.js convention) [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Standard Node convention: callback accepts `(err, result)`. If error exists, `err` is first argument; otherwise `null`.

### 🎯 Code Solution
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

## Problem #105: Transform Array of Objects to Add Calculated Property [5. Array Map (map)]

### 💡 Concept & Explanation
Creates a new object for each element using spread operator (`...student`) and adds a boolean `passed` flag without mutating original objects.

### 🎯 Code Solution
```javascript
const students = [
    { name: "John", score: 85 },
    { name: "Sarah", score: 92 },
    { name: "Mark", score: 68 }
];

const gradedStudents = students.map(student => ({
    ...student,
    passed: student.score >= 70
}));

console.log(gradedStudents);
```

### 🖥️ Expected Output
```text
[
  { name: 'John', score: 85, passed: true },
  { name: 'Sarah', score: 92, passed: true },
  { name: 'Mark', score: 68, passed: false }
]
```


---

## Problem #106: Group Objects by Property [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Initial accumulator `{}` groups names into arrays indexed by `person.age` key.

### 🎯 Code Solution
```javascript
const people = [
    { name: "Alice", age: 21 },
    { name: "Bob", age: 25 },
    { name: "Charlie", age: 21 },
    { name: "David", age: 25 }
];

const groupedByAge = people.reduce((acc, person) => {
    const age = person.age;
    if (!acc[age]) acc[age] = [];
    acc[age].push(person.name);
    return acc;
}, {});

console.log(groupedByAge);
```

### 🖥️ Expected Output
```text
{ '21': [ 'Alice', 'Charlie' ], '25': [ 'Bob', 'David' ] }
```


---

## Problem #107: Handling Errors in Nested Callbacks [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Error handling in callbacks requires manually checking `err` in every single nesting level.

### 🎯 Code Solution
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

## Problem #108: Single-Line Implicit Return Arrow Function [3. Arrow Functions]

### 💡 Concept & Explanation
When an arrow function has a single expression, curly braces `{}` and `return` keyword can be omitted. It implicitly returns the evaluated result.

### 🎯 Code Solution
```javascript
const add = (a, b) => a + b;

console.log(add(15, 25));
```

### 🖥️ Expected Output
```text
40
```


---

## Problem #109: Asynchronous Callback with setTimeout [12. Callbacks & Callback Hell]

### 💡 Concept & Explanation
Simulates async IO operation. The callback function executes after the timer completes.

### 🎯 Code Solution
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

## Problem #110: Polymorphism (Overriding Parent Methods) [10. Classes & Constructors]

### 💡 Concept & Explanation
Child class `Circle` overrides parent `Shape`'s `draw()` method implementation.

### 🎯 Code Solution
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

## Problem #111: Basic Function Declaration for Calculating Area [1. Standard Functions]

### 💡 Concept & Explanation
A standard function declaration uses the `function` keyword, takes parameters (`width`, `height`), performs a calculation, and explicitly `return`s the result. It is hoisted to the top of its scope.

### 🎯 Code Solution
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

## Problem #112: Importing All Exports as Namespace Object (* as) [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
`import * as Name` gathers all named exports into a single namespace object (`StringUtils`).

### 🎯 Code Solution
```javascript
// stringHelpers.js
export const capitalize = str => str.toUpperCase();
export const reverse = str => str.split("").reverse().join("");

// app.js
import * as StringUtils from "./stringHelpers.js";
console.log(StringUtils.capitalize("hello"));
```

### 🖥️ Expected Output
```text
HELLO
```


---

## Problem #113: Async Arrow Function Syntax [14. Async / Await]

### 💡 Concept & Explanation
Arrow functions can be marked async with `const fn = async () => { ... }`.

### 🎯 Code Solution
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

## Problem #114: Filtering Variadic Arguments using Rest [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Gathers variadic items into `items` array and filters items matching `typeof item === type`.

### 🎯 Code Solution
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

## Problem #115: Async Function Automatically Returns a Promise [14. Async / Await]

### 💡 Concept & Explanation
An `async` function automatically wraps any returned value inside a resolved Promise (`Promise.resolve("Hello World")`).

### 🎯 Code Solution
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

## Problem #116: Async Method inside a Class [14. Async / Await]

### 💡 Concept & Explanation
Class methods can be declared `async` (`async getData()`) and awaited seamlessly.

### 🎯 Code Solution
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

## Problem #117: Find Maximum Value in an Array [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Compares `current` element against accumulator `max` on each iteration to return the maximum value.

### 🎯 Code Solution
```javascript
const scores = [45, 89, 92, 67, 100, 54];

const maxScore = scores.reduce((max, current) => current > max ? current : max, scores[0]);
console.log("Max score:", maxScore);
```

### 🖥️ Expected Output
```text
Max score: 100
```


---

## Problem #118: Combining Multiple Objects with Nested Spread [7. Spread Operator (...)]

### 💡 Concept & Explanation
Spreads multiple objects together into a unified flat product model.

### 🎯 Code Solution
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

## Problem #119: Nested Destructuring inside Array of Objects [9. Object Destructuring]

### 💡 Concept & Explanation
In a `for...of` loop, destructures nested `{ name, address: { city } }` from each user object during iteration.

### 🎯 Code Solution
```javascript
const users = [
    { name: "Alice", address: { city: "New York" } },
    { name: "Bob", address: { city: "London" } }
];

for (const { name, address: { city } } of users) {
    console.log(`${name} from ${city}`);
}
```

### 🖥️ Expected Output
```text
Alice from New York
Bob from London
```


---

## Problem #120: Nested Object Destructuring [9. Object Destructuring]

### 💡 Concept & Explanation
Unpacks nested properties directly: `location: { state }` extracts `state` variable from inside `location`.

### 🎯 Code Solution
```javascript
const company = {
    name: "TechCorp",
    location: {
        country: "USA",
        state: "California"
    }
};

const { name, location: { state } } = company;

console.log(`${name} is located in ${state}.`);
```

### 🖥️ Expected Output
```text
TechCorp is located in California.
```


---

## Problem #121: Class with Default Parameter Constructor Values [10. Classes & Constructors]

### 💡 Concept & Explanation
Constructors support default parameter values just like standard functions.

### 🎯 Code Solution
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

## Problem #122: Arrow Function with Array.prototype.map [3. Arrow Functions]

### 💡 Concept & Explanation
Arrow functions provide clean, concise syntax when passing callback functions to array transformation methods like `.map()`.

### 🎯 Code Solution
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

## Problem #123: Combining Fixed Parameters with Rest Parameter [8. Rest Parameter (...)]

### 💡 Concept & Explanation
First argument binds to `level`, while all remaining positional arguments are collected into `messages` array.

### 🎯 Code Solution
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

## Problem #124: Function Returning Another Function (Closure) [1. Standard Functions]

### 💡 Concept & Explanation
The outer function returns an inner function that retains access to the outer `factor` variable. This pattern is called a Closure.

### 🎯 Code Solution
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

## Problem #125: Ignoring Unwanted Object Properties with Rest [8. Rest Parameter (...)]

### 💡 Concept & Explanation
Common utility pattern: destructure and ignore internal keys (`_internalId`), saving sanitized clean data in `publicData`.

### 🎯 Code Solution
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

## Problem #126: Passing Array Elements as Function Arguments [7. Spread Operator (...)]

### 💡 Concept & Explanation
`Math.max()` expects individual comma-separated number arguments. `Math.max(...numbers)` spreads the array into arguments.

### 🎯 Code Solution
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

## Problem #127: Currying with Chained Arrow Functions [3. Arrow Functions]

### 💡 Concept & Explanation
Chained arrow functions create curried functions where each function takes one argument and returns another function.

### 🎯 Code Solution
```javascript
const add = x => y => z => x + y + z;

console.log(add(1)(2)(3));
```

### 🖥️ Expected Output
```text
6
```


---

## Problem #128: Flatten a 2D Array into a Single Array [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Initializes `acc` as `[]` and uses `.concat(curr)` to merge each sub-array into a single flat array.

### 🎯 Code Solution
```javascript
const nestedArray = [[1, 2], [3, 4], [5, 6]];

const flatArray = nestedArray.reduce((acc, curr) => acc.concat(curr), []);
console.log(flatArray);
```

### 🖥️ Expected Output
```text
[ 1, 2, 3, 4, 5, 6 ]
```


---

## Problem #129: Returning an Object Literal Implicitly [3. Arrow Functions]

### 💡 Concept & Explanation
To implicitly return an object literal, wrap the object in parentheses `({ ... })` so JavaScript does not confuse `{}` with function body braces.

### 🎯 Code Solution
```javascript
const createUser = (id, name) => ({ id: id, name: name, active: true });

console.log(createUser(101, "Alice"));
```

### 🖥️ Expected Output
```text
{ id: 101, name: 'Alice', active: true }
```


---

## Problem #130: Function Scope vs Block Scope [1. Standard Functions]

### 💡 Concept & Explanation
`var` is scoped to the nearest function block, whereas `let` and `const` are scoped to the exact block `{}` where they are defined.

### 🎯 Code Solution
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

## Problem #131: Recursive Function (Factorial) [1. Standard Functions]

### 💡 Concept & Explanation
A recursive function calls itself until it reaches a base condition (`n <= 1`). 5! = 5 * 4 * 3 * 2 * 1 = 120.

### 🎯 Code Solution
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

## Problem #132: Exporting Classes with Named Exports [11. ES Modules (Import/Export)]

### 💡 Concept & Explanation
Individual classes can be exported directly with `export class ClassName`.

### 🎯 Code Solution
```javascript
// Models.js
export class User { constructor(name) { this.name = name; } }
export class Product { constructor(title) { this.title = title; } }
```

### 🖥️ Expected Output
```text
Exported multiple domain classes
```


---

## Problem #133: Multi-Line Arrow Function with Explicit Return [3. Arrow Functions]

### 💡 Concept & Explanation
When using curly braces `{}` in an arrow function, an explicit `return` statement is required to send back a value.

### 🎯 Code Solution
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

## Problem #134: No Arguments Object in Arrow Functions [3. Arrow Functions]

### 💡 Concept & Explanation
Arrow functions do NOT possess an `arguments` object. To accept a dynamic number of arguments, use the rest parameter `(...args)`.

### 🎯 Code Solution
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

## Problem #135: Filter Unique Elements (Remove Duplicates using Index) [4. Array Filter (filter)]

### 💡 Concept & Explanation
`array.indexOf(item)` returns the first occurrence index of `item`. If current `index` matches first index, it's unique!

### 🎯 Code Solution
```javascript
const items = ["apple", "banana", "apple", "orange", "banana"];
const uniqueItems = items.filter((item, index, array) => array.indexOf(item) === index);

console.log(uniqueItems);
```

### 🖥️ Expected Output
```text
[ 'apple', 'banana', 'orange' ]
```


---

## Problem #136: Construct Query String from Parameters Object [6. Array Reduce (reduce)]

### 💡 Concept & Explanation
Builds a formatted URL query string dynamically using index condition inside `reduce`.

### 🎯 Code Solution
```javascript
const params = [
    { key: "search", val: "javascript" },
    { key: "page", val: 2 },
    { key: "sort", val: "asc" }
];

const queryString = params.reduce((acc, item, index) => {
    const prefix = index === 0 ? "?" : "&";
    return `${acc}${prefix}${item.key}=${item.val}`;
}, "");

console.log(queryString);
```

### 🖥️ Expected Output
```text
?search=javascript&page=2&sort=asc
```


---

## Problem #137: Merging Two Objects (Overriding Properties) [7. Spread Operator (...)]

### 💡 Concept & Explanation
When merging objects with `{ ...obj1, ...obj2 }`, properties from rightmost objects (`userSettings`) overwrite matching properties from earlier objects.

### 🎯 Code Solution
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

## Problem #138: Function with Variadic Rest Arguments [8. Rest Parameter (...)]

### 💡 Concept & Explanation
The rest parameter `...nums` gathers any number of passed arguments into a real JS array named `nums`.

### 🎯 Code Solution
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

## Problem #139: Checking Arguments Object in Traditional Functions [1. Standard Functions]

### 💡 Concept & Explanation
Traditional functions automatically have an array-like `arguments` object containing all arguments passed to the function call.

### 🎯 Code Solution
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

## Problem #140: Arrow Function in Event Listener / Async Callback [3. Arrow Functions]

### 💡 Concept & Explanation
The inner arrow function inside `forEach` preserves `this.status` from `statusChecker`, avoiding `undefined` bugs.

### 🎯 Code Solution
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

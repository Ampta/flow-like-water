# 6. Array Reduce (reduce) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Reduces an array to a single cumulative value (sum, object tally, flattened array).


---

## Practice #1: Sum All Numbers in an Array

### 💡 Concept & Explanation
`reduce` uses an accumulator (`acc`) initialized to `0`. On each iteration, `curr` is added to `acc`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #2: Calculate Total Price of Shopping Cart Items

### 💡 Concept & Explanation
Accumulates `item.price * item.qty` across all cart items starting from initial value `0`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #3: Count Occurrences of Items (Frequency Tally)

### 💡 Concept & Explanation
Initial accumulator is an empty object `{}`. It builds a frequency dictionary counting occurrences of each string.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #4: Find Maximum Value in an Array

### 💡 Concept & Explanation
Compares `current` element against accumulator `max` on each iteration to return the maximum value.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #5: Group Objects by Property

### 💡 Concept & Explanation
Initial accumulator `{}` groups names into arrays indexed by `person.age` key.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #6: Flatten a 2D Array into a Single Array

### 💡 Concept & Explanation
Initializes `acc` as `[]` and uses `.concat(curr)` to merge each sub-array into a single flat array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #7: Convert Array of Key-Value Objects to Single Object

### 💡 Concept & Explanation
Transforms array of metadata key-value objects into a clean key-value lookup object.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #8: Construct Query String from Parameters Object

### 💡 Concept & Explanation
Builds a formatted URL query string dynamically using index condition inside `reduce`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #9: Calculate Average Value

### 💡 Concept & Explanation
Sums values during iterations, and on the final index (`index === array.length - 1`), divides by total length.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #10: Function Composition Pipeline with Reduce

### 💡 Concept & Explanation
Pipes initial input `10` through a chain of functions: `(10 + 5) = 15 => (15 * 2) = 30 => (30 - 3) = 27`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

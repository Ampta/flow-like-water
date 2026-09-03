# 5. Array Map (map) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Creates a new array by transforming every element of an existing array.


---

## Practice #1: Double All Numbers in an Array

### 💡 Concept & Explanation
`.map()` transforms every element of `numbers` by multiplying it by 2 and returns a brand-new array of identical length.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #2: Extract Specific Key from Objects

### 💡 Concept & Explanation
Mapping over objects allows plucking out specific property values (`user.name`) into a simple string array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #3: Format Prices with Currency Symbol

### 💡 Concept & Explanation
Converts numerical values into formatted string strings using template literals and `.toFixed(2)`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #4: Transform Array of Objects to Add Calculated Property

### 💡 Concept & Explanation
Creates a new object for each element using spread operator (`...student`) and adds a boolean `passed` flag without mutating original objects.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #5: Convert Celsius Temperatures to Fahrenheit

### 💡 Concept & Explanation
Applies temperature conversion formula `(C * 9/5) + 32` to each item in the array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #6: Using Element Index inside Map Callback

### 💡 Concept & Explanation
The second parameter of the `.map()` callback is the current `index` of the element.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #7: Map String Characters to Uppercase

### 💡 Concept & Explanation
Calls `.toUpperCase()` on each string item in the array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #8: Convert Array of String Numbers to Actual Integers

### 💡 Concept & Explanation
Converts string array `["10", "20"]` into integer array `[10, 20]` by passing `Number` transformation.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #9: Generate HTML List Item Strings

### 💡 Concept & Explanation
Maps each category into an `<li>...</li>` HTML string snippet, suitable for rendering into DOM.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #10: Map Array of Objects to Key-Value Pairs Tuple

### 💡 Concept & Explanation
Transforms each object into a 2-element key-value array tuple `[id, role]`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

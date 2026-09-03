# 4. Array Filter (filter) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Creates a new array containing elements that pass a test condition.


---

## Practice #1: Filter Even Numbers from Array

### 💡 Concept & Explanation
`.filter()` iterates through each element and keeps only those for which the callback returns `true` (here `num % 2 === 0`).

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #2: Filter Active Users from User Objects

### 💡 Concept & Explanation
Filtering an array of objects tests properties on each object to produce a new array containing matching objects.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #3: Filter Out Falsy Values (Boolean Shorthand)

### 💡 Concept & Explanation
Passing the global `Boolean` constructor as callback checks truthiness for each item, filtering out `0`, `""`, `false`, `null`, and `undefined`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #4: Filter Products by Price Range

### 💡 Concept & Explanation
Filters products whose `price` is less than or equal to 100, returning a subset without mutating the original array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #5: Filter Strings by Minimum Length

### 💡 Concept & Explanation
Evaluates `name.length > 5` for each string, keeping only names with strictly more than 5 characters.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #6: Filter Unique Elements (Remove Duplicates using Index)

### 💡 Concept & Explanation
`array.indexOf(item)` returns the first occurrence index of `item`. If current `index` matches first index, it's unique!

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #7: Filter Tasks completed in Specific Date Range

### 💡 Concept & Explanation
Filtering by string property equality allows targeting exact categories or priority levels.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #8: Filter Search Query Match in Array of Strings

### 💡 Concept & Explanation
Case-insensitive search filtering using `.toLowerCase()` and `.includes()` inside the `.filter()` callback.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #9: Filter Out Items from Excluded List

### 💡 Concept & Explanation
`!outOfStock.includes(item)` filters out items present in a blacklisted / out-of-stock array.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #10: Filter Sparse Array (Removing Empty Slots)

### 💡 Concept & Explanation
`.filter()` skips unassigned sparse array holes and strips out `undefined` entries.

### 🎯 Code Solution (Type this manually into your JS script file)
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

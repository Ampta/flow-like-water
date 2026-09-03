# 9. Object Destructuring - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Unpacks properties from objects into distinct variables easily.


---

## Practice #1: Basic Object Destructuring

### 💡 Concept & Explanation
`const { name, city } = person` extracts properties into standalone variables matching the property names.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #2: Renaming Variables During Destructuring (Aliasing)

### 💡 Concept & Explanation
`property: newVariableName` syntax renames properties to clean camelCase variables.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #3: Setting Default Values for Missing Properties

### 💡 Concept & Explanation
If a property is `undefined` on the source object, default values (`theme = 'dark'`) are assigned.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #4: Nested Object Destructuring

### 💡 Concept & Explanation
Unpacks nested properties directly: `location: { state }` extracts `state` variable from inside `location`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #5: Destructuring Directly in Function Parameters

### 💡 Concept & Explanation
Destructuring parameters inside function signature `function({ title, price })` directly extracts arguments.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #6: Combining Aliasing and Default Values

### 💡 Concept & Explanation
`status_message: msg = "OK"` renames `status_message` to `msg` and falls back to `"OK"` if missing.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #7: Destructuring Dynamic Computed Property Names

### 💡 Concept & Explanation
Using bracket notation `[key]: variableName` allows destructuring properties computed dynamically from a variable.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #8: Destructuring Return Value of a Function

### 💡 Concept & Explanation
Directly destructure object returned by function calls without declaring temporary object variable.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #9: Destructuring with Assignment to Existing Variables

### 💡 Concept & Explanation
Parentheses `({ a, b } = ...)` are mandatory when destructuring without `const`, `let`, or `var` keywords.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #10: Nested Destructuring inside Array of Objects

### 💡 Concept & Explanation
In a `for...of` loop, destructures nested `{ name, address: { city } }` from each user object during iteration.

### 🎯 Code Solution (Type this manually into your JS script file)
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

# 11. ES Modules (Import/Export) - Practice Guide

> **Goal**: Read the explanation, understand the concept, create your own `.js` file, and type out the code manually to practice!

**Topic Description**: Sharing code across JavaScript files using named and default exports.


---

## Practice #1: Named Exports Syntax

### 💡 Concept & Explanation
Named exports allow exporting multiple functions/variables from a single module file using `{ add, subtract }` syntax.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #2: Default Export Syntax

### 💡 Concept & Explanation
`export default` exports a single primary value/class. When importing, curly braces are NOT used.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #3: Importing Named Exports with Aliases (as)

### 💡 Concept & Explanation
The `as` keyword renames imported symbols to avoid identifier naming conflicts.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #4: Importing All Exports as Namespace Object (* as)

### 💡 Concept & Explanation
`import * as Name` gathers all named exports into a single namespace object (`StringUtils`).

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #5: Combining Default and Named Exports

### 💡 Concept & Explanation
Modules can have ONE default export along with multiple named exports, imported together as `import Default, { Named }`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #6: Re-exporting / Export Aggregation (index.js)

### 💡 Concept & Explanation
Aggregating exports via `export { ... } from './file.js'` allows creating a single clean entrypoint for package utilities.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #7: Exporting List of Variables at End of File

### 💡 Concept & Explanation
Instead of writing `export` before each variable, you can export a grouped object list at the bottom of the file.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #8: Dynamic Imports with import() Promise

### 💡 Concept & Explanation
`import('module.js')` returns a Promise for dynamic code splitting / lazy loading modules.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #9: Exporting Classes with Named Exports

### 💡 Concept & Explanation
Individual classes can be exported directly with `export class ClassName`.

### 🎯 Code Solution (Type this manually into your JS script file)
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

## Practice #10: Module Scope Strictness (Modules are strict by default)

### 💡 Concept & Explanation
ES Modules automatically enforce Strict Mode (`'use strict'`), preventing undeclared global variable assignments.

### 🎯 Code Solution (Type this manually into your JS script file)
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

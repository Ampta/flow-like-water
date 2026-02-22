# 🛡️ Rebuild Tutorial Part 2: Backend Auth & Routing

In Part 1, we defined our data shape. Now, we need a way for the Frontend to actually talk to our Database. We do this through an API (Application Programming Interface).

Think of the API as a waiter at a restaurant. Your React frontend asks the waiter (API) for data, the waiter checks with the Kitchen (Database), and brings the data back. But first, the waiter needs to know if you're allowed to be in the restaurant: **Authentication**.

---

## Step 7: Input Validation (`src/utils/response.ts` & Schemas)

First, let's create a standard way to send responses back to the client. Create `src/utils/response.ts`:

```typescript
import type { Context } from 'hono';

export const successResponse = (c: Context, data: any, message = 'Success', status = 200) => {
    return c.json({ success: true, message, data }, status);
};

export const errorResponse = (c: Context, error: any, status = 500) => {
    const message = error.message || 'Something went wrong';
    return c.json({ success: false, message }, status);
};
```

> **Why?** Uniformity! Every single request should return `{ success, message, data }`. If we don't standardize this, our frontend will struggle to know if a request worked or failed because the JSON shapes keep changing.

## Step 8: The Auth Service (`src/services/AuthService.ts`)

Create `src/services/AuthService.ts`:

```typescript
import User from '../models/User';
import jwt from 'jsonwebtoken';

export class AuthService {
    static async register(email: string, password: string, username: string) {
        // 1. Check if user already exists
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            throw new Error('Email is already registered!');
        }

        // 2. Create the user
        const user = await User.create({ email, password, username });
        return { user: { id: user._id, email: user.email, username: user.username } };
    }

    static async login(email: string, password: string) {
        // 1. Find user and explicitly select the hidden password
        const user = await User.findOne({ email }).select('+password');
        if (!user) {
            throw new Error('Invalid email or password');
        }

        // 2. Compare passwords using the method we built on the Model
        const isMatch = await user.comparePassword(password);
        if (!isMatch) {
            throw new Error('Invalid email or password');
        }

        // 3. Generate a JWT Token
        // This token acts as a VIP pass. The frontend will show it to us on every future request.
        const token = jwt.sign(
            { userId: user._id, roles: user.roles }, 
            process.env.JWT_SECRET || 'fallback_secret', 
            { expiresIn: '1d' }
        );

        return { token, user: { id: user._id, email, username: user.username } };
    }
}
```

> **Why?** Logic belongs in "Services". This keeps our routing files small and clean. `AuthService.register` handles the business logic of checking for duplicates, creating the user, and throwing errors if things go wrong.

## Step 9: The Auth Controller (`src/controllers/AuthController.ts`)

Create `src/controllers/AuthController.ts`:

```typescript
import type { Context } from 'hono';
import { z } from 'zod';
import { AuthService } from '../services/AuthService';
import { successResponse, errorResponse } from '../utils/response';

// 1. Zod lets us define EXACTLY what shape we expect the data to be.
const registerSchema = z.object({
    email: z.string().email(),
    password: z.string().min(6), // Password must be 6+ chars
    username: z.string().min(3),
});

export const register = async (c: Context) => {
    try {
        const body = await c.req.json();
        
        // 2. We run the Zod parse. If it fails, execution stops and goes to the 'catch' block!
        const { email, password, username } = await registerSchema.parseAsync(body);

        // 3. Data is safe! Pass it to the Service
        const result = await AuthService.register(email, password, username);
        return successResponse(c, result, 'User registered successfully', 201);
    } catch (err: any) {
        return errorResponse(c, err, 400); // 400 means "Bad Request" (Client's fault)
    }
};

const loginSchema = z.object({
    email: z.string().email(),
    password: z.string(),
});

export const login = async (c: Context) => {
    try {
        const body = await c.req.json();
        const { email, password } = await loginSchema.parseAsync(body);
        
        const result = await AuthService.login(email, password);
        return successResponse(c, result, 'Login successful');
    } catch (err: any) {
        return errorResponse(c, err, 401); // 401 means "Unauthorized" (Bad password)
    }
};
```

> **Why?** The Controller's job is **strictly** talking to the internet. It receives the HTTP Request (`c.req.json()`), validates it (`zod`), passes data to the Service, and formulates the HTTP Response (`successResponse`).

## Step 10: Routing & Middleware (`src/routes/auth.routes.ts`)

Create your route handler in `src/routes/auth.routes.ts`:

```typescript
import { Hono } from 'hono';
import * as AuthController from '../controllers/AuthController';

const auth = new Hono();

// When someone POSTs to /register, run the register function
auth.post('/register', AuthController.register);

// When someone POSTs to /login, run the login function
auth.post('/login', AuthController.login);

export default auth;
```

> **Why check JWTs?** Eventually, you’ll want routes that are "protected" (like Creating an Event). To do this, you’ll write a middleware function that checks headers for `Authorization: Bearer <token>`, verifies it using `jsonwebtoken`, and extracts the `userId`. If the token is invalid, it returns `401 Unauthorized` before the controller even runs.

## Step 11: Bringing it all together in `src/index.ts`!

Now we wire our routes and database to the main Server file. Open `src/index.ts` and paste this:

```typescript
import { Hono } from 'hono';
import { cors } from 'hono/cors';
import { connectDB } from './config/db';
import authRoutes from './routes/auth.routes';

const app = new Hono();

// 1. CORS allows our frontend (localhost:5173) to talk to our backend
app.use('*', cors({
    origin: ['http://localhost:5173'],
    credentials: true,
}));

// 2. Start the database
connectDB();

// 3. Mount our auth routes. Now they live at /auth/register and /auth/login
app.route('/auth', authRoutes);

// 4. A simple health-check route
app.get('/', (c) => c.json({ message: 'Welcome to AMPTA Events API!' }));

export default {
    port: 3000,
    fetch: app.fetch,
};
```

> **Why?** `index.ts` is the entry point. `bun run dev` looks here first. We configure `cors` to safely allow cross-origin requests, connect to the database, and attach the `/auth` router onto the main `app`. 

### 🎉 Milestone Reached!
Your backend now has a fully working Authentication API! You can run this with `bun run --watch src/index.ts` and test `/auth/register` using Postman!

In Part 3, we will set up the Frontend to connect to this API.

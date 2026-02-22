# 🎨 Rebuild Tutorial Part 3: Frontend Setup & Routing

Now that the backend is ready to receive requests, we need a beautiful, interactive User Interface (UI) to send them. We will use **React** powered by **Vite**. 

---

## Step 12: Initialize the Frontend with Vite

1. Open your terminal in a **new window** (keep your backend running!).
2. Run this command to create a new React app. Name it `frontend`:
   ```bash
   npm create vite@latest frontend -- --template react-ts
   ```
3. Navigate into the folder and install dependencies:
   ```bash
   cd frontend
   npm install react-router-dom axios zustand react-hot-toast @tanstack/react-query lucide-react
   ```
4. Setup Tailwind CSS (Vite styling):
   ```bash
   npm install -D tailwindcss postcss autoprefixer
   npx tailwindcss init -p
   ```

> **Why?** React paints our UI. `react-router-dom` lets us change pages without reloading the browser. `axios` is how we fetch data from our Backend. `zustand` is "global state" (like remembering if we are logged in across all pages). 

## Step 13: Configure Tailwind CSS (`tailwind.config.js` & `index.css`)

Replace `tailwind.config.js` with:
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

Replace `src/index.css` with:
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  background-color: #f8fafc; /* A nice light gray background */
}
```

> **Why?** Tailwind allows us to style our components rapidly using utility classes (like `className="text-white bg-blue-500"`). The `content` array tells Tailwind exactly which files to scan so it doesn't inflate our CSS file size with unused classes.

## Step 14: Global State for Authentication (`src/store/useAuthStore.ts`)

Create `src/store/useAuthStore.ts` and paste this code:

```typescript
import { create } from 'zustand';

interface AuthState {
    user: { id: string, email: string, username: string } | null;
    token: string | null;
    login: (user: any, token: string) => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    // Try to load any existing token from the browser's Local Storage!
    user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')!) : null,
    token: localStorage.getItem('token') || null,

    login: (user, token) => {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        set({ user, token });
    },
    
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        set({ user: null, token: null });
    }
}));
```

> **Why Zustand?** Normally in React, data flows strictly from a Parent to a Child component. But the `Navbar` (to show a Logout button) AND the `CreateEvent` form (to know who is creating it) both need the `user` object. Zustand creates a "global store" that *any* component can access instantly without messy prop-drilling!
> Notice `localStorage`? Without it, every time the user refreshes the page, React forgets the state and the user is logged out!

## Step 15: Configuring Axios (`src/api/axios.ts`)

Create `src/api/axios.ts` and paste this code:

```typescript
import axios from 'axios';
import { useAuthStore } from '../store/useAuthStore';

// Create a pre-configured Axios instance!
const api = axios.create({
    baseURL: 'http://localhost:3000', // Change this when deploying to production!
});

// Axios "Interceptor": Runs BEFORE every request is sent to the backend
api.interceptors.request.use((config) => {
    // 1. Get our VIP pass (Token) from Zustand
    const token = useAuthStore.getState().token;
    
    // 2. If we have a token, stick it onto the Authorization Header!
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;
```

> **Why Interceptors?** Instead of manually adding the `Authorization: Bearer <token>` header to the 50 different API calls (login, fetch event, create booking, delete comment, etc.), an Interceptor catches EVERY Axios request and automatically glues the token to it. It saves massive amounts of time.

## Step 16: Setup React Router (`src/App.tsx`)

Replace the entirety of `src/App.tsx` with:

```tsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';

// We will create these components in the next step!
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col">
        {/* The Navbar stays on screen forever because it is outside <Routes> */}
        <Navbar />
        
        {/* The toaster displays success/error popups anywhere in our app */}
        <Toaster position="top-right" />
        
        <main className="flex-grow container mx-auto px-4 py-8">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            {/* Add more routes here like /events/:id or /register later! */}
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
```

> **Why the structure?** This is standard SPA layout. We declare a `min-h-screen flex flex-col` so our UI stretches to the bottom of the screen. The `<Navbar>` is placed outside of `<Routes>` so it *never* unmounts or re-renders as the user navigates between the Home or Login pages!

### 🎉 Milestone Reached!
You've scaffolded a completely modern frontend application with routing, centralized state, and authenticated HTTP clients! You can run this right now with `npm run dev` (although it will crash until we build the `Navbar`, `Home`, and `Login` files!). Let's write the UI integration in the final part.

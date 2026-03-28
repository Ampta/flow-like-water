# Developer Onboarding Guide

Welcome to the team! This guide is designed to help you go from "Zero to Hero" in managing and contributing to our codebase. Since you are starting your journey with JavaScript, we have structured this path to build your foundations before diving into the complex logic of our application.

---

## Phase 1: The Foundations (Learn in order)
*Estimated Time: 1-2 Weeks*

Before touching the code, you need to understand the language it's written in.

1.  **JavaScript (ES6+)**: The core language.
    *   **Focus on**: Variables (`let`/`const`), Arrow Functions `() => {}`, Array methods (`.map`, `.filter`, `.reduce`), Destructuring (`const { a } = obj`), and Promises (`async`/`await`).
    *   *Why?* The entire app is built on this logic.

2.  **TypeScript**: JavaScript with superpowers (types).
    *   **Focus on**: Interfaces, Basic Types (`string`, `number`, `boolean`), and Generics.
    *   *Why?* We use TypeScript to catch errors *before* the code runs. You'll see `: string` or `interface Props` everywhere.

3.  **React (The Framework)**:
    *   **Focus on**: Components (`function MyComponent()`), Props (passing data), Hooks (`useState`, `useEffect`), and JSX (writing HTML in JS).
    *   *Why?* Our entire UI is built with React components.

4.  **Tailwind CSS (Styling)**:
    *   **Focus on**: Utility classes like `flex`, `p-4`, `text-center`, `bg-white`.
    *   *Why?* We don't write traditional `.css` files often. We style directly in the HTML elements.

---

## Phase 2: Our Tech Stack (The Tools We Use)

Once you know the basics, here are the specific power tools we use in this project:

*   **Vite**: The build tool. It makes the "Run Dev" server extremely fast.
*   **Shadcn UI**: A collection of beautiful, reusable components (Buttons, Cards, Inputs) located in `src/components/ui`. *Don't build a button from scratch; check here first!*
*   **Zustand**: State Management. It's how we keep track of the "Global State" (like: Is the user logged in?) across the entire app. See `src/store`.
*   **TanStack Query (React Query)**: Data Fetching. It handles loading, error, and caching states when we talk to the backend API.

---

## Phase 3: Project Tour (Deep Dive)

Let's walk through the core files that make this application tick. Understanding these 5 files is 80% of understanding the architecture.

### 1. The Entry Point: `src/main.tsx`
**What it does:** This is the "Big Bang". It's the first code that runs when the website loads.
*   **Key Responsibilities:**
    *   **Mounting React**: It finds the `<div id="root">` in the HTML and injects our React app into it.
    *   **Providers**: It wraps the app in "Providers" like `QueryClientProvider` (for fetching data) and `Toaster` (for showing popup notifications). This ensures these tools are available everywhere in the app.
    *   **Global Styles**: It imports `index.css`, which contains our Tailwind directives and custom fonts.

### 2. The Traffic Cop: `src/App.tsx`
**What it does:** It handles **Routing**. It decides which page component to display based on the URL in the browser bar.
*   **Key Concepts:**
    *   **`Lazy Loading`**: You'll see `const HomePage = lazy(() => ...`. This splits our code so users only download the code for the page they are currently viewing, making the app faster.
    *   **`Routes` Definition**:
        *   **Public Routes**: `/login`, `/register`, `/` (Home). Anyone can see these.
        *   **Protected Routes**: Wrapped in `<RequireAuth />`. E.g., `/dashboard`. If you aren't logged in, these routes bounce you back to Login.
        *   **Admin Routes**: Wrapped in `<RequireRole allowedRoles={['admin']} />`. Only users with the 'admin' role can enter logic.

### 3. The API Handler: `src/api/axios.ts`
**What it does:** This is our configured messenger. Instead of using the raw `fetch` command, we use this `api` object to talk to our Backend.
*   **Why we need it:**
    *   **Interceptors**: Think of these as "Middlewares" for the frontend.
    *   **Request Interceptor**: Automatically attaches the user's `accessToken` to the header of *every* request. You never have to manually type `Authorization: Bearer...`.
    *   **Response Interceptor**: Handles errors globally. Crucially, if the backend says "401 Unauthorized" (your token expired), this file automatically tries to use your `refreshToken` to get a new session *without logging you out*.

### 4. The Global State: `src/store/authStore.ts`
**What it does:** It acts as the "Brain" of the frontend. It remembers who the user is using a library called **Zustand**.
*   **Key Features:**
    *   **`user` object**: Stores your name, email, avatar, etc.
    *   **`accessToken`**: Your digital key to the API.
    *   **Persistence**: We use `persist` middleware. This means if you refresh the page, we don't forget who you are. We save this data in the browser's `localStorage`.
    *   **Actions**: Functions like `setTokens`, `logout`, and `setUser` are defined here so any component can call them.

### 5. The Frame: `src/layouts/RootLayout.tsx`
**What it does:** It provides the consistent "Shell" around every page.
*   **Structure:**
    *   It renders the `<Header />` (navigation bar) at the top.
    *   It contains the `<MobileNavigation />` logic for phone screens.
    *   It renders `<Outlet />`. The `Outlet` is a placeholder where the specific Page content (Home, Dashboard, etc.) is injected by the Router.
    *   *Analogy*: RootLayout is the picture frame; the `Outlet` is where we swap different photos (Pages).

---

## Phase 4: Feature-Based Architecture

We organize our code by **Feature**, not by file type. This makes the codebase scalable.

### Example: The "Dashboard" Feature (`src/features/dashboard/`)
Everything related to the Dashboard lives here.
*   **`components/`**: UI building blocks specific to the dashboard.
    *   `StatsOverview.tsx`: The cards showing your stats.
    *   `GrowthCard.tsx`: The gamification progress card.
*   **`hooks/`**: Data fetching logic strictly for the dashboard.
    *   `useUserEvents.ts`: A custom hook that asks the API for the user's event history.

**Why this is better:**
If you need to fix a bug in the Dashboard, you go to the `dashboard` folder. You don't have to hunt through a giant `components` folder mixed with unrelated Login or Home page components.

---

## Special Section: For the Spring Boot Developer
*Don't get lost in translation. Here is how your Backend knowledge maps to the Frontend.*

### 1. The Translation Dictionary

| Spring Boot Concept | React/Frontend Equivalent | Explanation |
| :--- | :--- | :--- |
| **`@RestController`** | **`Route / Page Component`** | In Spring, a Controller receives a request and returns data. In React, a Page (`src/pages/`) receives a URL visit and "returns" the UI. |
| **`Service` Class** | **Custom Hook (`use...`)** | Business logic lives in Services in Spring. In React, we extract logic into Hooks (`src/features/.../hooks/`) so UI components stay dumb. |
| **DTO / Entity** | **Interface (`types.ts`)** | The shape of your data. We define it with TypeScript interfaces (e.g., `interface User { id: string }`). |
| **Dependency Injection** | **Props / Context** | Instead of `@Autowired`, we pass data down via **Props** or use **Context** (like `useAuthStore` or `ThemeContext`) to make data available everywhere. |
| **Maven / Gradle** | **`npm` / `package.json`** | `package.json` is your `pom.xml`. It lists all dependencies. `npm install` is your `mvn clean install`. |
| **Hibernate / JPA** | **TanStack Query** | Okay, not a direct match. But just as JPA manages talking to the DB, React Query manages talking to the *API* (caching, loading states, refetching). |

### 2. Mental Shift: "State" vs "Stateless"
*   **Backend (Rest API)**: Usually stateless. Request comes in, response goes out.
*   **Frontend**: **Stateful**. The browser remembers "Is the modal open?", "What is typed in the input?", "Is the user loading?".
    *   *Challenge*: You aren't just processing data; you are **managing time**. You have to handle *while* the data is loading, *if* it fails, and *when* it updates.

---

## The "1 Hour Daily" Learning Routine
*Use this structure to learn without burning out.*

*   **Min 0-15: Read one component source code.**
    *   Pick a random file (e.g., `Button.tsx` or `EventCard.tsx`). Read it line-by-line. Google any syntax you don't know (e.g., "What is `forwardRef` in React?").
*   **Min 15-45: The "Tiny Change" Challenge.**
    *   Change a color.
    *   Add a `console.log` to see when a component renders.
    *   Add a new exclamation mark to a title.
    *   *Goal*: predict what will happen, then verify if you were right.
*   **Min 45-60: The "Why" Search.**
    *   Pick one library we use (Zustand, React Query, Tailwind) and read **one page** of their official docs. Just one.

---

## Final Advice for the Fresher
> "Read the code more than you write it."

1.  **Start Small**: Change the color of a button. Change the text on the Home page.
2.  **Trace the path**: Open generic `App.tsx`, click on `dashboard` route, see it leads to `Dashboard.tsx`. Open that file, see it uses `useAuthStore`. Open that store... following the thread is the best way to learn.
3.  **Ask Questions**: If `axios.ts` looks scary, ask! It's the most complex file in the app.

Good luck! You've got this. 🚀

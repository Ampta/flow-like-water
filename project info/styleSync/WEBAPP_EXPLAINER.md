# Knowledge Base: Web App Explainer

## React Frontend (`webapp`)

This is a Vite-powered React application that serves as the user interface for StyleSync. It uses Tailwind CSS for styling and Context API for state management.

### 📄 Component Name: `src/App.jsx`
1. **Purpose (The "Why")**: The root component that defines the application structure and routing.
2. **Logic (The "How")**:
   - `react-router-dom`: Manages client-side navigation.
   - `ProtectedLayout`: Wraps private routes (Dashboard, Feed, etc.) to ensure only authenticated users can access them.
   - `Auth/ThemeProvider`: Wraps the app to provide global state (User session, Dark mode).
3. **Interactions (The "Where")**: Renders pages based on the URL path.
4. **App Role**: **Entry Point** (Router).

### 📄 Component Name: `src/context/AuthContext.jsx`
1. **Purpose (The "Why")**: Manages global user authentication state.
2. **Logic (The "How")**:
   - `useState`: Holds `user` object and `loading` status.
   - `useEffect`: Checks `localStorage` on load to persist login across refreshes.
   - `login/register`: Async functions that call the API and update state.
3. **Interactions (The "Where")**: Used by `ProtectedLayout`, `Login.jsx`, and `Register.jsx`.
4. **App Role**: **State Management** (Authentication).

### 📄 Component Name: `src/services/api.js`
1. **Purpose (The "Why")**: Centralized HTTP client for communicating with the Backend.
2. **Logic (The "How")**:
   - `Axios`: Library for HTTP requests.
   - `Interceptors`: Automatically attaches the JWT `Bearer` token from `localStorage` to every outgoing request.
3. **Interactions (The "Where")**: Called by all Pages and Contexts to fetch data.
4. **App Role**: **Communication** (API Client).

### 📄 Component Name: `src/pages/Dashboard.jsx`
1. **Purpose (The "Why")**: The main landing page for logged-in users, displaying their virtual closet.
2. **Logic (The "How")**:
   - Fetches closet items on mount via `api.getCloset()`.
   - Supports filtering (by Category) and sorting.
   - Renders a masonry grid of `ClothingItemCard` components.
3. **Interactions (The "Where")**: Calls `api.js` and renders `ClothingItemCard`.
4. **App Role**: **Page** (Main View).

### 📄 Component Name: `src/components/FeedPost.jsx`
1. **Purpose (The "Why")**: Displays a single item in the social feed (Image + User + Actions).
2. **Logic (The "How")**:
   - Handles localized state for "Like" counts (Optimistic UI updates).
   - Manages comment input and visibility.
   - "Social Shopping": Includes a "Shop the Look" button triggering `VisualSearchModal`.
3. **Interactions (The "Where")**: Used by `Feed.jsx`. Calls `api.likePost` / `api.commentPost`.
4. **App Role**: **Component** (UI Widget).

### 📄 Component Name: `src/context/ThemeContext.jsx`
1. **Purpose (The "Why")**: Manages Light/Dark mode preference.
2. **Logic (The "How")**:
   - Toggles a class on the `<html>` document element.
   - Persists preference to `localStorage`.
3. **Interactions (The "Where")**: Used by `Navbar` and global `App` wrapper.
4. **App Role**: **State Management** (UI Theme).

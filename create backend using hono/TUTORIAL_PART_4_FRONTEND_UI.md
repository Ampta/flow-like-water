# 🖼️ Rebuild Tutorial Part 4: Frontend UI & Integration

The final piece of the puzzle! We need to build the `Navbar` so we can log out, the `Login` page to connect to our backend, and the `Home` page to fetch Events!

---

## Step 17: The Navbar (`src/components/Navbar.tsx`)

Create the folder `src/components` and the file `Navbar.tsx` inside it:

```tsx
import { Link, useNavigate } from 'react-router-dom';
import { LogOut, User } from 'lucide-react';
import { useAuthStore } from '../store/useAuthStore';

const Navbar = () => {
  const navigate = useNavigate();
  // 1. Grab our user data and logout function from global state!
  const { user, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    navigate('/login'); // Redirect to login page
  };

  return (
    <nav className="bg-white shadow">
      <div className="container mx-auto px-4 py-4 flex justify-between items-center">
        {/* React Router uses <Link> instead of <a> tags so it doesn't reload the page! */}
        <Link to="/" className="text-2xl font-bold text-blue-600">
          AMPTA Events
        </Link>
        <div className="flex items-center space-x-4">
          {/* Conditional Rendering! */}
          {user ? (
            <>
              <div className="flex items-center gap-2 text-gray-600">
                <User size={20} />
                <span>Hi, {user.username}</span>
              </div>
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 text-red-500 hover:text-red-700"
              >
                <LogOut size={20} /> Logout
              </button>
            </>
          ) : (
            <Link to="/login" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
              Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
```

> **Why?** Since the Navbar sits outside `<Routes>` in `App.tsx`, it always renders. We use `useAuthStore()` to see if a token exists. If it does, we show their name and a "Logout" button. If not, we show a "Login" button.

## Step 18: The Login Page (`src/pages/Login.tsx`)

Create `src/pages/Login.tsx`:

```tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import api from '../api/axios';
import { useAuthStore } from '../store/useAuthStore';

const Login = () => {
  const navigate = useNavigate();
  const loginFn = useAuthStore((state) => state.login); // Zustand Action

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // Stop forms from refreshing the browser!

    try {
      setLoading(true);
      // Make the actual HTTP request to your Bun backend!
      const response = await api.post('/auth/login', { email, password });

      // If successful, extract the data:
      const { token, user } = response.data.data;

      // Tell Zustand to save it globally (and to LocalStorage)
      loginFn(user, token);
      
      toast.success('Login successful!');
      navigate('/'); // Redirect back to Home
    } catch (error: any) {
      // Show the backend's error message (e.g. "Invalid credentials")
      const message = error.response?.data?.message || 'Failed to login';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-16 bg-white p-8 border rounded-lg shadow">
      <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm p-2 border"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm p-2 border"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white p-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400"
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
};

export default Login;
```

> **Why?** This is your main connector! It binds React state (`useState`) to inputs. When submitted, `axios` sends that state to `http://localhost:3000/auth/login`. If the password matches in MongoDB, your backend replies with the JWT. You store that JWT via Zustand (`loginFn`) and reroute the user. The Navbar will instantly update!

## Step 19: The Home Page (Data Fetching) (`src/pages/Home.tsx`)

Create `src/pages/Home.tsx`:

```tsx
import { useEffect, useState } from 'react';
import api from '../api/axios';
import { Calendar, MapPin } from 'lucide-react';

export default function Home() {
  const [events, setEvents] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // A standard function to reach out to our backend and fetch events
    const fetchEvents = async () => {
      try {
        const response = await api.get('/events');
        setEvents(response.data.data.events); // Assuming your exact API structure
      } catch (error) {
        console.error("Failed to fetch events");
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []); // The empty [] means this only runs ONCE when the Home page loads!

  if (loading) return <div className="text-center mt-20 text-xl font-bold">Loading Events...</div>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8 text-gray-800">Upcoming Events</h1>
      {/* Grid Layout using Tailwind */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {events.map((event) => (
          <div key={event._id} className="bg-white rounded-lg shadow overflow-hidden border">
            {/* If there's an image, we show it, else a fallback color */}
            <div className="h-48 bg-gray-200">
               {event.thumbnail && <img src={event.thumbnail} alt={event.title} className="w-full h-full object-cover" />}
            </div>
            
            <div className="p-4">
              <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full uppercase tracking-wide font-semibold">
                {event.category}
              </span>
              <h2 className="text-xl font-semibold mt-2">{event.title}</h2>
              <p className="text-gray-600 mt-2 line-clamp-2">{event.description}</p>
              
              <div className="mt-4 space-y-2 text-sm text-gray-500">
                <div className="flex items-center gap-2">
                  <Calendar size={16} />
                  <span>{new Date(event.date).toLocaleDateString()}</span>
                </div>
                <div className="flex items-center gap-2">
                  <MapPin size={16} />
                  <span>{event.location}</span>
                </div>
              </div>
            </div>
          </div>
        ))}
        {events.length === 0 && <p className="text-gray-500">No events found in the database.</p>}
      </div>
    </div>
  );
}
```

> **Why?** React's `useEffect` acts as a trigger when the `<Home>` component renders on the screen. It fires an Axios GET request to your backend's `/events` route. It takes the resulting array of JSON objects, saves it in `useState`, and maps over it to render beautiful Tailwind cards for every single event found in MongoDB!

## 🚀 The End of the Beginning

You have just rebuilt an entire Full-Stack system from scratch:
1. You set up a Mongo database.
2. You built secure Models.
3. You exposed them securely via Hono routes and Zod validation.
4. You built a Vite React App.
5. You linked Zustand for global state.
6. You made them talk via Axios.

**What to do next?**
Using the exact logic template in `Login.tsx`, try building `Register.tsx` to hit the `/auth/register` endpoint! Or, create an `EventDetails.tsx` that fetches `GET /events/:id`!

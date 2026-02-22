# 🛠️ Rebuild Tutorial Part 1: Backend Setup & Models

This is exactly what you need to copy, file by file, to rebuild your backend from scratch. 

> [!IMPORTANT]
> **Why are we doing this?** The backend is the brain of your application. It securely talks to the database, verifies passwords, and ensures users can only do what they are allowed to do. We are using **Bun** (super fast runtime) and **Hono** (lightweight web framework) for maximum performance.

---

## Step 1: Initialize the Project

1. Open your terminal and create a new folder for your project.
2. Run this command to initialize a new Bun project:
   ```bash
   bun init -y
   ```
3. Install the required dependencies:
   ```bash
   bun add hono mongoose bcryptjs zod jsonwebtoken resend @hono/swagger-ui
   bun add -d @types/mongoose @types/bcryptjs @types/jsonwebtoken
   ```

> **Why?** `hono` is our server framework. `mongoose` lets us talk to MongoDB easily. `bcryptjs` encrypts passwords securely. `zod` validates incoming data (like ensuring an email is actually an email).

## Step 2: Configure TypeScript (`tsconfig.json`)

Replace the contents of `tsconfig.json` with this:

```json
{
  "compilerOptions": {
    "target": "ESNext",
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "strict": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "outDir": "./dist",
    "types": ["bun-types"]
  },
  "include": ["src/**/*"]
}
```

> **Why?** This tells TypeScript how strictly to check your code and ensures it recognizes Bun's special features (like reading `.env` files automatically). We restrict our source files to the `src` folder.

## Step 3: Database Connection (`src/config/db.ts`)

Create `src/config/db.ts` and paste this code:

```typescript
import mongoose from 'mongoose';

export const connectDB = async () => {
    try {
        // We use an environment variable so we don't hardcode our password!
        const mongoUri = process.env.MONGO_URI || 'mongodb://localhost:27017/ampta_events';
        await mongoose.connect(mongoUri);
        console.log('✅ MongoDB Connected Successfully');
    } catch (err) {
        console.error('❌ MongoDB Connection Error:', err);
        process.exit(1); // Stop the server if the database fails
    }
};
```

> **Why?** Without a database, our app can't remember anything. This file uses Mongoose to open a connection to your MongoDB URL. If it fails, we `process.exit(1)` because a backend without a database is useless.

## Step 4: Define the User Model (`src/models/User.ts`)

Create `src/models/User.ts` and paste this code:

```typescript
import mongoose, { Schema, Document } from 'mongoose';
import bcrypt from 'bcryptjs';

export interface IUser extends Document {
    username: string;
    email: string;
    password?: string;
    roles: ('user' | 'admin')[];
    comparePassword(candidatePassword: string): Promise<boolean>;
}

const UserSchema: Schema = new Schema(
    {
        username: { type: String, required: true, unique: true },
        email: { type: String, required: true, unique: true, lowercase: true },
        password: { type: String, required: true, select: false }, // "select: false" hides password by default!
        roles: { type: [String], enum: ['user', 'admin'], default: ['user'] },
    },
    { timestamps: true }
);

// Pre-save hook: Hash the password BEFORE saving it to MongoDB
UserSchema.pre('save', async function () {
    if (!this.isModified('password')) return; // Only hash if password changed
    const salt = await bcrypt.genSalt(10);
    this.password = await bcrypt.hash(this.password as string, salt);
});

// Method: Compare a typed-in password with the hashed one in the DB
UserSchema.methods.comparePassword = async function (candidatePassword: string) {
    return bcrypt.compare(candidatePassword, this.password as string);
};

export default mongoose.model<IUser>('User', UserSchema);
```

> **Why?** The Model is our "Shape" of data. We want every user to have a unique email. 
> Notice the **`pre('save')` hook**? This is critical for security! Whenever we save a new User, Mongoose intercepts it, takes the plain-text password ("password123"), scrambles it (hashes it) using `bcrypt`, and ONLY saves the scrambled version.

## Step 5: Define the Event Model (`src/models/Event.ts`)

Create `src/models/Event.ts` and paste this code:

```typescript
import mongoose, { Schema, Document } from 'mongoose';

export interface IEvent extends Document {
    title: string;
    description: string;
    date: Date;
    capacity: number;
    price: number;
    creator: mongoose.Types.ObjectId;
}

const EventSchema: Schema = new Schema(
    {
        title: { type: String, required: true },
        description: { type: String, required: true },
        date: { type: Date, required: true },
        capacity: { type: Number, required: true, min: 1 },
        price: { type: Number, default: 0 },
        // This links the Event to a specific User document
        creator: { type: Schema.Types.ObjectId, ref: 'User', required: true },
    },
    { timestamps: true }
);

export default mongoose.model<IEvent>('Event', EventSchema);
```

> **Why?** Notice the `creator` field. We use `Schema.Types.ObjectId` with `ref: 'User'`. This tells MongoDB that this string isn't just random text; it's the exact ID of a User in the Users collection. This is how we know who created the event!

## Step 6: Define the (New) Booking Model (`src/models/Booking.ts`)

Create `src/models/Booking.ts` and paste this code:

```typescript
import mongoose, { Schema, Document } from 'mongoose';

export interface IBooking extends Document {
    user: mongoose.Types.ObjectId;
    event: mongoose.Types.ObjectId;
    status: 'confirmed' | 'cancelled';
    tickets: number;
}

const BookingSchema: Schema = new Schema(
    {
        user: { type: Schema.Types.ObjectId, ref: 'User', required: true },
        event: { type: Schema.Types.ObjectId, ref: 'Event', required: true },
        status: { type: String, enum: ['confirmed', 'cancelled'], default: 'confirmed' },
        tickets: { type: Number, required: true, min: 1, default: 1 }
    },
    { timestamps: true }
);

// A user can only book a specific event once!
BookingSchema.index({ user: 1, event: 1 }, { unique: true });

export default mongoose.model<IBooking>('Booking', BookingSchema);
```

> **Why?** Originally, you stored attendees inside an array on the Event itself. That doesn't scale well (what if 10,000 people attend?). By creating a dedicated `Booking` model, we can easily track how many tickets one person bought, independently manage cancellations, and run queries without loading a massive Event document.

---
### 🎉 Milestone Reached!
You've just built your data foundation. Next, we will write the **Controllers and Routes** so users can actually interact with these models! If you're ready, let's move to Part 2!

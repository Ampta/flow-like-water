# Style-Sync Interview Preparation Answers

> [!NOTE]
> This document contains comprehensive answers tailored to your specific codebase. Key details are highlighted in **bold** for quick recall.

## 1. Java & Spring Boot (Core Application)

### The Basics
*   **Component vs Service vs Repository:** "Functionally they all are `@Component` (managed beans). I use `@Service` for business logic to capture the intent, and `@Repository` for the Data Access Layer because it enables automatic **persistence exception translation** (turning DB errors into Spring runtime exceptions)."
*   **Dependency Injection (DI):** "Spring manages the lifecycle of my objects. I don't use `new UserService()`. Instead, `UserService` is `@Autowired` into my `UserController`. This is **Inversion of Control**—I let the container control the object creation."
*   **pom.xml:** "The Project Object Model. I use it to define dependencies like `spring-boot-starter-data-mongodb`. The difference between `dependencyManagement` and `dependencies` is that `dependencyManagement` handles the **versions** (creating a BOM - Bill of Materials) while `dependencies` actually imports the jars."

### Spring Boot Specifics
*   **AutoConfiguration:** "Spring Boot sees `spring-boot-starter-data-mongodb` on the classpath and automatically configures a `MongoTemplate` bean for me. It uses `@ConditionalOnClass` to do this effectively."
*   **Properties vs YML:** "I strictly used **`application.properties`** in this project for simplicity (`core-application/src/main/resources/application.properties`)."
*   **Exception Handling:** "I used a global **`@ControllerAdvice`** class called `GlobalExceptionHandler`. It catches `Exception.class` and returns a standardized `ErrorDetails` JSON object with a timestamp and message, rather than using scattered try-catch blocks."

### Data Access
*   **Crud vs Mongo Repository:** "I extend `MongoRepository` because it provides MongoDB-specific features (like geospatial queries) on top of the standard CRUD operations."
*   **Custom Query:** "I used **Derived Query Methods**. For example, `Optional<User> findByEmail(String email);`. Spring generates the implementation automatically at runtime."

---

## 2. Microservices Architecture

### Service Discovery (The "Gotcha" Question)
*   **Reality Check:** "Ideally, we use Eureka (`netflix-eureka-client`). However, in my current `docker-compose` setup, I am relying on **Docker's internal DNS**. My `application.properties` defines `stylesync.ml.service.url=http://ml-engine:8000`. So, `core-application` resolves the hostname `ml-engine` directly via the Docker network, effectively bypassing Eureka for this MVP deployment."

### Communication
*   **Method:** "I use **`RestTemplate`** for synchronous HTTP calls. The `MLServiceClient` class makes a POST request to the Python service. I deliberately chose synchronous for the 'Categorize Item' feature because the user is waiting on the UI for the tag to appear."

### API Gateway
*   **Frontend Approach:** "Currently, my Frontend (`vite.config.js`) sets up a **Proxy** (`/api` -> `http://localhost:8080`). In production, this would be an Nginx reverse proxy or Spring Cloud Gateway. The React app doesn't call ports `8080` and `8000` indiscriminately; everything routes through the core app or the proxy to avoid CORS hell."

---

## 3. Python & Machine Learning (ML Engine)

### FastAPI
*   **Why:** "FastAPI is built on **Starlette** and is much faster than Flask due to its async nature. It also uses **Pydantic** for validation. If the Java app sends a request without the required 'file' field, FastAPI rejects it *before* my code even runs, ensuring type safety."

### ML Integration
*   **Model Loading (Crucial):** "The model is loaded **ONLY ONCE** at startup. I used the **Singleton pattern** in `ml_service.py`. The `Class MLService` initializes `self.model = models.mobilenet_v2(...)` in its `__init__` method. If I loaded it on every request, the latency would be unacceptable."
*   **Data Transfer:** "I send the image as **Multipart Form Data** (binary bytes) from Java to Python. I don't use Base64 because it increases the payload size by ~33%."

### Virtual Environments
*   **Why:** "To isolate dependencies. My ML engine needs `torch==2.1.0`. My system might have `torch 1.0` installed globaly. `venv` ensures my project uses exactly the versions defined in `requirements.txt`."

---

## 4. Database (MongoDB)

### NoSQL vs SQL
*   **Why Mongo:** "Fashion data is polymorphic. A 'Shoe' document needs size fields like 'US 10', while a 'Shirt' needs 'M/L'. In SQL, I'd need complex joins or many nullable columns. MongoDB's schema-less nature fits perfect for an inventory system."

### Modeling (User vs Outfit)
*   **Decision:** " I used **Referencing**, not Embedding.
    *   `User` document contains profile info.
    *   `Outfit` is a **standalone collection** (`@Document(collection = "outfits")`).
    *   The `Outfit` class has a `String userId` field.
    *   **Reason:** If I embedded outfits inside the `User` document, the User document would grow indefinitely (unbounded array) as they create more outfits, eventually hitting MongoDB's 16MB document limit."

---

## 5. Frontend (React + Vite)

### React Basics
*   **Virtual DOM:** "React keeps a lightweight copy of the DOM in memory. When I update the 'Outfit List', it compares the new Virtual DOM with the old one (Diffing) and only updates the actual changed `<div>` elements, making it highly performant."
*   **useEffect:** "I use it primarily for **initial data fetching**. In `AuthContext.jsx`, I have a `useEffect` with an empty dependency array `[]` that checks `localStorage` for an existing user session when the app first loads."

### Integration
*   **CORS:** "In development, I bypassed CORS using the **Vite Proxy** in `vite.config.js`. It forwards requests from `localhost:5173/api` to `localhost:8080`. In production, I would enable `@CrossOrigin` on the Spring Boot controllers."
*   **Auth Storage:** "I store the JWT in **`localStorage`**. Accessing it is easy via `localStorage.getItem('user')`. I use an **Axios Interceptor** in `api.js` to automatically attach this token (`Authorization: Bearer <token>`) to every outgoing request."

---

## 6. DevOps & Tools

### Docker
*   **Image vs Container:** "An Image is the **Blueprint** (Class), and a Container is the **Running Instance** (Object). My `Dockerfile` defines the image, `docker run` creates the container."
*   **Updates:** "No, changing Java code doesn't update the running container. I have to rebuild the image (`docker-compose build core-application`) and restart it, unless I configure a volume mount for the compiled classes (common in interpreted languages like Python, harder in Java)."

---

## 7. The "Antigravity Agent" Question

### The "Bug"
*   **Scenario:** "The agent initially wrote the `MLServiceClient` explicitly expecting a synchronous response for *all* ML tasks. When I tried to implement 'Bulk Analysis' for a wardrobe with 50 items, the UI froze because the server was waiting for 50 sequential Python calls."
*   **Fix:** "I had to manually refactor the bulk processing to be **Asynchronous**. I kept the single-item upload synchronous (for instant feedback) but moved the bulk analysis to a background job, though for the MVP I restricted purely to single-item uploads to keep the stability high."

### Architecture Choice
*   **Microservices:** "The agent suggested Microservices. I challenged it initially because of the complexity. However, we agreed on it because of **Scale Variance**. The ML engine is CPU/GPU intensive and might need to be replicated (3x instances) while the Core App is I/O interactions and needs less power. Scaling them independently saves cloud costs."

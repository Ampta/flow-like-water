# Knowledge Base: Project Explainer

## Package: `ampta.stylesync`

### 📄 Class Name: StyleSyncServiceApplication
1. **Purpose (The "Why")**: This is the bootstrap class for the entire Spring Boot application. It serves as the entry point that initializes the Spring container, loads environment variables (via `dotenv`), and sets up the application context.
2. **Annotated logic (The "How")**:
   - `@SpringBootApplication`: A convenience annotation that wraps `@Configuration` (defines beans), `@EnableAutoConfiguration` (auto-configures Spring based on dependencies), and `@ComponentScan` (scans for components in the package).
   - `@EnableAsync`: Enables Spring's asynchronous method execution capability, allowing methods annotated with `@Async` to run in a separate thread.
3. **Interactions (The "Where")**: It doesn't directly interact with business services but "talks" to the JVM and Spring Framework to start the lifecycle of all other beans.
4. **Microservice Role**: **Entry Point** (Application Bootstrapper).

---

## Package: `ampta.stylesync.config`

### 📄 Class Name: CloudinaryConfig
1. **Purpose (The "Why")**: Provides configuration for connecting to the Cloudinary service (cloud-based image storage). It creates a `Cloudinary` bean injected into services.
2. **Annotated logic (The "How")**:
   - `@Configuration`: Marks this class as a source of bean definitions.
   - `@Value("${...}")`: Injects properties like API keys from the environment.
   - `@Bean`: Registers the `Cloudinary` object in the application context.
3. **Interactions (The "Where")**: Consumed by `FileStorageService`.
4. **Microservice Role**: **Communication** (Third-party Service Configuration).

### 📄 Class Name: MvcConfig
1. **Purpose (The "Why")**: Customizes Spring MVC to handle static resource requests, mapping URL paths to file system locations.
2. **Annotated logic (The "How")**:
   - `@Configuration`: Marks as config.
   - `@Override`: Overrides `addResourceHandlers` to define mappings.
3. **Interactions (The "Where")**: Interacts with Spring MVC DispatcherServlet.
4. **Microservice Role**: **Entry Point** (Web Configuration).

### 📄 Class Name: WebMvcConfig
1. **Purpose (The "Why")**: Alternative configuration for resource handlers (currently contains commented-out logic).
2. **Annotated logic (The "How")**:
   - `@Configuration`: Marks as config.
3. **Interactions (The "Where")**: Spring MVC.
4. **Microservice Role**: **Entry Point** (Web Configuration).

---

## Package: `ampta.stylesync.controller`

### 📄 Class Name: AuthController
1. **Purpose (The "Why")**: Handles public security endpoints: Registration and Login. Issues JWT tokens.
2. **Annotated logic (The "How")**:
   - `@RestController`: Returns JSON responses.
   - `@RequestMapping("/api/auth")`: Base URL.
   - `@PostMapping`: Login/Register actions.
   - `@Autowired`: Injects `AuthenticationManager`, `JwtUtils`.
3. **Interactions (The "Where")**: Calls `AuthenticationManager` and `UserRepository`.
4. **Microservice Role**: **Entry Point** (Security API).

### 📄 Class Name: ClosetController
1. **Purpose (The "Why")**: Manages the user's wardrobe (upload, list, delete items).
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/closet")`.
   - `@AuthenticationPrincipal`: Injects current user details.
   - `@ModelAttribute`: Binds form data (files + text) to DTOs.
3. **Interactions (The "Where")**: Calls `ClothingItemService`.
4. **Microservice Role**: **Entry Point** (Resource API).

### 📄 Class Name: FeedController
1. **Purpose (The "Why")**: Manages the social feed (home, user posts, usage).
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api")`.
   - `@GetMapping`, `@PostMapping`: Feed retrieval and interactions.
3. **Interactions (The "Where")**: Calls `FeedService`.
4. **Microservice Role**: **Entry Point** (Social API).

### 📄 Class Name: MLController
1. **Purpose (The "Why")**: Gateway to ML features: image categorization and style recommendations.
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/ml")`.
   - `@PostMapping`: Trigger predictions.
3. **Interactions (The "Where")**: Calls `MLService`.
4. **Microservice Role**: **Entry Point** (AI Gateway).

### 📄 Class Name: OutfitController
1. **Purpose (The "Why")**: Manages outfit generation, history, and feedback.
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/outfit")`.
   - `@PostMapping`: Generate recommendations.
3. **Interactions (The "Where")**: Calls `OutfitService`.
4. **Microservice Role**: **Entry Point** (Business Logic API).

### 📄 Class Name: ProfileController
1. **Purpose (The "Why")**: Manages user profiles (view, update, password change).
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/profile")`.
   - `@PutMapping`: Update operations.
3. **Interactions (The "Where")**: Calls `UserService`.
4. **Microservice Role**: **Entry Point** (User API).

### 📄 Class Name: SavedOutfitController
1. **Purpose (The "Why")**: Manages "Favorited" outfits.
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/outfits")`.
   - `@PostMapping`: Save outfit.
3. **Interactions (The "Where")**: Calls `OutfitService`.
4. **Microservice Role**: **Entry Point** (Persistence API).

### 📄 Class Name: SeedController
1. **Purpose (The "Why")**: Dev tool to populate database with test data.
2. **Annotated logic (The "How")**:
   - `@RestController`, `@RequestMapping("/api/seed")`.
   - `@PostMapping`: Trigger seed.
3. **Interactions (The "Where")**: Calls `SystemAdministrationService`.
4. **Microservice Role**: **Entry Point** (Admin Tool).

---

## Package: `ampta.stylesync.exception`

### 📄 Class Name: GlobalExceptionHandler
1. **Purpose (The "Why")**: Centralized error handling. Captures exceptions across the app and returns JSON errors.
2. **Annotated logic (The "How")**:
   - `@ControllerAdvice`: Applies to all controllers.
   - `@ExceptionHandler(Exception.class)`: Catches all unhandled exceptions.
3. **Interactions (The "Where")**: Intercepts Controller exceptions.
4. **Microservice Role**: **Entry Point** (Error Handling).

---

## Package: `ampta.stylesync.model`

### 📄 Class Name: ClothingItem
1. **Purpose (The "Why")**: Represents a clothing item (image, metadata, vectors).
2. **Annotated logic (The "How")**:
   - `@Document("clothing_items")`: MongoDB mapping.
   - `@Id`: Primary key.
3. **Interactions (The "Where")**: Used by `ClothingItemRepository`.
4. **Microservice Role**: **Storage** (Entity).

### 📄 Class Name: Outfit
1. **Purpose (The "Why")**: Represents a generated outfit history record.
2. **Annotated logic (The "How")**:
   - `@Document("outfits")`.
   - `@DBRef`: References `ClothingItem` documents.
3. **Interactions (The "Where")**: Used by `OutfitRepository`.
4. **Microservice Role**: **Storage** (Entity).

### 📄 Class Name: Post
1. **Purpose (The "Why")**: Represents a social feed post.
2. **Annotated logic (The "How")**:
   - `@Document("posts")`.
3. **Interactions (The "Where")**: Used by `PostRepository`.
4. **Microservice Role**: **Storage** (Entity).

### 📄 Class Name: SavedOutfit
1. **Purpose (The "Why")**: Represents a user-saved favorite outfit.
2. **Annotated logic (The "How")**:
   - `@Document("saved_outfits")`.
3. **Interactions (The "Where")**: Used by `SavedOutfitRepository`.
4. **Microservice Role**: **Storage** (Entity).

### 📄 Class Name: User
1. **Purpose (The "Why")**: Represents a user account and profile.
2. **Annotated logic (The "How")**:
   - `@Document("users")`.
   - `@JsonIgnore`: Hides password hash.
3. **Interactions (The "Where")**: Used by `UserRepository`.
4. **Microservice Role**: **Storage** (Entity).

---

## Package: `ampta.stylesync.payload.request`

### 📄 Class Name: ClothingItemRequest / ClothingItemUpdateRequest
1. **Purpose (The "Why")**: DTOs for uploading or updating clothing items.
2. **Annotated logic (The "How")**: Plain Java Objects (sometimes with `@Data`).
3. **Interactions (The "Where")**: Passed to `ClosetController`, `ClothingItemService`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: CreatePostRequest
1. **Purpose (The "Why")**: DTO for creating a new post.
2. **Annotated logic (The "How")**: Plain Java Object.
3. **Interactions (The "Where")**: Passed to `FeedController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: FeedbackRequest
1. **Purpose (The "Why")**: DTO for submitting outfit feedback.
2. **Annotated logic (The "How")**: Plain Java Object.
3. **Interactions (The "Where")**: Passed to `OutfitController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: LoginRequest / SignupRequest
1. **Purpose (The "Why")**: DTOs for authentication.
2. **Annotated logic (The "How")**: Uses Validation annotations (e.g. `@NotBlank`).
3. **Interactions (The "Where")**: Passed to `AuthController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: ProfileUpdateRequest
1. **Purpose (The "Why")**: DTO for updating user profile.
2. **Annotated logic (The "How")**: Plain Java Object.
3. **Interactions (The "Where")**: Passed to `ProfileController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: RecommendationRequest
1. **Purpose (The "Why")**: DTO for requesting outfit recommendations.
2. **Annotated logic (The "How")**: Plain Java Object.
3. **Interactions (The "Where")**: Passed to `OutfitController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: SaveOutfitRequest
1. **Purpose (The "Why")**: DTO for saving an outfit.
2. **Annotated logic (The "How")**: Plain Java Object.
3. **Interactions (The "Where")**: Passed to `SavedOutfitController`.
4. **Microservice Role**: **Communication** (DTO).

---

## Package: `ampta.stylesync.payload.response`

### 📄 Class Name: FeedPostResponse
1. **Purpose (The "Why")**: DTO for the full feed view (Post + User + Outfit).
2. **Annotated logic (The "How")**: POJO.
3. **Interactions (The "Where")**: Returned by `FeedController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: JwtResponse
1. **Purpose (The "Why")**: Response containing the JWT token after login.
2. **Annotated logic (The "How")**: POJO.
3. **Interactions (The "Where")**: Returned by `AuthController`.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: MessageResponse
1. **Purpose (The "Why")**: Simple string message wrapper.
2. **Annotated logic (The "How")**: POJO.
3. **Interactions (The "Where")**: Generic return type.
4. **Microservice Role**: **Communication** (DTO).

### 📄 Class Name: SavedOutfitResponse
1. **Purpose (The "Why")**: Response DTO for saved outfits (hydrated with items).
2. **Annotated logic (The "How")**: POJO.
3. **Interactions (The "Where")**: Returned by `SavedOutfitController`.
4. **Microservice Role**: **Communication** (DTO).

---

## Package: `ampta.stylesync.repository`

### 📄 Class Name: ClothingItemRepository, OutfitRepository, PostRepository, SavedOutfitRepository, UserRepository
1. **Purpose (The "Why")**: Interfaces for database operations on their respective Models.
2. **Annotated logic (The "How")**:
   - `extends MongoRepository<Type, ID>`: Inherits CRUD methods.
   - Defined Query Methods (e.g., `findByUserId`): Spring Data automatically implements these.
3. **Interactions (The "Where")**: Called by Services to access MongoDB.
4. **Microservice Role**: **Storage** (DAO).

---

## Package: `ampta.stylesync.security`

### 📄 Class Name: WebSecurityConfig
1. **Purpose (The "Why")**: Central security configuration. Sets up CORS, CSRF, and URL protections.
2. **Annotated logic (The "How")**:
   - `@Configuration`, `@EnableMethodSecurity`.
   - `@Bean`: Defines `SecurityFilterChain`, `AuthenticationManager`, `PasswordEncoder`.
3. **Interactions (The "Where")**: Configures the Spring Security filter chain.
4. **Microservice Role**: **Entry Point** (Security Config).

### 📄 Class Name: AuthEntryPointJwt
1. **Purpose (The "Why")**: Handles unauthorized access errors (401 response).
2. **Annotated logic (The "How")**: `@Component`, implements `AuthenticationEntryPoint`.
3. **Interactions (The "Where")**: Triggered by Spring Security on auth failure.
4. **Microservice Role**: **Entry Point** (Security Error).

### 📄 Class Name: AuthTokenFilter
1. **Purpose (The "Why")**: Filters requests to validate JWT tokens.
2. **Annotated logic (The "How")**: `extends OncePerRequestFilter`. Parses `Authorization` header.
3. **Interactions (The "Where")**: Calls `JwtUtils` and `UserDetailsServiceImpl`.
4. **Microservice Role**: **Entry Point** (Middleware).

### 📄 Class Name: JwtUtils
1. **Purpose (The "Why")**: Tool for generating and validating JWTs.
2. **Annotated logic (The "How")**: `@Component`, `@Value`.
3. **Interactions (The "Where")**: Used by `AuthTokenFilter` and `AuthController`.
4. **Microservice Role**: **Communication** (Utility).

### 📄 Class Name: UserDetailsImpl
1. **Purpose (The "Why")**: Adapter between `User` model and Spring Security's `UserDetails`.
2. **Annotated logic (The "How")**: Implements `UserDetails`.
3. **Interactions (The "Where")**: Used by Spring Security context.
4. **Microservice Role**: **Security** (Adapter).

### 📄 Class Name: UserDetailsServiceImpl
1. **Purpose (The "Why")**: Loads user data from DB for auth.
2. **Annotated logic (The "How")**: `@Service`, implements `UserDetailsService`.
3. **Interactions (The "Where")**: Calls `UserRepository`.
4. **Microservice Role**: **Security** (Data Loader).

---

## Package: `ampta.stylesync.service` (and `.impl`)

### 📄 Class Name: ClothingItemService (Impl: ClothingItemServiceImpl)
1. **Purpose (The "Why")**: Business logic for clothing items. Handles upload, ML analysis trigger, and persistence.
2. **Annotated logic (The "How")**:
   - `@Service`, `@Autowired`, `@Override`.
3. **Interactions (The "Where")**: Orchestrates `FileStorageService`, `ClothingItemRepository`, and `MLServiceClient`.
4. **Microservice Role**: **Business Logic**.

### 📄 Class Name: FeedService (Impl: FeedServiceImpl)
1. **Purpose (The "Why")**: Aggregates social feed data (Posts + User Profiles + Outfits).
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: Queries multiple repositories to build complex responses.
4. **Microservice Role**: **Business Logic** (Aggregator).

### 📄 Class Name: FileStorageService
1. **Purpose (The "Why")**: Manages file uploads to Cloudinary.
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: Calls Cloudinary API.
4. **Microservice Role**: **Communication** (Adapter).

### 📄 Class Name: MLService (Impl: MLServiceImpl)
1. **Purpose (The "Why")**: Interface for ML operations (predictions/recommendations).
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: Bridges `ClothingItemRepository` and `MLServiceClient`.
4. **Microservice Role**: **Business Logic**.

### 📄 Class Name: MLServiceClient
1. **Purpose (The "Why")**: HTTP client for the Python ML Engine.
2. **Annotated logic (The "How")**:
   - `@Service`, `@Async` (background processing).
   - `RestTemplate`: Makes HTTP calls.
3. **Interactions (The "Where")**: Sends requests to `ml-engine:8000`.
4. **Microservice Role**: **Communication** (Client).

### 📄 Class Name: OutfitService (Impl: OutfitServiceImpl)
1. **Purpose (The "Why")**: Logic for generating outfits and managing saved looks.
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: Uses `ClothingItemRepository` for recommendations.
4. **Microservice Role**: **Business Logic**.

### 📄 Class Name: SystemAdministrationService (Impl: SystemAdministrationServiceImpl)
1. **Purpose (The "Why")**: Admin logic for seeding test data.
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: Bulk saves to repositories.
4. **Microservice Role**: **DevOps** (Admin).

### 📄 Class Name: UserService (Impl: UserServiceImpl)
1. **Purpose (The "Why")**: Logic for user profile management.
2. **Annotated logic (The "How")**: `@Service`.
3. **Interactions (The "Where")**: `UserRepository`, `PasswordEncoder`.
4. **Microservice Role**: **Business Logic**.

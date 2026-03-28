# Style-Sync Codebase Walkthrough

This guide connects your interview answers directly to the code. Open these files in your IDE to "prove" your knowledge during the interview.

## 1. System Architecture & Docker
**Concept**: Microservices & Orchestration
*   **File**: [docker-compose.yml](file:///c:/Users/ashva/Documents/workspace/expand/docker-compose.yml)
*   **What to show**:
    *   **Lines 36 & 20**: Show the `core-application` (Java) and `ml-engine` (Python) defined as separate services.
    *   **Lines 46-47**: Point out the environment variables `SPRING_DATA_MONGODB_URI` and `STYLESYNC_ML_SERVICE_URL`. This proves you know how services find each other (Docker DNS).
    *   **Line 40**: `depends_on`. Show how you ensure the database starts before the app.

## 2. Java Core Application (Spring Boot)
**Concept**: Global Exception Handling
*   **File**: [GlobalExceptionHandler.java](file:///c:/Users/ashva/Documents/workspace/expand/core-application/src/main/java/com/ampta/stylesync/exception/GlobalExceptionHandler.java)
*   **What to show**:
    *   **Line 10**: The `@ControllerAdvice` annotation. Explain: "I don't put try-catch everywhere; this class catches global errors."
    *   **Line 14**: The `@ExceptionHandler(Exception.class)` method that standardizes the error response.

**Concept**: Synchronous ML Integration
*   **File**: [MLServiceClient.java](file:///c:/Users/ashva/Documents/workspace/expand/core-application/src/main/java/com/ampta/stylesync/service/MLServiceClient.java)
*   **What to show**:
    *   **Line 32**: `RestTemplate` instantiation.
    *   **Line 203**: The `recommendItems` method. Explain: "This makes a blocking HTTP POST call to the Python service. I chose this because the UI needs the recommendation immediately."

## 3. Database Modeling (MongoDB)
**Concept**: Referencing vs Embedding
*   **File**: [User.java](file:///c:/Users/ashva/Documents/workspace/expand/core-application/src/main/java/com/ampta/stylesync/model/User.java)
    *   **Observation**: Notice there is NO list of outfits here.
*   **File**: [Outfit.java](file:///c:/Users/ashva/Documents/workspace/expand/core-application/src/main/java/com/ampta/stylesync/model/Outfit.java)
    *   **Line 9**: `@Document(collection = "outfits")`.
    *   **Line 14**: `private String userId;`.
    *   **Explanation**: "I reference the User by ID here. If I embedded outfits inside User, the User document would get too bit."

## 4. Python ML Engine
**Concept**: Singleton Model Loading
*   **File**: [ml_service.py](file:///c:/Users/ashva/Documents/workspace/expand/ml-engine/app/services/ml_service.py)
*   **What to show**:
    *   **Line 74**: The `__init__` method.
    *   **Line 82**: `self.model = models.mobilenet_v2(...)`.
    *   **Line 180**: `ml_service = MLService()`.
    *   **Explanation**: "This lines runs ONLY ONCE when the server starts. I don't load the model on every request, which is why it's fast."

**Concept**: Logic vs AI (The "Casual/Formal" Trick)
*   **File**: [ml_service.py](file:///c:/Users/ashva/Documents/workspace/expand/ml-engine/app/services/ml_service.py)
*   **What to show**:
    *   **Lines 29-43**: `STYLE_MAP`.
    *   **Explanation**: "I didn't train a neural net for 'Formal vs Casual'. I just mapped 'Blazer' to 'Formal' using this dictionary. It's faster and 100% accurate for my needs."

## 5. Frontend (React)
**Concept**: JWT Storage & Axios Interceptors
*   **File**: [code](file:///c:/Users/ashva/Documents/workspace/expand/webapp/src/services/api.js)
*   **What to show**:
    *   **Line 8**: `api.interceptors.request.use(...)`.
    *   **Line 10**: `const user = JSON.parse(localStorage.getItem('user'));`.
    *   **Explanation**: "I store the token in LocalStorage and this interceptor automatically adds the `Authorization: Bearer` header to every single API call."

**Concept**: Solving CORS in Dev
*   **File**: [vite.config.js](file:///c:/Users/ashva/Documents/workspace/expand/webapp/vite.config.js)
*   **What to show**:
    *   **Lines 8-12**: The `proxy` configuration.
    *   **Explanation**: "This routes `/api` to `localhost:8080`, tricking the browser into thinking it's the same origin."

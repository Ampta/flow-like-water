# Knowledge Base: ML Service Explainer

## Python Microservice (`ml-engine`)

This service is a FastAPI application responsible for all AI/ML operations, including image vectorization, categorization, and outfit recommendation logic.

### 📄 Class/Module Name: `app.main`
1. **Purpose (The "Why")**: Entry point for the FastAPI application.
2. **Annotated logic (The "How")**:
   - `FastAPI`: Initializes the app framework.
   - `@asynccontextmanager`: Defines the application lifespan (startup/shutdown logic).
   - `uvicorn.run`: Runs the ASGI server.
3. **Interactions (The "Where")**: Loads routes from `api_router`.
4. **Microservice Role**: **Entry Point** (Server).

### 📄 Class/Module Name: `app.core.config`
1. **Purpose (The "Why")**: Centralized configuration settings (Environment variables).
2. **Annotated logic (The "How")**:
   - `pydantic.BaseSettings`: Automatically loads values from `.env` file or environment variables.
3. **Interactions (The "Where")**: Used by `main.py` and services to access credentials/paths.
4. **Microservice Role**: **Configuration**.

### 📄 Class/Module Name: `app.api.v1.endpoints.prediction`
1. **Purpose (The "Why")**: Defines HTTP endpoints for AI features.
2. **Annotated logic (The "How")**:
   - `@router.post("/vectorize")`: Endpoint to convert an image into a 1280-dim vector.
   - `@router.post("/predict_category")`: Endpoint to classify an image (Category + Color + Style).
   - `@router.post("/predict")`: Endpoint to recommend outfits based on vector similarity.
   - `run_in_threadpool`: Offloads CPU-intensive PyTorch math to prevent blocking the async event loop.
3. **Interactions (The "Where")**: Calls `ml_service` for math/model inference and `db_service` for data lookup.
4. **Microservice Role**: **Entry Point** (API Layer).

### 📄 Class/Module Name: `app.services.ml_service` (Class: `MLService`)
1. **Purpose (The "Why")**: Encapsulates the PyTorch model logic and heuristics.
2. **Annotated logic (The "How")**:
   - `mobilenet_v2`: Pretrained deep learning model used for feature extraction.
   - `torch.no_grad()`: Disables gradient calculation for faster inference (since we are not training).
   - Heuristics: Contains dictionaries (`STYLE_MAP`, `SEASON_MAP`) to infer attributes based on category.
3. **Interactions (The "Where")**: Uses `torch`, `torchvision`, and `PIL` (Pillow) for image processing.
4. **Microservice Role**: **Business Logic** (Inference Engine).

### 📄 Class/Module Name: `app.models.core_model` (Class: `MultiOutputMobileNetV2`)
1. **Purpose (The "Why")**: Custom Neural Network architecture definition.
2. **Annotated logic (The "How")**:
   - `nn.Module`: PyTorch base class for neural networks.
   - `Three Heads`: Designed to output 3 things simultaneously: Category (Softmax), Color, and Style vectors.
3. **Interactions (The "Where")**: Intended for future training. Currently `ml_service` uses standard MobileNetV2, but this class is ready for advanced implementation.
4. **Microservice Role**: **Data Model** (Neural Architecture).

### 📄 Class/Module Name: `app.models.schemas`
1. **Purpose (The "Why")**: Pydantic models (DTOs) for request/response validation.
2. **Annotated logic (The "How")**:
   - `pydantic.BaseModel`: Ensures input JSON matches expected types (e.g., `feature_vector` must be a list of floats).
3. **Interactions (The "Where")**: Used by API endpoints to serialize JSON.
4. **Microservice Role**: **Communication** (DTO).

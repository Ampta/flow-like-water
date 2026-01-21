# ML-Engine Interview Guide

Use this document to prepare for your interview. It breaks down technical concepts from "Easy" (for HR/Product Managers) to "Complex" (for Engineers/CTOs).

---

## 1. The High-Level Overview (The "Elevator Pitch")

**Q: "What is the ML Engine?"**

*   **Easy (The Concept)**:
    "It's the 'brain' of our application. It looks at images of clothing and automatically understands what they are—like 'This is a red t-shirt'. It also powers our recommendation system by finding other items that look similar to what you like."

*   **Technical (The Architecture)**:
    "It is a standalone microservice written in **Python** using **FastAPI**. It leverages **PyTorch** for Computer Vision tasks. We serve it separately from the main app so that heavy AI computations don't slow down the user interface."

---

## 2. The Model (The "Brain")

**Q: "What AI model are you using?"**

*   **The Answer**: **MobileNetV2**.

*   **Easy Explanation**:
    "We use a pre-trained model called MobileNetV2. Think of it as an AI that has already studied millions of images. We rely on its knowledge to recognize shapes, patterns, and objects in our fashion catalog."

*   **Complex Explanation (For Engineers)**:
    "We use MobileNetV2 pre-trained on ImageNet. We chose it because it's **lightweight and fast**, making it perfect for efficient deployment without needing expensive heavy-duty GPUs. We use it in two distinct ways:
    1.  **Classification**: To identify if an object is a 'shoe' or a 'shirt'.
    2.  **Feature Extraction**: We remove the final classification layer to get a **feature vector** (a list of numbers) that represents the visual 'essence' of the image (texture, shape, style) for similarity search."

---

## 3. Explaining the Endpoints (The "Hands")

The service has 3 main jobs (endpoints). Here is how to explain them.

### Endpoint 1: `/vectorize` (The Translator)

*   **What it does**: Takes an image file ➔ Returns a list of numbers (Vector).
*   **Why?**: Computers can't "compare" two pictures directly. They compare numbers. This endpoint turns a picture into a list of mathematical coordinates.
*   **The Code Logic**:
    ```python
    # 1. Resize image to 224x224 (Standard for AI)
    # 2. Pass through MobileNetV2
    # 3. Flatten the output into a list of floats
    vector = model.features(image).flatten()
    ```

### Endpoint 2: `/predict` (The Recommender)

*   **What it does**: "You viewed this Blue Hoodie used by User A? Here are similar items locally available."
*   **Easy Explanation**:
    "It calculates a 'similarity score' between the item you are looking at and every other item in the database, then returns the top 5 matches."
    
*   **Complex Explanation**:
    "It performs **Cosine Similarity** search.
    1.  **Inputs**: It takes a `user_id` or `item_id`.
    2.  **Fetch**: It gets the vector for the target item.
    3.  **Matrix Match**: It loads all candidate vectors from the database and uses **NumPy** to calculate the dot-product similarity matrix.
    4.  **Ranking**: It sorts the results by score (highest match first).
    5.  **Heuristics**: We add 'Style Boosts'—if the item style matches (e.g., both are Formal), we bump up the score to ensure the outfit makes sense."

### Endpoint 3: `/predict_category` (The Tagger)

*   **What it does**: Auto-fills the upload form. "You uploaded a file? I'll guess it's a 'Red Casual T-Shirt'."
*   **The "Secret Sauce" (Hybrid Approach)**:
    We don't just trust the AI blindly. We use a **Hybrid approach**:
    
    1.  **AI Prediction**: MobileNet guesses the object (e.g., "Velvet"). We map "Velvet" ➔ "Top".
    2.  **Filename Fallback**: If the AI is unsure, we look at the filename. If it's called `my_blue_jeans.jpg`, we treat it as "Jeans". This makes the system much more robust.
    3.  **Color Extraction**: We don't use AI for color. We use a pixel-counting algorithm (K-Means Clustering) to find the most dominant color in the center of the image.

---

## 4. Advanced "Bonus Points"

Mentioning these proves you understand *software engineering*, not just scripts.

1.  **Singleton Pattern**:
    *   "We load the AI model **only once** when the server starts. This is called the Singleton pattern. If we loaded it for every user request, the app would be incredibly slow."

2.  **Threadpooling (`run_in_threadpool`)**:
    *   "PyTorch is CPU-heavy and 'blocking'. If we ran it normally, one user processing an image would freeze the server for everyone else. We use FastAPI's `run_in_threadpool` to move that heavy work to a background thread, keeping the API responsive."

3.  **Scalability (Future Roadmap)**:
    *   "Right now, we compare vectors in memory (NumPy). This works for thousands of items. If we had *millions* of items, I would upgrade this to use a **Vector Database** like **Milvus** or **Pinecone** for faster searching."
